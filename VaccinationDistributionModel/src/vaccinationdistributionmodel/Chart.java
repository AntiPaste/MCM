package vaccinationdistributionmodel;

import java.awt.Color;
import java.util.List;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart extends JFrame {
	private History history;
	private XYSeriesCollection dataset = new XYSeriesCollection();
	
	public Chart(History history) {
		super("Chart");
		this.history = history;
	}
	
	public void draw() {
		/*this.population = population;
		this.susceptible = susceptible;
		this.exposed = exposed;
		this.infected = infected;
		this.advanced = advanced;
		this.recovered = recovered;
		this.dead = dead;*/
		
		XYSeries population = new XYSeries("Population");
		List<CityState> states = this.history.getStates();
		
		for (int i = 1; i < states.size(); i++) {
			population.add(i, states.get(i - 1).population);
		}
		
		this.dataset.addSeries(population);
		
		XYSeries susceptible = new XYSeries("Susceptible");
		
		for (int i = 1; i < states.size(); i++) {
			susceptible.add(i, states.get(i - 1).susceptible);
		}
		
		this.dataset.addSeries(susceptible);
		
		XYSeries exposed = new XYSeries("Exposed");
		
		for (int i = 1; i < states.size(); i++) {
			exposed.add(i, states.get(i - 1).exposed);
		}
		
		this.dataset.addSeries(exposed);
		
		XYSeries infected = new XYSeries("Infected");
		
		for (int i = 1; i < states.size(); i++) {
			infected.add(i, states.get(i - 1).infected);
		}
		
		this.dataset.addSeries(infected);
		
		XYSeries advanced = new XYSeries("Advanced");
		
		for (int i = 1; i < states.size(); i++) {
			advanced.add(i, states.get(i - 1).advanced);
		}
		
		this.dataset.addSeries(advanced);
		
		XYSeries recovered = new XYSeries("Recovered");
		
		for (int i = 1; i < states.size(); i++) {
			recovered.add(i, states.get(i - 1).recovered);
		}
		
		this.dataset.addSeries(recovered);
		
		XYSeries dead = new XYSeries("Dead");
		
		for (int i = 1; i < states.size(); i++) {
			dead.add(i, states.get(i - 1).dead);
		}
		
		this.dataset.addSeries(dead);
		
		JFreeChart chart = createChart(this.dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		setContentPane(chartPanel);
	}

	private JFreeChart createChart(final XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart(
				"", // chart title
				"Days", // x axis label
				"Values", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL,
				true, // include legend
				true, // tooltips
				false // urls
		);

		chart.setBackgroundPaint(Color.white);

		// final StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);
		
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));

		/*XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesShapesVisible(1, false);
		plot.setRenderer(renderer);*/

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;

	}
}
