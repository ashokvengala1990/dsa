package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/partition-equal-subset-sum/description/

This is about existence of an reqTargetSum in nums array or bot by selecting an elements and sum of them is equal = reqTargetSum:
- if exists, return true. otherwise, return false

This problem is similar to TargetSum LC 494 problem.
 */

public class Partition_Equal_Subset_Sum_LC_416 {
    class Solution {
        public boolean canPartition2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return false;
            }

            int size = nums.length, totalSum = 0;

            for(int n: nums) {
                totalSum += n;
            }

            if(totalSum % 2 != 0) {
                return false;
            }

            int reqTargetSum = totalSum / 2;
            int[] frontRow = new int[reqTargetSum+1];
            frontRow[0] = 1;

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[reqTargetSum+1];
                for(int targetSum = 1; targetSum <= reqTargetSum; targetSum++) {
                    // Include current index if element is <= targetSum
                    int newTargetSum = targetSum - nums[idx], take = 0;
                    if(newTargetSum >= 0) {
                        take = frontRow[newTargetSum];
                    }

                    // Don't include current index
                    int notTake = frontRow[targetSum];

                    currRow[targetSum] = (take == 1 || notTake == 1) ? 1 : 0;
                }
                frontRow = currRow;
            }

            return frontRow[reqTargetSum] == 1;
        }

        public boolean canPartition1(int[] nums) {
            if(nums == null || nums.length == 0) {
                return false;
            }

            int size = nums.length, totalSum = 0;

            for(int n: nums) {
                totalSum += n;
            }

            if(totalSum % 2 != 0) {
                return false;
            }

            int reqTargetSum = totalSum / 2;
            int[][] cache = new int[size+1][reqTargetSum+1];
            cache[size][0] = 1;

            for(int idx = size-1; idx >= 0; idx--) {
                for(int targetSum = 1; targetSum <= reqTargetSum; targetSum++) {
                    // Include current index if element is <= targetSum
                    int newTargetSum = targetSum - nums[idx], take = 0;
                    if(newTargetSum >= 0) {
                        take = cache[idx+1][newTargetSum];
                    }

                    // Don't include current index
                    int notTake = cache[idx+1][targetSum];

                    cache[idx][targetSum] = (take == 1 || notTake == 1) ? 1 : 0;
                }
            }

            return cache[0][reqTargetSum] == 1;
        }

        private int helper0(int idx, int targetSum, int[] nums, int[][] cache) {
            if(idx >= nums.length) {
                if(targetSum == 0) {
                    return 1;
                } else {
                    return 0;
                }
            } else if(cache[idx][targetSum] != -1) {
                return cache[idx][targetSum];
            }

            // Include current index if element is <= targetSum
            int newTargetSum = targetSum - nums[idx], take = 0;
            if(newTargetSum >= 0) {
                take = helper0(idx+1, newTargetSum, nums, cache);
            }

            // Don't include current index
            int notTake = helper0(idx+1, targetSum, nums, cache);

            return cache[idx][targetSum] = (take == 1 || notTake == 1) ? 1 : 0;
        }

        public boolean canPartition0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return false;
            }

            int size = nums.length, totalSum = 0;

            for(int n: nums) {
                totalSum += n;
            }

            if(totalSum % 2 != 0) {
                return false;
            }

            int targetSum = totalSum / 2;
            int[][] cache = new int[size][targetSum+1];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper0(0, targetSum, nums, cache) == 1;
        }
    }
}
