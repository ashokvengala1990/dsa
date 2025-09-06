package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/longest-palindromic-subsequence/description/

Two Core Approaches for Longest Palindromic Subsequence (LPS)

Core Approach 1: Two-Pointer DP on the same string (s[left....right])
This approach works directly on the original string, considering substrings from index left to right.

✅ Key Idea:
- Check if s.charAt(left) == s.charAt(right):
- If yes → include both characters: 2 + LPS(left + 1, right - 1)
- If no → skip one end: max(LPS(left + 1, right), LPS(left, right - 1))

🧠 Used in:
- longestPalindromeSubseq0 – Plain recursion
- longestPalindromeSubseq1 – Recursion + 2D memoization
- longestPalindromeSubseq2 – Bottom-up DP with 2D table
- longestPalindromeSubseq3 – Bottom-up DP with 1D optimization

Core Approach 2: LCS Between s and reverse(s)
This transforms the LPS problem into a Longest Common Subsequence (LCS) problem between the string and its reverse.

✅ Key Idea:
- The LPS of a string is the LCS of the string and its reverse.
- LCS(i, j) = 1 + LCS(i+1, j+1) if s[i] == t[j]
- Else: max(LCS(i+1, j), LCS(i, j+1))

🧠 Used in:
- longestPalindromeSubseq4 – Recursion + 2D memoization
- longestPalindromeSubseq5 – Bottom-up DP with 2D table
- longestPalindromeSubseq6 – Bottom-up DP with 1D space optimization


- TC: O(n^2)
- SC: O(n)
 */

public class Longest_Palindromic_Subsequence_LC_516 {
    class Solution {
        public int longestPalindromeSubseq6(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[] frontRow = new int[size+1];
            String t = new StringBuilder(s).reverse().toString();

            for(int idx1 = size-1; idx1 >= 0; idx1--) {
                int[] currRow = new int[size+1];
                for(int idx2 = size-1; idx2 >= 0; idx2--) {
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

        public int longestPalindromeSubseq5(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[][] cache = new int[size+1][size+1];
            String t = new StringBuilder(s).reverse().toString();

            for(int idx1 = size-1; idx1 >= 0; idx1--) {
                for(int idx2 = size-1; idx2 >= 0; idx2--) {
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

        private int helper2(int idx1, int idx2, String s, String t, int[][] cache) {
            if(idx1 >= s.length() || idx2 >= t.length()) {
                return 0;
            } else if(cache[idx1][idx2] != -1) {
                return cache[idx1][idx2];
            }

            int result = 0;
            if(s.charAt(idx1) == t.charAt(idx2)) {
                result = 1 + helper2(idx1+1, idx2+1, s, t, cache);
            } else {
                result = Math.max(helper2(idx1+1, idx2, s, t, cache), helper2(idx1, idx2+1, s, t, cache));
            }

            return cache[idx1][idx2] = result;
        }

        public int longestPalindromeSubseq4(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            String t = new StringBuilder(s).reverse().toString();
            int[][] cache = new int[size][size];

            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper2(0, 0, s, t, cache);
        }

        public int longestPalindromeSubseq3(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[] frontRow = new int[size];

            for(int left = size-1; left >= 0; left--) {
                int[] currRow = new int[size];
                currRow[left] = 1;
                for(int right = left+1; right < size; right++) {
                    int result = 0;

                    if(s.charAt(left) == s.charAt(right)) {
                        result = 2 + frontRow[right-1];
                    } else {
                        result = Math.max(frontRow[right], currRow[right-1]);
                    }

                    currRow[right] = result;
                }
                frontRow = currRow;
            }

            return frontRow[size-1];
        }

        public int longestPalindromeSubseq2(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[][] cache = new int[size+1][size];

            for(int i = 0; i < size; i++) {
                cache[i][i] = 1;
            }

            for(int left = size-1; left >= 0; left--) {
                for(int right = left+1; right < size; right++) {
                    int result = 0;

                    if(s.charAt(left) == s.charAt(right)) {
                        result = 2 + cache[left+1][right-1];
                    } else {
                        result = Math.max(cache[left+1][right], cache[left][right-1]);
                    }

                    cache[left][right] = result;
                }
            }

            return cache[0][size-1];
        }

        private int helper1(int left, int right, String s, int[][] cache) {
            if(left > right) {
                return 0;
            } else if(left == right) {
                return 1;
            } else if(cache[left][right] != -1) {
                return cache[left][right];
            }

            int result = 0;

            if(s.charAt(left) == s.charAt(right)) {
                result = 2 + helper1(left+1, right-1, s, cache);
            } else {
                result = Math.max(helper1(left+1, right, s, cache), helper1(left, right-1, s, cache));
            }

            return cache[left][right] = result;
        }

        public int longestPalindromeSubseq1(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[][] cache = new int[size][size];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, size-1, s, cache);
        }

        private int helper(int left, int right, String s) {
            if(left > right) {
                return 0;
            } else if(left == right) {
                return 1;
            }

            int result = 0;
            if(s.charAt(left) == s.charAt(right)) {
                result = 2 + helper(left+1, right-1, s);
            } else {
                int excludeLeft = helper(left+1, right, s);
                int excludeRight = helper(left, right-1, s);
                result = Math.max(excludeLeft, excludeRight);
            }

            return result;
        }

        public int longestPalindromeSubseq0(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            return helper(0, size-1, s);
        }
    }
}

