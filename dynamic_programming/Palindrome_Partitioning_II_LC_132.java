package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/palindrome-partitioning-ii/description/

- Using Front Partition Approach (With For-Loop based recursion)
- Recursion
    * TC: O(exponential)
    * SC: O(n)
- Recursion with Memoization
    * TC: O(n*k)
    * SC: O(n) + O(n)
- True Dynamic Programming
    * TC: O(n*k)
    * SC: O(n)

 */

public class Palindrome_Partitioning_II_LC_132 {
    class Solution {
        private boolean isPalindrome3(int left, int right, String s, Boolean[][] isPalindromeCache) {
            if(isPalindromeCache[left][right] != null) {
                return isPalindromeCache[left][right];
            }

            while (left < right) {
                if(s.charAt(left) != s.charAt(right)) {
                    return isPalindromeCache[left][right] = false;
                }

                left++;
                right--;
            }

            return isPalindromeCache[left][right] = true;
        }

        public int minCut3(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[] cache = new int[size+1];
            cache[size] = 0;
            Boolean[][] isPalindromeCache = new Boolean[size+1][size+1];

            for(int idx = size-1; idx >= 0; idx--) {
                int minCut = Integer.MAX_VALUE;

                for(int i = idx; i < s.length(); i++) {
                    if(isPalindrome3(idx, i, s, isPalindromeCache)) {
                        minCut = Math.min(minCut, 1 + cache[i+1]);
                    }
                }

                cache[idx] = minCut;
            }

            return cache[0] - 1;
        }

        private boolean isPalindrome2(int left, int right, String s) {
            while (left < right) {
                if(s.charAt(left) != s.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }

        public int minCut2(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[] cache = new int[size+1];
            cache[size] = 0;

            for(int idx = size-1; idx >= 0; idx--) {
                int minCut = Integer.MAX_VALUE;

                for(int i = idx; i < s.length(); i++) {
                    if(isPalindrome2(idx, i, s)) {
                        minCut = Math.min(minCut, 1 + cache[i+1]);
                    }
                }

                cache[idx] = minCut;
            }

            return cache[0] - 1;
        }

        private boolean isPalindrome1(int left, int right, String s) {
            while (left < right) {
                if(s.charAt(left) != s.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }

        private int helper1(int idx, String s, int[] cache) {
            if(idx >= s.length()) {
                return 0;
            } else if(cache[idx] != -1) {
                return cache[idx];
            }

            int minCut = Integer.MAX_VALUE;

            for(int i = idx; i < s.length(); i++) {
                if(isPalindrome1(idx, i, s)) {
                    minCut = Math.min(minCut, 1 + helper1(i+1, s, cache));
                }
            }

            return cache[idx] = minCut;
        }

        public int minCut1(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[] cache = new int[size];
            Arrays.fill(cache, -1);
            return helper1(0, s, cache) - 1;
        }

        private boolean isPalindrome0(int left, int right, String s) {
            while (left < right) {
                if(s.charAt(left) != s.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }

        private int helper0(int idx, String s) {
            if(idx >= s.length()) {
                return 0;
            }

            int minCut = Integer.MAX_VALUE;

            for(int i = idx; i < s.length(); i++) {
                if(isPalindrome0(idx, i, s)) {
                    minCut = Math.min(minCut, 1 + helper0(i+1, s));
                }
            }

            return minCut;
        }

        public int minCut0(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            return helper0(0, s) - 1;
        }
    }
}
