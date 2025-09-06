package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/n-th-tribonacci-number/description/

- Using Recursion
- Using Recursion with Memoization
- Using Dynamic Programming
- Space optimized Dynamic Programming
 */

public class N_th_Tribonacci_Number_LC_1137 {
    class Solution {
        public int tribonacci3(int n) {
            if(n < 3) {
                return n > 0 ? 1 : 0;
            }

            int prev0 = 0, prev1 = 1, prev2 = 1;

            for(int i = 3; i <= n; i++) {
                int tmp = prev0 + prev1 + prev2;
                prev0 = prev1;
                prev1 = prev2;
                prev2 = tmp;
            }

            return prev2;
        }

        public int tribonacci2(int n) {
            if(n < 3) {
                return n > 0 ? 1 : 0;
            }

            int[] cache = new int[n+1];
            cache[0] = 0;
            cache[1] = cache[2] = 1;

            for(int i = 3; i <= n; i++) {
                cache[i] = cache[i-1] + cache[i-2] + cache[i-3];
            }

            return cache[n];
        }

        private int helper1(int n, int[] cache) {
            if(n < 3) {
                return n > 0 ? 1 : 0;
            } else if(cache[n] != -1) {
                return cache[n];
            }

            return cache[n] = helper1(n-1, cache) + helper1(n-2, cache) + helper1(n-3, cache);
        }

        public int tribonacci1(int n) {
            int[] cache = new int[n+1];
            Arrays.fill(cache, -1);

            return helper1(n, cache);
        }

        private int helper0(int n) {
            if(n < 3) {
                return n > 0 ? 1 : 0;
            }

            return helper0(n-1) + helper0(n-2) + helper0(n-3);
        }

        public int tribonacci0(int n) {
            return helper0(n);
        }
    }
}
