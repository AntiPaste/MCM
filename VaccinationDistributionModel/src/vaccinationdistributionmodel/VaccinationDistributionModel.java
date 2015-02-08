/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import vaccinationdistributionmodel.display.AreaChart;
import vaccinationdistributionmodel.display.Chart;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.world.CityParameters;
import vaccinationdistributionmodel.world.Edge;
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
        chartRegionHubs(7);
    }
    
    public static void sth(){
        //System.out.println("<b>Ebola</b>");

        /*City x = new City(100000, 90000, 0, 10000, 0, 0, 0);
        
        CityParameters params = new CityParameters();
        params.contaminationRate = 0.1;
        params.mortalityRate = 0.1;
        
        x.setParameters(params);
        
        System.out.println(x);
        
        for (int i = 0; i < 100; i++) {
            x.update(i);
        }
        
        Chart y = new Chart(x.getHistory());
        y.setTitle(x.name);
        y.draw();
        y.pack();
        y.setVisible(true);
        
        try {
            Thread.sleep(1000000);
        } catch (Exception e) {}*/
    }
    
    public static void chartOneCity(){
        
        CityParameters parameters = new CityParameters();
        
        City city = new City(10000, 9900, 100, 0, 0, 0, 0);
        city.setParameters(parameters);

        System.out.println(city);

        for (int i = 0; i < 100; i++) {
            city.update(0);
            System.out.println(city);

            try {
                //Thread.sleep(3000);
            } catch (Exception e) {
            }
        }

        AreaChart chart = new AreaChart(city.getHistory());
        chart.draw();
        chart.pack();
        chart.setVisible(true);
    }
    
    public static void chartSomeCities(int citiesToChart){
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
        city.getValues().contaminate(500_000);
        int toChart = 4;
        while (toChart > 0) {
            selected.add(city);
            city = region.nearCities(city).get(3);
            toChart--;
        }

        for (int j = 0; j < 100; j++) {
            region.update(j);
        }

        selected.stream().forEach((chartable) -> {
            System.out.println(chartable);
            
            Chart chart = new Chart(chartable.getHistory());
            chart.setTitle(chartable.name);
            chart.draw();
            chart.pack();
            chart.setVisible(true);
        });
    }
    
    public static void chartRegionHubs(int hubsToChart){
        
        Globe globe = new Globe();
        
        List<Region> regions = new ArrayList<>();
        
        for (int i=0; i<hubsToChart; i++){
            if (regions.isEmpty()){
                regions.add(globe.getRegions().getNodes().get(3));
            }
            Region last = regions.get(regions.size()-1);
            for (Edge<Region> e: globe.getRegions().getEdges(last)){
                Region next = e.one;
                if (e.one==last) next = e.other;
                if (regions.contains(next)){
                    continue;
                }
                regions.add(next);
            }
        }
        
        if (regions.size() < hubsToChart) System.out.println("Caught in a trap :(");
        
        List<City> hubs = new ArrayList<>(regions.stream().map((Region r) -> {
            return r.getGraph().getBigCities().get(0);
        }).collect(Collectors.toList()));
        
        for (City city : hubs){
            
        }
    }

}
