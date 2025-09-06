package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/longest-common-subsequence/

- Recursion (no memo)	Exponential	O(1)	Brute force, not practical
- Top-down DP (with memo)	O(m*n)	O(m*n)	Uses recursion + memoization
- Bottom-up DP (2D array)	O(m*n)	O(m*n)	Standard tabulation
- Bottom-up DP (2 rows) ✅	O(m*n)	O(n)	Your approach — optimal & elegant
 */

public class Longest_Common_Subsequence_LC_1143 {
    class Revision01 {
        public int longestCommonSubsequence3(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();
            int[] frontRow = new int[size2+1];

            for(int idx1 = size1-1; idx1 >= 0; idx1--) {
                int[] currRow = new int[size2+1];
                for(int idx2 = size2-1; idx2 >= 0; idx2--) {
                    int longLen = 0;

                    if(s.charAt(idx1) == t.charAt(idx2)) {
                        longLen = 1 + frontRow[idx2+1];
                    } else {
                        longLen = Math.max(frontRow[idx2],
                                currRow[idx2+1]);
                    }

                    currRow[idx2] = longLen;
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int longestCommonSubsequence2(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();
            int[][] cache = new int[size1+1][size2+1];

            for(int idx1 = size1-1; idx1 >= 0; idx1--) {
                for(int idx2 = size2-1; idx2 >= 0; idx2--) {
                    int longLen = 0;

                    if(s.charAt(idx1) == t.charAt(idx2)) {
                        longLen = 1 + cache[idx1+1][idx2+1];
                    } else {
                        longLen = Math.max(cache[idx1+1][idx2],
                                cache[idx1][idx2+1]);
                    }

                    cache[idx1][idx2] = longLen;
                }
            }

            return cache[0][0];
        }

        private int helper1(int idx1, int idx2, String s, String t, int size1, int size2, int[][] cache) {
            if(idx1 >= size1 || idx2 >= size2) {
                return 0;
            } else if(cache[idx1][idx2] != -1) {
                return cache[idx1][idx2];
            }

            int longLen = 0;

            if(s.charAt(idx1) == t.charAt(idx2)) {
                longLen = 1 + helper1(idx1+1, idx2+1, s, t, size1, size2, cache);
            } else {
                longLen = Math.max(helper1(idx1+1, idx2, s, t, size1, size2, cache),
                        helper1(idx1, idx2+1, s, t, size1, size2, cache));
            }

            return cache[idx1][idx2] = longLen;
        }

        public int longestCommonSubsequence1(String s, String t) {
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
            if(idx1 >= size1 || idx2 >= size2) {
                return 0;
            }
            int longLen = 0;

            if(s.charAt(idx1) == t.charAt(idx2)) {
                longLen = 1 + helper0(idx1+1, idx2, s, t, size1, size2);
            } else {
                longLen = Math.max(helper0(idx1+1, idx2, s, t, size1, size2), helper0(idx1, idx2+1, s, t, size1, size2));
            }

            return longLen;
        }

        public int longestCommonSubsequence0(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();
            return helper0(0, 0, s, t, size1, size2);
        }
    }

    class Solution {
        public int longestCommonSubsequence3(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();
            int[] frontRow = new int[size2+1];

            for(int idx1 = size1-1; idx1 >= 0; idx1--) {
                int[] currRow = new int[size2+1];
                for(int idx2 = size2-1; idx2 >= 0; idx2--) {
                    int result = 0;
                    if(s.charAt(idx1) == t.charAt(idx2)) {
                        result = 1 + frontRow[idx2+1];
                    } else {
                        result = Math.max(frontRow[idx2], currRow[idx2+1]);
                    }

                    currRow[idx2] = result;
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int longestCommonSubsequence2(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();
            int[][] cache = new int[size1+1][size2+1];

            for(int idx1 = size1-1; idx1 >= 0; idx1--) {
                for(int idx2 = size2-1; idx2 >= 0; idx2--) {
                    int result = 0;
                    if(s.charAt(idx1) == t.charAt(idx2)) {
                        result = 1 + cache[idx1+1][idx2+1];
                    } else {
                        result = Math.max(cache[idx1+1][idx2], cache[idx1][idx2+1]);
                    }

                    cache[idx1][idx2] = result;
                }
            }

            return cache[0][0];
        }

        private int helper1(int idx1, int idx2, String s, String t, int[][] cache) {
            if(idx1 >= s.length() || idx2 >= t.length()) {
                return 0;
            } else if(cache[idx1][idx2] != -1) {
                return cache[idx1][idx2];
            }

            int result = 0;
            if(s.charAt(idx1) == t.charAt(idx2)) {
                result = 1 + helper1(idx1+1, idx2+1, s, t, cache);
            } else {
                result = Math.max(helper1(idx1+1, idx2, s, t, cache), helper1(idx1, idx2+1, s, t, cache));
            }

            return cache[idx1][idx2] = result;
        }

        public int longestCommonSubsequence1(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            int size1 = s.length(), size2 = t.length();
            int[][] cache = new int[size1][size2];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, 0, s, t, cache);
        }

        private int helper0(int idx1, int idx2, String s, String t) {
            if(idx1 >= s.length() || idx2 >= t.length()) {
                return 0;
            }

            if(s.charAt(idx1) == t.charAt(idx2)) {
                return 1 + helper0(idx1+1, idx2+1, s, t);
            } else {
                return Math.max(helper0(idx1+1, idx2, s, t), helper0(idx1, idx2+1, s, t));
            }
        }

        public int longestCommonSubsequence0(String s, String t) {
            if(s == null || s.isEmpty() || t == null || t.isEmpty()) {
                return 0;
            }

            return helper0(0, 0, s, t);
        }
    }
}
