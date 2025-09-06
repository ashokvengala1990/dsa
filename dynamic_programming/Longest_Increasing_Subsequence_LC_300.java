package neetcode.dynamic_programming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
https://leetcode.com/problems/longest-increasing-subsequence/description/

- Using Recursion
    * Don't include current index element
    * Include current index element according to condition check with previous element
    * TC: O(2^n)
    * SC: O(n)
- Using Recursion with Memoization
    * TC: O(n^2)
    * SC: O(n^2) + O(n) because of cache and recursion
- Using True Dynamic Programming (Bottom-up approach)
    * TC: O(n^2)
    * SC: O(n^2)
    Note: increment by 1 in this approach for the second dimension of the cache
- Space Optimization
    * TC: O(n^2)
    * SC: O(2*n)
 */

public class Longest_Increasing_Subsequence_LC_300 {
    static class Solution {
        public int lengthOfLIS4(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            List<Integer> result = new ArrayList<>();

            for(int i = 0; i < size; i++) {
                if(result.isEmpty() || result.get(result.size()-1) < nums[i]) {
                    result.add(nums[i]);
                } else {
                    int idx = Collections.binarySearch(result, nums[i]);
                    if(idx < 0) {
                        idx = -1 * idx - 1;
                    }
                    result.set(idx, nums[i]);
                }
            }

            return result.size();
        }

        public int lengthOfLIS3(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int[] front = new int[size+1];

            for(int currIdx = size-1; currIdx >= 0; currIdx--) {
                int[] curr = new int[size+1];
                for(int prevIdx = currIdx-1; prevIdx >= -1; prevIdx--) {
                    // Don't include current index element
                    int notTake = 0 + front[prevIdx+1];

                    int take = 0;
                    if(prevIdx == -1 || nums[prevIdx] < nums[currIdx]) {
                        take = 1 + front[currIdx+1];
                    }

                    curr[prevIdx+1] = Math.max(take, notTake);
                }
                front = curr;
            }

            return front[0];
        }

        public int lengthOfLIS2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int[][] cache = new int[size+1][size+1];

            for(int currIdx = size-1; currIdx >= 0; currIdx--) {
                for(int prevIdx = currIdx-1; prevIdx >= -1; prevIdx--) {
                    // Don't include current index element
                    int notTake = 0 + cache[currIdx+1][prevIdx+1];

                    int take = 0;
                    if(prevIdx == -1 || nums[prevIdx] < nums[currIdx]) {
                        take = 1 + cache[currIdx+1][currIdx+1];
                    }

                    cache[currIdx][prevIdx+1] = Math.max(take, notTake);
                }
            }

            return cache[0][0];
        }

        private int helper1(int currIdx, int prevIdx, int[] nums, int[][] cache) {
            if(currIdx >= nums.length) {
                return 0;
            } else if(cache[currIdx][prevIdx+1] != -1) {
                return cache[currIdx][prevIdx+1];
            }

            // Don't include current index element
            int notTake = 0 + helper1(currIdx+1, prevIdx, nums, cache);

            int take = 0;
            if(prevIdx == -1 || nums[prevIdx] < nums[currIdx]) {
                take = 1 + helper1(currIdx+1, currIdx, nums, cache);
            }

            return cache[currIdx][prevIdx+1] = Math.max(take, notTake);
        }

        public int lengthOfLIS1(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int[][] cache = new int[size][size];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, -1, nums, cache);
        }

        private int helper0(int currIdx, int prevIdx, int[] nums) {
            if(currIdx >= nums.length) {
                return 0;
            }

            // Don't include current index element
            int notTake = 0 + helper0(currIdx+1, prevIdx, nums);

            int take = 0;
            if(prevIdx == -1 || nums[prevIdx] < nums[currIdx]) {
                take = 1 + helper0(currIdx+1, currIdx, nums);
            }

            return Math.max(take, notTake);
        }

        public int lengthOfLIS0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            return helper0(0, -1, nums);
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 10, 2, 2};

        Solution s = new Solution();
        System.out.println(s.lengthOfLIS4(nums));
    }
}
