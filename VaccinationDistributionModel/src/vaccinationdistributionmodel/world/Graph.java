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

    public Graph(List<Edge<T>> edges) {
        this.edges = edges;
        this.generateNodesList();
    }
    
    private void generateNodesList(){
        this.nodes = new ArrayList<>();
        for (Edge<T> edge : this.edges){
            if (!nodes.contains(edge.one)) nodes.add(edge.one);
            if (!nodes.contains(edge.other)) nodes.add(edge.other);
        }
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

}
