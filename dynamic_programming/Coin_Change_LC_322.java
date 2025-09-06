package neetcode.dynamic_programming;

import java.util.Arrays;

/*
- Unbounded Knapsack problem

https://leetcode.com/problems/coin-change/description/
 */

public class Coin_Change_LC_322 {
    class Solution {
        public int coinChange3(int[] coins, int paramAmount) {
            if(coins == null || coins.length == 0) {
                return 0;
            }

            int size = coins.length;
            int[] frontRow = new int[paramAmount+1];
            frontRow[0] = 0;

            for(int currAmt = 1; currAmt <= paramAmount; currAmt++) {
                frontRow[currAmt] = (int)1e9;
            }

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[paramAmount+1];
                for(int amount = 0; amount <= paramAmount; amount++) {
                    // Don't include current index
                    int notTake = 0 + frontRow[amount];

                    int newAmount = amount - coins[idx], take = (int)1e9;
                    if(newAmount >= 0) {
                        take = 1 + currRow[newAmount];
                    }

                    currRow[amount] = Math.min(take, notTake);
                }
                frontRow = currRow;
            }

            return frontRow[paramAmount] == (int)1e9 ? -1 : frontRow[paramAmount];
        }

        public int coinChange2(int[] coins, int paramAmount) {
            if(coins == null || coins.length == 0) {
                return 0;
            }

            int size = coins.length;
            int[][] cache = new int[size+1][paramAmount+1];
            cache[size][0] = 0;

            for(int currAmt = 1; currAmt <= paramAmount; currAmt++) {
                cache[size][currAmt] = (int)1e9;
            }

            for(int idx = size-1; idx >= 0; idx--) {
                for(int amount = 0; amount <= paramAmount; amount++) {
                    // Don't include current index
                    int notTake = 0 + cache[idx+1][amount];

                    int newAmount = amount - coins[idx], take = (int)1e9;
                    if(newAmount >= 0) {
                        take = 1 + cache[idx][newAmount];
                    }

                    cache[idx][amount] = Math.min(take, notTake);
                }
            }

            return cache[0][paramAmount] == (int)1e9 ? -1 : cache[0][paramAmount];
        }

        private int helper1(int idx, int amount, int[] coins, int[][] cache) {
            if(idx >= coins.length) {
                if(amount == 0) {
                    return 0;
                } else {
                    return (int) 1e9;
                }
            } else if(cache[idx][amount] != -1) {
                return cache[idx][amount];
            }

            // Don't include current index
            int notTake = 0 + helper1(idx+1, amount, coins, cache);

            int newAmount = amount - coins[idx], take = (int)1e9;
            if(newAmount >= 0) {
                take = 1 + helper1(idx, newAmount, coins, cache);
            }

            return cache[idx][amount] = Math.min(take, notTake);
        }

        public int coinChange1(int[] coins, int amount) {
            if(coins == null || coins.length == 0) {
                return 0;
            }

            int size = coins.length;
            int[][] cache = new int[size][amount+1];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            int result = helper1(0, amount, coins, cache);
            return result == (int) 1e9 ? -1 : result;
        }

        private int helper0(int idx, int[] coins, int amount) {
            if(idx >= coins.length) {
                if(amount == 0) {
                    return 0;
                } else {
                    return (int) 1e9;
                }
            }

            // Don't include current index
            int notTake = 0 + helper0(idx+1, coins, amount);

            int newAmount = amount - coins[idx], take = (int)1e9;
            if(newAmount >= 0) {
                take = 1 + helper0(idx, coins, newAmount);
            }

            return Math.min(take, notTake);
        }

        public int coinChange0(int[] coins, int amount) {
            if(coins == null || coins.length == 0) {
                return 0;
            }

            int size = coins.length;

            int result = helper0(0, coins, amount);
            return result == (int) 1e9 ? -1 : result;
        }
    }
}
