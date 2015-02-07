package vaccinationdistributionmodel.display;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class AreaChart extends JFrame {

	private History history;
	private CategoryDataset dataset;

	public AreaChart(History history) {
		super("Chart");
		this.history = history;
	}

	public void draw() {
		Map<String, List<Long>> lines = this.history.getData();
		double[][] data = new double[lines.size() - 1][];

		int id = 0;
		for (Map.Entry<String, List<Long>> e : lines.entrySet()) {
			if (e.getKey().equals("population")) {
				continue;
			}

			List<Long> values = e.getValue();

			double[] entries = new double[values.size()];

			for (int i = 0; i < values.size(); i++) {
				entries[i] = values.get(i);
			}

			data[id++] = entries;
		}

		this.dataset = DatasetUtilities.createCategoryDataset(
				"Series ", "Type ", data
		);

		JFreeChart chart = createChart(this.dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		setContentPane(chartPanel);
	}

	private JFreeChart createChart(CategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createStackedAreaChart(
				null, // chart title
				"Days", // x axis label
				"Values", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL,
				true, // include legend
				true, // tooltips
				false // urls
		);

		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLowerMargin(0.0);
		domainAxis.setUpperMargin(0.0);

		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;

	}
}
