package neetcode.greedy;

/*
Jump Game II — Revision Notes
Core Goal
Find minimum jumps to reach last index.

4 Approaches
1. Memoization — jump0 (Top-down DP)
From each index, try all jumps, cache minimum
TC: O(n²)  SC: O(n)
2. DP — jump1 (Bottom-up)
dp[n-1] = 0, fill backwards
dp[idx] = min(1 + dp[idx+i]) for i in 1..nums[idx]
TC: O(n²)  SC: O(n)
3. BFS — jump2
l, r = window of current jump level
farthest = max reach within window
l = r+1, r = farthest, jumps++
TC: O(n)  SC: O(1)
4. Greedy — jump3 ⭐ Best
Track farthest reachable at each step
When i == currentEnd → must jump, currentEnd = farthest
TC: O(n)  SC: O(1)

Key Greedy Trick
javafor(int i = 0; i < n-1; i++) {        // stop at n-2!
    farthest = Math.max(farthest, i + nums[i]);
    if(i == currentEnd) {
        currentEnd = farthest;
        jumps++;
        if(currentEnd >= n-1) break;   // early exit
    }
}
Loop stops at n-2 — no need to jump FROM last index!

Interview Approach

Start with Memoization → optimize to Greedy
 */

import java.util.Arrays;

public class Jump_Game_II_LC_45 {
    class Solution {
        public int jump3(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int n = nums.length, currentEnd = 0, farthest = 0, jumps = 0;

            for(int i = 0; i < n-1; i++) {
                farthest = Math.max(farthest, i + nums[i]);

                if(i == currentEnd) {
                    currentEnd = farthest;
                    jumps++;

                    if(currentEnd >= n-1) {
                        break;
                    }
                }
            }

            return jumps;
        }

        public int jump2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int n = nums.length, l = 0, r = 0, jumps = 0;

            while (r < n-1) {
                int farthest = 0;
                for(int i = l; i <= r; i++) {
                    farthest = Math.max(farthest, i + nums[i]);
                }
                l = r+1;
                r = farthest;
                jumps++;
            }

            return jumps;
        }

        public int jump1(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int n = nums.length;
            int[] dp = new int[n];
            Arrays.fill(dp, -1);
            dp[n-1] = 0;

            for(int idx = n-2; idx >= 0; idx--) {
                int minJumps = Integer.MAX_VALUE, numJumps = nums[idx];

                for(int i = 1; i <= numJumps; i++) {
                    if(idx + i < n) {
                        int sub = dp[idx + i];
                        if (sub != Integer.MAX_VALUE) {
                            minJumps = Math.min(minJumps, 1 + sub);
                        }
                    }
                }
                dp[idx] = minJumps;
            }

            return dp[0] == Integer.MAX_VALUE ? 0 : dp[0];
        }

        private int helper0(int idx, int[] nums, int[] cache) {
            if(idx >= nums.length-1) {
                return 0;
            } else if(cache[idx] != -1) {
                return cache[idx];
            }

            int minJumps = Integer.MAX_VALUE, numJumps = nums[idx];

            for(int i = 1; i <= numJumps; i++) {
                int sub = helper0(idx+i, nums, cache);
                if(sub != Integer.MAX_VALUE) {
                    minJumps = Math.min(minJumps, 1 + sub);
                }
            }

            return cache[idx] = minJumps;
        }

        public int jump0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int n = nums.length;
            int[] cache = new int[n];
            Arrays.fill(cache, -1);

            int minJumps = helper0(0, nums, cache);
            return minJumps == Integer.MAX_VALUE ? 0 : minJumps;
        }
    }
}
