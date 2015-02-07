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

    private List<Edge<T>> adjancencyList;

    public Graph(List<Edge<T>> neighbours) {
        this.adjancencyList = neighbours;
    }

    public List<Edge<T>> edges() {
        return this.adjancencyList;
    }

}
