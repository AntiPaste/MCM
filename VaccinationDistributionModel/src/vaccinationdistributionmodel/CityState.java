package vaccinationdistributionmodel;

public class CityState {

	public int population;

	public int susceptible;
	public int exposed = 0;
	public int infected = 0;
	public int advanced = 0;
	public int recovered = 0;

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
}
