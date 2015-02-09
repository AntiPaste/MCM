/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel.vaccination;

import java.util.List;
import vaccinationdistributionmodel.Modelable;

/**
 *
 * @author ilari
 */
public class VaccinationFactory implements Modelable {

    private int vaccinesDaily = 700_000; // ~ 5M weekly
    private int vaccinesToDistribute = 0;
    private int openingDay;
    private List<VaccinationSupplier> customers;

    public VaccinationFactory(List<VaccinationSupplier> customers, int openingDay) {
        this.customers = customers;
        this.openingDay = openingDay;
    }

    @Override
    public void update(int currentDay) {
        if (currentDay <= this.openingDay) {
            return;
        }
        this.vaccinesToDistribute += this.vaccinesDaily;
        
        pollSuppliers();
    }
    
    private void pollSuppliers(){
        
    }   
}
