package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/partition-to-k-equal-sum-subsets/description/

Approach:
We are trying to divide the array into k subsets where each subset has the same sum.
To do this:
1) Calculate the target sum each subset should have: totalSu / k
2) Use recursion with backtracking to build each subset one by one
3) In each recursive call, we:
    - Loop through the array and try to add unused numbers (!seen[i]) to the current subset if they don't exceed the
    target sum.
    - If we reach the target sum for one subset, we reset and try to build the next subset (k-1 remaining).
4) If all k subsets are built successfully, return true.
 */

public class Partition_to_K_Equal_Sum_Subsets_LC_698 {
    class Solution {
        private boolean helper1(int idx, int currSum, int targetSum, int[] nums, int k, boolean[] seen) {
            if(k == 0) {
                return true;
            } else if(currSum == targetSum) {
                return helper1(0, 0, targetSum, nums, k-1, seen);
            }

            for(int i = idx; i < nums.length; i++) {
                if(seen[i] || currSum + nums[i] > targetSum) {
                    continue;
                }

                seen[i] = true;
                if(helper1(i+1, currSum+nums[i], targetSum, nums, k, seen)) {
                    return true;
                }
                seen[i] = false;
            }

            return false;
        }

        public boolean canPartitionKSubsets1(int[] nums, int k) {
            if(nums == null || nums.length == 0 || k <= 0) {
                return false;
            }

            int size = nums.length, totalSum = 0;
            boolean[] seen = new boolean[size];

            for(int itr: nums) {
                totalSum += itr;
            }

            if(totalSum % k != 0) {
                return false;
            }
            Arrays.sort(nums);
            reverse(nums);

            int targetSum = totalSum / k;
            return helper1(0, 0, targetSum, nums, k, seen);
        }

        private boolean helper0(int idx, int currSum, int targetSum, int[] nums, int k, boolean[] seen) {
            if(k == 0) {
                return true;
            } else if(currSum == targetSum) {
                return helper0(0, 0, targetSum, nums, k-1, seen);
            } else if(idx >= nums.length) {
                return false;
            }

            // Include current idx element
            if(!seen[idx] && currSum + nums[idx] <= targetSum) {
                seen[idx] = true;
                if(helper0(idx+1, currSum+nums[idx], targetSum, nums, k, seen)) {
                    return true;
                }
                seen[idx] = false;
            }

            // Don't include current idx element
            return helper0(idx+1, currSum, targetSum, nums, k, seen);
        }

        private void reverse(int[] nums) {
            int left = 0, right = nums.length-1;

            while (left < right) {
                int tmp = nums[left];
                nums[left] = nums[right];
                nums[right] = tmp;

                left++;
                right--;
            }
        }

        public boolean canPartitionKSubsets(int[] nums, int k) {
            if(nums == null || nums.length == 0 || k <= 0) {
                return false;
            }

            int size = nums.length, totalSum = 0;
            boolean[] seen = new boolean[size];

            for(int i = 0; i < size; i++) {
                totalSum += nums[i];
            }

            if(totalSum % k != 0) {
                return false;
            }

            int targetSum = totalSum/k;
            Arrays.sort(nums);
            reverse(nums);

            return helper0(0, 0, targetSum, nums, k, seen);
        }
    }
}
