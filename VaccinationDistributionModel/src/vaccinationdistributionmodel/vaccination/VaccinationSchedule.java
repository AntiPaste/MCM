package vaccinationdistributionmodel.vaccination;

import java.util.SortedSet;
import java.util.TreeSet;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.Modelable;
import vaccinationdistributionmodel.world.CityState;

public class VaccinationSchedule implements Modelable {

    private City city;
    private int vaccinations;
    private double targetingRatio;

    public VaccinationSchedule(City city, int startingDay, int vaccinations, double targetingRatio) {
        this.vaccinations = vaccinations;
        this.targetingRatio = targetingRatio;
        this.city = city;
        int activationDay = startingDay;

    }

    public CityState getState() {
        return this.city.getValues();
    }

    @Override
    public void update(int currentDay) {
        int give = Constraints.maximumDailyVaccination;
        if (give > vaccinations) {
            give = vaccinations;
        }
        int inf = (int) (give * this.targetingRatio);
        int uninf = give - inf;
        
        this.city.vaccinate(inf, true);
        this.city.vaccinate(uninf, false);
    }
}
