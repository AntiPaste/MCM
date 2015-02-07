/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel.world;

import java.util.ArrayList;
import vaccinationdistributionmodel.Modelable;

/**
 *
 * @author ilari
 */
public class Region implements Modelable {

	private ArrayList<Region> neighbouringRegions;
	private Graph<City> cities;
	private Parameters regionParams;

	public Region(Parameters params, Graph cities) {
		this.cities = cities;
		this.regionParams = params;
	}

	private void init() {
		for (City c : cities.getNodes()) {
			c.setParameters(regionParams);
		}
	}

	private void interact(Region nearRegion) {

	}

	@Override
	public void update(int currentDay) {
		// city's internal changes
		for (City city : cities.getNodes()) {
			city.update(0);
		}

		// city - city interaction
        for (Edge<City> edge : this.cities.edges()){
            edge.one.interact(edge.other, edge.weight);
        }

		// region-region interaction
		for (Region nearRegion : this.neighbouringRegions) {
			this.interact(nearRegion);
		}
	}
}
