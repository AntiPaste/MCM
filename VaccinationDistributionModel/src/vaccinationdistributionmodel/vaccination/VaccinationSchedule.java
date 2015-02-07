package vaccinationdistributionmodel.vaccination;

import java.util.SortedSet;
import java.util.TreeSet;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.Modelable;
import vaccinationdistributionmodel.world.CityState;

public class VaccinationSchedule implements Modelable {

    private SortedSet<VaccineOrder> orders;
    private City city;

    public VaccinationSchedule(City city) {
        this.orders = new TreeSet();
        this.city = city;
    }
    
    public CityState getState(){
        return this.city.getValues();
    }

    public SortedSet<VaccineOrder> getOrders() {
        return this.orders;
    }

    public void addVaccineOrder(VaccineOrder order) {
        this.orders.add(order);
    }

    @Override
    public void update(int currentDay) {
        for (VaccineOrder order : this.orders) {
            if (order.days > currentDay) {
                break;
            }
            this.city.vaccinate(order.vaccinesToInfected, true);
            this.city.vaccinate(order.vaccinesToUninfected, false);
        }
    }
}
