package neetcode.dynamic_programming;

import java.util.Arrays;

/*

https://leetcode.com/problems/burst-balloons/description/

Burst balloons

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

public class Burst_Balloons_LC_312 {
    class Solution {
        public int maxCoins2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int[] newNums = new int[size+2];
            newNums[0] = 1;
            System.arraycopy(nums, 0, newNums, 1, size);
            newNums[size+1] = 1;
            int[][] cache = new int[size+2][size+2];

            for(int i = size; i >= 1; i--) {
                for(int j = 1; j <= size; j++) {
                    if(i > j) {
                        continue;
                    }

                    int maxCoins = Integer.MIN_VALUE;

                    for(int idx = i; idx <= j; idx++) {
                        int currMaxCoins = newNums[i-1] * newNums[idx] * newNums[j+1]
                                + cache[i][idx-1]
                                + cache[idx+1][j];
                        maxCoins = Math.max(maxCoins, currMaxCoins);
                    }

                    cache[i][j] = maxCoins;
                }
            }

            return cache[1][size];
        }

        private int helper1(int i, int j, int[] newNums, int[][] cache) {
            if(i > j) {
                return 0;
            } else if(cache[i][j] != -1) {
                return cache[i][j];
            }

            int maxCoins = Integer.MIN_VALUE;

            for(int idx = i; idx <= j; idx++) {
                int currMaxCoins = newNums[i-1] * newNums[idx] * newNums[j+1]
                        + helper1(i, idx-1, newNums, cache)
                        + helper1(idx+1, j, newNums, cache);
                maxCoins = Math.max(maxCoins, currMaxCoins);
            }

            return cache[i][j] = maxCoins;
        }

        public int maxCoins1(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int[] newNums = new int[size+2];
            newNums[0] = 1;
            System.arraycopy(nums, 0, newNums, 1, size);
            newNums[size+1] = 1;
            int[][] cache = new int[size+1][size+1];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(1, size, newNums, cache);
        }

        private int helper0(int i, int j, int[] newNums) {
            if(i > j) {
                return 0;
            }

            int maxCoins = Integer.MIN_VALUE;

            for(int idx = i; idx <= j; idx++) {
                int currMaxCoins = newNums[i-1] * newNums[idx] * newNums[j+1]
                        + helper0(i, idx-1, newNums)
                        + helper0(idx+1, j, newNums);
                maxCoins = Math.max(maxCoins, currMaxCoins);
            }

            return maxCoins;
        }

        public int maxCoins0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int[] newNums = new int[size+2];
            newNums[0] = 1;
            System.arraycopy(nums, 0, newNums, 1, size);
            newNums[size+1] = 1;

            return helper0(1, size, newNums);
        }
    }
}
