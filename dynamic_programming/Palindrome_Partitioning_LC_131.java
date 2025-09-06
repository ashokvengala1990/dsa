package neetcode.dynamic_programming;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.com/problems/palindrome-partitioning/description/

- Using Front Partition Approach (With For-Loop based recursion)
 */

public class Palindrome_Partitioning_LC_131 {
    class Solution {
        private boolean isPalindrome1(int left, int right, String s, Boolean[][] isPalindromeCache) {
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

        private void helper1(int idx, String s, List<String> currPalindrome, List<List<String>> result, Boolean[][] isPalindromeCache) {
            if(idx >= s.length()) {
                result.add(new ArrayList<>(currPalindrome));
                return;
            }

            for(int i = idx; i < s.length(); i++) {
                if(isPalindrome1(idx, i, s, isPalindromeCache)) {
                    currPalindrome.add(s.substring(idx, i+1));
                    helper1(i+1, s, currPalindrome, result, isPalindromeCache);
                    currPalindrome.remove(currPalindrome.size()-1);
                }
            }
        }

        public List<List<String>> partition1(String s) {
            if (s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            int size = s.length();
            List<List<String>> result = new ArrayList<>();
            List<String> currPalindrome = new ArrayList<>();
            Boolean[][] isPalindromeCache = new Boolean[size][size];

            helper1(0, s, currPalindrome, result, isPalindromeCache);

            return result;
        }

        private boolean isPalindrome(int left, int right, String s) {
            while (left < right) {
                if(s.charAt(left) != s.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }

        private void helper0(int idx, String s, List<String> currPalindrome, List<List<String>> result) {
            if(idx >= s.length()) {
                result.add(new ArrayList<>(currPalindrome));
                return;
            }

            for(int i = idx; i < s.length(); i++) {
                if(isPalindrome(idx, i, s)) {
                    currPalindrome.add(s.substring(idx, i+1));
                    helper0(i+1, s, currPalindrome, result);
                    currPalindrome.remove(currPalindrome.size()-1);
                }
            }
        }

        public List<List<String>> partition(String s) {
            if(s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            List<List<String>> result = new ArrayList<>();
            List<String> currPalindrome = new ArrayList<>();

            helper0(0, s, currPalindrome, result);

            return result;
        }
    }
}
