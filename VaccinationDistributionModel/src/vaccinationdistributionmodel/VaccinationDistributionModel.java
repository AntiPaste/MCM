/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel;

import java.util.ArrayList;
import java.util.List;
import vaccinationdistributionmodel.display.AreaChart;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.world.Globe;
import vaccinationdistributionmodel.world.Parameters;
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
        //System.out.println("<b>Ebola</b>");

        Globe globe = new Globe();

        int i = 0;
        Region region;
        while (true) {
            region = globe.getRegions().getNodes().get(i);
            if (region.getCities().size() > 10) {
                break;
            }
            i++;
        }
        List<City> selected = new ArrayList<>();

        City city = region.getCities().get(0);
        city.getValues().infect(500_000);
        System.out.println(city.name+" is now TAINTED with <b>EBOLA</b> !!!");
        int toChart = 4;
        while (toChart > 0) {
            selected.add(city);
            city = region.nearCities(city).get(3);
            toChart--;
        }

        for (int j = 0; j < 10_000; j++) {
            for (City c : selected){
                c.update(j);
            }
        }

        selected.stream().forEach((chartable) -> {
            System.out.println(chartable);
            
            AreaChart chart = new AreaChart(chartable.getHistory());
            chart.draw();
            chart.pack();
            chart.setVisible(true);
        });

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
