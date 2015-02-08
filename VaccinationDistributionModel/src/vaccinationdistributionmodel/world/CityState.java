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
    public long[] advancedWaiting = new long[GlobalParameters.ADVANCED_PROBABILITIES.length];
    
    public int dead = 0;

    public CityState(int population) {
        this.population = population;
        this.susceptible = population;
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
    }

    public void vaccinate(long amount, boolean targetInfected) {
        if (targetInfected) {
            if (amount > this.infected) {
                amount = this.infected;
            }

            this.infected -= amount;
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
            this.exposed -= exposedHits;
            this.vaccinated += susceptibleHits + exposedHits;
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
            double probability = GlobalParameters.ADVANCED_PROBABILITIES[i];
            
            long result = (long) (people * probability);
            this.advancedWaiting[i] -= result;
            
            this.dead += result * mortalityRate;
            this.recovered += result * (1 - mortalityRate);
            
            this.advanced -= result;
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
