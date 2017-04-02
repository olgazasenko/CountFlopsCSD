/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colourfulsimplicialdepthintheplane;

import java.util.Arrays;

/**
 *
 * source:
 * http://www.vogella.com/tutorials/JavaAlgorithmsMergesort/article.html
 * modified
 * 
 * memory used is O(2n + 2n + 2n + 2n) = O(n)
 */
public class MergeSort {

    private float[] numbers;
    private float[] helper;
    private int[] w;
    private int[] w1;

    private final int number;

    MergeSort(float[] values, int[] w) {
        this.numbers = values;
        number = values.length;
        this.helper = new float[number];
        this.w1 = new int[number];
        this.w = w;
    }

    protected void mergesort(int low, int high) {
        // check if low is smaller then high, if not then the array is sorted
        if (low < high) {
            // Get the index of the element which is in the middle
            int middle = low + (high - low) / 2;
            // Sort the left side of the array
            mergesort(low, middle);
            // Sort the right side of the array
            mergesort(middle + 1, high);
            // Combine them both
            merge(low, middle, high);
        }
    }

    private void merge(int low, int middle, int high) {

        // Copy both parts into the helper array
        for (int i = low; i <= high; i++) {
            helper[i] = numbers[i];
            w1[i] = w[i];
        }

        int i = low;
        int j = middle + 1;
        int k = low;
        // Copy the smallest values from either the left or the right side back
        // to the original array
        while (i <= middle && j <= high) {
            if (helper[i] <= helper[j]) {
                numbers[k] = helper[i];
                w[k] = w1[i];
                i++;
            } else {
                numbers[k] = helper[j];
                w[k] = w1[j];
                j++;
            }
            k++;
        }
        // Copy the rest of the left side of the array into the target array
        while (i <= middle) {
            numbers[k] = helper[i];
            w[k] = w1[i];
            k++;
            i++;
        }

    }
    
    protected void display(){
        System.out.println(Arrays.toString(numbers));
        System.out.println(Arrays.toString(w));
    }
}


