package vaccinationdistributionmodel.vaccination;

import java.util.SortedSet;
import java.util.TreeSet;
import vaccinationdistributionmodel.City;
import vaccinationdistributionmodel.Modelable;

public class VaccinationProgram implements Modelable {
	SortedSet<VaccineOrder> orders;
	City city;
	
	public VaccinationProgram(City city) {
		this.orders = new TreeSet();
		this.city = city;
	}
	
	public SortedSet<VaccineOrder> getOrders() {
		return this.orders;
	}
	
	public void addVaccineOrder(VaccineOrder order) {
		this.orders.add(order);
	}
	
	@Override
	public void update() {
		
	}
}
