package vaccinationdistributionmodel.world;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import vaccinationdistributionmodel.Modelable;

public class Globe implements Modelable {

    private Graph<Region> regionGraph;

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
                this.giveParams(city);

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
            Region region = new Region(new Parameters(), graph);
            region.setBigCities(graph.getBigCities());
            regions.add(region);
        }

        Graph<Region> graph = new Graph();
        graph.makeRegionEdges(regions);
        this.regionGraph = graph;
    }

    private void giveParams(City c) {

        Parameters parameters = new Parameters();
        parameters.contaminationRate = 1;
        parameters.mortalityRate = 0.1;
        parameters.recoveryRate = 0.1;

        c.setParameters(parameters);
    }

    public Graph<Region> getRegions() {
        return this.regionGraph;
    }

    @Override
    public void update(int currentDay) {
        for (Edge<Region> edge : this.regionGraph.edges()) {
            edge.one.interact(edge.other, edge.weight);
        }
    }
}
