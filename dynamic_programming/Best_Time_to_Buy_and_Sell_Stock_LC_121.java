package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/best-time-to-buy-and-sell-stock/description/

=======> Comparison of All Approaches in Your Code
Method	Approach	Time Complexity	Space Complexity	Notes
maxProfit0	Pure Recursion (Exponential)	O(2ⁿ)	O(n) (recursion stack)	Tries all possibilities of buy/skip/sell. Not feasible for large input.
maxProfit1	Recursion + Memoization (Top-Down DP)	O(n·2) ≈ O(n)	O(n·2)	Stores results in cache[idx][buy]. Efficient but still DP-based.
maxProfit2	Bottom-Up DP (Tabulation)	O(n·2) ≈ O(n)	O(n·2)	Iterative, avoids recursion overhead.
maxProfit3	Space-Optimized DP (2 arrays)	O(n·2) ≈ O(n)	O(2) = O(1)	Reduces memory by reusing only frontRow and currRow.
maxProfit4	Space-Optimized DP (explicit two vars)	O(n)	O(1)	Same as maxProfit3 but broken into buy/sell states directly.
maxProfit5	Further Simplified Space-Optimized DP	O(n)	O(1)	Clean and iterative DP, essentially the same recurrence in minimal form.
maxProfit6	Greedy (Optimal)	O(n)	O(1)	Track minPrice and compute best profit in one pass. This is the truly optimal solution.
 */

public class Best_Time_to_Buy_and_Sell_Stock_LC_121 {
    class Solution {
        public int maxProfit6(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length, minPrice = prices[0], maxProfit = 0;

            for(int i = 1; i < size; i++) {
                if(minPrice < prices[i]) {
                    maxProfit = Math.max(maxProfit, prices[i] - minPrice);
                } else {
                    minPrice = prices[i];
                }
            }

            return maxProfit;
        }

        public int maxProfit5(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length, frontRowBuy = 0, frontRowSell = 0, currRowBuy = 0, currRowSell = 0;

            for(int idx = size-1; idx >= 0; idx--) {
                //Option 01:
                // Choice 1: Buy today (-prices[idx]) and move to buy = 1 state
                int buyToday = -prices[idx] + frontRowSell;
                // Choice 2: Skip today (do nothing)
                currRowBuy = Math.max(buyToday, frontRowBuy);

                //Option 02:
                // Choice: 1: Sell today (+prices[idx]) and stop it
                int sellToday = prices[idx];
                // Choice 2: Skip today (hold stock)
                currRowSell = Math.max(sellToday, frontRowSell);
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
                        // Choice 1: Buy today (-prices[idx]) and move to buy = 1 state
                        int buyToday = -prices[idx] + frontRowSell;
                        // Choice 2: Skip today (do nothing)
                        currRowBuy = Math.max(buyToday, frontRowBuy);
                    } else {
                        // Choice: 1: Sell today (+prices[idx]) and stop it
                        int sellToday = prices[idx];
                        // Choice 2: Skip today (hold stock)
                        currRowSell = Math.max(sellToday, frontRowSell);
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
                        // Choice 1: Buy today (-prices[idx]) and move to buy = 1 state
                        int buyToday = -prices[idx] + frontRow[1];
                        // Choice 2: Skip today (do nothing)
                        int skipToday = frontRow[0];
                        currRow[buy] = Math.max(buyToday, skipToday);
                    } else {
                        // Choice: 1: Sell today (+prices[idx]) and stop it
                        int sellToday = prices[idx];
                        // Choice 2: Skip today (hold stock)
                        int holdToday = frontRow[1];
                        currRow[buy] = Math.max(sellToday, holdToday);
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
                        // Choice 1: Buy today (-prices[idx]) and move to buy = 1 state
                        int buyToday = -prices[idx] + cache[idx+1][1];
                        // Choice 2: Skip today (do nothing)
                        int skipToday = cache[idx+1][0];
                        cache[idx][buy] = Math.max(buyToday, skipToday);
                    } else {
                        // Choice: 1: Sell today (+prices[idx]) and stop it
                        int sellToday = prices[idx];
                        // Choice 2: Skip today (hold stock)
                        int holdToday = cache[idx+1][1];
                        cache[idx][buy] = Math.max(sellToday, holdToday);
                    }
                }
            }

            return cache[0][0];
        }

        private int helper1(int idx, int buy, int[] prices, int[][] cache) {
            if(idx >= prices.length) {
                return 0; // no more stocks or days left
            } else if(cache[idx][buy] != -1) {
                return cache[idx][buy];
            }

            if(buy == 0) {
                // Choice 1: Buy today (-prices[idx]) and move to buy = 1 state
                int buyToday = -prices[idx] + helper1(idx+1, 1, prices, cache);
                // Choice 2: Skip today (do nothing)
                int skipToday = helper1(idx+1, 0, prices, cache);
                return cache[idx][buy] = Math.max(buyToday, skipToday);
            } else {
                // Choice: 1: Sell today (+prices[idx]) and stop it
                int sellToday = prices[idx];
                // Choice 2: Skip today (hold stock)
                int holdToday = helper1(idx+1, 1, prices, cache);
                return cache[idx][buy] = Math.max(sellToday, holdToday);
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
                return 0; // no more stocks or days left
            }

            if(buy == 0) {
                // Choice 1: Buy today (-prices[idx]) and move to buy = 1 state
                int buyToday = -prices[idx] + helper0(idx+1, 1, prices);
                // Choice 2: Skip today (do nothing)
                int skipToday = helper0(idx+1, 0, prices);
                return Math.max(buyToday, skipToday);
            } else {
                // Choice: 1: Sell today (+prices[idx]) and stop it
                int sellToday = prices[idx];
                // Choice 2: Skip today (hold stock)
                int holdToday = helper0(idx+1, 1, prices);
                return Math.max(sellToday, holdToday);
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
