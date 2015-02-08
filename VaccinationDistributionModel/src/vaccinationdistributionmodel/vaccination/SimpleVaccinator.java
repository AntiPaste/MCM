/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

import java.util.ArrayList;
import java.util.List;
import vaccinationdistributionmodel.world.Globe;

/**
 *
 * A very basic plan to combat Ebola
 *
 * @author ilari
 */
public class SimpleVaccinator implements VaccinationStrategy {
    
    private List<VaccinationSchedule> schedules;
    private int myDay = 0;
    
    public SimpleVaccinator() {
        this.schedules = new ArrayList<>();
    }
    
    @Override
    public void update(int currentDay){
        
    }
    
    @Override
    public void createVaccinationSchedules(Globe globe){
        
    }
}
