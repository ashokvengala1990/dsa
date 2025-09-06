package neetcode.dynamic_programming;

import java.util.Arrays;

public class House_Robber_LC_198 {
    class Solution {
        public int rob3(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int front1 = 0, front2 = 0;

            for(int idx = size-1; idx >= 0; idx--) {
                // Rob a house
                int pick = nums[idx] + front2;

                // Don't rob current Index house
                int notPick = 0 + front1;
                front2 = front1;
                front1 = Math.max(pick, notPick);
            }

            return front1;
        }

        public int rob2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length;
            int[] cache = new int[size+2];

            for(int idx = size-1; idx >= 0; idx--) {
                // Rob a house
                int pick = nums[idx] + cache[idx+2];

                // Don't rob current Index house
                int notPick = 0 + cache[idx+1];
                cache[idx] = Math.max(pick, notPick);
            }

            return cache[0];
        }

        private int helper1(int idx, int[] nums, int[] cache) {
            if(idx >= nums.length) {
                return 0;
            } else if(cache[idx] != -1) {
                return cache[idx];
            }

            // Rob a house
            int pick = nums[idx] + helper1(idx+2, nums, cache);

            // Don't rob current Index house
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

            return helper1(0, nums, cache);
        }

        private int helper0(int idx, int[] nums) {
            if(idx >= nums.length) {
                return 0;
            }

            // rob a house
            int pick = nums[idx] + helper0(idx+2, nums);

            // don't rob a house
            int notPick = 0 + helper0(idx+1, nums);

            return Math.max(pick, notPick);
        }

        public int rob0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            return helper0(0, nums);
        }
    }
}
