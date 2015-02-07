/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel;

import vaccinationdistributionmodel.display.AreaChart;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.world.Globe;
import vaccinationdistributionmodel.world.Region;

/**
 *
 * @author ilari
 */
public class VaccinationDistributionModel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("<b>Ebola</b>");

        Globe globe = new Globe();
        
        int i=0;
        Region region;
        while (true){
            region = globe.getRegions().getNodes().get(i);
            if (region.getCities().size()>10) break;
            i++;
        }
        City city = region.getCities().get(0);
        int toChart = 4;
        while (toChart > 0){
            AreaChart chart = new AreaChart(city.getHistory());
            chart.draw();
            chart.pack();
            chart.setVisible(true);
            city = region.nearCities(city).get(0);
            toChart--;
        }

//        Parameters parameters = new Parameters();
//        parameters.contaminationRate = 1;
//        parameters.infectionRate = 0.1;
//        parameters.mortalityRate = 0.1;
//        parameters.recoveryRate = 0.1;
//
//        City city = new City(10000, 9900, 100, 0, 0, 0, 0);
//        city.setParameters(parameters);
//
//        System.out.println(city);
//
//        for (int i = 0; i < 100; i++) {
//            city.update(0);
//            System.out.println(city);
//
//            try {
//                //Thread.sleep(3000);
//            } catch (Exception e) {
//            }
//        }
//
//        AreaChart chart = new AreaChart(city.getHistory());
//        AreaChart chart = null;
//        chart.draw();
//        chart.pack();
//        chart.setVisible(true);
    }

}
