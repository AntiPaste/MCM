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
        
        amount = amount * ((int) ((double) this.susceptible) / (this.susceptible + this.exposed));
        
        if (amount > this.susceptible) {
            amount = this.susceptible;
        }
        
        this.exposedWaiting[0] = amount;
        this.susceptible -= amount;
        this.exposed += amount;
    }
    
    public void kill(long amount) {
        if (amount > this.infected) {
            amount = this.infected;
        }
        
        this.infected -= amount;
        this.dead += amount;
    }
    
    public void recover(long amount) {
        if (amount > this.infected) {
            amount = this.infected;
        }
        
        this.infected -= amount;
        this.recovered += amount;
    }
    
    public void infect() {
        for (int i = 0; i < this.exposedWaiting.length; i++) {
            long people = this.exposedWaiting[i];
            double probability = GlobalParameters.EXPOSED_PROBABILITIES[i];
            
            long result = (long) (people * probability);
            this.exposedWaiting[i] -= result;
            this.infected += result;
            this.exposed -= result;
        }
    }
    
    public void moveWaiting() {
        long people = this.exposedWaiting[this.exposedWaiting.length - 1];
        this.exposed += people;
        this.susceptible -= people;
        
        for (int i = this.exposedWaiting.length - 1; i > 0; i--) {
            this.exposedWaiting[i] = this.exposedWaiting[i - 1];
        }
        
        this.exposedWaiting[0] = 0;
    }

    public long getContaminating() {
        return this.advanced + this.infected;
    }
}
