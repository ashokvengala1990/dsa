package neetcode.dynamic_programming;

import java.util.Arrays;
/*
https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/description/

Here’s a summary table for LC 309 (Best Time to Buy and Sell Stock with Cooldown) based on your solutions:

Method	Approach	Time Complexity	Space Complexity	Notes
maxProfit0	Pure Recursion	O(2ⁿ)	O(n)	Tries all buy/sell possibilities with 1-day cooldown. Exponential, not feasible for large input.
maxProfit1	Recursion + Memoization (Top-Down DP)	O(n·2) ≈ O(n)	O(n·2)	Uses cache [idx][buy] to store results. Efficient for moderate input.
maxProfit2	Bottom-Up DP (Tabulation)	O(n·2) ≈ O(n)	O(n·2)	Iterative DP avoids recursion stack. Handles cooldown by accessing idx+2.
maxProfit3	Space-Optimized DP (2 arrays)	O(n·2) ≈ O(n)	O(2) = O(1)	Uses frontRow1 and frontRow2 arrays for cooldown effect.
maxProfit4	Further Space-Optimized DP	O(n)	O(1)	Only two arrays of size 2. Clean and minimal iterative DP. Optimal solution.
 */

public class Best_Time_to_Buy_and_Sell_Stock_with_Cooldown_LC_309 {
    class Solution {
        public int maxProfit4(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[] frontRow1 = new int[2];
            int[] frontRow2 = new int[2];

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[2];
                currRow[0] = Math.max(-prices[idx] + frontRow1[1], frontRow1[0]);
                currRow[1] = Math.max(prices[idx] + frontRow2[0], frontRow1[1]);
                frontRow2 = frontRow1;
                frontRow1 = currRow;
            }

            return frontRow1[0];
        }

        public int maxProfit3(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[] frontRow1 = new int[2];
            int[] frontRow2 = new int[2];

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[2];
                for(int buy = 0; buy <= 1; buy++) {
                    if(buy == 0) {
                        currRow[buy] = Math.max(-prices[idx] + frontRow1[1], frontRow1[0]);
                    } else {
                        currRow[buy] = Math.max(prices[idx] + frontRow2[0], frontRow1[1]);
                    }
                }
                frontRow2 = frontRow1;
                frontRow1 = currRow;
            }

            return frontRow1[0];
        }

        public int maxProfit2(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][] cache = new int[size+2][2];

            for(int idx = size-1; idx >= 0; idx--) {
                for(int buy = 0; buy <= 1; buy++) {
                    if(buy == 0) {
                        cache[idx][buy] = Math.max(-prices[idx] + cache[idx+1][1], cache[idx+1][0]);
                    } else {
                        cache[idx][buy] = Math.max(prices[idx] + cache[idx+2][0], cache[idx+1][1]);
                    }
                }
            }

            return cache[0][0];
        }

        private int helper1(int idx, int buy, int[] prices, int[][] cache) {
            if(idx >= prices.length) {
                return 0;
            } else if(cache[idx][buy] != -1) {
                return cache[idx][buy];
            }

            if(buy == 0) {
                return cache[idx][buy] = Math.max(-prices[idx] + helper1(idx+1, 1, prices, cache), helper1(idx+1, 0, prices, cache));
            } else {
                return cache[idx][buy] = Math.max(prices[idx] + helper1(idx+2, 0, prices, cache), helper1(idx+1, 1, prices, cache));
            }
        }

        public int maxProfit1(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][] cache = new int[size][2];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, 0, prices, cache);
        }

        private int helper0(int idx, int buy, int[] prices) {
            if(idx >= prices.length) {
                return 0;
            }

            if(buy == 0) {
                return Math.max(-prices[idx] + helper0(idx+1, 1, prices), helper0(idx+1, 0, prices));
            } else {
                return Math.max(prices[idx] + helper0(idx+2, 0, prices), helper0(idx+1, 1, prices));
            }
        }

        public int maxProfit0(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            return helper0(0, 0, prices);
        }
    }
}
