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
public class RegionParameters {
    
    double mobility;
    double hygiene;
    
    public RegionParameters(double mobility, double hygiene) {
        this.mobility = mobility;
        this.hygiene = hygiene;
    }
}
