package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/house-robber-ii/description/


 */

public class House_Robber_II_LC_213 {
    class Solution {
        private int helper3(int[] nums) {
            int size = nums.length;
            int front1 = 0, front2 = 0;

            for(int idx = size-1; idx >= 0; idx--) {
                // Rob a house
                int pick = nums[idx] + front2;

                // Don't rob a house
                int notPick = 0 + front1;
                front2 = front1;
                front1 = Math.max(pick, notPick);
            }

            return front1;
        }

        public int rob3(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            } else if(nums.length == 1) {
                return nums[0];
            }

            int size = nums.length;
            int[] temp1 = new int[size-1], temp2 = new int[size-1];

            for(int i = 0; i < size; i++) {
                if(i != 0) {
                    temp2[i-1] = nums[i];
                }
                if(i != size-1) {
                    temp1[i] = nums[i];
                }
            }

            return Math.max(helper3(temp1), helper3(temp2));
        }

        private int helper2(int[] nums) {
            int size = nums.length;
            int[] cache = new int[size+2];

            for(int idx = size-1; idx >= 0; idx--) {
                // Rob a house
                int pick = nums[idx] + cache[idx+2];

                // Don't rob a house
                int notPick = 0 + cache[idx+1];

                cache[idx] = Math.max(pick, notPick);
            }

            return cache[0];
        }

        public int rob2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            } else if(nums.length == 1) {
                return nums[0];
            }

            int size = nums.length;
            int[] temp1 = new int[size-1], temp2 = new int[size-1];

            for(int i = 0; i < size; i++) {
                if(i != 0) {
                    temp2[i-1] = nums[i];
                }
                if(i != size-1) {
                    temp1[i] = nums[i];
                }
            }

            return Math.max(helper2(temp1), helper2(temp2));
        }

        private int helper1(int idx, int[] nums, int[] cache) {
            if(idx >= nums.length) {
                return 0;
            } else if(cache[idx] != -1) {
                return cache[idx];
            }

            // Rob a house
            int pick = nums[idx] + helper1(idx+2, nums, cache);

            // Don't rob a house
            int notPick = 0 + helper1(idx+1, nums, cache);

            return cache[idx] = Math.max(pick, notPick);
        }

        public int rob1(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int[] cache = new int[size];
            Arrays.fill(cache, -1);
            int[] temp1 = new int[size-1], temp2 = new int[size-1];

            for(int i = 0; i < size; i++) {
                if(i != 0) {
                    temp2[i-1] = nums[i];
                }

                if(i != size-1) {
                    temp1[i] = nums[i];
                }
            }

            int first = helper1(0, temp1, cache);
            Arrays.fill(cache, -1);
            int second = helper1(0, temp2, cache);

            return Math.max(first, second);
        }

        private int helper0(int idx, int[] nums) {
            if(idx >= nums.length) {
                return 0;
            }

            // Rob a house
            int pick = nums[idx] + helper0(idx+2, nums);

            // Don't rob a house
            int notPick = 0 + helper0(idx+1, nums);

            return Math.max(pick, notPick);
        }

        public int rob0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int[] temp1 = new int[size-1], temp2 = new int[size-1];

            for(int i = 0; i < size; i++) {
                if(i != 0) {
                    temp2[i] = nums[i];
                }
                if(i != size-1) {
                    temp1[i] = nums[i];
                }
            }

            return Math.max(helper0(0, temp1), helper0(0, temp2));
        }
    }
}
