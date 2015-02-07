package vaccinationdistributionmodel.world;

public class CityState {

	public int population;

	public int susceptible;
	public int exposed = 0;
	public int infected = 0;
	public int advanced = 0;
	public int recovered = 0;
	public int vaccinated = 0;

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

	public void vaccinate(int amount, boolean targetInfected) {
		if (targetInfected) {
			if (amount > this.infected) {
				amount = this.infected;
			}
			
			this.infected -= amount;
			this.vaccinated += amount;
		} else {
			int susceptibleHits = amount * this.susceptible / (this.susceptible + this.exposed);
			int exposedHits = amount - susceptibleHits;
			
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
        
        private void expose(int amount){
            if (amount > this.susceptible) amount = this.susceptible;
            this.exposed += amount;
            this.susceptible -= amount;
        }
        
        public void infect(int amount){
            expose(amount*(this.susceptible)/(this.susceptible+this.exposed));
        }
        
        public int getContaminating(){
            return this.advanced + this.infected;
        }
}
