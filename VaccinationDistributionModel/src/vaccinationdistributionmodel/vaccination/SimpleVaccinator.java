/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.world.Globe;

/**
 *
 * A very basic plan to combat Ebola
 *
 * @author ilari
 */
public class SimpleVaccinator implements VaccinationStrategy {
    
    private List<VaccinationSchedule> schedules;
    private List<VaccinationFactory> factories;
    private Map<City, VaccinationSchedule> plansByCity;
    private int myDay = 0;
    
    public SimpleVaccinator(List<VaccinationFactory> factories) {
        this.schedules = new ArrayList<>();
        this.factories = factories;
    }
    
    @Override
    public void update(int currentDay){
        schedules.stream().forEach((m) -> {m.update(currentDay);});
        factories.stream().forEach((m) -> {m.update(currentDay);});
    }
    
    @Override
    public void createVaccinationSchedules(Globe globe){
        
    }
}
