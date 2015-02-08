/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel.world;

import java.util.ArrayList;
import java.util.List;
import vaccinationdistributionmodel.Modelable;

/**
 *
 * @author ilari
 */
public class Region implements Modelable {

    private ArrayList<Region> neighbouringRegions;
    private Graph<City> cities;
    private List<City> bigCities;
    private RegionParameters regionParams;
    public double latitude;
    public double longitude;

    public Region(RegionParameters params, Graph<City> cities) {
        this.cities = cities;
        this.bigCities = new ArrayList();
        this.regionParams = params;
        this.computeAverageLatitudeAndLongitude();
        this.init();
    }
    
    public Graph<City> getGraph(){
        return this.cities;
    }
    
    private double toRadians(double degrees) {
        return (degrees * Math.PI / 180.0);
    }

    private double toDegrees(double radians) {
        return (radians * 180 / Math.PI);
    }
    
    
    private void computeAverageLatitudeAndLongitude(){
        
        double R = 6378.1;
        double x = 0, y = 0, z = 0;
        int n = this.cities.getNodes().size();
        
        for (City city : this.cities.getNodes()){
            double phii = toRadians(city.latitude);
            double lambda = toRadians(city.longitude);
            x += R*Math.cos(phii)*Math.cos(lambda);
            y += R*Math.sin(phii)*Math.cos(lambda);
            z += R*Math.sin(lambda);
        }
        
        x/=n; y/=n; z/=n;
        
        latitude = toDegrees(Math.atan2(y,x));
        longitude = toDegrees(Math.atan2(z, Math.sqrt(x*x + y*y)));
        
        // the arithmetic average will not cause problems
//        double latSum = 0;
//        double lonSum = 0;
//        for (City city : this.cities.getNodes()){
//            latSum += city.latitude;
//            lonSum += city.longitude;
//        }
//        latitude = latSum / this.cities.getNodes().size();
//        longitude = lonSum / this.cities.getNodes().size();
    }

    public void setBigCities(List<City> bigCities) {
        this.bigCities = bigCities;
    }
    
    private void init() {
        for (City c : cities.getNodes()) {
            c.setParameters(generateCityParameters());
        }
    }
    
    private CityParameters generateCityParameters(){
        CityParameters params = new CityParameters();
        params.contaminationRate = GlobalParameters.CONTAMINATION_RATE;
        params.mortalityRate = GlobalParameters.MORTALITY_RATE * this.regionParams.hygiene;
        return params;
    }

    public void interact(Region nearRegion, double weight) {
        /*for (City myBig : this.bigCities) {
            for (City otherBig : nearRegion.bigCities) {
                myBig.interact(otherBig, weight);
            }
        }*/
    }
    
    public List<City> getCities() {
        return this.cities.getNodes();
    }

    public List<City> nearCities(City city) {
        if (!cities.getNodes().contains(city)) {
            return new ArrayList<>();
        }
        List<City> nearCities = new ArrayList<City>();

        this.cities.getEdges(city).stream().forEach((Edge<City> edge) -> {
            if (edge.one.equals(city)) {
                nearCities.add(edge.other);
            } else {
                nearCities.add(edge.one);
            }
        });

        return nearCities;
    }

    @Override
    public void update(int currentDay) {
        // city's internal changes
        for (City city : cities.getNodes()) {
            city.update(currentDay);
        }

        // city - city interaction
        for (Edge<City> edge : this.cities.edges()) {
            edge.one.interact(edge.other, edge.weight);
        }

		// region-region interaction
		/*for (Region nearRegion : this.neighbouringRegions) {
         this.interact(nearRegion);
         }*/
    }
}
