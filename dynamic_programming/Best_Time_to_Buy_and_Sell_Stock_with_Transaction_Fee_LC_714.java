package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/description/

Here’s a summary table for LC 714 (Best Time to Buy and Sell Stock with Transaction Fee) based on your solutions:

Method	Approach	Time Complexity	Space Complexity	Notes
maxProfit0	Pure Recursion	O(2ⁿ)	O(n)	Tries all possibilities of buy/skip/sell with fee. Exponential, not feasible for large input.
maxProfit1	Recursion + Memoization (Top-Down DP)	O(n·2) ≈ O(n)	O(n·2)	Uses cache [idx][buy] to store results. Efficient for moderate input.
maxProfit2	Bottom-Up DP (Tabulation)	O(n·2) ≈ O(n)	O(n·2)	Iterative DP avoids recursion stack.
maxProfit3	Space-Optimized DP (2 arrays)	O(n·2) ≈ O(n)	O(2) = O(1)	Uses frontRow and currRow arrays.
maxProfit4	Further Space-Optimized DP	O(n)	O(1)	Only two variables for buy/sell states.
maxProfit5	Clean Iterative DP	O(n)	O(1)	Minimal and clear recurrence using front/back row. Optimal solution.
 */

public class Best_Time_to_Buy_and_Sell_Stock_with_Transaction_Fee_LC_714 {
    class Solution {
        public int maxProfit5(int[] prices, int fee) {
            if(prices == null || prices.length == 0 || fee < 0) {
                return 0;
            }

            int size = prices.length, frontRowBuy = 0, frontRowSell = 0, currRowBuy = 0, currRowSell = 0;

            for(int idx = size-1; idx >= 0; idx--) {
                currRowBuy = Math.max(-prices[idx] + frontRowSell, frontRowBuy);
                currRowSell = Math.max(prices[idx] - fee + frontRowBuy, frontRowSell);
                frontRowBuy = currRowBuy;
                frontRowSell = currRowSell;
            }

            return frontRowBuy;
        }

        public int maxProfit4(int[] prices, int fee) {
            if(prices == null || prices.length == 0 || fee < 0) {
                return 0;
            }

            int size = prices.length;
            int[] frontRow = new int[2];

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[2];
                currRow[0] = Math.max(-prices[idx] + frontRow[1], frontRow[0]);
                currRow[1] = Math.max(prices[idx] - fee + frontRow[0], frontRow[1]);
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int maxProfit3(int[] prices, int fee) {
            if(prices == null || prices.length == 0 || fee < 0) {
                return 0;
            }

            int size = prices.length;
            int[] frontRow = new int[2];

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[2];
                for(int buy = 0; buy <= 1; buy++) {
                    if(buy == 0) {
                        currRow[buy] = Math.max(-prices[idx] + frontRow[1], frontRow[0]);
                    } else {
                        currRow[buy] = Math.max(prices[idx] - fee + frontRow[0], frontRow[1]);
                    }
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int maxProfit2(int[] prices, int fee) {
            if(prices == null || prices.length == 0 || fee < 0) {
                return 0;
            }

            int size = prices.length;
            int[][] cache = new int[size+1][2];

            for(int idx = size-1; idx >= 0; idx--) {
                for(int buy = 0; buy <= 1; buy++) {
                    if(buy == 0) {
                        cache[idx][buy] = Math.max(-prices[idx] + cache[idx+1][1], cache[idx+1][0]);
                    } else {
                        cache[idx][buy] = Math.max(prices[idx] - fee + cache[idx+1][0], cache[idx+1][1]);
                    }
                }
            }

            return cache[0][0];
        }

        private int helper1(int idx, int buy, int fee, int[] prices, int[][] cache) {
            if(idx >= prices.length) {
                return 0;
            } else if(cache[idx][buy] != -1) {
                return cache[idx][buy];
            }

            if(buy == 0) {
                return cache[idx][buy] = Math.max(-prices[idx] + helper1(idx+1, 1, fee, prices, cache), helper1(idx+1, 0, fee, prices, cache));
            } else {
                return cache[idx][buy] = Math.max(prices[idx] - fee + helper1(idx+1, 0, fee, prices, cache), helper1(idx+1, 1, fee, prices, cache));
            }
        }

        public int maxProfit1(int[] prices, int fee) {
            if(prices == null || prices.length == 0 || fee < 0) {
                return 0;
            }

            int size = prices.length;
            int[][] cache = new int[size][2];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, 0, fee, prices, cache);
        }

        private int helper0(int idx, int buy, int fee, int[] prices) {
            if(idx >= prices.length) {
                return 0;
            }

            if(buy == 0) {
                return Math.max(-prices[idx] + helper0(idx+1, 1, fee, prices), helper0(idx+1, 0, fee, prices));
            } else {
                return Math.max(prices[idx] - fee + helper0(idx+1, 0, fee, prices), helper0(idx+1, 1, fee, prices));
            }
        }

        public int maxProfit0(int[] prices, int fee) {
            if(prices == null || prices.length == 0 || fee < 0) {
                return 0;
            }

            return helper0(0, 0, fee, prices);
        }
    }
}
