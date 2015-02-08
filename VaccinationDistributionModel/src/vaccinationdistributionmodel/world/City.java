package vaccinationdistributionmodel.world;

import vaccinationdistributionmodel.display.History;
import vaccinationdistributionmodel.Modelable;

public class City implements Modelable {

    public String name = "";
    public double latitude = 0.0;
    public double longitude = 0.0;
    
    private History history;
    private CityState values;
    private CityParameters parameters;

    public City(int population) {
        this(population, population, 0, 0, 0, 0, 0);
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
    
    public CityState getValues() {
        return this.values;
    }

    public void setParameters(CityParameters parameters) {
        this.parameters = parameters;
    }
    
    public void setInformation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void interact(City other, double weight) {
        // infect this city
        int contaminations = (int) (other.values.getContaminating()*weight);
        this.values.contaminate(contaminations);
        
        // infect the other city
        contaminations = (int) (this.values.getContaminating()*weight);
        other.values.contaminate(contaminations);
    }

    public void vaccinate(int amount, boolean targetInfected) {
        this.values.vaccinate(amount, targetInfected);
    }

    @Override
    public void update(int currentDay) {
        this.values.moveWaiting();
        
        // advanced -> dead / recovered
        this.values.remove(this.parameters.mortalityRate);
        
        // infected -> advanced
        this.values.advance();

        // exposed -> infected
        this.values.infect();

        // susceptible -> exposed
        int peopleToContaminate = (int) ((this.values.susceptible * this.values.getContaminating() * this.parameters.contaminationRate)
                / this.values.population);
        this.values.contaminate(peopleToContaminate);
        
        /*if (this.name.equals("Surakarta"))
            this.values.printWaiting();*/
        
        this.history.addState(this.values);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        double fPopulation = (double) this.values.population;
        builder.append(String.format("Name: %s\n", this.name));
        builder.append(String.format("Population: %d\n", this.values.population));
        builder.append(String.format("Susceptible: %d (%.2f%%)\n", this.values.susceptible, this.values.susceptible / fPopulation * 100));
        builder.append(String.format("Exposed: %d (%.2f%%)\n", this.values.amountOfExposed(), this.values.amountOfExposed() / fPopulation * 100));
        builder.append(String.format("Infected: %d (%.2f%%)\n", this.values.infected, this.values.infected / fPopulation * 100));
        builder.append(String.format("Advanced: %d (%.2f%%)\n", this.values.advanced, this.values.advanced / fPopulation * 100));
        builder.append(String.format("Recovered: %d (%.2f%%)\n", this.values.recovered, this.values.recovered / fPopulation * 100));
        builder.append(String.format("Dead: %d (%.2f%%)\n", this.values.dead, this.values.dead / fPopulation * 100));

        return builder.toString();
    }
}
