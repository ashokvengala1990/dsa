package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/minimum-cost-for-tickets/

Approach 1: Linear Scan (mincostTickets2)
- For each day from the end to the begining:
    * Tried all 3 pass options (1, 7, 30)
    * Used a linear while loop to find the next index j where the current pass doesn't cover.
    * Calculated cost: cost[i] + cache[j]
    * Took the min of all such options.
- Time Complexity: O(N * K * N) where N = number of days, K = 3 (pass types) and inner while loop
  may go up to N.

Approach 2: Binary Search Optimization (mincostTickets3):
- Same logic, but used binary search instead of linear scan to find the next index j.
- Binary search finds the smallest index where days[i] >= days[idx] + pass[i]
* Time Complexity: O(n * logN) - reduced from linear scan to binary lookup for better performance on
  large inputs.

 */

public class Minimum_Cost_For_Tickets_LC_983 {
    class Solution {
        private int binarySearch(int left, int[] days, int target) {
            int right = days.length-1;

            while (left <= right) {
                int mid = left + ((right - left)/2);

                if(days[mid] < target) {
                    left = mid+1;
                } else {
                    right = mid-1;
                }
            }
            return left;
        }

        public int mincostTickets3(int[] days, int[] costs) {
            if (days == null || days.length == 0 || costs == null || costs.length == 0) {
                return 0;
            }

            int size = days.length;
            int[] pass = new int[]{1, 7, 30};
            int[] cache = new int[size + 1];
            Arrays.fill(cache, 0);

            for (int idx = size - 1; idx >= 0; idx--) {
                int minCurr = Integer.MAX_VALUE;
                for (int i = 0; i < costs.length; i++) {
                    int passValue = pass[i], costValue = costs[i], j = idx;
                    int nextIdx = binarySearch(idx + 1, days, days[idx] + passValue);
                    minCurr = Math.min(minCurr, costValue + cache[nextIdx]);
                }
                cache[idx] = minCurr;
            }

            return cache[0];
        }

        public int mincostTickets2(int[] days, int[] costs) {
            if(days == null || days.length == 0 || costs == null || costs.length == 0) {
                return 0;
            }

            int size = days.length;
            int[] pass = new int[]{1, 7, 30};
            int[] cache = new int[size+1];
            Arrays.fill(cache, 0);

            for(int idx = size-1; idx >= 0; idx--) {
                int minCurr = Integer.MAX_VALUE;
                for(int i = 0; i < costs.length; i++) {
                    int passValue = pass[i], costValue = costs[i], j = idx;

                    while (j < days.length && days[j] < (days[idx] + passValue)) {
                        j++;
                    }

                    minCurr = Math.min(minCurr , costValue + cache[j]);
                }
                cache[idx] = minCurr;
            }

            return cache[0];
        }

        private int helper1(int idx, int[] days, int[] costs, int[] pass, int[] cache) {
            if(idx >= days.length) {
                return 0;
            } else if(cache[idx] != Integer.MAX_VALUE) {
                return cache[idx];
            }

            for(int i = 0; i < costs.length; i++) {
                int passValue = pass[i], costValue = costs[i], j = idx;

                while (j < days.length && days[j] < (days[idx] + passValue)) {
                    j++;
                }

                cache[idx] = Math.min(cache[idx], costValue + helper1(j, days, costs, pass, cache));
            }

            return cache[idx];
        }

        public int mincostTickets1(int[] days, int[] costs) {
            if(days == null || days.length == 0 || costs == null || costs.length == 0) {
                return 0;
            }

            int size = days.length;
            int[] pass = new int[]{1, 7, 30};
            int[] cache = new int[size];
            Arrays.fill(cache, Integer.MAX_VALUE);

            return helper1(0, days, costs, pass, cache);
        }

        private int helper0(int idx, int[] days, int[] costs, int[] pass) {
            if(idx >= days.length) {
                return 0;
            }

            int minCost = Integer.MAX_VALUE;

            for(int i = 0; i < costs.length; i++) {
                int passValue = pass[i], j = idx, costValue = costs[i];

                while (j < days.length && days[j] < days[idx] + passValue) {
                    j++;
                }
                minCost = Math.min(minCost, costValue + helper0(j, days, costs, pass));
            }

            return minCost;
        }

        public int mincostTickets(int[] days, int[] costs) {
            if (days == null || days.length == 0 || costs == null || costs.length == 0) {
                return 0;
            }

            int[] pass = {1, 7, 30};

            return helper0(0, days, costs, pass);
        }
    }
}
