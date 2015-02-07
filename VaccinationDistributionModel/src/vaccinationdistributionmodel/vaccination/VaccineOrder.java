package vaccinationdistributionmodel.vaccination;

public class VaccineOrder implements Comparable {
	public int vaccines;
	public int days;
	
	public VaccineOrder(int vaccines, int days) {
		this.vaccines = vaccines;
		this.days = days;
	}
	
	@Override
	public String toString() {
		return String.format("%d vaccines on day #%d", this.vaccines, this.days);
	}

	@Override
	public int compareTo(Object o) {
		VaccineOrder order = (VaccineOrder) o;
		return (this.days - order.days);
	}
}
