package vaccinationdistributionmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History {
	private List<CityState> states;
	
	public History() {
		this.states = new ArrayList();
	}
	
	public List<CityState> getStates() {
		return this.states;
	}
        
        public Map<String, List<Integer>> getData(){
            HashMap<String, List<Integer>> map = new HashMap<>();
            
            map.put("population", new ArrayList<>());
            map.put("susceptible", new ArrayList<>());
            map.put("exposed", new ArrayList<>());
            map.put("infected", new ArrayList<>());
            map.put("advanced", new ArrayList<>());
            map.put("recovered", new ArrayList<>());
            map.put("dead", new ArrayList<>());
            
            for (CityState state: this.states){
                map.get("population").add(state.population);
                map.get("susceptible").add(state.susceptible);
                map.get("exposed").add(state.exposed);
                map.get("infected").add(state.infected);
                map.get("advanced").add(state.advanced);
                map.get("recovered").add(state.recovered);
                map.get("dead").add(state.dead);
            }
            
            return map;
        }
	
	public void addState(CityState state) {
		this.states.add(state);
	}
}
