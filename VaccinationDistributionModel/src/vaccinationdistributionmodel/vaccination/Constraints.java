/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

/**
 *
 * @author ilari
 */
public class Constraints {
    public int maximumDailyVaccination;
    public int maximumDailyProduction;
            
    public Constraints(int maxVaccination, int maxProduction){
        this.maximumDailyVaccination = maxVaccination;
        this.maximumDailyProduction = maxProduction;
    }
}