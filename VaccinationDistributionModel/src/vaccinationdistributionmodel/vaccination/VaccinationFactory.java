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
public class VaccinationFactory implements Modelable {

    private City homeCity;
    private Region homeRegion;
    private int openingDay;
    private int batchProductionTime;
    private int batchSize;
    private int vaccinesAvailable;
    private int optimalVaccinationAmount;
    private double targetRatio = 1;
    private int minDeliveryTime = 1;
    private List<VaccinationSchedule> schedules;
    private PriorityQueue<City> cities;

    public VaccinationFactory(Region homeRegion, City home, int openingDay, int productionTime, int size) {
        this.homeCity = home;
        this.homeRegion = homeRegion;
        this.openingDay = openingDay;
        this.batchProductionTime = productionTime;
        this.batchSize = size;
        this.vaccinesAvailable = 0;
        this.optimalVaccinationAmount = Constraints.maximumDailyVaccination * this.batchProductionTime;
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

    public void setOptimalVaccineAmount(int amount) {
        this.optimalVaccinationAmount = amount;
    }

    @Override
    public void update(int currentDay) {
        if (currentDay <= this.openingDay) {
            return;
        }
        if ((currentDay - this.openingDay) % this.batchProductionTime == 0) {
            vaccinesAvailable += this.batchSize;
        }

        processQueue(currentDay);
        
        for (VaccinationSchedule s : this.schedules){
            s.update(currentDay);
        }

    }

    private int deliveryTime(City city) {
        return this.minDeliveryTime;
    }

    private void processQueue(int day) {
        Iterator<City> it = this.cities.iterator();
        while (it.hasNext()) {
            if (vaccinesAvailable == 0) {
                break;
            }
            City c = it.next();
            if (c.scheduleInAction()) {
                continue;
            }
            int vaccines = this.optimalVaccinationAmount;
            vaccines = Math.min(vaccines, this.vaccinesAvailable);
            int startingDay = day + deliveryTime(c);
            VaccinationSchedule plan = new VaccinationSchedule(c, startingDay, vaccines, targetRatio);
            this.vaccinesAvailable -= vaccines;
            c.setSchedule(plan);
            this.schedules.add(plan);
            it.remove();
        }
    }

    public City getHomeCity() {
        return homeCity;
    }

    public int getBatchProductionTime() {
        return batchProductionTime;
    }

    public int getBatchSize() {
        return batchSize;
    }
}
