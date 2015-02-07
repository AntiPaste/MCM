/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel.world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    private List<Edge<T>> edges;
    private List<T> nodes;
    private Map<T, List<Edge<T>>> neighbours;

    public Graph() {
        this.neighbours = new HashMap();
    }
    
    public void setEdges(List<Edge<T>> edges) {
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
        this.edges = new ArrayList();
        
        SortedMap<Edge<City>, Double> distances = new TreeMap(new Comparator<Map.Entry<Edge<City>, Double>>() {
            @Override
            public int compare(Map.Entry<Edge<City>, Double> e1, Map.Entry<Edge<City>, Double> e2) {
                return (int) (e1.getValue() - e2.getValue());
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
                
                Edge<City> pair = new Edge(city, other, 0);
                distances.put(pair, this.distance(city.latitude, city.longitude, other.latitude, other.longitude));
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
                
                Edge<T> edge = new Edge(bigCity, other, 0);
                this.edges.add(edge);
            }
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

    public List<Edge<T>> edges() {
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

}
