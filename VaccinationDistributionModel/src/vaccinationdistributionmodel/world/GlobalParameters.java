package vaccinationdistributionmodel.world;

public class GlobalParameters {
    public static final double[] EXPOSED_PROBABILITIES = new double[] {
        0.000, 0.020, 0.088, 0.177, 0.225,
        0.252, 0.291, 0.283, 0.297, 0.349,
        0.338, 0.376, 0.377, 0.444, 0.304,
        0.333, 0.375, 0.500, 0.600, 0.750,
        1.000,
    };
    
    public static final double[] INFECTED_PROBABILITIES = new double[] {
        0.076, 0.103, 0.149, 0.222, 0.269,
        0.249, 0.315, 0.369, 0.365, 0.287,
        0.242, 0.319, 0.375, 0.500, 0.600,
        0.750, 1.000
    };
    
    public static final double[] ADVANCED_PROBABILITIES = new double[] {
        0.01, 0.01, 0.02, 0.05, 0.10,
        0.20, 0.50, 0.70, 0.80, 1,
    };
    
    public static final double MORTALITY_RATE = 0.4;
    public static final double CONTAMINATION_RATE = 0.27;
    
    public static double[] getAdvancedProbabilities() {
        int days = 0;
        double[] probabilities = new double[days];
        return probabilities;
    }
}
