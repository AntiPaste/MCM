/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationdistributionmodel;

/**
 *
 * @author ilari
 */
public class VaccinationDistributionModel {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		System.out.println("<b>Ebola</b>");
		
		Parameters parameters = new Parameters();
		parameters.contaminationRate = 1;
		parameters.infectionRate = 0.1;
		parameters.mortalityRate = 0.1;
		parameters.recoveryRate = 0.1;
		
		City city = new City(10000, 9900, 100, 0, 0, 0, 0);
		city.setParameters(parameters);
		
		System.out.println(city);
		
		for (int i = 0; i < 100; i++) {
			city.update();
			System.out.println(city);
			
			try {
				//Thread.sleep(3000);
			} catch (Exception e) {}
		}
		
		Chart chart = new Chart(city.getHistory());
		chart.draw();
		chart.pack();
		chart.setVisible(true);
	}

}
