package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/distinct-subsequences/

Tried:
- Brute Recursion → Memoization → Tabulation → Space Optimized

Final Used:
- Bottom-up DP
- 1D space optimization (prev row only)
- Iterate from end to start of both strings
- Used relation:
  if match → dp[i][j] = dp[i+1][j+1] + dp[i+1][j]
  else → dp[i][j] = dp[i+1][j]

TC: O(n × m)
SC: O(m)

 */

public class Distinct_Subsequences_LC_115 {
    class Solution {
        public int numDistinct3(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();
            int[] frontRow = new int[size2+1];
            frontRow[size2] = 1;

            for(int idx1 = size1-1; idx1 >= 0; idx1--) {
                int[] currRow = new int[size2+1];
                currRow[size2] = 1;
                for(int idx2 = size2-1; idx2 >= 0; idx2--) {
                    int count = 0;

                    if(s.charAt(idx1) == t.charAt(idx2)) {
                        count = frontRow[idx2+1]
                                + frontRow[idx2];
                    } else {
                        count = frontRow[idx2];
                    }

                    currRow[idx2] = count;
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int numDistinct2(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();
            int[][] cache = new int[size1+1][size2+1];
            for(int idx1 = 0; idx1 <= size1; idx1++) {
                cache[idx1][size2] = 1;
            }

            for(int idx1 = size1-1; idx1 >= 0; idx1--) {
                for(int idx2 = size2-1; idx2 >= 0; idx2--) {
                    int count = 0;

                    if(s.charAt(idx1) == t.charAt(idx2)) {
                        count = cache[idx1+1][idx2+1]
                                + cache[idx1+1][idx2];
                    } else {
                        count = cache[idx1+1][idx2];
                    }

                    cache[idx1][idx2] = count;
                }
            }

            return cache[0][0];
        }

        private int helper1(int idx1, int idx2, String s, String t, int size1, int size2, int[][] cache) {
            if(idx2 == size2) {
                return 1;
            } else if(idx1 == size1) {
                return 0;
            } else if(cache[idx1][idx2] != -1) {
                return cache[idx1][idx2];
            }

            int count = 0;

            if(s.charAt(idx1) == t.charAt(idx2)) {
                count = helper1(idx1+1, idx2+1, s, t, size1, size2, cache)
                        + helper1(idx1+1, idx2, s, t, size1, size2, cache);
            } else {
                count = helper1(idx1+1, idx2, s, t, size1, size2, cache);
            }

            return cache[idx1][idx2] = count;
        }

        public int numDistinct1(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();
            int[][] cache = new int[size1][size2];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, 0, s, t, size1, size2, cache);
        }

        private int helper0(int idx1, int idx2, String s, String t, int size1, int size2) {
            if(idx2 == size2) {
                return 1;
            } else if(idx1 == size1) {
                return 0;
            }

            int count = 0;

            if(s.charAt(idx1) == t.charAt(idx2)) {
                count = helper0(idx1+1, idx2+1, s, t, size1, size2)
                        + helper0(idx1+1, idx2, s, t, size1, size2);
            } else {
                count = helper0(idx1+1, idx2, s, t, size1, size2);
            }

            return count;
        }

        public int numDistinct0(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();

            return helper0(0, 0, s, t, size1, size2);
        }
    }
}
