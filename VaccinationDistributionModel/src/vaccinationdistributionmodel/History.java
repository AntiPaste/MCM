package vaccinationdistributionmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History {
	private Map<String, List<Integer>> states;

	public History() {
		this.states = new HashMap();
	}

	public Map<String, List<Integer>> getStates() {
		return this.states;
	}

	public void addState(CityState state) {
		this.addData("population", state.population);
		this.addData("susceptible", state.susceptible);
		this.addData("exposed", state.exposed);
		this.addData("infected", state.infected);
		this.addData("advanced", state.advanced);
		this.addData("recovered", state.recovered);
		this.addData("dead", state.dead);
	}

	public void addData(String key, int value) {
		if (!this.states.containsKey(key)) {
			this.states.put(key, new ArrayList());
		}

		this.states.get(key).add(value);
	}

	public Map<String, List<Integer>> getData() {
		return this.states;
	}
}
