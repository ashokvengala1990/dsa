package neetcode.dynamic_programming;

/*
Q: Given a string S, return the length of the longest palindromic substring within S.

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

public class Longest_Palindromic_Substring {
    class Solution {
        private int longest(String s, int left, int right) {
            int maxLength = 0;

            while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
                if((right - left + 1) > maxLength) {
                    maxLength = right - left + 1;
                }

                left--;
                right++;
            }

            return maxLength;
        }

        public int longestPalindromeSubstring(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length(), maxLength = 0;

            for(int i = 0 ; i < size; i++) {
                // Odd length substrings
                maxLength = Math.max(maxLength, longest(s, i, i));

                // Even length substrings
                maxLength = Math.max(maxLength, longest(s, i, i+1));
            }

            return maxLength;
        }
    }
}
