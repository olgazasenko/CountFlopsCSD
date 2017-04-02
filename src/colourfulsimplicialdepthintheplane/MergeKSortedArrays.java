/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colourfulsimplicialdepthintheplane;

import java.util.*;

/**
 *
 * source:
 * http://www.programcreek.com/2014/05/merge-k-sorted-arrays-in-java/
 */
public class MergeKSortedArrays {

    protected static class ArrayContainer implements Comparable<ArrayContainer> {

        protected Double[] arr;
        protected int index;

        protected ArrayContainer(Double[] arr, int index) {
            this.arr = arr;
            this.index = index;
        }

        @Override
        public int compareTo(ArrayContainer o) {
            return Double.compare(this.arr[this.index], o.arr[o.index]);
        }
    }

    protected static class KSortedArray {
        
        protected static PriorityQueue<ArrayContainer> queue;

        /**
         *
         * @param arr
         * @return
         */
        protected static Double[] mergeSort(ArrayList<Double[]> arr) {
            //PriorityQueue is heap in Java 
            queue = new PriorityQueue<>();
            int total = 0;

            //add arrays to heap
            for (Double[] arr1 : arr) {
                queue.add(new ArrayContainer(arr1, 0));
                total += arr1.length;
                Flops.count(1);
            }

            int m = 0;
            Double result[] = new Double[total];

            //while heap is not empty
            while (!queue.isEmpty()) {
                ArrayContainer ac = queue.poll();
                result[m++] = ac.arr[ac.index];
                Flops.count(2); // m++, if

                if (ac.index < ac.arr.length - 1) {
                    queue.add(new ArrayContainer(ac.arr, ac.index + 1));
                    Flops.count(1); // ac.index + 1
                }
            }

            return result;
        }

    }
}
