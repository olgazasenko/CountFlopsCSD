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
 *
 * computes the monochrome simplicial depth alpha is a sorted array of polar
 * angles alpha[i] in [0, 2*pi)
 */
public class RousseeuwAndRuts {

    private static int len = 0;
    private static int doubleLen;
    private static int lenMinusOne;
    private static double[] alpha1;
    private static double[] beta;
    private static double[] gamma; // common sorted array
    private static int[] w; // 
    protected static int NU; // number of alphas in the semicircle of alpha1[0] inclusive

    /**
     *
     * @param alpha
     */
    private static boolean fillAlphaBeta(Double[] alpha) {
        /* let alpha1[0] = 0, alpha1[i] = alpha[i] - alpha[0]
           let beta[i] = (alpha1[i] + pi) mod 2*pi
           sort beta
         */
        len = alpha.length;
        doubleLen = 2 * len;
        lenMinusOne = len - 1;
        
        NU = 0; 
        alpha1 = new double[len];
        beta = new double[len];

        boolean flag = false; // for looping around beta
        int startB = 0;

        for (int i = 0; i < len; i++) {
            
            alpha1[i] = alpha[i] - alpha[0];
                        
            if (alpha1[i] < 180) {
                NU++;
                Flops.count(1);
            } else if (!flag) {
                // we just looped for the first time
                startB = i;
                flag = true;
            }
        }
        Flops.count(2 * len + 2); 
        // we do all the checks here, because we want to calculate NU correclty
        if (len == 0) {
            return false;
        } else if (len < 3) {
            return false;
        }
        if (alpha1[lenMinusOne] < 180) {
            return false;
        }
        for (int i = 0; i < len; i++){
            Flops.count(3); // i++, i+1, -
            if (i != (lenMinusOne) && (alpha1[i + 1] - alpha1[i] > 180)) {
                return false;
            }
        }
        int j = startB, indB = 0;
        while (indB != len) {
            if (alpha1[j] < 180) {
                beta[indB] = alpha1[j] + 180;
            } else {
                beta[indB] = alpha1[j] - 180;
            }
            j++;
            indB++;
            j %= len;
            Flops.count(4); // +/-, ++, ++, %
        }
        return true;
    }

    /**
     *
     * @param alpha
     * @return the monochrome simplicial depth
     */
    protected static long computeDepth(Double[] alpha) {

        if (!fillAlphaBeta(alpha)) { //something went wrong
            return 0;
        }

        int start = merge(); //merge alpha and beta into gamma sorted
        // start is the index of the entry right after 180

        int NF = NU;
        int[] hi = new int[len];
        hi[0] = NU - 1;

        int i = start, t = 1, end = start - 1;
        while (i != end) { // completed full circle
            if (w[i] == 1) {
                NF++;
                Flops.count(1);
            } else {
                hi[t] = NF - (++t); // h(i) = F(i) - i
                Flops.count(2);
            }
            i++;
            i %= doubleLen;
            Flops.count(2);
        }

        long sum = 0;
        for (i = 0; i < len; i++) {
            sum += choose(hi[i], 2);
        }
        long total = choose(len, 3);

        Flops.count(3 + 2 * len);
        return (total - sum);
    }

    /*
    source:
    http://stackoverflow.com/questions/15301885/calculate-value-of-n-choose-k
    
     */
    private static long choose(int n, int k) {
        if (k == 0) {
            return 1;
        }
        Flops.count(4);
        return (n * choose(n - 1, k - 1)) / k;
    }

    /*
    source:
    http://stackoverflow.com/questions/5958169/how-to-merge-two-sorted-arrays-into-a-sorted-array
     */
    private static int merge() {
        /*
           w[i] = 1, if gamma[i] = alpha1[j]
           w[i] = -1, if gamma[i] = beta[j]
           merge also finds first element greater than 180
           or alpha[start] = 180
         */
        w = new int[doubleLen];
        gamma = new double[doubleLen];
        int i = 0, j = 0, k = 0, start = NU;
        boolean flag = false;

        while (i < len && j < len) {
            if (alpha1[i] < beta[j]) {
                gamma[k] = alpha1[i++];
                w[k] = 1;
            } else {
                gamma[k] = beta[j++];   // priority given to beta      
                w[k] = -1;
            }
            if (gamma[k] > 180 && !flag) {
                start = k;
                flag = true;
            }
            k++;
            Flops.count(2); // k++, i++ or j++
        }

        while (i < len) {
            gamma[k] = alpha1[i++];
            w[k] = 1;
            if (gamma[k] > 180 && !flag) {
                start = k;
                flag = true;
            }
            k++;
            Flops.count(2); // i++, k++
        }

        while (j < len) {
            gamma[k] = beta[j++];
            w[k] = -1;
            if (gamma[k] > 180 && !flag) {
                start = k;
                flag = true;
            }
            k++;
            Flops.count(2); // j++, k++
        }
        return start;
    }
}
