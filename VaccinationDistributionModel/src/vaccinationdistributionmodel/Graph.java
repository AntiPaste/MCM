/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel;

import java.util.ArrayList;

/**
 *
 * @author ilari
 * @param <T>
 */
public class Graph <T> {
    private ArrayList<T> cities;
    private double[][] weights;
    
    public Graph(ArrayList<T> cities, double[][] weights){
        
    }
    
    public ArrayList<T> getCities(){
        return this.cities;
    }
    
    public double weight(T one, T two){
        int i = cities.indexOf(one);
        int j = cities.indexOf(two);
        return weights[i][j];
    }
    
    
}
