package vaccinationdistributionmodel.world;

public class GlobalParameters {

    public static final double[] EXPOSED_PROBABILITIES = new double[]{
        0.000, 0.020, 0.088, 0.177, 0.225,
        0.252, 0.291, 0.283, 0.297, 0.349,
        0.338, 0.376, 0.377, 0.444, 0.304,
        0.333, 0.375, 0.500, 0.600, 0.750,
        1.000
    };

    public static final double[] INFECTED_PROBABILITIES = new double[]{
        0.076, 0.103, 0.149, 0.222, 0.269,
        0.249, 0.315, 0.369, 0.365, 0.287,
        0.242, 0.319, 0.375, 0.500, 0.600,
        0.750, 1.000
    };
    
    public static final int ADVANCED_DAYS = 27;

    public static final double MORTALITY_RATE = 0.4;
    public static final double CONTAMINATION_RATE = 0.27;
    public static final double INTERCITY_CONSTANT = 100.0;
    public static final double INTERCITY_WEIGHT_CONSTANT = 10000.0;
    public static int PRODUCTION_DAY = 365;
    public static int STARTING_DAY = PRODUCTION_DAY + 2;
    public static final double VACCINATION_CONSTANT = 0.0001;

    public static double[] getRecoveryProbabilities(double deathRate) {
        double[] data = {1, 2, 4, 7, 11, 14, 17, 19, 20, 21, 20, 19, 18, 16, 14, 13, 11, 10, 9, 7, 5, 4, 3, 2, 2, 1, 1};
        double[] data2 = {1, 25, 23, 15, 12, 10, 8, 6, 5, 4, 3, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[] muData = new double[data.length];
        
        int days = data.length;
        double mu = (271.0 * deathRate) / (117.0 * (1.0 - deathRate));
        
        for (int i = 0; i < data.length; i++) {
            muData[i] = data2[i] * mu;
        }
        
        double[] sums = new double[days];
        double[] sums2 = new double[days];
        double[] recovery = new double[days];
        
        sums[0] = data[0];
        sums2[0] = muData[0];
        for (int i = 1; i < days; i++) {
            sums[i] += data[i] + sums[i - 1];
            sums2[i] += muData[i] + sums2[i - 1];
        }
        recovery[0] = data[0] / (sums[days - 1] + sums2[days - 1]);
        for (int i = 1; i < days; i++) {
            recovery[i] = data[i] / (sums[days - 1] + sums2[days - 1] - sums[i - 1] - sums2[i - 1]);
        }
        
        return recovery;
    }
    
    public static double[] getMortalityProbabilities(double deathRate) {
        double[] data = {1, 2, 4, 7, 11, 14, 17, 19, 20, 21, 20, 19, 18, 16, 14, 13, 11, 10, 9, 7, 5, 4, 3, 2, 2, 1, 1};
        double[] data2 = {1, 25, 23, 15, 12, 10, 8, 6, 5, 4, 3, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[] muData = new double[data.length];
        
        int days = data.length;
        double mu = (271.0 * deathRate) / (117.0 * (1.0 - deathRate));
        
        for (int i = 0; i < data.length; i++) {
            muData[i] = data2[i] * mu;
        }
        
        double[] sums = new double[days];
        double[] sums2 = new double[days];
        double[] mortality = new double[days];
        
        sums[0] = data[0];
        sums2[0] = mu * data2[0];
        for (int i = 1; i < days; i++) {
            sums[i] += data[i] + sums[i - 1];
            sums2[i] += muData[i] + sums2[i - 1];
        }
        
        mortality[0] = muData[0] / (sums[days - 1] + sums2[days - 1]);
        for (int i = 1; i < days; i++) {
            mortality[i] = muData[i] / (sums[days - 1] + sums2[days - 1] - sums[i - 1] - sums2[i - 1]);
        }
        
        return mortality;
    }
}
