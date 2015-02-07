package vaccinationdistributionmodel.world;

public class CityState {

    public long population;

    public long susceptible;
    public long exposed = 0;
    public long infected = 0;
    public long advanced = 0;
    public long recovered = 0;
    public long vaccinated = 0;

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

    private void expose(long amount) {
        if (amount > this.susceptible) {
            amount = this.susceptible;
        }
        
        if (amount < 0) {
            System.out.println(amount);
        }
        
        this.exposed += amount;
        this.susceptible -= amount;
    }

    public void infect(int amount) {
        // System.out.println(amount * (int) ((double) this.susceptible) / (this.susceptible + this.exposed));
        // = 721
        
        this.expose(amount * ((int) ((double) this.susceptible) / (this.susceptible + this.exposed)));
    }

    public long getContaminating() {
        return this.advanced + this.infected;
    }
}
