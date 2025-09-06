package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/description/

Method	Approach	Time Complexity	Space Complexity	Notes
maxProfit0	Pure Recursion	O(2ⁿ·k) = O(2ⁿ·2)	O(n)	Exponential, tries all buy/sell decisions. Not feasible for large n.
maxProfit1	Recursion + Memoization (Top-Down DP)	O(n·2·3) = O(n)	O(n·2·3) = O(n)	Memoized solution storing [idx][buy][cap]. Efficient and clear.
maxProfit2	Bottom-Up DP (Tabulation)	O(n·2·3) = O(n)	O(n·2·3) = O(n)	Iterative version of memoization. Avoids recursion stack.
maxProfit3	Space-Optimized DP (2D arrays)	O(n·2·3) = O(n)	O(2·3) = O(1)	Only keeps current and next row. Reduces memory drastically.
maxProfit4	Further Simplified Space-Optimized DP	O(n·2·2) = O(n)	O(2·3) = O(1)	Explicitly writes buy/cap states. Minimal variables, still optimal.
 */

public class Best_Time_to_Buy_and_Sell_Stock_III_LC_123 {
    class Solution {
        public int maxProfit4(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][] frontRow = new int[2][3];

            for(int idx = size-1; idx >= 0; idx--) {
                int[][] currRow = new int[2][3];
                currRow[0][1] = Math.max(-prices[idx] + frontRow[1][1], frontRow[0][1]);
                currRow[0][2] = Math.max(-prices[idx] + frontRow[1][2], frontRow[0][2]);
                currRow[1][1] = Math.max(prices[idx] + frontRow[0][0], frontRow[1][1]);
                currRow[1][2] = Math.max(prices[idx] + frontRow[0][1], frontRow[1][2]);
                frontRow = currRow;
            }

            return frontRow[0][2];
        }

        public int maxProfit3(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][] frontRow = new int[2][3];

            for(int idx = size-1; idx >= 0; idx--) {
                int[][] currRow = new int[2][3];
                for(int buy = 0; buy <= 1; buy++) {
                    for(int cap = 1; cap <= 2; cap++) {
                        if(buy == 0) {
                            currRow[buy][cap] = Math.max(-prices[idx] + frontRow[1][cap], frontRow[0][cap]);
                        } else {
                            currRow[buy][cap] = Math.max(prices[idx] + frontRow[0][cap-1], frontRow[1][cap]);
                        }
                    }
                }
                frontRow = currRow;
            }

            return frontRow[0][2];
        }

        public int maxProfit2(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][][] cache = new int[size+1][2][3];

            for(int idx = size-1; idx >= 0; idx--) {
                for(int buy = 0; buy <= 1; buy++) {
                    for(int cap = 1; cap <= 2; cap++) {
                        if(buy == 0) {
                            cache[idx][buy][cap] = Math.max(-prices[idx] + cache[idx+1][1][cap], cache[idx+1][0][cap]);
                        } else {
                            cache[idx][buy][cap] = Math.max(prices[idx] + cache[idx+1][0][cap-1], cache[idx+1][1][cap]);
                        }
                    }
                }
            }

            return cache[0][0][2];
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

        public int maxProfit1(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            int size = prices.length;
            int[][][] cache = new int[size][2][3];

            for(int[][] itr1: cache) {
                for(int[] itr2: itr1) {
                    Arrays.fill(itr2, -1);
                }
            }

            return helper1(0, 0, 2, prices, cache);
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

        public int maxProfit0(int[] prices) {
            if(prices == null || prices.length == 0) {
                return 0;
            }

            return helper0(0, 0, 2, prices);
        }
    }
}
