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
		parameters.infectionRate = 1;
		
		City city = new City(1000, 990, 0, 10, 0, 0, 0);
		city.setParameters(parameters);
		
		for (int i = 0; i < 100; i++) {
			city.update();
			System.out.println(city);
			
			try {
				Thread.sleep(3000);
			} catch (Exception e) {}
		}
	}

}
