/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel.world;

/**
 *
 * @author ilari
 */
public class Edge<T> {

    public T one;
    public T other;
    public double weight;

    public Edge() {
    }

    public Edge(T one, T other, double weight) {
        this.one = one;
        this.other = other;
        this.weight = weight;
    }
}
