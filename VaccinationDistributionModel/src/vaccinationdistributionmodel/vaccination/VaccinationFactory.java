/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import vaccinationdistributionmodel.Modelable;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.world.Graph;

/**
 *
 * @author ilari
 */
public class VaccinationFactory implements Modelable{
    private City homeCity;
    private int openingDay;
    private int batchProductionTime;
    private int batchSize;
    private int vaccinesAvailable;
    private double targetRatio = 1;
    private PriorityQueue<City> cities;
    
    public VaccinationFactory(City home, int openingDay, int productionTime, int size){
        this.homeCity = home;
        this.openingDay = openingDay;
        this.batchProductionTime = productionTime;
        this.batchSize = size;
        this.vaccinesAvailable = 0;
        this.cities = new PriorityQueue<>(new Comparator<City>(){
            @Override
            public int compare(City c1, City c2) {
                return (int) (Graph.distance(homeCity,c1) - Graph.distance(homeCity, c2));
            }
        });
    }

    @Override
    public void update(int currentDay) {
        if (currentDay <= this.openingDay) return;
        if ((currentDay - this.openingDay) % this.batchProductionTime == 0) {
            vaccinesAvailable += this.batchSize;
        }
        
        processQueue(currentDay);
        
    }
    
    private void processQueue(int day){
        Iterator<City> it = this.cities.iterator();
        while (it.hasNext()){
            if (vaccinesAvailable==0) break;
            City c = it.next();
            if (c.scheduleInAction()){
                continue; // don't 
            }
            int vaccines = this.vaccinesAvailable;
            VaccinationSchedule plan = new VaccinationSchedule(c,day,vaccines,targetRatio);
            this.vaccinesAvailable -= vaccines;
            c.setSchedule(plan);
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
