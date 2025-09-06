package neetcode.dynamic_programming;

import java.util.Arrays;

/*
Given a list of N items, and a backpack with a limited capacity, return the maximum total profit that can be contained in the backpack.
The i-th item's profit is profit[i] and it's weight is weight[i]. Assume you can only add each item to the bag at most one time.

Approaches:
- Brute Force Recursion:
    - TC: O(2^n)
    - SC: O(n)
- Recursion with Memoization (Top-Down Approach):
    - TC: O(n*m)
    - SC: O(n*m) + O(n)
- True Dynamic Programming (Bottom-Up Approach):
    - TC: O(n*m)
    - SC: O(n*m), avoided recursion call
- True Dynamic Programming with Optimized space (Bottom-Up Approach)
    - TC: O(n*m)
    - SC: O(m)

Where n is the number of items and m is the capacity of our knapsack.
 */

public class Max_Profit_0_or_1_Knapsack {
    class Solution {
        public int maxProfit3(int[] weights, int[] profits, int capacity) {
            if(weights == null || weights.length == 0 || profits == null || profits.length == 0 || weights.length != profits.length) {
                return 0;
            }

            int size = weights.length;
            int[] frontRow = new int[capacity+1];

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[capacity+1];
                for(int currCap = 0; currCap <= capacity; currCap++) {
                    // Skip item at index: idx
                    int notInclude = frontRow[currCap];

                    // Include item at index: idx
                    int newCap = currCap - weights[idx], include = 0;
                    if(newCap >= 0) {
                        include = profits[idx] + frontRow[newCap];
                    }

                    // Compute maxProfit from either include or notInclude item path or decision and store in cache[idx][capacity]
                    currRow[currCap] = Math.max(include, notInclude);
                }
                frontRow = currRow;
            }

            return frontRow[capacity];
        }

        public int maxProfit2(int[] weights, int[] profits, int capacity) {
            if(weights == null || weights.length == 0 || profits == null || profits.length == 0 || weights.length != profits.length) {
                return 0;
            }

            int size = weights.length;
            int[][] dp = new int[size+1][capacity+1];

            for(int idx = size-1; idx >= 0; idx--) {
                // we can start with currCap = 0 or currCap = 1
                for(int currCap = 0; currCap <= capacity; currCap++) {
                    // Skip item at index: idx
                    int notInclude = dp[idx+1][currCap];

                    // Include item at index: idx
                    int newCap = currCap - weights[idx], include = 0;
                    if(newCap >= 0) {
                        include = profits[idx] + dp[idx+1][newCap];
                    }

                    // Compute maxProfit from either include or notInclude item path or decision and store in cache[idx][capacity]
                    dp[idx][currCap] = Math.max(include, notInclude);
                }
            }

            return dp[0][capacity];
        }

        private int helper1(int idx, int[] weights, int[] profits, int capacity, int[][] cache) {
            if(idx >= weights.length) {
                return 0;
            } else if(cache[idx][capacity] != -1) {
                return cache[idx][capacity];
            }

            // Skip item at index: idx
            int notInclude = helper1(idx+1, weights, profits, capacity, cache);

            // Include item at index: idx
            int newCap = capacity - weights[idx], include = 0;
            if(newCap >= 0) {
                include = profits[idx] + helper1(idx+1, weights, profits, newCap, cache);
            }

            // Compute maxProfit from either include or notInclude item path or decision and store in cache[idx][capacity]
            return cache[idx][capacity] = Math.max(include, notInclude);
        }

        public int maxProfit1(int[] weights, int[] profits, int capacity) {
            if(weights == null || weights.length == 0 || profits == null || profits.length == 0 || weights.length != profits.length) {
                return 0;
            }

            int size = weights.length;
            int[][] cache = new int[size][capacity+1];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, weights, profits, capacity, cache);
        }

        private int helper0(int idx, int[] weights, int[] profits, int capacity) {
            if(idx >= weights.length) {
                return 0;
            }

            // skip item at index: idx
            int notInclude = helper0(idx+1, weights, profits, capacity);

            // Include item at index: idx
            int newCap = capacity - weights[idx], include = 0;
            if(newCap >= 0) {
                include = profits[idx] + helper0(idx+1, weights, profits, newCap);
            }

            // Return maxProfit either from include or notInclude item path or decision
            return Math.max(include, notInclude);
        }

        public int maxProfit0(int[] weights, int[] profits, int capacity) {
            if(weights == null || weights.length == 0 || profits == null || profits.length == 0 || weights.length != profits.length) {
                return 0;
            }

            return helper0(0, weights, profits, capacity);
        }
    }
}
