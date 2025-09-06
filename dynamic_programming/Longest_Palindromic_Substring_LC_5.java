package neetcode.dynamic_programming;

/*
 https://leetcode.com/problems/longest-palindromic-substring/description/
 Using For loop and while loop:
     - By comparing adjacent elements
     - Case 01: Check for odd length palindrome
     - Case 02: Check for even length palindrome

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

public class Longest_Palindromic_Substring_LC_5 {
    class Revision01 {
        public String longestPalindrome(String s) {
            if(s == null || s.isEmpty()) {
                return "";
            }

            int size = s.length(), maxLength = 0, starLen = -1;

            for(int i = 0; i < size; i++) {
                // Case 01: Check for Odd Length substrings
                int left = i, right = i;
                while (left >= 0 && right < size && s.charAt(left) == s.charAt(right)) {
                    if((right - left + 1) > maxLength) {
                        maxLength = right - left + 1;
                        starLen = left;
                    }

                    left--;
                    right++;
                }

                // Case 02: Check for Even Length substrings
                left = i;
                right = i+1;
                while (left >= 0 && right < size && s.charAt(left) == s.charAt(right)) {
                    if((right - left + 1) > maxLength) {
                        maxLength = right - left + 1;
                        starLen = left;
                    }

                    left--;
                    right++;
                }
            }

            return starLen == -1 ? "" : s.substring(starLen, starLen + maxLength);
        }
    }

    class Solution {
        public String longestPalindrome(String s) {
            if(s == null || s.isEmpty()) {
                return s;
            }

            int size = s.length(), maxLen = Integer.MIN_VALUE, startLen = -1;

            for(int i=0; i < size; i++) {

                // Case 01: Check for Odd Length Palindrome
                int left = i, right = i;
                while (left >= 0 && right < size && s.charAt(left) == s.charAt(right)) {
                    if((right - left + 1) > maxLen) {
                        maxLen = right - left + 1;
                        startLen = left;
                    }

                    left--;
                    right++;
                }

                // Case 02: Check for Even length Palindrome
                left = i;
                right = i+1;
                while (left >= 0 && right < size && s.charAt(left) == s.charAt(right)) {
                    if((right - left + 1) > maxLen) {
                        maxLen = right - left + 1;
                        startLen = left;
                    }
                    left--;
                    right++;
                }
            }

            return maxLen == Integer.MIN_VALUE ? "" : s.substring(startLen, startLen + maxLen);
        }
    }
}
