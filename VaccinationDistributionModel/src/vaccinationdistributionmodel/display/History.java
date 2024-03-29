package vaccinationdistributionmodel.display;

import vaccinationdistributionmodel.world.CityState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History {

    private Map<String, List<Long>> states;

    public History() {
        this.states = new HashMap();
    }

    public Map<String, List<Long>> getStates() {
        return this.states;
    }

    public void addState(CityState state) {
        this.addData("population", state.population);
        this.addData("susceptible", state.susceptible);
        this.addData("exposed", state.amountOfExposed());
        this.addData("infected", state.infected);
        this.addData("advanced", state.advanced);
        this.addData("recovered", state.recovered);
        this.addData("dead", state.dead);
        this.addData("vaccinated", state.vaccinated);
    }

    public void addData(String key, long value) {
        if (!this.states.containsKey(key)) {
            this.states.put(key, new ArrayList());
        }

        this.states.get(key).add(value);
    }

    public Map<String, List<Long>> getData() {
        return this.states;
    }
}
