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
 */
public class Graph {
    private ArrayList<City> cities;
    private double[][] weights;
    
    public Graph(ArrayList<City> cities, double[][] weights){
        
    }
    
    public ArrayList<City> getCities(){
        return this.cities;
    }
}
