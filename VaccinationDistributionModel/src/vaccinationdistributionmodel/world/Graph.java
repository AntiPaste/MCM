/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel.world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author ilari
 * @param <T>
 */
public class Graph<T> {

    private Set<Edge<T>> edges;
    private List<T> nodes;
    private List<City> bigCities;
    private List<Region> bigRegions;
    private Map<T, List<Edge<T>>> neighbours;

    public Graph() {
        this.neighbours = new HashMap();
        this.edges = new HashSet();
        this.bigRegions = new ArrayList();
    }

    public void setEdges(Set<Edge<T>> edges) {
        this.edges = edges;
        this.generateNodesList();
    }

    private void generateNodesList() {
        this.nodes = new ArrayList();
        for (Edge<T> edge : this.edges) {
            if (!nodes.contains(edge.one)) {
                nodes.add(edge.one);
            }
            if (!nodes.contains(edge.other)) {
                nodes.add(edge.other);
            }
        }
    }

    public List<Region> getBigRegions() {
        return this.bigRegions;
    }
    
    public List<City> getBigCities() {
        return this.bigCities;
    }

    public void makeCityEdges(List<City> cities) {
        this.edges = new HashSet();
        this.nodes = (List<T>) cities;

        SortedMap<String, Edge<City>> distances = new TreeMap<String, Edge<City>>(new Comparator<String>() {
            @Override
            public int compare(String e1, String e2) {
                return (int) (Double.parseDouble(e1) - Double.parseDouble(e2));
            }
        });

        SortedSet<City> citiesByPopulation = new TreeSet(new Comparator<City>() {
            @Override
            public int compare(City c1, City c2) {
                return (int) (c2.getValues().population - c1.getValues().population);
            }
        });

        for (City city : cities) {
            for (City other : cities) {
                if (city == other) {
                    continue;
                }

                Double distance = this.distance(city.latitude, city.longitude, other.latitude, other.longitude);
                Edge<City> pair = new Edge(city, other, distance);
                distances.put(distance.toString(), pair);
            }

            citiesByPopulation.add(city);
        }

        this.bigCities = new ArrayList();
        int bigCityCount = (int) Math.sqrt(cities.size());
        Iterator<City> it = citiesByPopulation.iterator();

        for (int i = 0; it.hasNext() && i < bigCityCount; i++) {
            this.bigCities.add(it.next());
        }

        // List<City> bigCities - List of big cities
        // SortedSet<City> citiesByPopulation - List of cities sorted by population
        // Map<Edge<City>, Double> distances - Matrix of distances between cities
        for (City bigCity : this.bigCities) {
            for (City other : cities) {
                if (bigCity == other) {
                    continue;
                }

                Edge<T> edge = new Edge(bigCity, other, this.distance(bigCity.latitude, bigCity.longitude, other.latitude, other.longitude));
                this.edges.add(edge);
            }
        }

        int citiesToConnect = (int) (cities.size() * Math.sqrt(cities.size()));
        for (Map.Entry<String, Edge<City>> entry : distances.entrySet()) {
            this.edges.add((Edge<T>) entry.getValue());
            if (citiesToConnect-- <= 0) {
                break;
            }
        }

        this.initMap();
    }

    public void makeRegionEdges(List<T> regions) {
        this.nodes = regions;

        SortedSet<T> regionsByCityNumber = new TreeSet<>(new Comparator<T>() {
            @Override
            public int compare(T one, T other) {
                return ((Region) one).getCities().size() - ((Region) other).getCities().size();
            }
        });

        for (T region : regions) {
            regionsByCityNumber.add(region);
        }

        List<T> bigRegionsNow = new ArrayList<>();

        int n = (int) Math.sqrt(regions.size());
         
        for (int i = 0; i < n; i++) {
            T take = regionsByCityNumber.last();
            bigRegionsNow.add(take);
            regionsByCityNumber.remove(take);
        }

        // the main "hub" regions are all connected to one-another
        for (T one : bigRegionsNow) {
            for (T other : bigRegionsNow) {
                if (one == other) {
                    continue;
                }
                Region region = (Region) one;
                Region otherRegion = (Region) other;
                this.edges.add(new Edge<>(one, other, distance(
                        region.latitude, region.longitude, otherRegion.latitude, otherRegion.longitude)));
            }
        }

        // connect smaller regions to "hubs"
        for (T other : regionsByCityNumber) { // the not hubs will remain
            T closest = null;
            Region me = (Region) other;
            for (T hub : bigRegionsNow) {
                if (closest == null) {
                    closest = hub;
                    continue;
                }
                Region c = (Region) closest;
                Region h = (Region) hub;
                if (distance(me.latitude, me.longitude, c.latitude, c.longitude)
                        > distance(me.latitude, me.longitude, h.latitude, h.longitude)) {
                    // this hub is closer;
                    closest = hub;
                }
            }
            Region connect = (Region) closest;
            this.edges.add(new Edge<>(other, closest, distance(
                    me.latitude, me.longitude, connect.latitude, connect.longitude)));
        }

        PriorityQueue<Edge<Region>> heap = new PriorityQueue<>(new Comparator<Edge<Region>>() {
            @Override
            public int compare(Edge<Region> one, Edge<Region> other) {
                return (int) (other.distance - one.distance);
            }
        });

        int newEdges = 2000; // ~ n * sqrt(n)
        
        this.nodes.stream().map((r) -> {return (Region) r;}).forEach((one) -> {
            this.nodes.stream().map((r) -> {return (Region) r;}).filter((other) -> !(one == other)).forEach(
                    (other) -> {
                        double distance = distance(one.latitude, one.longitude, other.latitude, other.longitude);
                        if (heap.size() < newEdges){
                            Edge<Region> edge = new Edge<>(one, other, distance);
                            edge.distance = distance;
                            heap.add(edge);
                        }else{
                            if (distance < heap.peek().distance){
                                heap.poll();
                                Edge<Region> edge = new Edge<>(one,other, distance);
                                edge.distance = distance;
                                heap.add(edge);
                            }
                        }
            });
        });
        
        while (!heap.isEmpty()){
            this.edges.add((Edge<T>) heap.poll());
        }
        
        this.bigRegions = (List<Region>) bigRegionsNow;

        this.initMap();
        this.generateNodesList();
        
    }

    public List<T> getNodes() {
        return this.nodes;
    }

    private void initMap() {
        for (Edge<T> edge : this.edges) {
            injectNodes(edge.one, edge);
            injectNodes(edge.other, edge);
        }
    }

    private void injectNodes(T node, Edge<T> edge) {
        if (neighbours.containsKey(node)) {
            List<Edge<T>> adjacent = this.neighbours.get(node);
            if (!adjacent.contains(edge)) {
                adjacent.add(edge);
            }
        } else {
            List<Edge<T>> adjacent = new ArrayList<>();
            adjacent.add(edge);
            neighbours.put(node, adjacent);
        }
    }

    public List<Edge<T>> getEdges(T node) {
        if (this.neighbours == null) {
            this.initMap();
        }
        return this.neighbours.get(node);
    }

    public Set<Edge<T>> edges() {
        return this.edges;
    }

    public static double distance(City c1, City c2){
        return distance(c1.latitude, c1.longitude, c2.latitude, c2.longitude);
    }
    
    public static double distance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double theta = longitude1 - longitude2;
        double distance = Math.sin(toRadians(latitude1)) * Math.sin(toRadians(latitude2)) + Math.cos(toRadians(latitude1)) * Math.cos(toRadians(latitude2)) * Math.cos(toRadians(theta));
        distance = Math.acos(distance);
        distance = toDegrees(distance);
        distance = distance * 60 * 1.1515 * 1.609344;

        return distance;
    }
    
    public static double toRadians(double degrees) {
        return (degrees * Math.PI / 180.0);
    }

    public static double toDegrees(double radians) {
        return (radians * 180.0 / Math.PI);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<T, List<Edge<T>>> entry : this.neighbours.entrySet()) {
            builder.append(String.format("%s: ", entry.getKey().toString()));

            for (Edge<T> edge : entry.getValue()) {
                if (edge.one == entry.getKey()) {
                    builder.append(String.format("%s, ", edge.other.toString()));
                } else {
                    builder.append(String.format("%s, ", edge.one.toString()));
                }
            }

            builder.setLength(builder.length() - 2);
            builder.append("\n");
        }

        return builder.toString();
    }

}
