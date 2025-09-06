package neetcode.dynamic_programming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*

TotalSum = S1 + S2;
Consider, s1 is the plus sign and s2 is the negative sign

S1 = TotalSum - S2;

S1 - S2 = target
TotalSum - S2 - S2 = target
TotalSum - 2*S2 = target
S2 = (TotalSum - target)/2

Eg: {0 0 1}
-0, -0, +1
+0, +0, +1
-0, +0, +1
0, -0, +1

https://leetcode.com/problems/target-sum/description/

In this problem, it is asking to count the number of ways we have for the subset s2. It is in-directly asking the subset s2. In
questions stated that we need to find the number of ways we can assign plus and negative sign to elements in nums array.

 */

public class Target_Sum_LC_494 {
    class Solution {
        public int findTargetSumWays5(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length, totalSum = 0;

            for(int n: nums) {
                totalSum += n;
            }

            if((totalSum - target) < 0 || (totalSum - target) % 2 != 0) {
                return 0;
            }
            int reqTargetSum = (totalSum - target) / 2;
            int[] frontRow = new int[reqTargetSum+1];
            frontRow[0] = 1;

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[reqTargetSum+1];
                for(int targetSum = 0; targetSum <= reqTargetSum; targetSum++) {
                    // Include current index element if element is withing the targetSum value
                    int newTargetSum = targetSum - nums[idx], take = 0;
                    if(newTargetSum >= 0) {
                        take = frontRow[newTargetSum];
                    }

                    // Don't include current index element
                    int notTake = frontRow[targetSum];
                    int totalWays = take + notTake;

                    currRow[targetSum] = totalWays;
                }
                frontRow = currRow;
            }

            return frontRow[reqTargetSum];
        }

        public int findTargetSumWays4(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length, totalSum = 0;

            for(int n: nums) {
                totalSum += n;
            }

            if((totalSum - target) < 0 || (totalSum - target) % 2 != 0) {
                return 0;
            }
            int reqTargetSum = (totalSum - target) / 2;
            int[][] cache = new int[size+1][reqTargetSum+1];
            cache[size][0] = 1;


            for(int idx = size-1; idx >= 0; idx--) {
                for(int targetSum = 0; targetSum <= reqTargetSum; targetSum++) {
                    // Include current index element if element is withing the targetSum value
                    int newTargetSum = targetSum - nums[idx], take = 0;
                    if(newTargetSum >= 0) {
                        take = cache[idx+1][newTargetSum];
                    }

                    // Don't include current index element
                    int notTake = cache[idx+1][targetSum];
                    int totalWays = take + notTake;

                    cache[idx][targetSum] = totalWays;
                }
            }

            return cache[0][reqTargetSum];
        }

        private int helper3(int idx, int targetSum, int[] nums, int[][] cache) {
            if(idx >= nums.length) {
                if(targetSum == 0) {
                    return 1;
                } else {
                    return 0;
                }
            } else if(cache[idx][targetSum] != -1) {
                return cache[idx][targetSum];
            }

            // Include current index element if element is withing the targetSum value
            int newTargetSum = targetSum - nums[idx], take = 0;
            if(newTargetSum >= 0) {
                take = helper3(idx+1, newTargetSum, nums, cache);
            }

            // Don't include current index element
            int notTake = helper3(idx+1, targetSum, nums, cache);
            int totalWays = take + notTake;

            return cache[idx][targetSum] = totalWays;
        }

        public int findTargetSumWays3(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length, totalSum = 0;

            for(int n: nums) {
                totalSum += n;
            }

            if((totalSum - target) < 0 || (totalSum - target) % 2 != 0) {
                return 0;
            }
            int reqTargetSum = (totalSum - target) / 2;
            int[][] cache = new int[size][reqTargetSum+1];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper3(0, reqTargetSum, nums, cache);
        }

        private int helper2(int idx, int targetSum, int[] nums) {
            if(idx >= nums.length) {
                if(targetSum == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }

            // Include current index element if element is withing the targetSum value
            int newTargetSum = targetSum - nums[idx], take = 0;
            if(newTargetSum >= 0) {
                take = helper2(idx+1, newTargetSum, nums);
            }

            // Don't include current index element
            int notTake = helper2(idx+1, targetSum, nums);
            int totalWays = take + notTake;
            return totalWays;
        }

        public int findTargetSumWays2(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length, totalSum = 0;

            for(int n: nums) {
                totalSum += n;
            }

            if((totalSum - target) < 0 || (totalSum - target) % 2 != 0) {
                return 0;
            }
            int reqTargetSum = (totalSum - target) / 2;

            return helper2(0, reqTargetSum, nums);
        }

        private int helper1(int idx, int currSum, int targetSum, int[] nums, Map<String, Integer> cache) {
            if(idx >= nums.length) {
                if(currSum == targetSum) {
                    return 1;
                } else {
                    return 0;
                }
            }
            String key = idx +"-" + currSum;
            if(cache.containsKey(key)) {
                return cache.get(key);
            }

            // Include + symbol
            int plusSign = helper1(idx+1, currSum+nums[idx], targetSum, nums, cache);
            // Include - symbol
            int negativeSign = helper1(idx+1,currSum-nums[idx], targetSum, nums, cache);

            // Total ways
            int totalWays = plusSign + negativeSign;
            cache.put(key, totalWays);
            return totalWays;
        }

        public int findTargetSumWays1(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            Map<String, Integer> cache = new HashMap<>();

            return helper1(0, 0, target, nums, cache);
        }

        private int helper0(int idx, int currSum, int targetSum, int[] nums) {
            if(idx >= nums.length) {
                if(currSum == targetSum) {
                    return 1;
                } else {
                    return 0;
                }
            }

            // Include + symbol
            int plusSign = helper0(idx+1, currSum+nums[idx], targetSum, nums);
            // Include - symbol
            int negativeSign = helper0(idx+1,currSum-nums[idx], targetSum, nums);

            // Total ways
            int totalWays = plusSign + negativeSign;
            return totalWays;
        }

        public int findTargetSumWays0(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;

            return helper0(0, 0, target, nums);
        }
    }
}
