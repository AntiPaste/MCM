package vaccinationdistributionmodel.world;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import vaccinationdistributionmodel.Modelable;
import vaccinationdistributionmodel.display.History;
import vaccinationdistributionmodel.vaccination.VaccinationFactory;
import vaccinationdistributionmodel.vaccination.VaccinationSupplier;

public class Globe implements Modelable {

    private Graph<Region> regionGraph;
    private List<VaccinationFactory> factories;
    private History history = new History();
    public long days = 0;
    public long daysToOutbreakEnd = 42;

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
    
    public History getHistory() {
        return this.history;
    }

    private void initializeVaccinationScheme() {
        this.factories = new ArrayList<>();
        List<VaccinationSupplier> suppliers = new ArrayList<>();

        for (Region region : this.regionGraph.getNodes()) {
            City homecity = region.getGraph().getBigCities().get(0);
            VaccinationSupplier supplier = new VaccinationSupplier(region, homecity,
                    GlobalParameters.STARTING_DAY);

            suppliers.add(supplier);
        }

        for (Region bigRegion : this.regionGraph.getBigRegions()) {
            VaccinationFactory fac = new VaccinationFactory(suppliers, GlobalParameters.PRODUCTION_DAY);
            this.factories.add(fac);
        }

    }

    public Globe(List<Region> regions) {
        for (Region r : regions) {
            r.setBigCities(r.getCities().subList(0, 2));
        }

        Graph<Region> graph = new Graph();
        graph.makeRegionEdges(regions);
        this.regionGraph = graph;
    }

    public double cost() {
        long cost = 0;
        for (VaccinationFactory f : this.factories) {
            cost += f.vaccinationsGiven();
        }
        return ((double) (this.getPopulation() - this.getDeaths()) / cost);
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

    public long getExposed() {
        long exposed = 0;
        for (Region region : this.regionGraph.getNodes()) {
            exposed += region.getExposed();
        }

        return exposed;
    }

    public long getInfected() {
        long infected = 0;
        for (Region region : this.regionGraph.getNodes()) {
            infected += region.getInfected();
        }

        return infected;
    }

    public long getAdvanced() {
        long advanced = 0;
        for (Region region : this.regionGraph.getNodes()) {
            advanced += region.getAdvanced();
        }

        return advanced;
    }

    public long getSusceptible() {
        long susceptible = 0;
        for (Region region : this.regionGraph.getNodes()) {
            susceptible += region.getSusceptible();
        }

        return susceptible;
    }

    @Override
    public void update(int currentDay) {
        for (Edge<Region> edge : this.regionGraph.edges()) {
            edge.one.interact(edge.other, edge.weight);
        }

        for (Region region : this.regionGraph.getNodes()) {
            region.update(currentDay);
        }
       

        if (this.daysToOutbreakEnd > 0) {
            for (VaccinationFactory factory : this.factories) {
                factory.update(currentDay);
            }
        }
        
        if (this.daysToOutbreakEnd == 0) {
            System.out.println("Ebola has been eradicated on day " + this.days + "!");
            System.out.println("Vaccines used: " + this.getVaccinated());
            System.out.println("Deathtoll: " + this.getDeaths());
        }
        
        if (this.getAdvanced()==0 && this.getExposed() ==0 && this.getInfected() ==0){
            this.daysToOutbreakEnd--;
        }
        
        CityState state = new CityState((int) this.getPopulation());
        state.susceptible = 0;
        
        for (Region region : this.regionGraph.getNodes()) {
            for (City city : region.getCities()) {
                CityState values = city.getValues();
                state.susceptible += values.susceptible;
                state.exposedWaiting[0] += values.amountOfExposed();
                state.infected += values.infected;
                state.advanced += values.advanced;
                state.recovered += values.recovered;
                state.vaccinated += values.vaccinated;
                state.dead += values.dead;
            }
        }
        
        this.history.addState(state);
        this.days++;
    }
}
