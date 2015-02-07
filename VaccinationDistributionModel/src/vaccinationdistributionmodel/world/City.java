package vaccinationdistributionmodel;

public class City implements Modelable {

    private History history;
    private CityState values;
    private Parameters parameters;

    public City(int population) {
        this(population, 0, 0, 0, 0, 0, 0);
    }

    public City(int population, int susceptible, int exposed,
            int infected, int advanced, int recovered, int dead) {
        this.values = new CityState(population, susceptible, exposed,
                infected, advanced, recovered, dead);

        this.history = new History();
        this.history.addState(this.values);
    }

    public History getHistory() {
        return this.history;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public void interact(City other, double weight) {
		
    }

    public void vaccinate(int amount, boolean targetInfected) {
        this.values.vaccinate(amount, targetInfected);
    }

    @Override
    public void update() {
        // infected -> recovered
        int peopleToRecover = (int) (this.values.infected * this.parameters.recoveryRate);

        this.values.infected -= peopleToRecover;
        this.values.recovered += peopleToRecover;

        // infected -> removed
        int peopleToKill = (int) (this.values.infected * this.parameters.mortalityRate);

        this.values.infected -= peopleToKill;
        this.values.dead += peopleToKill;

        // exposed -> infected
        int peopleToInfect = (int) (this.values.exposed * this.parameters.infectionRate);

        this.values.exposed -= peopleToInfect;
        this.values.infected += peopleToInfect;

        // susceptible -> exposed
        int peopleToContaminate = (int) ((this.values.susceptible * this.values.infected * this.parameters.contaminationRate)
                / (this.values.susceptible + this.values.infected));

        this.values.susceptible -= peopleToContaminate;
        this.values.exposed += peopleToContaminate;

        this.history.addState(this.values);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        double fPopulation = (double) this.values.population;

        builder.append(String.format("Population: %d\n", this.values.population));
        builder.append(String.format("Susceptible: %d (%.2f%%)\n", this.values.susceptible, this.values.susceptible / fPopulation * 100));
        builder.append(String.format("Exposed: %d (%.2f%%)\n", this.values.exposed, this.values.exposed / fPopulation * 100));
        builder.append(String.format("Infected: %d (%.2f%%)\n", this.values.infected, this.values.infected / fPopulation * 100));
        builder.append(String.format("Advanced: %d (%.2f%%)\n", this.values.advanced, this.values.advanced / fPopulation * 100));
        builder.append(String.format("Recovered: %d (%.2f%%)\n", this.values.recovered, this.values.recovered / fPopulation * 100));
        builder.append(String.format("Dead: %d (%.2f%%)\n", this.values.dead, this.values.dead / fPopulation * 100));

        return builder.toString();
    }
}
