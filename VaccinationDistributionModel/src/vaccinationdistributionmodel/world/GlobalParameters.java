package vaccinationdistributionmodel.world;

public class GlobalParameters {
    public static final double[] EXPOSED_PROBABILITIES = new double[] {
        0.01, 0.02, 0.04, 0.07, 0.10,
        0.15, 0.20, 0.27, 0.35, 0.42,
        0.50, 0.55, 0.60, 0.65, 0.70,
        0.75, 0.80, 0.85, 0.9, 0.95,
        1,
    };
    
    public static final double[] INFECTED_PROBABILITIES = new double[] {
        0.01, 0.04, 0.10, 0.15, 0.50,
        0.70, 0.85, 0.99, 1, 1,
    };
    
    public static final double[] ADVANCED_PROBABILITIES = new double[] {
        0.01, 0.01, 0.02, 0.05, 0.10,
        0.20, 0.50, 0.70, 0.80, 1,
    };
    
    public static final double GLOBAL_MORTALITY_RATE = 0.9;
          
}
