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

    @Override
    public int hashCode() {
        return (this.one.hashCode() + this.other.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        
        if (this.getClass() != obj.getClass())
            return false;
        
        Edge<T> other = (Edge<T>) obj;
        return ((this.one == other.one && this.other == other.other)
                || (this.one == other.other && this.other == other.one));
    }

    
}
