package vaccinationdistributionmodel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart extends JFrame {
	private History history;
	private XYSeriesCollection dataset = new XYSeriesCollection();
	private XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
	public Chart(History history) {
		super("Chart");
		this.history = history;
	}
	
	public void draw() {
		
                Map<String, List<Integer>> lines = this.history.getData();
                
                int id = 0;
                for (Entry<String, List<Integer>> e: lines.entrySet()){
                    XYSeries series = new XYSeries(e.getKey());
                    List<Integer> values = e.getValue();
                    for (int i=1; i < values.size(); i++){
                        series.add(i, values.get(i-1));
                    }
                    this.renderer.setSeriesStroke(id++, new BasicStroke(2.0f));
                    this.dataset.addSeries(series);
                }
                		
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
                
		plot.setRenderer(this.renderer);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;

	}
}
