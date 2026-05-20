package neetcode.greedy;

/*
https://leetcode.com/problems/jump-game/description/

Jump Game — revision notes
Given nums[], each value = max steps you can jump forward. Can you reach the last index?
Approach 0
Recursion + Memoization (Top-Down DP)
Start from index 0, try every jump length. Cache results so you don't redo work.
If idx ≥ n-1 → reached end, return 1 (success)
Check cache → return if already computed
Try all jumps i = 1 to nums[idx]. If any recursive call returns 1 → cache & return 1
If no jump works → cache & return 0
Cache uses -1 = unvisited, 0 = can't reach, 1 = can reach. Don't use boolean — you need a "not computed yet" state.
Time O(n²)
Space O(n)
Slowest of the three
Approach 1
Bottom-Up DP (Iterative)
Work backwards. Mark each index as reachable if any jump from it lands on a known-reachable index.
Start with dp[n-1] = true (last index is always reachable from itself)
Loop idx from n-2 down to 0
For each jump i = 1 to nums[idx]: if idx+i ≥ n-1 OR dp[idx+i] == true → set dp[idx] = true and break
Return dp[0]
The check idx+i ≥ n-1 covers the case where a jump overshoots the end — that's still valid!
Time O(n²)
Space O(n)
No recursion overhead
Approach 2
Greedy — Optimal ✓
Track the farthest index reachable so far. If you ever reach an index beyond that max, you're stuck.
Keep maxIndex = 0 (farthest reachable index)
For each index i: if i > maxIndex → return false (can't even get here)
Update maxIndex = max(maxIndex, i + nums[i])
If loop finishes → return true
Key insight: you don't care how you get to index i. You only care about the farthest you can reach from any index you've visited so far.
Time O(n)
Space O(1)
Best approach
Quick comparison
Approach	Time	Space	Direction
0 — Memo recursion	O(n²)	O(n)	Forward, recursive
1 — Bottom-up DP	O(n²)	O(n)	Backward, iterative
2 — Greedy	O(n)	O(1)	Forward, no extra space


Common gotchas to remember:

The edge case nums == null || length == 0 returns true — an empty/null array trivially reaches the "last index" (nothing to jump over).
In approach 1, the condition idx+i >= n-1 matters — overshooting the end is still a win, not out-of-bounds.
In approach 2, the check i > maxIndex (not >=) is the trap door. If you're at index i and it equals maxIndex, you're still standing on reachable ground.
Approach 0 uses int[] cache (not boolean[]) precisely because you need three states: unvisited, reachable, stuck.

The greedy is the one to internalize — once you see that maxIndex is just a running "horizon" that expands as you walk forward, the logic becomes obvious.
 */

import java.util.Arrays;

public class Jump_Game_LC_55 {
    class Revision01 {
        public boolean canJump2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return true;
            }

            int n = nums.length, maxIndex = 0;

            for(int i = 0; i < n; i++) {
                if(i > maxIndex) {
                    return false;
                }

                maxIndex = Math.max(maxIndex, i + nums[i]);
            }

            return true;
        }

        public boolean canJump1(int[] nums) {
            if(nums == null || nums.length == 0) {
                return true;
            }

            int n = nums.length;
            boolean[] dp = new boolean[n];
            dp[n-1] = true;

            for(int idx = n-2; idx >= 0; idx--) {
                int numJumps = nums[idx];
                for(int i = 1; i <= numJumps; i++) {
                    if((idx+i) > (n-1) || dp[idx+i]) {
                        dp[idx] = true;
                        break;
                    }
                }
            }

            return dp[0];
        }

        private int helper0(int idx, int[] nums, int[] cache) {
            if(idx >= nums.length-1) {
                return 1;
            } else if(cache[idx] != -1) {
                return cache[idx];
            }

            int numJumps = nums[idx];

            for(int i = 1; i <= numJumps; i++) {
                if(helper0(idx+i, nums, cache) == 1) {
                    return cache[idx] = 1;
                }
            }

            return cache[idx] = 0;
        }

        public boolean canJump0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return true;
            }

            int n = nums.length;
            int[] cache = new int[n];
            Arrays.fill(cache, -1);

            return helper0(0, nums, cache) == 1;
        }
    }

    class Solution {
        public boolean canJump2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return true;
            }

            int n = nums.length, maxIndex = 0;

            for(int i = 0; i < n; i++) {
                if(i > maxIndex) {
                    return false;
                }

                maxIndex = Math.max(maxIndex, i + nums[i]);
            }

            return true;
        }

        public boolean canJump1(int[] nums) {
            if(nums == null || nums.length == 0) {
                return true;
            }

            int n = nums.length;
            boolean[] dp = new boolean[n];
            dp[n-1] = true;

            for(int idx = n-1; idx >= 0; idx--) {
                int numJumps = nums[idx];
                for(int i = 1; i <= numJumps; i++) {
                    if(idx + i > (n-1) || dp[idx+i]) {
                        dp[idx] = true;
                        break;
                     }
                }
            }

            return dp[0];
        }

        private int helper0(int idx, int[] nums, int[] cache) {
            if(idx >= nums.length-1) {
                return 1;
            } else if(cache[idx] != -1) {
                return cache[idx];
            }

            int numJumps = nums[idx];

            for(int i = 1; i <= numJumps; i++) {
                if(helper0(idx+i, nums, cache) == 1) {
                    return cache[idx] = 1;
                }
            }

            return cache[idx] = 0;
        }

        public boolean canJump0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return true;
            }

            int n = nums.length;
            int[] cache = new int[n];
            Arrays.fill(cache, -1);

            return helper0(0, nums, cache) == 1;
        }
    }
}
