/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel.vaccination;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import vaccinationdistributionmodel.Modelable;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.world.Graph;
import vaccinationdistributionmodel.world.Region;

/**
 *
 * @author ilari
 */
public class VaccinationSupplier implements Modelable {

    private City homeCity;
    private Region homeRegion;
    private VaccinationFactory factory;
    private int openingDay;
    private long vaccinesAvailable;
    private double targetRatio = 0.5;
    private int deliverySpeed = 100; // km / h
    private int minDeliveryTime = 1;
    private List<VaccinationSchedule> schedules;
    private PriorityQueue<City> cities;

    public VaccinationSupplier(Region homeRegion, City home, int openingDay) {
        this.homeCity = home;
        this.homeRegion = homeRegion;
        this.openingDay = openingDay;
        this.vaccinesAvailable = 0;
        this.schedules = new ArrayList<>();
        this.cities = new PriorityQueue<>(new Comparator<City>() {
            @Override
            public int compare(City c1, City c2) {
                return (int) (Graph.distance(homeCity, c1) - Graph.distance(homeCity, c2));
            }
        });

        this.initializeQueue();
    }

    private void initializeQueue() {
        for (City c : this.homeRegion.getCities()) {
            this.cities.add(c);
        }
    }

    public void setTargetRatio(double ratio) {
        this.targetRatio = ratio;
    }

    @Override
    public void update(int currentDay) {
        if (currentDay <= this.openingDay) {
            return;
        }
        //greedyQueue(currentDay);
        //processQueue(currentDay);
        evenQueue(currentDay);
        for (VaccinationSchedule s : this.schedules){
            s.update(currentDay);
        }

    }
    
    public void giveVaccines(long amount){
        this.vaccinesAvailable += amount;
    }
    
    public long currentNeed(){
        long need = 0;
        for (City c : this.cities){
            need += c.getSaveable();
        }
        need = (long) (need * homeRegion.necessityConstant);
        long request = Math.max(0, need - this.vaccinesAvailable);
        return request;
    }

    private int deliveryTime(City city) {
        return 2; // totally scientific
        //return (int) (Graph.distance(this.homeCity, city) / (this.deliverySpeed * 24)) + 1;
        //return this.minDeliveryTime;
    }
    
    private long supplyAmount(City c, long regionSaveable){
        return (long) (this.vaccinesAvailable*((double) c.getSaveable()) / regionSaveable);
    }
    
    private void greedyQueue(int day){
        
        Iterator<City> it = this.cities.iterator();
        while (it.hasNext()) {
            if (vaccinesAvailable == 0) {
                break;
            }
            City c = it.next();
            if (c.scheduleInAction()) {
                continue;
            }
            
            long vaccines = c.getSaveable();
            vaccines = Math.min(vaccines, this.vaccinesAvailable);
            
            int startingDay = day + deliveryTime(c);
            VaccinationSchedule plan = new VaccinationSchedule(c, startingDay, vaccines, targetRatio);
            this.vaccinesAvailable -= vaccines;
            c.setSchedule(plan);
            this.schedules.add(plan);
            it.remove();
        }
    }
    
    public void processQueue(int day){
        long regionSaveable = this.homeRegion.getSaveable();
        
        Iterator<City> it = this.cities.iterator();
        while (it.hasNext()) {
            if (vaccinesAvailable == 0) {
                break;
            }
            City c = it.next();
            if (c.scheduleInAction()) {
                continue;
            }
            
            long vaccines = this.supplyAmount(c, regionSaveable);
            vaccines = Math.min(vaccines, this.vaccinesAvailable);
            
            int startingDay = day + deliveryTime(c);
            VaccinationSchedule plan = new VaccinationSchedule(c, startingDay, vaccines, targetRatio);
            this.vaccinesAvailable -= vaccines;
            c.setSchedule(plan);
            this.schedules.add(plan);
            it.remove();
        }
    }

    private void evenQueue(int day) {
        long citiesRemaining = this.cities.size();
        
        Iterator<City> it = this.cities.iterator();
        while (it.hasNext()) {
            if (vaccinesAvailable == 0) {
                break;
            }
            City c = it.next();
            if (c.scheduleInAction()) {
                continue;
            }
            
            long vaccines = this.vaccinesAvailable/citiesRemaining +1;
            vaccines = Math.min(vaccines, c.getSaveable());
            vaccines = Math.min(vaccines, this.vaccinesAvailable);
            citiesRemaining --;
            int startingDay = day + deliveryTime(c);
            VaccinationSchedule plan = new VaccinationSchedule(c, startingDay, vaccines, targetRatio);
            this.vaccinesAvailable -= vaccines;
            c.setSchedule(plan);
            this.schedules.add(plan);
            it.remove();
        }
    }
    
    public long getVaccinesAvailable(){
        return this.vaccinesAvailable;
    }

    public City getHomeCity() {
        return homeCity;
    }

}
