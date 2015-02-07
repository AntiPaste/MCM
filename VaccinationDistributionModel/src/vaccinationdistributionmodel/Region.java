/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel;

import java.util.ArrayList;

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
        for (City c : cities.getCities()) {
            c.setParameters(regionParams);
        }
    }

    private void interact(Region nearRegion) {

    }

    @Override
    public void update() {
        // city's internal changes
        for (City city : cities.getCities()) {
            city.update();
        }

        // city- city interaction
        for (City one : cities.getCities()) {
            for (City other : cities.getCities()) {
                double w = cities.weight(one, other);
                one.interact(other, w);
            }
        }

        // region-region interaction
        for (Region nearRegion : this.neighbouringRegions) {
            this.interact(nearRegion);
        }
    }
}
