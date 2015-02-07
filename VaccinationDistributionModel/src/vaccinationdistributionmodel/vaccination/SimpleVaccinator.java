/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

import java.util.List;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.world.Globe;
import vaccinationdistributionmodel.world.Region;

/**
 *
 * A very basic plan to combat ebola
 *
 * @author ilari
 */
public class SimpleVaccinator implements VaccinationStrategy {
    
    private Constraints constraints;
    private List<VaccinationSchedule> schedules;
    private int myDay = 0;
    
    public SimpleVaccinator() {
        this.constraints = new Constraints(100, 1000);
    }
    
    @Override
    public void update(int day){
        this.schedules.stream().forEach((plan) -> {
            plan.update(myDay);
        });
        myDay++;
    }
    
    @Override
    public void createVaccinationSchedules(Globe globe) {
        globe.getRegions().getNodes().stream().forEach((region) -> {
            createVaccinationSchedules(region);
        });
    }
    
    private void createVaccinationSchedules(Region region) {
        region.getCities().stream().forEach((city) -> {
            createVaccinationSchedules(city);
        });
    }
    
    private void createVaccinationSchedules(City city) {
        VaccinationSchedule plan = new VaccinationSchedule(city);
        
        //issue orders
        VaccineOrder order = new VaccineOrder(100, 1000, 0);
        
        plan.addVaccineOrder(order);
    }    
    
}
