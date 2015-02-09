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

    private long vaccinesDaily = 700_000; // ~ 5M weekly
    private long vaccinesToDistribute = 0;
    private long storageCapacity = 100_000_000;
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
        this.vaccinesToDistribute = Math.min(this.vaccinesDaily + this.vaccinesToDistribute,
                this.storageCapacity);

        this.customers.stream().forEach((c) -> {
            c.update(currentDay);
        });

        pollSuppliers();
    }

    private void pollSuppliers() {
        long leftToGive = this.vaccinesToDistribute;
        long totalNeed = 0;
        totalNeed = this.customers.stream().map((s)
                -> s.currentNeed()).reduce(totalNeed, (Long accumulator, Long item) -> accumulator + item);
        for (VaccinationSupplier supplier : this.customers){
            long supplierNeed = supplier.currentNeed();
            
            long give = (long) (this.vaccinesToDistribute * supplierNeed 
                    / ((double) totalNeed));
            give = Math.max(give, supplierNeed);
            give = Math.min(give, leftToGive);
            supplier.giveVaccines(give);
            leftToGive -= give;
            if (leftToGive==0) break;
        }
    }
}
