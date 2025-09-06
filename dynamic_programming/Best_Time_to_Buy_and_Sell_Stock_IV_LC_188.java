package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/description/

Here’s a summary table for LC 188 (Best Time to Buy and Sell Stock IV) based on your code:

Method	Approach	Time Complexity	Space Complexity	Notes
maxProfit0	Pure Recursion	O(2ⁿ·k)	O(n)	Tries all buy/sell possibilities with k transactions. Exponential, infeasible for large n.
maxProfit1	Recursion + Memoization (Top-Down DP)	O(n·2·(k+1)) ≈ O(n·k)	O(n·2·(k+1)) ≈ O(n·k)	Stores results in cache [idx][buy][cap]. Efficient for moderate n and k.
maxProfit2	Bottom-Up DP (Tabulation)	O(n·2·(k+1)) ≈ O(n·k)	O(n·2·(k+1)) ≈ O(n·k)	Iterative DP avoids recursion stack.
maxProfit3	Space-Optimized DP (2D arrays)	O(n·2·(k+1)) ≈ O(n·k)	O(2·(k+1)) = O(k)	Only keeps frontRow and currRow. Best space-wise.
 */

public class Best_Time_to_Buy_and_Sell_Stock_IV_LC_188 {
    class Solution {
        public int maxProfit3(int k, int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][] frontRow = new int[2][k+1];

            for(int idx = size-1; idx >= 0; idx--) {
                int[][] currRow = new int[2][k+1];
                for(int buy = 0; buy <= 1; buy++) {
                    for(int cap = 1; cap <= k; cap++) {
                        if(buy == 0) {
                            currRow[buy][cap] = Math.max(-prices[idx] + frontRow[1][cap], frontRow[0][cap]);
                        } else {
                            currRow[buy][cap] = Math.max(prices[idx] + frontRow[0][cap-1], frontRow[1][cap]);
                        }
                    }
                }
                frontRow = currRow;
            }

            return frontRow[0][k];
        }

        public int maxProfit2(int k, int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][][] cache = new int[size+1][2][k+1];

            for(int idx = size-1; idx >= 0; idx--) {
                for(int buy = 0; buy <= 1; buy++) {
                    for(int cap = 1; cap <= k; cap++) {
                        if(buy == 0) {
                            cache[idx][buy][cap] = Math.max(-prices[idx] + cache[idx+1][1][cap], cache[idx+1][0][cap]);
                        } else {
                            cache[idx][buy][cap] = Math.max(prices[idx] + cache[idx+1][0][cap-1], cache[idx+1][1][cap]);
                        }
                    }
                }
            }

            return cache[0][0][k];
        }

        private int helper1(int idx, int buy, int cap, int[] prices, int[][][] cache) {
            if(idx >= prices.length || cap == 0) {
                return 0;
            } else if(cache[idx][buy][cap] != -1) {
                return cache[idx][buy][cap];
            }

            if(buy == 0) {
                return cache[idx][buy][cap] = Math.max(-prices[idx] + helper1(idx+1, 1, cap, prices, cache), helper1(idx+1, 0, cap, prices, cache));
            } else {
                return cache[idx][buy][cap] = Math.max(prices[idx] + helper1(idx+1, 0, cap-1, prices, cache), helper1(idx+1, 1, cap, prices, cache));
            }
        }

        public int maxProfit1(int k, int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][][] cache = new int[size][2][k+1];

            for(int[][] itr1: cache) {
                for(int[] itr2: itr1) {
                    Arrays.fill(itr2, -1);
                }
            }

            return helper1(0, 0, k, prices, cache);
        }

        private int helper0(int idx, int buy, int cap, int[] prices) {
            if(idx >= prices.length || cap == 0) {
                return 0;
            }

            if(buy == 0) {
                return Math.max(-prices[idx] + helper0(idx+1, 1, cap, prices), helper0(idx+1, 0, cap, prices));
            } else {
                return Math.max(prices[idx] + helper0(idx+1, 0, cap-1, prices), helper0(idx+1, 1, cap, prices));
            }
        }

        public int maxProfit0(int k, int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            return helper0(0, 0, k, prices);
        }
    }
}
