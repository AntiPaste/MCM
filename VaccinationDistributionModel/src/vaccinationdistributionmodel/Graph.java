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
	private double[][] weights;

	public Graph(List<T> cities, double[][] weights) {

	}

	public List<T> getNodes() {
		return this.nodes;
	}

	public double weight(T one, T two) {
		int i = this.nodes.indexOf(one);
		int j = this.nodes.indexOf(two);
		return this.weights[i][j];
	}

}
