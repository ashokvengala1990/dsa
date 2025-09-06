package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/partition-array-for-maximum-sum/description/

- Using Front Partition Approach (With For-Loop based recursion)
- Recursion
    * TC: O(exponential)
    * SC: O(n)
- Recursion with Memoization
    * TC: O(n*k)
    * SC: O(n) + O(n)
- True Dynamic Programming
    * TC: O(n*k)
    * SC: O(n)

 */

public class Partition_Array_for_Maximum_Sum_LC_1043 {
    class Solution {
        public int maxSumAfterPartitioning2(int[] arr, int k) {
            if(arr == null || arr.length == 0 || k <= 0) {
                return 0;
            }

            int size = arr.length;
            int[] cache = new int[size+1];

            for(int idx = size-1; idx >= 0; idx--) {
                int maxSum = Integer.MIN_VALUE, currMaxElement = Integer.MIN_VALUE;

                for(int i = idx; i < Math.min(idx+k, arr.length); i++) {
                    currMaxElement = Math.max(currMaxElement, arr[i]);
                    int currSum = (currMaxElement * (i - idx + 1)) + cache[i+1];
                    maxSum = Math.max(maxSum, currSum);
                }

                cache[idx] = maxSum;
            }

            return cache[0];
        }

        private int helper1(int idx, int[] arr, int k, int[] cache) {
            if(idx >= arr.length) {
                return 0;
            } else if(cache[idx] != -1) {
                return cache[idx];
            }

            int maxSum = Integer.MIN_VALUE, currMaxElement = Integer.MIN_VALUE;

            for(int i = idx; i < Math.min(idx+k, arr.length); i++) {
                currMaxElement = Math.max(currMaxElement, arr[i]);
                int currSum = (currMaxElement * (i - idx + 1)) + helper1(i+1, arr, k, cache);
                maxSum = Math.max(maxSum, currSum);
            }

            return cache[idx] = maxSum;
        }

        public int maxSumAfterPartitioning1(int[] arr, int k) {
            if(arr == null || arr.length == 0 || k <= 0) {
                return 0;
            }

            int size = arr.length;
            int[] cache = new int[size];
            Arrays.fill(cache, -1);

            return helper1(0, arr, k, cache);
        }

        private int helper0(int idx, int[] arr, int k) {
            if(idx >= arr.length) {
                return 0;
            }

            int maxSum = Integer.MIN_VALUE, maxElement = Integer.MIN_VALUE;
            for(int i = idx; i < Math.min(idx+k, arr.length); i++) {
                maxElement = Math.max(maxElement, arr[i]);
                int currSum = (maxElement * (i - idx + 1)) + helper0(i+1, arr, k);
                maxSum = Math.max(maxSum, currSum);
            }

            return maxSum;
        }

        public int maxSumAfterPartitioning0(int[] arr, int k) {
            return helper0(0, arr, k);
        }
    }
}
