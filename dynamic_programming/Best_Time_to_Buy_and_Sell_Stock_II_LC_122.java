package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/description/

Method	Approach	Time Complexity	Space Complexity	Notes
maxProfit0	Pure Recursion (Exponential)	O(2ⁿ)	O(n)	Tries all possibilities of buy/skip/sell. Not feasible for large input.
maxProfit1	Recursion + Memoization (Top-Down DP)	O(n·2) ≈ O(n)	O(n·2)	Stores results in cache[idx][buy]. Efficient DP-based solution.
maxProfit2	Bottom-Up DP (Tabulation)	O(n·2) ≈ O(n)	O(n·2)	Iterative approach avoids recursion overhead.
maxProfit3	Space-Optimized DP (2 arrays)	O(n·2) ≈ O(n)	O(2) = O(1)	Reduces memory by reusing only frontRow and currRow.
maxProfit4	Space-Optimized DP (explicit two vars)	O(n)	O(1)	Same as maxProfit3 but using separate currRowBuy/currRowSell.
maxProfit5	Further Simplified Space-Optimized DP	O(n)	O(1)	Clean iterative DP, minimal form of recurrence.
maxProfit6	Greedy (Optimal)	O(n)	O(1)	Track minPrice and sum positive differences. Truly optimal solution.
 */

public class Best_Time_to_Buy_and_Sell_Stock_II_LC_122 {
    class Solution {
        public int maxProfit6(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length, totalMaxProfit = 0;

            for(int i =0; i < size-1; i++) {
                if(prices[i] < prices[i+1]) {
                    totalMaxProfit += prices[i+1] - prices[i];
                }
            }

            return totalMaxProfit;
        }

        public int maxProfit5(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length, frontRowBuy = 0, frontRowSell = 0, currRowBuy = 0, currRowSell = 0;

            for(int idx = size-1; idx >= 0; idx--) {
                currRowBuy = Math.max(-prices[idx] + frontRowSell, frontRowBuy);
                currRowSell = Math.max(prices[idx] + frontRowBuy, Math.max(prices[idx]+currRowBuy, frontRowSell));
                frontRowBuy = currRowBuy;
                frontRowSell = currRowSell;
            }

            return frontRowBuy;
        }

        public int maxProfit4(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length, frontRowBuy = 0, frontRowSell = 0, currRowBuy = 0, currRowSell = 0;

            for(int idx = size-1; idx >= 0; idx--) {
                for(int buy = 0; buy <= 1; buy++) {
                    if(buy == 0) {
                        currRowBuy = Math.max(-prices[idx] + frontRowSell, frontRowBuy);
                    } else {
                        currRowSell = Math.max(prices[idx] + frontRowBuy,
                                Math.max(prices[idx]+currRowBuy, frontRowSell));
                    }
                }
                frontRowBuy = currRowBuy;
                frontRowSell = currRowSell;
            }

            return frontRowBuy;
        }

        public int maxProfit3(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[] frontRow = new int[2];

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[2];
                for(int buy = 0; buy <= 1; buy++) {
                    if(buy == 0) {
                        currRow[buy] = Math.max(-prices[idx] + frontRow[1]
                                , frontRow[0]);
                    } else {
                        currRow[buy] = Math.max(prices[idx] + frontRow[0],
                                Math.max(prices[idx]+currRow[0]
                                        , frontRow[1]));
                    }
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int maxProfit2(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][] cache = new int[size+1][2];

            for(int idx = size-1; idx >= 0; idx--) {
                for(int buy = 0; buy <= 1; buy++) {
                    if(buy == 0) {
                        cache[idx][buy] = Math.max(-prices[idx] + cache[idx+1][1]
                                , cache[idx+1][0]);
                    } else {
                        cache[idx][buy] = Math.max(prices[idx] + cache[idx+1][0],
                                Math.max(prices[idx]+ cache[idx][0]
                                        , cache[idx+1][1]));
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
                return cache[idx][buy] = Math.max(-prices[idx] + helper1(idx+1, 1, prices, cache)
                        , helper1(idx+1, 0, prices, cache));
            } else {
                return cache[idx][buy] = Math.max(prices[idx] + helper1(idx+1, 0, prices, cache),
                        Math.max(prices[idx]+ helper1(idx, 0, prices, cache)
                                , helper1(idx+1, 1, prices, cache)));
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
                return Math.max(prices[idx] + helper0(idx+1, 0, prices),
                        Math.max(prices[idx]+ helper0(idx, 0, prices), helper0(idx+1, 1, prices)));
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
