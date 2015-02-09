package vaccinationdistributionmodel.world;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import vaccinationdistributionmodel.Modelable;
import vaccinationdistributionmodel.vaccination.VaccinationFactory;
import vaccinationdistributionmodel.vaccination.VaccinationSupplier;

public class Globe implements Modelable {

    private Graph<Region> regionGraph;
    private List<VaccinationFactory> factories;

    public Globe() {
        Map<String, List<City>> countries = new HashMap();
        List<Region> regions = new ArrayList();

        try {
            Scanner reader = new Scanner(new File("cities"));
            while (reader.hasNext()) {
                String line = reader.nextLine();
                String[] data = line.split("\t");

                String name = data[0];
                String country = data[1];
                int population = Integer.parseInt(data[2]);
                
                String[] coordinates = data[3].split(",");
                double latitude = Double.parseDouble(coordinates[0]);
                double longitude = Double.parseDouble(coordinates[1]);

                if (!countries.containsKey(country)) {
                    countries.put(country, new ArrayList());
                }

                City city = new City(population);
                //System.out.println(population);

                city.setInformation(name, latitude, longitude);
                countries.get(country).add(city);
            }
        } catch (Exception e) {
            System.out.println("Could not read file");
            System.exit(0);
        }

        for (Map.Entry<String, List<City>> entry : countries.entrySet()) {
            Graph<City> graph = new Graph();
            graph.makeCityEdges(entry.getValue());

            //System.out.println("Region: " + entry.getKey() + "\n");
            //System.out.println(graph);
            Region region = new Region(new RegionParameters(), graph);
            region.setBigCities(graph.getBigCities());
            region.name = entry.getKey();
            
            regions.add(region);
        }

        Graph<Region> graph = new Graph();
        graph.makeRegionEdges(regions);
        this.regionGraph = graph;
        
        initializeVaccinationScheme();
    }
    
    private void initializeVaccinationScheme(){
        this.factories = new ArrayList<>();
        List<VaccinationSupplier> suppliers = new ArrayList<>();
        
        for (Region region : this.regionGraph.getNodes()){
            City homecity = region.getGraph().getBigCities().get(0);
            VaccinationSupplier supplier = new VaccinationSupplier(region, homecity, 
                    GlobalParameters.STARTING_DAY);
            
            suppliers.add(supplier);
        }
        
        for (Region bigRegion : this.regionGraph.getBigRegions()){
            VaccinationFactory fac = new VaccinationFactory(suppliers, GlobalParameters.PRODUCTION_DAY);
        }
    }
    
    public Globe(List<Region> regions){
        for (Region r : regions){
            r.setBigCities(r.getCities().subList(0, 2));
        }

        Graph<Region> graph = new Graph();
        graph.makeRegionEdges(regions);
        this.regionGraph = graph;
    }



    public Graph<Region> getRegions() {
        return this.regionGraph;
    }
    
    public long getPopulation() {
        long population = 0;
        for (Region region : this.regionGraph.getNodes()) {
            population += region.getPopulation();
        }
        
        return population;
    }
    
    public long getRecovered() {
        long recovered = 0;
        for (Region region : this.regionGraph.getNodes()) {
            recovered += region.getRecovered();
        }
        
        return recovered;
    }
    
    public long getDeaths() {
        long deaths = 0;
        for (Region region : this.regionGraph.getNodes()) {
            deaths += region.getDeaths();
        }
        
        return deaths;
    }
    
    public long getVaccinated() {
        long vaccinated = 0;
        for (Region region : this.regionGraph.getNodes()) {
            vaccinated += region.getVaccinated();
        }
        
        return vaccinated;
    }

    @Override
    public void update(int currentDay) {
        for (Edge<Region> edge : this.regionGraph.edges()) {
            edge.one.interact(edge.other, edge.weight);
        }
        
        for (Region region : this.regionGraph.getNodes()) {
            region.update(currentDay);
        }
        
        for (VaccinationFactory factory : this.factories){
            factory.update(currentDay);
        }
    }
}
