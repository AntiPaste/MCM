/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

import vaccinationdistributionmodel.world.City;

/**
 *
 * This is a skeleton for the actual vaccinationStrategies to be developed from
 * 
 * @author ilari
 */
public class FirstVaccinator implements VaccinationStrategy {
    
    public FirstVaccinator(){
        
    }

    @Override
    public VaccinationSchedule createVaccinationSchedule(City city) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setProductionSpeed(int vaccinesPerDay) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
