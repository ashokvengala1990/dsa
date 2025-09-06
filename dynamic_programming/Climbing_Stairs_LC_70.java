package neetcode.dynamic_programming;

import java.util.Arrays;

public class Climbing_Stairs_LC_70 {
    class Solution {
        public int climbStairs3(int n) {
            if(n == 1 || n == 0) {
                return 1;
            } else if(n < 0) {
                return 0;
            }

            int prev1 = 1, prev2 = 1;

            for(int i = 2; i <=n; i++) {
                int curr = prev1 + prev2;
                prev1 = prev2;
                prev2 = curr;
            }

            return prev2;
        }

        public int climbStairs2(int n) {
            if(n == 1 || n == 0) {
                return 1;
            } else if(n < 0) {
                return 0;
            }

            int[] cache = new int[n+1];
            cache[0] = 1;
            cache[1] = 1;

            for(int i = 2; i <= n; i++) {
                int jumpOne = cache[i-1];
                int jumpTwo = cache[i-2];
                cache[i] = jumpOne + jumpTwo;
            }

            return cache[n];
        }

        private int helper1(int n, int[] cache) {
            if(n <= 1) {
                return 1;
            } else if(cache[n] != -1) {
                return cache[n];
            }

            int jumpOne = helper1(n-1, cache);
            int jumpTwo = helper1(n-2, cache);
            return cache[n] = jumpOne + jumpTwo;
        }

        public int climbStairs1(int n) {
            if(n == 1 || n == 0) {
                return 1;
            } else if(n < 0) {
                return 0;
            }

            int[] cache = new int[n+1];
            Arrays.fill(cache, -1);

            return helper1(n, cache);
        }

        private int helper0(int n) {
            if(n <= 1) {
                return 1;
            }

            int jumpOne = helper0(n-1);
            int jumpTwo = helper0(n-2);
            return jumpOne + jumpTwo;
        }

        public int climbStairs0(int n) {
            return helper0(n);
        }
    }
}
