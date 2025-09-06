package neetcode.dynamic_programming;

import java.util.Arrays;
/*
https://www.geeksforgeeks.org/problems/matrix-chain-multiplication0303/1

- Partition on DP
- Refer to Partition_DP file about pattern
- Recursion:
    * TC: O(exponential)
    * SC: O(n) because of recursion
- Memoization:
    * TC: O(n^3)
    * SC: O(n^2) + O(n) because of cache 2D array and recursion
- Bottom-Up (True Dynamic Programming Solution)
    * TC: O(n^3)
    * SC: O(n^2) because of cache 2D array

 */

public class Matrix_Chain_Multiplication_LC_GFG {
    class Solution {
        public int matrixMultiplication2(int[] arr) {
            if(arr == null || arr.length <= 1) {
                return 0;
            }

            int size = arr.length;
            int[][] cache = new int[size][size];

            for(int i = size-1; i >= 1; i--) {
                for(int j = i+1; j < size; j++) {
                    int minOps = Integer.MAX_VALUE;
                    for(int k = i; k <= (j-1); k++) {
                        minOps = Math.min(minOps, arr[i-1] * arr[k] * arr[j] + cache[i][k] + cache[k+1][j]);
                    }

                    cache[i][j] = minOps;
                }
            }

            return cache[1][size-1];
        }

        private int helper1(int i, int j, int[] arr, int[][] cache) {
            if(i == j) {
                return 0;
            } else if(cache[i][j] != -1) {
                return cache[i][j];
            }

            int minOps = Integer.MAX_VALUE;
            for(int k = i; k <= (j-1); k++) {
                minOps = Math.min(minOps, arr[i-1] * arr[k] * arr[j] + helper1(i, k, arr, cache) + helper1(k+1, j, arr, cache));
            }

            return cache[i][j] = minOps;
        }

        public int matrixMultiplication1(int[] arr) {
            if(arr == null || arr.length <= 1) {
                return 0;
            }

            int size = arr.length;
            int[][] cache = new int[size][size];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }
            return helper1(1, size-1, arr, cache);
        }

        private int helper0(int i, int j, int[] arr) {
            if(i == j) {
                return 0;
            }

            int minOps = Integer.MAX_VALUE;
            for(int k = i; k <= (j-1); k++) {
                minOps = Math.min(arr[i-1] * arr[k] * arr[j] + helper0(i, k, arr) + helper0(k+1, j, arr), minOps);
            }

            return minOps;
        }

        public int matrixMultiplication(int[] arr) {
            if(arr == null || arr.length <= 1) {
                return 0;
            }

            return helper0(1, arr.length-1, arr);
        }
    }
}
