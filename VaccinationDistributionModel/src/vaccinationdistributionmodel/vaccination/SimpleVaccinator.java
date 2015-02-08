/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

import java.util.ArrayList;
import java.util.List;
import vaccinationdistributionmodel.Modelable;

/**
 *
 * A very basic plan to combat Ebola
 *
 * @author ilari
 */
public class SimpleVaccinator implements Modelable{
    
    private List<VaccinationSchedule> schedules;
    private List<VaccinationFactory> factories;
    
    public SimpleVaccinator(List<VaccinationFactory> factories) {
        this.schedules = new ArrayList<>();
        this.factories = factories;
    }
    
    @Override
    public void update(int currentDay){
        schedules.stream().forEach((m) -> {m.update(currentDay);});
        factories.stream().forEach((m) -> {m.update(currentDay);});
    }
    
}
