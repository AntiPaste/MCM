package vaccinationdistributionmodel.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import vaccinationdistributionmodel.world.Edge;
import vaccinationdistributionmodel.world.Globe;
import vaccinationdistributionmodel.world.Region;

public class MapChart extends JFrame {

    private class Surface extends JPanel {

        private class Listener implements MouseListener, MouseMotionListener {

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point location = e.getPoint();
                Region previous = Surface.this.displayRegion;
                Surface.this.displayRegion = null;

                List<Region> regions = Surface.this.globe.getRegions().getNodes();
                for (Region region : regions) {
                    double[] coordinates = Surface.this.coordinatesToXY(region.latitude, region.longitude);
                    int x = (int) coordinates[0];
                    int y = (int) coordinates[1];

                    if (location.x > x && location.x < (x + 10) && location.y > y && location.y < (y + 10)) {
                        Surface.this.displayRegion = region;
                        break;
                    }
                }

                if (Surface.this.displayRegion != previous) {
                    Surface.this.repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        }

        private Graphics2D g2d;
        private Globe globe;
        private int width;
        private int height;
        public Region displayRegion = null;
        private Image mapImage;
        private int mode = 0;

        public Surface(Globe globe, int width, int height) {
            this.globe = globe;
            this.width = width;
            this.height = height;

            try {
                this.mapImage = ImageIO.read(new File("eqdc.png"));
            } catch (IOException ex) {
                System.out.println("Fail.");
                System.exit(0);
            }

            this.addMouseMotionListener(new Listener());
        }

        private void draw(Graphics g) {
            this.g2d = (Graphics2D) g;
            this.g2d.drawImage(this.mapImage, -120, -20, 2600, 1250, null);

            this.g2d.setColor(Color.red);

            Dimension size = getSize();
            Insets insets = getInsets();

            Set<Edge<Region>> edges = this.globe.getRegions().edges();
            for (Edge<Region> edge : edges) {
                Region source = edge.one;
                Region target = edge.other;

                double[] sourceCoordinates = this.coordinatesToXY(source.latitude, source.longitude);
                double[] targetCoordinates = this.coordinatesToXY(target.latitude, target.longitude);

                this.g2d.drawLine((int) sourceCoordinates[0], (int) sourceCoordinates[1], (int) targetCoordinates[0], (int) targetCoordinates[1]);
            }

            List<Region> regions = this.globe.getRegions().getNodes();
            for (Region region : regions) {
                double[] coordinates = this.coordinatesToXY(region.latitude, region.longitude);
                boolean isBig = this.globe.getRegions().getBigRegions().contains(region);
                int[] colours;
                
                if (this.mode == 1)
                    colours = region.vaccinationLevel();
                else
                    colours = region.ebolaLevel();

                int colorR = colours[0];
                int colorG = colours[1];
                int colorB = colours[2];

                this.g2d.setColor(new Color(colorR, colorG, colorB));

                this.g2d.drawString(region.name, (int) coordinates[0], (int) coordinates[1]);
                this.g2d.fillOval((int) coordinates[0], (int) coordinates[1], (isBig ? 15 : 10), (isBig ? 15 : 10));
                this.g2d.setColor(Color.red);
            }

            this.g2d.drawString(String.format("Day: %,d", this.globe.days), 5, 15);
            //this.g2d.drawString(String.format("x: %d", this.globe.daysToOutbreakEnd), 5, 250);
            //this.g2d.drawString(String.format("Cost: %.2f", this.globe.cost()), 5, 300);
            this.g2d.drawString(String.format("Population: %,d", this.globe.getPopulation()), 5, 30);
            this.g2d.drawString(String.format("Recovered: %,d", this.globe.getRecovered()), 5, 45);
            this.g2d.drawString(String.format("Deaths: %,d", this.globe.getDeaths()), 5, 60);
            this.g2d.drawString(String.format("Vaccinated: %,d", this.globe.getVaccinated()), 5, 75);
            this.g2d.drawString(String.format("Exposed: %,d", this.globe.getExposed()), 5, 90);
            this.g2d.drawString(String.format("Infected: %,d", this.globe.getInfected()), 5, 105);
            this.g2d.drawString(String.format("Advanced: %,d", this.globe.getAdvanced()), 5, 120);
            this.g2d.drawString(String.format("Susceptible: %,d", this.globe.getSusceptible()), 5, 135);

            if (this.displayRegion == null) {
                return;
            }

            if (this.globe.getRegions().getBigRegions().contains(this.displayRegion)) {
                this.g2d.setColor(Color.blue);
            }

            this.g2d.drawString(this.displayRegion.name, 0, 10);
            this.g2d.setColor(Color.red);
        }

        private double[] coordinatesToXY(double latitude, double longitude) {
            return new double[]{
                (double) (this.width * (180.0 + latitude) / 360.0),
                (double) (this.height * (90.0 - longitude) / 180.0),};
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.draw(g);
        }
    }

    private Globe globe;
    private XYSeriesCollection dataset = new XYSeriesCollection();
    private XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

    public MapChart(Globe globe, int width, int height) {
        super("Map Chart");
        this.globe = globe;

        this.add(new Surface(globe, width, height));
        this.setLocationRelativeTo(null);
    }
}
