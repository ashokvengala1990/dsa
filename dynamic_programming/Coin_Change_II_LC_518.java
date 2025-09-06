package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/coin-change-ii/description/

Unbounded Knapsack Problem
 */

public class Coin_Change_II_LC_518 {
    class Solution {
        public int change3(int paramAmount, int[] coins) {
            if(coins == null || coins.length == 0) {
                return 0;
            }

            int size = coins.length;
            int[] frontRow = new int[paramAmount+1];
            frontRow[0] = 1;

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[paramAmount+1];
                for(int amount = 0; amount <= paramAmount; amount++) {
                    // Don't include current index element
                    int notTake = 0 + frontRow[amount];

                    // Include current index element
                    int take = 0, newAmount = amount - coins[idx];
                    if(newAmount >= 0) {
                        take = currRow[newAmount];
                    }

                    currRow[amount] = take + notTake;
                }
                frontRow = currRow;
            }

            return frontRow[paramAmount];
        }

        public int change2(int paramAmount, int[] coins) {
            if(coins == null || coins.length == 0) {
                return 0;
            }

            int size = coins.length;
            int[][] cache = new int[size+1][paramAmount+1];
            cache[size][0] = 1;

            for(int idx = size-1; idx >= 0; idx--) {
                for(int amount = 0; amount <= paramAmount; amount++) {
                    // Don't include current index element
                    int notTake = 0 + cache[idx+1][amount];

                    // Include current index element
                    int take = 0, newAmount = amount - coins[idx];
                    if(newAmount >= 0) {
                        take = cache[idx][newAmount];
                    }

                    cache[idx][amount] = take + notTake;
                }
            }

            return cache[0][paramAmount];
        }

        private int helper1(int idx, int amount, int[] coins, int[][] cache) {
            if(idx >= coins.length) {
                if(amount == 0) {
                    return 1;
                } else {
                    return 0;
                }
            } else if(cache[idx][amount] != -1) {
                return cache[idx][amount];
            }

            // Don't include current index element
            int notTake = 0 + helper1(idx+1, amount, coins, cache);

            // Include current index element
            int take = 0, newAmount = amount - coins[idx];
            if(newAmount >= 0) {
                take = helper1(idx, newAmount, coins, cache);
            }

            return cache[idx][amount] = take + notTake;
        }

        public int change1(int amount, int[] coins) {
            if(coins == null || coins.length == 0) {
                return 0;
            }

            int size = coins.length;
            int[][] cache = new int[size][amount+1];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, amount, coins, cache);
        }

        private int helper0(int idx, int amount, int[] coins) {
            if(idx >= coins.length) {
                if(amount == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }

            // Don't include current index element
            int notTake = 0 + helper0(idx+1, amount, coins);

            // Include current index element
            int take = 0, newAmount = amount - coins[idx];
            if(newAmount >= 0) {
                take = helper0(idx, newAmount, coins);
            }

            return take + notTake;
        }

        public int change0(int amount, int[] coins) {
            if(coins == null || coins.length == 0) {
                return 0;
            }

            int size = coins.length;

            return helper0(0, amount, coins);
        }
    }
}
