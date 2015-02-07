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
    private Map<T, List<Edge<T>>> neighbours;

    public Graph() {
        this.neighbours = new HashMap();
        this.edges = new HashSet<>();
    }
    
    public void setEdges(Set<Edge<T>> edges) {
        this.edges = edges;
        this.generateNodesList();
    }
    
    private void generateNodesList() {
        this.nodes = new ArrayList();
        for (Edge<T> edge : this.edges){
            if (!nodes.contains(edge.one)) nodes.add(edge.one);
            if (!nodes.contains(edge.other)) nodes.add(edge.other);
        }
    }
    
    public void makeCityEdges(List<City> cities) {
        this.edges = new HashSet();
        
        SortedMap<String, Edge<City>> distances = new TreeMap<String, Edge<City>>(new Comparator<String>() {
            @Override
            public int compare(String e1, String e2) {
                return (int) (Double.parseDouble(e1) - Double.parseDouble(e2));
            }
        });
        
        SortedSet<City> citiesByPopulation = new TreeSet(new Comparator<City>() {
            @Override
            public int compare(City c1, City c2) {
                return (c2.getValues().population - c1.getValues().population);
            }
        });
        
        for (City city : cities) {
            for (City other : cities) {
                if (city == other) continue;
                
                Double distance = this.distance(city.latitude, city.longitude, other.latitude, other.longitude);
                Edge<City> pair = new Edge(city, other, 1 / distance);
                distances.put(distance.toString(), pair);
            }
            
            citiesByPopulation.add(city);
        }
        
        List<City> bigCities = new ArrayList();
        int bigCityCount = (int) Math.sqrt(cities.size());
        Iterator<City> it = citiesByPopulation.iterator();
        
        for (int i = 0; it.hasNext() && i < bigCityCount; i++) {
            bigCities.add(it.next());
        }
        
        // List<City> bigCities - List of big cities
        // SortedSet<City> citiesByPopulation - List of cities sorted by population
        // Map<Edge<City>, Double> distances - Matrix of distances between cities
        
        for (City bigCity : bigCities) {
            for (City other : cities) {
                if (bigCity == other) continue;
                
                Edge<T> edge = new Edge(bigCity, other, 1 / this.distance(bigCity.latitude, bigCity.longitude, other.latitude, other.longitude));
                this.edges.add(edge);
            }
        }
        
        int citiesToConnect = (int) (cities.size() * Math.sqrt(cities.size()));
        for (Map.Entry<String, Edge<City>> entry : distances.entrySet()) {
            this.edges.add((Edge<T>) entry.getValue());
            if (citiesToConnect-- <= 0) break;
        }
        
        this.initMap();
        this.generateNodesList();
    }
    
    public void makeRegionEdges(List<Region> regions) {
        this.generateNodesList();
    }
    
    public List<T> getNodes(){
        return this.nodes;
    }
    
    private void initMap(){
        for (Edge<T> edge : this.edges){
            injectNodes(edge.one, edge);
            injectNodes(edge.other, edge);
        }
    }
    
    private void injectNodes(T node, Edge<T> edge){
        if (neighbours.containsKey(node)){
            List<Edge<T>> adjacent = this.neighbours.get(node);
            if (!adjacent.contains(edge)) adjacent.add(edge);
        }else{
            List<Edge<T>> adjacent = new ArrayList<>();
            adjacent.add(edge);
            neighbours.put(node,adjacent);
        }
    }
    
    public List<Edge<T>> getEdges(T node){
        if (this.neighbours == null){
            this.initMap();
        }
        return this.neighbours.get(node);
    }

    public Set<Edge<T>> edges() {
        return this.edges;
    }

    private double distance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double theta = longitude1 - longitude2;
        double distance = Math.sin(toRadians(latitude1)) * Math.sin(toRadians(latitude2)) + Math.cos(toRadians(latitude1)) * Math.cos(toRadians(latitude2)) * Math.cos(toRadians(theta));
        distance = Math.acos(distance);
        distance = toDegrees(distance);
        distance = distance * 60 * 1.1515 * 1.609344;

        return distance;
    }

    private double toRadians(double degrees) {
        return (degrees * Math.PI / 180.0);
    }

    private double toDegrees(double radians) {
        return (radians * 180 / Math.PI);
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
