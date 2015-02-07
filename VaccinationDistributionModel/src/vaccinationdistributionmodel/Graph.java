/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel;

import java.util.List;

/**
 *
 * @author ilari
 * @param <T>
 */
public class Graph<T> {

    private List<T> nodes;
    private List<List<Edge<T>>> adjancencyList;

    public Graph(List<T> cities, List<List<Edge<T>>> neighbours) {
        this.nodes = cities;
        this.adjancencyList = neighbours;
    }

    public List<T> getNodes() {
        return this.nodes;
    }

    public List<Edge<T>> neighbours(T element) {
        return this.adjancencyList.get(this.nodes.indexOf(element));
    }

}
