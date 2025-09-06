package neetcode.dynamic_programming;

import java.util.Arrays;

/*
- Using Recursion
    * Single Digit
    * Two Digit
    * TC: O(2^n)
    * SC: O(n) because of recursion stack
- Using Recursion with Memoization (Top-up approach)
    * TC: O(n)
    * SC: O(n) + O(n) (cache and recursion stack)
- Using True Dynamic Programming (Bottom-up approach)
    * TC: O(n)
    * SC: O(n)
- Space optimization from Bottom-up approach
    * TC: O(n)
    * SC: O(1)

https://leetcode.com/problems/decode-ways/description/
 */

public class Decode_Ways_LC_91 {
    class Solution {
        public int numDecodings3(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length(), front1 = 1, front2 = 0;

            for(int idx = size-1; idx >= 0; idx--) {
                if(s.charAt(idx) == '0') {
                    front2 = front1;
                    front1 = 0;
                    continue;
                }

                int singleDigitCount = 0;
                // Include single digit
                singleDigitCount = front1;

                int twoDigitsCount = 0;
                // Include two digit according condition
                if((idx+1) < s.length()
                        && ((s.charAt(idx) == '1' && s.charAt(idx+1) <= '9')
                        || (s.charAt(idx) == '2' && s.charAt(idx+1) <= '6'))) {
                    twoDigitsCount = front2;
                }

                front2 = front1;
                front1 = singleDigitCount + twoDigitsCount;
            }

            return front1;
        }

        public int numDecodings2(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[] cache = new int[size+1];
            cache[size] = 1;

            for(int idx = size-1; idx >= 0; idx--) {
                if(s.charAt(idx) == '0') {
                    cache[idx] = 0;
                    continue;
                }

                int singleDigitCount = 0;
                // Include single digit
                singleDigitCount = cache[idx+1];

                int twoDigitsCount = 0;
                // Include two digit according condition
                if((idx+1) < s.length()
                        && ((s.charAt(idx) == '1' && s.charAt(idx+1) <= '9')
                        || (s.charAt(idx) == '2' && s.charAt(idx+1) <= '6'))) {
                    twoDigitsCount = cache[idx+2];
                }

                cache[idx] = singleDigitCount + twoDigitsCount;
            }

            return cache[0];
        }

        private int helper1(int idx, String s, int[] cache) {
            if(idx >= s.length()) {
                return 1;
            } else if(s.charAt(idx) == '0') {
                return 0;
            } else if(cache[idx] != -1) {
                return cache[idx];
            }

            int singleDigitCount = 0;
            // Include single digit
            singleDigitCount = helper1(idx+1, s, cache);

            int twoDigitsCount = 0;
            // Include two digit according condition
            if((idx+1) < s.length()
                    && ((s.charAt(idx) == '1' && s.charAt(idx+1) <= '9')
                    || (s.charAt(idx) == '2' && s.charAt(idx+1) <= '6'))) {
                twoDigitsCount = helper1(idx+2, s, cache);
            }

            return cache[idx] = singleDigitCount + twoDigitsCount;
        }

        public int numDecodings1(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            int size = s.length();
            int[] cache = new int[size];
            Arrays.fill(cache, -1);

            return helper1(0, s, cache);
        }

        private int helper0(int idx, String s) {
            if(idx >= s.length()) {
                return 1;
            } else if(s.charAt(idx) == '0') {
                return 0;
            }

            int singleDigitCount = 0;
            // Include single digit
            singleDigitCount = helper0(idx+1, s);

            int twoDigitsCount = 0;
            // Include two digit according condition
            if((idx+1) < s.length()
                    && ((s.charAt(idx) == '1' && s.charAt(idx+1) <= '9')
                    || (s.charAt(idx) == '2' && s.charAt(idx+1) <= '6'))) {
                twoDigitsCount = helper0(idx+2, s);
            }

            return singleDigitCount + twoDigitsCount;
        }

        public int numDecodings0(String s) {
            if(s == null || s.isEmpty()) {
                return 0;
            }

            return helper0(0, s);
        }
    }
}
