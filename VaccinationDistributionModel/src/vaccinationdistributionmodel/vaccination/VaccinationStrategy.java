/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

import vaccinationdistributionmodel.world.City;

/**
 *
 * @author ilari
 */
public interface VaccinationStrategy {
    
    public VaccinationSchedule createVaccinationSchedule(City city);
    public void setProductionSpeed(int vaccinesPerDay);
    
}
