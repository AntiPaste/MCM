/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public String name;
    public double necessityConstant = 0.6;

    public Region(RegionParameters params, Graph<City> cities) {
        this.cities = cities;
        this.bigCities = new ArrayList();
        this.regionParams = params;
        this.computeAverageLatitudeAndLongitude();
        this.init();
    }

    public Graph<City> getGraph() {
        return this.cities;
    }

    private double toRadians(double degrees) {
        return (degrees * Math.PI / 180.0);
    }

    private double toDegrees(double radians) {
        return (radians * 180 / Math.PI);
    }

    public int[] ebolaLevel() {
        List<City> cities = this.getCities();
        long totalAlive = 0;
        long totalPopulation = 0;
        double ebolaLevel = 0;

        for (City city : cities) {
            long alive = city.getValues().amountOfExposed() + city.getValues().infected + city.getValues().advanced + city.getValues().vaccinated
                    + city.getValues().susceptible + city.getValues().recovered;
            totalAlive += alive;
            totalPopulation += city.getValues().population;

            ebolaLevel += city.ebolaLevel() * alive;
        }

        return new int[]{
            (int) (255.0 * (1.0 - (ebolaLevel / totalAlive)) * (((double) totalAlive) / totalPopulation) * (((double) totalAlive) / totalPopulation)),
            (int) (255.0 * (ebolaLevel / totalAlive) * (((double) totalAlive) / totalPopulation) * (((double) totalAlive) / totalPopulation)),
            0
        };
    }
    
    public int[] vaccinationLevel() {
        List<City> cities = this.getCities();
        long totalAlive = 0;
        long totalPopulation = 0;
        long vaccinationLevel = 0;

        for (City city : cities) {
            long alive = city.getValues().amountOfExposed() + city.getValues().infected + city.getValues().advanced + city.getValues().vaccinated
                    + city.getValues().susceptible + city.getValues().recovered;
            totalAlive += alive;
            totalPopulation += city.getValues().population;

            vaccinationLevel += city.getValues().vaccinated;
        }

        return new int[]{
            (int) (255.0 * (1.0 - (vaccinationLevel / totalAlive))),
            0,
            (int) (255.0 * (vaccinationLevel / totalAlive))
        };
    }

    public long getPopulation() {
        long population = 0;
        for (City city : this.getCities()) {
            population += city.getValues().population;
        }

        return population;
    }

    public long getRecovered() {
        long recovered = 0;
        for (City city : this.getCities()) {
            recovered += city.getValues().recovered;
        }

        return recovered;
    }

    public long getDeaths() {
        long deaths = 0;
        for (City city : this.getCities()) {
            deaths += city.getValues().dead;
        }

        return deaths;
    }
    
    public long getVaccinated() {
        long vaccinated = 0;
        for (City city : this.getCities()) {
            vaccinated += city.getValues().vaccinated;
        }

        return vaccinated;
    }
    
    public long getExposed() {
        long exposed = 0;
        for (City city : this.getCities()) {
            exposed += city.getValues().amountOfExposed();
        }

        return exposed;
    }
    
    public long getInfected() {
        long infected = 0;
        for (City city : this.getCities()) {
            infected += city.getValues().infected;
        }

        return infected;
    }
    
    public long getAdvanced() {
        long advanced = 0;
        for (City city : this.getCities()) {
            advanced += city.getValues().advanced;
        }

        return advanced;
    }
    
    public long getSusceptible() {
        long susceptible = 0;
        for (City city : this.getCities()) {
            susceptible += city.getValues().susceptible;
        }

        return susceptible;
    }

    private void computeAverageLatitudeAndLongitude() {

        double R = 6378.1;
        double x = 0, y = 0, z = 0;
        int n = this.cities.getNodes().size();

        for (City city : this.cities.getNodes()) {
            double phii = toRadians(city.latitude);
            double lambda = toRadians(city.longitude);
            x += R * Math.cos(phii) * Math.cos(lambda);
            y += R * Math.sin(phii) * Math.cos(lambda);
            z += R * Math.sin(lambda);
        }

        x /= n;
        y /= n;
        z /= n;

        latitude = toDegrees(Math.atan2(y, x));
        longitude = toDegrees(Math.atan2(z, Math.sqrt(x * x + y * y)));

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

    private CityParameters generateCityParameters() {
        CityParameters params = new CityParameters();
        params.contaminationRate = GlobalParameters.CONTAMINATION_RATE;
        params.mortalityRate = GlobalParameters.MORTALITY_RATE * this.regionParams.hygiene;
        return params;
    }

    public void interact(Region nearRegion, double weight) {
        for (City myBig : this.bigCities) {
            for (City otherBig : nearRegion.bigCities) {
//                System.out.println(myBig.name);
//                System.out.println("to interact with");
//                System.out.println(otherBig.name);
//                System.out.println("with a weight: " + weight);
//                System.out.println("----------end interaction--------");

                myBig.interact(otherBig, weight);
            }
        }
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
    
    public long getSaveable(){
        long s = 0;
        s = this.getCities().stream().map((c) ->
                c.getSaveable()).reduce(s, (accumulator, _item) -> accumulator + _item);
        return s;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Region other = (Region) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
