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
    
    ArrayList<Region> neighbouringRegions;
    ArrayList<City> cities;
    double mortality; // rate of death from ebola
    double infectiveness; // rate of spread of ebola
    int [][] weights;
    
    public Region(double mortality, double infectiveness) {
        this(mortality, infectiveness, new ArrayList<>());
    }
    
    public Region(double mortality, double infectiveness, ArrayList<City> cities) {
        this.cities = cities;
        this.mortality = mortality;
        this.infectiveness = infectiveness;
        
        init();
    }
    
    private void init(){
        for (City c: cities){
            c.setParameters(mortality, infectiveness);
        }
    }
    
    
    @Override
    public void update() {
        for (City city: cities){
            city.update();
        }
    }
}
