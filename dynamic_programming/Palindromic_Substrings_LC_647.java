package neetcode.dynamic_programming;

/*
https://leetcode.com/problems/palindromic-substrings/description/

Brute Force Approach:
- Generate all possible substring and for each substring, use two pointer to identify whether that string is palindrome or not using
two pointer approach.
- TC: O(n^3), generate all possible substring takes O(n^2) and each of them we need to check whether it is palindrome or not.
- SC: O(n^2)

Dynamic Programming Approach:
- Dynamic programming approach to this problem tells us that we should solve a sub-problem first, and then expand out. Given the string
s = 'abaab', we can start from the middle (Odd Length Substrings: current Index) or "Even Length substrings(current and nextIndex) and
expand outwards.
- TC: O(n^2)
- SC: O(1)
 */

public class Palindromic_Substrings_LC_647 {
    class Solution {
        private int helper(int left, int right, String s) {
            int count = 0;

            while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
                count++;
                left--;
                right++;
            }

            return count;
        }

        public int countSubstrings(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length(), countPali = 0;

            for(int i = 0; i < size; i++) {
                // Odd length palindromes
                countPali += helper(i, i, s);

                // Even length palindromes
                countPali += helper(i, i+1, s);
            }

            return countPali;
        }
    }
}
