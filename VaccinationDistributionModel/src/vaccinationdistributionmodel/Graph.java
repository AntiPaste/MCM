/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel;

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
