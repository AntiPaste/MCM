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
public class Region implements Modelable{
    
    private ArrayList<Region> neighbouringRegions;
    private Graph cities;
    private Parameters regionParams;
    
    public Region(Parameters params, Graph cities){
        this.cities = cities;
        this.regionParams = params;
    }
    
    private void init(){
        for (City c: cities.getCities()){
            c.setParameters(regionParams);
        }
    }
    
    
    @Override
    public void update() {
        for (City city: cities.getCities()){
            city.update();
        }
    }
}
