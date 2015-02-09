package vaccinationdistributionmodel.display;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PieChart extends JFrame {

    private History history;
    private DefaultPieDataset dataset;

    public PieChart(History history) {
        super("Chart");
        this.history = history;
    }

    public void draw() {
        Map<String, List<Long>> lines = this.history.getData();
        this.dataset = new DefaultPieDataset();

        for (Map.Entry<String, List<Long>> e : lines.entrySet()) {
            if (e.getKey().equals("population")) {
                continue;
            }

            long value = e.getValue().get(e.getValue().size() - 1);
            this.dataset.setValue(e.getKey(), value);
        }

        JFreeChart chart = createChart(this.dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                null, // chart title
                dataset, // data
                true, // include legend
                true, // tooltips
                false // urls
        );

        chart.setBackgroundPaint(Color.white);

        PiePlot plot = (PiePlot) chart.getPlot();
        //plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        plot.setIgnoreZeroValues(true);
        plot.setSimpleLabels(true);
        plot.setSectionPaint("dead", new Color(66, 66, 66));
        plot.setSectionPaint("recovered", new Color(88, 196, 96));
        plot.setSectionPaint("susceptible", new Color(88, 173, 196));
        
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);

        return chart;

    }
}
