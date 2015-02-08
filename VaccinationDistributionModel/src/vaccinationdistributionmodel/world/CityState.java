package vaccinationdistributionmodel.world;

public class CityState {

    public long population;

    public long susceptible;
    public long exposed = 0;
    public long infected = 0;
    public long advanced = 0;
    public long recovered = 0;
    public long vaccinated = 0;

    public long[] exposedWaiting = new long[GlobalParameters.EXPOSED_PROBABILITIES.length];
    public long[] infectedWaiting = new long[GlobalParameters.INFECTED_PROBABILITIES.length];
    public long[] advancedWaiting = new long[GlobalParameters.ADVANCED_DAYS];
    
    public int dead = 0;

    public CityState(int population) {
        this(population, population, 0, 0, 0, 0, 0);
    }

    public CityState(int population, int susceptible, int exposed,
            int infected, int advanced, int recovered, int dead) {
        this.population = population;
        this.susceptible = susceptible;
        this.exposed = exposed;
        this.infected = infected;
        this.advanced = advanced;
        this.recovered = recovered;
        this.dead = dead;
        
        this.exposedWaiting[0] = exposed;
        this.infectedWaiting[0] = infected;
        this.advancedWaiting[0] = advanced;
    }
    
    public void vaccinateExposed(long amount){
        long shots = amount;
        for (int i = 0; i < this.exposedWaiting.length; i++){
            long heal = amount * ((long) (this.exposedWaiting[i]/((double)exposed)));
            if (heal > this.exposedWaiting[i]) heal = this.exposedWaiting[i];
            if (heal > shots) heal = shots;
            shots -= heal;
            this.exposedWaiting[i] -= heal;
        }
        if (shots != 0) System.out.println("WASTING VACCINES!"); 
        this.exposed -= amount;
        this.vaccinated += amount;
    }

    public void vaccinate(long amount, boolean targetInfected) {
        if (targetInfected) {
            if (amount > this.infected) {
                amount = this.infected;
            }
            this.infected -= amount;
            for (int i=this.infectedWaiting.length-1; i>=0; i--){
                if (this.infectedWaiting[i] < amount){
                    amount -= this.infectedWaiting[i];
                    this.infectedWaiting[i] = 0;
                } else {
                    this.infectedWaiting[i] -= amount;
                    break;
                }
            }
            this.vaccinated += amount;
        } else {
            long susceptibleHits = amount * this.susceptible / (this.susceptible + this.exposed);
            long exposedHits = amount - susceptibleHits;
            if (susceptibleHits > this.susceptible) {
                susceptibleHits = this.susceptible;
            }
            if (exposedHits > this.exposed) {
                exposedHits = this.exposed;
            }
            this.susceptible -= susceptibleHits;
            this.vaccinated += susceptibleHits;
            
            vaccinateExposed(exposedHits);
        }
    }

    public void contaminate(long amount) {
        // System.out.println(amount * (int) ((double) this.susceptible) / (this.susceptible + this.exposed));
        // = 721
        
        double dSusceptible = (double) this.susceptible;
        double base = (double) (this.susceptible + this.exposed);
        double ratio = (dSusceptible / base);
        amount = (long) (amount * ratio);
        
        if (amount > this.susceptible) {
            amount = this.susceptible;
        }
        
        this.exposedWaiting[0] += amount;
        this.susceptible -= amount;
        this.exposed += amount;
    }
    
    public void remove(double mortalityRate) {
        for (int i = 0; i < this.advancedWaiting.length; i++) {
            long people = this.advancedWaiting[i];
            
            double mortalityProbability = GlobalParameters.getMortalityProbabilities(mortalityRate)[i];
            double recoveryProbability = GlobalParameters.getRecoveryProbabilities(mortalityRate)[i];
            
            long total = (long) (people * mortalityProbability + people * recoveryProbability);
            this.advancedWaiting[i] -= total;
            
            this.dead += people * mortalityProbability;
            this.recovered += people * recoveryProbability;
            
            this.advanced -= total;
        }
    }
    
    public void advance() {
        for (int i = 0; i < this.infectedWaiting.length; i++) {
            long people = this.infectedWaiting[i];
            double probability = GlobalParameters.INFECTED_PROBABILITIES[i];
            
            long result = (long) (people * probability);
            this.infectedWaiting[i] -= result;
            this.advancedWaiting[0] += result;
            
            this.advanced += result;
            this.infected -= result;
        }
    }
    
    public void infect() {
        for (int i = 0; i < this.exposedWaiting.length; i++) {
            long people = this.exposedWaiting[i];
            double probability = GlobalParameters.EXPOSED_PROBABILITIES[i];
            
            long result = (long) (people * probability);
            this.exposedWaiting[i] -= result;
            this.infectedWaiting[0] += result;
            
            this.infected += result;
            this.exposed -= result;
        }
    }
    
    public void moveWaiting() {
        /*long people = this.exposedWaiting[this.exposedWaiting.length - 1];
        this.exposed += people;
        this.susceptible -= people;*/
        
        for (int i = this.advancedWaiting.length - 1; i > 0; i--) {
            this.advancedWaiting[i] = this.advancedWaiting[i - 1];
        }
        
        for (int i = this.infectedWaiting.length - 1; i > 0; i--) {
            this.infectedWaiting[i] = this.infectedWaiting[i - 1];
        }
        
        for (int i = this.exposedWaiting.length - 1; i > 0; i--) {
            this.exposedWaiting[i] = this.exposedWaiting[i - 1];
        }
        
        this.exposedWaiting[0] = 0;
        this.infectedWaiting[0] = 0;
        this.advancedWaiting[0] = 0;
    }

    public long getContaminating() {
        return this.advanced + this.infected;
    }
    
    public void printWaiting() {
        System.out.print("ExposedWaiting: ");
        for (int i = 0; i < this.exposedWaiting.length - 1; i++) {
            System.out.print(this.exposedWaiting[i] + " ");
        }
        
        System.out.println();
        
        System.out.print("InfectedWaiting: ");
        for (int i = 0; i < this.infectedWaiting.length - 1; i++) {
            System.out.print(this.infectedWaiting[i] + " ");
        }
        
        System.out.println();
        
        System.out.print("AdvancedWaiting: ");
        for (int i = 0; i < this.advancedWaiting.length - 1; i++) {
            System.out.print(this.advancedWaiting[i] + " ");
        }
        
        System.out.println();
        System.out.println();
    }
}
