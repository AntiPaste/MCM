/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

import vaccinationdistributionmodel.Modelable;
import vaccinationdistributionmodel.world.City;

/**
 *
 * @author ilari
 */
public class VaccinationFactory implements Modelable{
    City homeCity;
    int openingDay;
    int batchProductionTime;
    int batchSize;
    int vaccinesAvailable;
    
    public VaccinationFactory(City home, int openingDay, int productionTime, int size){
        this.homeCity = home;
        this.openingDay = openingDay;
        this.batchProductionTime = productionTime;
        this.batchSize = size;
        this.vaccinesAvailable = 0;
    }

    @Override
    public void update(int currentDay) {
        if (currentDay <= this.openingDay) return;
        if ((currentDay - this.openingDay) % this.batchProductionTime == 0) {
            vaccinesAvailable += this.batchSize;
        }
        
    }
    
    public int getVaccines(int request){
        request = Math.min(request, this.vaccinesAvailable);
        this.vaccinesAvailable -= request;
        return request;
    }
    
    
}
