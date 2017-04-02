/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colourfulsimplicialdepthintheplane;

import java.applet.Applet;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author Olga
 */
public class Paint extends Applet {

    Graphics2D g2;
    ArrayList<Point2D.Double[]> colourSets;
    Plot2DPanel plot;

    Paint(ArrayList<Point2D.Double[]> colourSets) {
        this.colourSets = colourSets;
        JFrame frame = new JFrame("a plot panel");
        frame.setSize(700, 700);
        plot = new Plot2DPanel();
        plot.setFixedBounds(0, -5, 15);
        plot.setFixedBounds(1, -5, 30);
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void paint() {
        paintPoints();

    }

    private void paintPoints() {

        int k = colourSets.size();
        Color[] colors = {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};
        for (int i = 0; i < k; i++) {
            int ni = colourSets.get(i).length;
            double[][] points = new double[ni][2];
            for (int j = 0; j < ni; j++) {
                points[j][0] = colourSets.get(i)[j].x;
                points[j][1] = colourSets.get(i)[j].y;
            }
            plot.addScatterPlot("plot", colors[i % colors.length], points);
        }
        double[][] points = {{0}, {0}};
        plot.addScatterPlot("plot", Color.BLACK, points);
    }
}
