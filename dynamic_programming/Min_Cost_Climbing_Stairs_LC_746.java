package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/min-cost-climbing-stairs/description/

 */

public class Min_Cost_Climbing_Stairs_LC_746 {
    class Solution {
        public int minCostClimbingStairs4(int[] cost) {
            if(cost == null || cost.length == 0) {
                return 0;
            }

            int size = cost.length;
            int prev1 = 0, prev2 = 0;

            for(int idx = size-1; idx >= 0; idx--) {
                int jumpOne = prev1;
                int jumpTwo = prev2;

                int minCost = Math.min(jumpOne, jumpTwo) + cost[idx];
                prev1 = prev2;
                prev2 = minCost;
            }

            return Math.min(prev1, prev2);
        }

        public int minCostClimbingStairs3(int[] cost) {
            if(cost == null || cost.length == 0) {
                return 0;
            }

            int size = cost.length;
            int[] cache = new int[size+2];

            for(int idx = size-1; idx >= 0; idx--) {
                int jumpOne = cache[idx+1];
                int jumpTwo = cache[idx+2];

                int minCost = Math.min(jumpOne, jumpTwo) + cost[idx];
                cache[idx] = minCost;
            }

            return Math.min(cache[0], cache[1]);
        }

        public int minCostClimbingStairs2(int[] cost) {
            if(cost == null || cost.length == 0) {
                return 0;
            }

            int size = cost.length;
            int[] cache = new int[size+2];

            for(int idx = size-1; idx >= 0; idx--) {
                int jumpOne = cache[idx+1];
                int jumpTwo = cache[idx+2];

                int minCost = Math.min(jumpOne, jumpTwo) + cost[idx];
                cache[idx] = minCost;
            }

            return Math.min(cache[0], cache[1]);
        }

        private int helper1(int idx, int[] cost, int[] cache) {
            if(idx >= cost.length) {
                return 0;
            } else if(cache[idx] != -1) {
                return cache[idx];
            }

            int jumpOne = helper1(idx+1, cost, cache);
            int jumpTwo = helper1(idx+2, cost, cache);

            int minCost = Math.min(jumpOne, jumpTwo) + cost[idx];
            return cache[idx] = minCost;
        }

        public int minCostClimbingStairs1(int[] cost) {
            if(cost == null || cost.length == 0) {
                return 0;
            }

            int size = cost.length;
            int[] cache = new int[size];
            Arrays.fill(cache, -1);

            return Math.min(helper1(0, cost, cache), helper1(1, cost, cache));
        }

        private int helper0(int idx, int[] cost) {
            if(idx >= cost.length) {
                return 0;
            }

            int jumpOne = helper0(idx+1, cost);
            int jumpTwo = helper0(idx+2, cost);

            return Math.min(jumpOne, jumpTwo) + cost[idx];
        }

        public int minCostClimbingStairs(int[] cost) {
            if(cost == null || cost.length == 0) {
                return 0;
            }

            return Math.min(helper0(0, cost), helper0(1, cost));
        }
    }
}
