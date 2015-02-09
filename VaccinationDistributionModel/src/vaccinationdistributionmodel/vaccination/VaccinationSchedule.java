package vaccinationdistributionmodel.vaccination;

import java.util.SortedSet;
import java.util.TreeSet;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.Modelable;
import vaccinationdistributionmodel.world.CityState;

public class VaccinationSchedule implements Modelable {

    private City city;
    private long vaccinations;
    private double targetingRatio;
    private int startingDay;

    public VaccinationSchedule(City city, int startingDay, long vaccinations, double targetingRatio) {
        this.vaccinations = vaccinations;
        this.targetingRatio = targetingRatio;
        this.city = city;
        this.startingDay = startingDay;
    }

    public CityState getState() {
        return this.city.getValues();
    }

    @Override
    public void update(int currentDay) {
        if (currentDay < this.startingDay) return;
        
        long give = Constraints.maximumDailyVaccination;
        if (give > vaccinations) {
            give = vaccinations;
        }
        long inf = (int) (give * this.targetingRatio);
        long uninf = give - inf;
               
        this.city.vaccinate(inf, true);
        this.city.vaccinate(uninf, false);
        
        if (this.vaccinations==0) this.city.setScheduleFinished();
    }
}
