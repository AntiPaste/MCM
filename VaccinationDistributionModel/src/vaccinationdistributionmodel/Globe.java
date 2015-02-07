package vaccinationdistributionmodel;

import java.util.ArrayList;
import java.util.List;

public class Globe implements Modelable {

    private Graph regionGraph;
    
    public Globe() {
        List<Region> regions = new ArrayList();
        
        City h = new City(10000);
        City i = new City(200000);
        City j = new City(20000);
        City k = new City(100000);
        
        List<City> cities = new ArrayList();
        cities.add(h);
        cities.add(i);
        
        List<Edge<City>> edges = new ArrayList();
        edges.add(new Edge(h, i, 1));
        
        Graph<City> cityGraph = new Graph(edges);
        
        Region rH = new Region(new Parameters(), cityGraph);
        regions.add(rH);
        
        cities.clear();
        cities.add(j);
        cities.add(k);
        
        edges.clear();
        edges.add(new Edge(j, k, 1));
        
        cityGraph = new Graph(edges);
        
        Region rI = new Region(new Parameters(), cityGraph);
        regions.add(rI);
        
        List<Edge<Region>> regionEdges = new ArrayList();
        edges.add(new Edge(rH, rI, 1));
        
        this.regionGraph = new Graph(regionEdges);
    }
    
    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
