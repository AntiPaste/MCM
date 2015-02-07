package vaccinationdistributionmodel.vaccination;

public class VaccineOrder implements Comparable {

    public int vaccinesToInfected;
    public int vaccinesToUninfected;
    public int days;

    public VaccineOrder(int vaccinesToInfected, int vaccinesToUninfected,  int days) {
        this.vaccinesToInfected = vaccinesToInfected;
        this.vaccinesToUninfected = vaccinesToUninfected;
        this.days = days;
    }

    @Override
    public String toString() {
        int vaccines = this.vaccinesToInfected + this.vaccinesToUninfected;
        return String.format("%d vaccines on day #%d", vaccines, this.days);
    }

    @Override
    public int compareTo(Object o) {
        VaccineOrder order = (VaccineOrder) o;
        return (this.days - order.days);
    }
}
