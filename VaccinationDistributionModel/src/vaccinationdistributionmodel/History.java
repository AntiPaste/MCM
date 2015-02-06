package vaccinationdistributionmodel;

import java.util.ArrayList;
import java.util.List;

public class History {
	private List<CityState> states;
	
	public History() {
		this.states = new ArrayList();
	}
	
	public List<CityState> getStates() {
		return this.states;
	}
	
	public void addState(CityState state) {
		this.states.add(state);
	}
}
