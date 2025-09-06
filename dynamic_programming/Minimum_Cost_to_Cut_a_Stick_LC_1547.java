package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/minimum-cost-to-cut-a-stick/

Minimum Cost to cut a Stick

* Use Partition on DP pattern
- Recursion
    * TC: Exponential
    * SC: O(n^2)
- Recursion with Memoization (Top-down approach)
    * TC: O(n^3)
    * SC: O(n^2) + O(n)
- True Dynamic Programming (Bottom-up approach)
    * TC: O(n^3)
    * SC: O(n^2)

 */

public class Minimum_Cost_to_Cut_a_Stick_LC_1547 {
    class Solution {
        public int minCost3(int n, int[] cuts) {
            if(n <= 0 || cuts == null || cuts.length == 0) {
                return 0;
            }

            int size = cuts.length;
            int[] newCuts = new int[size+2];
            newCuts[0] = 0;
            System.arraycopy(cuts, 0, newCuts, 1, size);
            newCuts[size+1] = n;
            Arrays.sort(newCuts);
            int[] frontRow = new int[size+2];

            for(int i = size; i >= 1; i--) {
                int[] currRow = new int[size+2];
                for(int j = 1; j <= size; j++) {
                    if(i > j) {
                        continue;
                    }
                    int minCost = Integer.MAX_VALUE;

                    for(int idx = i; idx <=j; idx++) {
                        int currCost = newCuts[j+1] - newCuts[i-1] + currRow[idx-1] + frontRow[j];
                        minCost = Math.min(minCost, currCost);
                    }

                    // cache[i][j] = minCost == Integer.MAX_VALUE ? 0 : minCost;
                    currRow[j] = minCost;
                }

                frontRow = currRow;
            }

            return frontRow[size];
        }

        public int minCost2(int n, int[] cuts) {
            if(n <= 0 || cuts == null || cuts.length == 0) {
                return 0;
            }

            int size = cuts.length;
            int[] newCuts = new int[size+2];
            newCuts[0] = 0;
            System.arraycopy(cuts, 0, newCuts, 1, size);
            newCuts[size+1] = n;
            Arrays.sort(newCuts);
            int[][] cache = new int[size+2][size+2];

            for(int i = size; i >= 1; i--) {
                for(int j = 1; j <= size; j++) {
                    if(i > j) {
                        continue;
                    }
                    int minCost = Integer.MAX_VALUE;

                    for(int idx = i; idx <=j; idx++) {
                        int currCost = newCuts[j+1] - newCuts[i-1] + cache[i][idx-1] + cache[idx+1][j];
                        minCost = Math.min(minCost, currCost);
                    }

                    // cache[i][j] = minCost == Integer.MAX_VALUE ? 0 : minCost;
                    cache[i][j] = minCost;
                }
            }

            return cache[1][size];
        }

        private int helper1(int i, int j, int[] newCuts, int[][] cache) {
            if(i > j) {
                return 0;
            } else if(cache[i][j] != -1) {
                return cache[i][j];
            }

            int minCost = Integer.MAX_VALUE;

            for(int idx = i; idx <=j; idx++) {
                int currCost = newCuts[j+1] - newCuts[i-1] + helper1(i, idx-1, newCuts, cache)
                        + helper1(idx+1, j, newCuts, cache);
                minCost = Math.min(minCost, currCost);
            }

            return cache[i][j] = minCost;
        }

        public int minCost1(int n, int[] cuts) {
            if(n <= 0 || cuts == null || cuts.length == 0) {
                return 0;
            }

            int size = cuts.length;
            int[] newCuts = new int[size+2];
            newCuts[0] = 0;
            System.arraycopy(cuts, 0, newCuts, 1, size);
            newCuts[size+1] = n;
            Arrays.sort(newCuts);
            int[][] cache = new int[size+1][size+1];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(1, size, newCuts, cache);
        }

        private int helper0(int i, int j, int[] newCuts) {
            if(i > j) {
                return 0;
            }

            int minCost = Integer.MAX_VALUE;

            for(int idx = i; idx <=j; idx++) {
                int currCost = newCuts[j+1] - newCuts[i-1] + helper0(i, idx-1, newCuts) + helper0(idx+1, j, newCuts);
                minCost = Math.min(minCost, currCost);
            }

            return minCost;
        }

        public int minCost(int n, int[] cuts) {
            if(n <= 0 || cuts == null || cuts.length == 0) {
                return 0;
            }

            int size = cuts.length;
            int[] newCuts = new int[size+2];
            newCuts[0] = 0;
            System.arraycopy(cuts, 0, newCuts, 1, size);
            newCuts[size+1] = n;
            Arrays.sort(newCuts);

            return helper0(1, size, newCuts);
        }
    }
}
