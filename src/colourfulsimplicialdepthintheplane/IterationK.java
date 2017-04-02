/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colourfulsimplicialdepthintheplane;

import java.util.Arrays;

/**
 *
 * @author Olga
 */
public class IterationK {

    private final Double[] arr;
    private Double[] res;
    private int[] w;
    private final Double[] thetaI;
    private final int totalLen;
    private final int ni;
    private int[] pointer;
    private int[] counts;
    private int[] lastInd;
    private int[] sumPrefix;
    private int[] tSumPrefix;

    IterationK(Double[] A, Double[] thetaI) {
        this.arr = A;
        this.thetaI = thetaI;
        this.totalLen = A.length + thetaI.length;
        this.ni = thetaI.length;
        Flops.count(1);
    }

    /*
    source:
    http://stackoverflow.com/questions/5958169/how-to-merge-two-sorted-arrays-into-a-sorted-array
     */
    protected void mergeTwoSortedArrays() {

        res = new Double[totalLen];
        w = new int[totalLen];
        pointer = new int[ni];
        int i = 0, j = 0, k = 0;

        while (i < arr.length && j < ni) {
            if (arr[i] < thetaI[j]) {
                res[k] = arr[i++];
                w[k] = -1;
            } else {
                res[k] = thetaI[j]; // if equal theta comes first
                w[k] = 1;
                pointer[j] = k;
                j++;
            }
            k++;
            Flops.count(2);
        }

        while (i < arr.length) {
            res[k] = arr[i];
            w[k] = -1;
            i++;
            k++;
            Flops.count(2);
        }

        while (j < ni) {
            res[k] = thetaI[j];
            w[k] = 1;
            pointer[j] = k;
            j++;
            k++;
            Flops.count(2);
        }
    }

    protected void fillCounts() {

        counts = new int[ni];
        for (int j = 1; j <= ni; j++) {

            if (ni == 1) {
                counts[0] = 0;
                break;
            }
            int jMod = j, jMinusOne = j - 1;
            if (j == ni) {
                jMod = 0;
            }
            double angle = thetaI[jMod] - thetaI[jMinusOne];
            if (angle < 0) {
                angle += 360;
                Flops.count(1);
            }
            if (angle >= 180 || (angle == 0 && jMod != j + 1)) {
                counts[jMod] = 0;
            } else {
                Flops.count(2);
                counts[jMod] = pointer[jMod] - pointer[jMinusOne] - 1;
                if (pointer[jMod] < pointer[jMinusOne]) {
                    counts[jMod] += totalLen;
                    Flops.count(1);
                }
            }
            if (pointer[jMod] == pointer[jMinusOne]) {
                System.out.println("THIS SHOULD NOT HAPPEN!");
            }
        }
        Flops.count(3 * ni);
    }

    /* take first index as an argument
       all subsequent ones just linear scan
       also fill the prefix sum arrays
     */
    protected void findLast(int lastInd0) {

        lastInd = new int[ni];
        sumPrefix = new int[ni];
        tSumPrefix = new int[ni];

        lastInd[0] = lastInd0;
        sumPrefix[0] = counts[0];
        tSumPrefix[0] = sumPrefix[0];

        int j = lastInd0; // could be the same
        double angle;
        for (int i = 1; i < ni; i++) {
            angle = thetaI[j] - thetaI[i];
            
            if (angle < 0) {
                angle += 360;
                Flops.count(1);
            }

            while (angle < 180) {
                j++;
                j %= ni;
                angle = thetaI[j] - thetaI[i];
                Flops.count(3);
                
                if (angle < 0) {
                    angle += 360;
                    Flops.count(1);
                }
                if (angle == 0 && i == j) {
                    break;
                }
            }
            lastInd[i] = (j - 1);
            if (lastInd[i] < 0) {
                lastInd[i] += ni;
                Flops.count(2);
            }

            int iMinusOne = i - 1;
            sumPrefix[i] = sumPrefix[iMinusOne] + counts[i];
            tSumPrefix[i] = tSumPrefix[iMinusOne] + sumPrefix[i];
        }
        Flops.count((ni - 1) * 6);
    }

    protected long getFormulaValue() {
        int depthI = 0;
        int temp;
        int niMinusOne = ni - 1;
        
        for (int j = 0; j < niMinusOne; j++) {
            if (j == lastInd[j]) {
                continue; // !!!
            }
            temp = lastInd[j] - j;
            
            if (temp < 0) {
                temp += ni;
                Flops.count(1);
            }
            depthI += tSumPrefix[lastInd[j]] - tSumPrefix[j] - temp * sumPrefix[j]; // OMG

            if (lastInd[j] < (j + 1)) {
                temp = lastInd[j] + 1; // NO MODULO HERE
                depthI += tSumPrefix[niMinusOne] + sumPrefix[niMinusOne] * temp;
                Flops.count(4);
            }
        }
        double angle = thetaI[0] - thetaI[niMinusOne];
        Flops.count(2 + niMinusOne * 7);
        
        if (angle < 0) {
            angle += 360;
            Flops.count(1);
        }
        if (angle < 180) { // NECCESSARY????
            depthI += tSumPrefix[lastInd[niMinusOne]]; // special case, when j = n_i - 1, 
            // and j + 1 = 0
            Flops.count(1);
        }
        return (depthI);
    }

}
