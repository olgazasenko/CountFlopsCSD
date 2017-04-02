/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colourfulsimplicialdepthintheplane;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Olga
 */
public class Setup {

    Scanner in = null;

    protected ArrayList<Point2D.Double[]> colourSets;
    protected ArrayList<Double[]> thetas;
    protected ArrayList<Double[]> antipodes;
    int[] sizes; // individual sizes of the arrays
    int n; // total num of points
    int k; // total num of colours
    int[] lastInd0;

    Double[] A; // sorted array of all antipodes
    long depth; // monochrome depth with respect to all n points
    long sum1 = 0;
    long sum2 = 0;

    Setup() {

        try {
            String filePath = new File("").getAbsolutePath();
            in = new Scanner(new FileReader(filePath + "/data/K7.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
        }
        k = in.nextInt(); // total number of colours
        if (k < 3) {
            System.out.println("Not enough colours! CSD = 0.");
            System.exit(0);
        }
        colourSets = new ArrayList<>(k);
        thetas = new ArrayList<>(k);
        antipodes = new ArrayList<>(k);

        sizes = new int[k];
        lastInd0 = new int[k];
        initialize();
    }

    private final void initialize() {

        int i = 0;

        while (in.hasNext()) {
            int ni = in.nextInt(); // n_i
            sizes[i] = ni;
            n += ni;
            
            colourSets.add(new Point2D.Double[ni]);
            thetas.add(new Double[ni]);
            antipodes.add(new Double[ni]);

            for (int j = 0; j < ni; j++) {
                double x = in.nextDouble();
                double y = in.nextDouble();
                colourSets.get(i)[j] = new Point2D.Double(x, y);
                double theta = Math.toDegrees(Math.atan2(y, x));

                if (theta < 0) {
                    thetas.get(i)[j] = theta + 360.0;
                    Flops.count(1);
                } else {
                    thetas.get(i)[j] = theta;
                }
                antipodes.get(i)[j] = (theta + 180.0) % 360.0;
            }
            sortThetasAndAntipodes(i);
            i++;
            Flops.count(2 + ni * 5);
        }
    }

    private void sortThetasAndAntipodes(int i) {
        Arrays.sort(thetas.get(i));
        Arrays.sort(antipodes.get(i));
        Flops.count((long)(thetas.size() * Math.log(thetas.size())
                + antipodes.size() * Math.log(antipodes.size())));
    }

    protected void firstPart() {
        for (int i = 0; i < k; i++) {
            sum1 += RousseeuwAndRuts.computeDepth(thetas.get(i));
            lastInd0[i] = RousseeuwAndRuts.NU - 1;
        }
        Flops.count(2 * k);
        A = MergeKSortedArrays.KSortedArray.mergeSort(antipodes);

        depth = RousseeuwAndRuts.computeDepth(A);
    }

    protected void runIterationK(int iteration) {

        IterationK itK = new IterationK(A, thetas.get(iteration));
        itK.mergeTwoSortedArrays();
        itK.fillCounts();
        itK.findLast(lastInd0[iteration]); // the last index for theta[0] of colour i
        sum2 += itK.getFormulaValue();
        Flops.count(1);
    }

    protected long colourfulSimplicialDepth() {
        for (int i = 0; i < k; i++) {
            runIterationK(i);
        }
        Flops.count(3 + k);
        return (depth - (sum2 - 2 * sum1));
    }
}
