/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import vaccinationdistributionmodel.display.Chart;
import vaccinationdistributionmodel.display.MapChart;
import vaccinationdistributionmodel.vaccination.VaccinationSchedule;
import vaccinationdistributionmodel.world.City;
import vaccinationdistributionmodel.world.CityParameters;
import vaccinationdistributionmodel.world.Edge;
import vaccinationdistributionmodel.world.GlobalParameters;
import vaccinationdistributionmodel.world.Globe;
import vaccinationdistributionmodel.world.Graph;
import vaccinationdistributionmodel.world.Region;
import vaccinationdistributionmodel.world.RegionParameters;

/**
 *
 * @author ilari
 */
public class VaccinationDistributionModel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //chartRegionHubs(7);
        /*System.out.println("0.01 / 0.99");
        System.out.println();
        
        System.out.println("Mortality: ");
        for (int i = 0; i < GlobalParameters.ADVANCED_DAYS- 10; i++) {
            System.out.println(String.format("%.3f / %.3f", GlobalParameters.getMortalityProbabilities(0.01)[i], GlobalParameters.getMortalityProbabilities(0.99)[i]));
        }
        
        System.out.println();
        System.out.println("Recovery: ");
        
        for (int i = 0; i < GlobalParameters.ADVANCED_DAYS - 10; i++) {
            System.out.println(String.format("%.3f / %.3f", GlobalParameters.getRecoveryProbabilities(0.01)[i], GlobalParameters.getRecoveryProbabilities(0.99)[i]));
        }
        
        System.out.println();
        chartOneCity();*/
        int height = 1200;
        
        Globe globe = new Globe();
        globe.getRegions().getBigRegions().get(0).getCities().get(0).getValues().contaminate(500_000);
        
        MapChart m = new MapChart(globe, height * 2, height);
        m.pack();
        m.setSize(height * 2, height);
        m.setVisible(true);
        
        for (int i = 0;; i++) {
            globe.update(i);
            m.repaint();
            
            try {
                Thread.sleep(100);
            } catch (Exception e) {}
            
            System.out.println("Ran.");
        }
        
        /*chartOneCity();
        chartOneCityWithVaccination(0);
        chartOneCityWithVaccination(10);
        chartOneCityWithVaccination(20);*/
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
        parameters.contaminationRate = GlobalParameters.CONTAMINATION_RATE;
        parameters.mortalityRate = GlobalParameters.MORTALITY_RATE;
        
        int infected = 100;
        int population = 100_000;
        
        City city = new City(population, population-infected, infected, 0, 0,0,0);
        city.setParameters(parameters);
        city.name = "Surakarta";
        
        for (int i = 0; i < 120; i++) {
            city.update(i);
            try {
                //Thread.sleep(3000);
            } catch (Exception e) {
            }
        }

        System.out.println(city);

        Chart chart = new Chart(city.getHistory());
        chart.setTitle("no vaccines lol :P");
        chart.draw();
        chart.pack();
        chart.setVisible(true);
    }
    
    public static void chartOneCityWithVaccination(int delay, double ratio){
        
        CityParameters parameters = new CityParameters();
        parameters.contaminationRate = GlobalParameters.CONTAMINATION_RATE;
        parameters.mortalityRate = GlobalParameters.MORTALITY_RATE;
        
        int infected = 100;
        int population = 100_000;
        
        City city = new City(population, population-infected, infected, 0, 0,0,0);
        city.setParameters(parameters);
        city.name = "Surakarta";
        
        VaccinationSchedule plan = new VaccinationSchedule(city, delay, 100000, ratio); 
        
        for (int i = 0; i < 120; i++) {
            city.update(i);
            plan.update(i);
            try {
                //Thread.sleep(3000);
            } catch (Exception e) {
            }
        }

        System.out.println(city);

        Chart chart = new Chart(city.getHistory());
        chart.setTitle("vaccination in "+delay+" days "+ratio);
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
        while (citiesToChart > 0) {
            selected.add(city);
            city = region.nearCities(city).get(3);
            citiesToChart--;
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

    public static void palikat(){
        List<Region> regions = new ArrayList<>();
        
        regions.add(createRegion("Hienomesta"));
        //regions.add(createRegion("Ebolamesta"));
        
        regions.get(0).getCities().get(0).getValues().contaminate(10_000);
        
        for (int i = 0; i < 100; i++) {
            for (Region r: regions){
                r.update(i);
            }
        }
        
        
        
        regions.stream().forEach((Region r) -> {
            for (City c: r.getCities()){
            Chart chart = new Chart(c.getHistory());
            chart.setTitle(c.name);
            chart.draw();
            chart.pack();
            chart.setVisible(true);
            }
        });
    }
    
    public static Region createRegion(String name){
        Graph<City> cities = new Graph<>();
        RegionParameters params = new RegionParameters();
        List<City> list = new ArrayList<>();
        String[] names = {"Ankkalinna", "Helsinki", "Ouagadougou"};
        for (int i=0; i<3; i++){
            City c = new City(100000);
            c.name = name + "-" +names[i];
            list.add(c); 
        }
        cities.makeCityEdges(list);
        
        return new Region(params, cities);
    } 
}
