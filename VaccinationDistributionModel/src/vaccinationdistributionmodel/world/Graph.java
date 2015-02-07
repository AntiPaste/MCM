/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        this.nodes = new ArrayList();
    }
    
    public void makeRegionEdges(List<Region> regions) {
        this.nodes = new ArrayList();
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
