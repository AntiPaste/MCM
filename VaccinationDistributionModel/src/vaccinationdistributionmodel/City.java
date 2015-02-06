package vaccinationdistributionmodel;

public class City implements Modelable {
	private int population;
	
	private int susceptible;
	private int exposed = 0;
	private int infected = 0;
	private int advanced = 0;
	private int recovered = 0;
	
	private int dead = 0;
	
	private Parameters parameters;
	
	public City(int population) {
		this.population = population;
		this.susceptible = population;
	}
	
	public City(int population, int susceptible, int exposed,
			int infected, int advanced, int recovered, int dead) {
		this.population = population;
		this.susceptible = susceptible;
		this.exposed = exposed;
		this.infected = infected;
		this.advanced = advanced;
		this.recovered = recovered;
		this.dead = dead;
	}
	
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public void update() {
		// susceptible -> infected
		int peopleToInfect = (int)
				((this.susceptible * this.infected * this.parameters.infectionRate)
				/ (this.susceptible + this.infected));
		
		this.susceptible -= peopleToInfect;
		this.infected += peopleToInfect;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		double fPopulation = (double) this.population;
		
		builder.append(String.format("Population: %d\n", this.population));
		builder.append(String.format("Susceptible: %d (%.2f%%)\n", this.susceptible, this.susceptible / fPopulation * 100));
		builder.append(String.format("Exposed: %d (%.2f%%)\n", this.exposed, this.exposed / fPopulation * 100));
		builder.append(String.format("Infected: %d (%.2f%%)\n", this.infected, this.infected / fPopulation * 100));
		builder.append(String.format("Advanced: %d (%.2f%%)\n", this.advanced, this.advanced / fPopulation * 100));
		builder.append(String.format("Recovered: %d (%.2f%%)\n", this.recovered, this.recovered / fPopulation * 100));
		builder.append(String.format("Dead: %d (%.2f%%)\n", this.dead, this.dead / fPopulation * 100));
		
		return builder.toString();
	}
}
