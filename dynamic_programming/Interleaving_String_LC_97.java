package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/interleaving-string/description/

1. Recursive (Brute Force) — isInterleave0
- Recursively try to match characters from s1 or s2 with s3.
- Base case: when both s1 and s2 are fully matched, return true.
- Highly exponential time complexity (2^(m+n)) due to overlapping subproblems.

2. Top-Down Memoization — isInterleave1
- Same recursive logic as brute force but with memo (cache) to avoid recomputation.
- Cache stores if substring starting at (idx1, idx2) can form the suffix of s3.
- Time complexity: O(m*n), where m = length of s1, n = length of s2.
- Space complexity: O(m*n).

3. Bottom-Up DP (2D array) — isInterleave2 and isInterleave4
- DP table dp[i][j] = true if s3[i+j:] can be formed by interleaving s1[i:] and s2[j:].
- Fill table from bottom-right to top-left.
- Base case: dp[m][n] = true.
Transition:
- dp[i][j] = true if either:
    * s1[i] == s3[i+j] && dp[i+1][j] is true, or
    * s2[j] == s3[i+j] && dp[i][j+1] is true.
- Time and space: O(m*n).

4. Bottom-Up DP (Space Optimized with 1D arrays) — isInterleave3 and isInterleave5
- Use two 1D arrays: frontRow (previous row) and currRow (current row) to save space.
- Update currRow[j] using values from frontRow[j] and currRow[j+1].
- Base case: frontRow[size2] = true.
- Time: O(m*n), Space: O(n) where n = length of s2.


Important Notes:
- Always check if s3.length == s1.length + s2.length. If not, return false early.
- Indices idx1 and idx2 traverse backward from end of strings for bottom-up.
- Matching logic carefully verifies the next character in s3 against s1 and s2.
- In 1D DP approach, carefully use frontRow and currRow to avoid overwriting needed values.
- For correctness, make sure base cases are explicitly set, especially dp[m][n] = true.
 */

public class Interleaving_String_LC_97 {
    class Solution {
        public boolean isInterleave5(String s1, String s2, String s3) {
            int size1 = s1.length(), size2 = s2.length();
            if (s3.length() != size1 + size2) return false;

            boolean[] dp = new boolean[size2 + 1];
            dp[size2] = true;

            for (int idx1 = size1; idx1 >= 0; idx1--) {
                boolean[] currRow = new boolean[size2 + 1];
                for (int idx2 = size2; idx2 >= 0; idx2--) {
                    if (idx1 < size1 && s1.charAt(idx1) == s3.charAt(idx1 + idx2) && dp[idx2]) {
                        currRow[idx2] = true;
                    } else if (idx2 < size2 && s2.charAt(idx2) == s3.charAt(idx1 + idx2) && currRow[idx2 + 1]) {
                        currRow[idx2] = true;
                    }
                }
                dp = currRow;
            }
            return dp[0];
        }

        public boolean isInterleave4(String s1, String s2, String s3) {
            int size1 = s1.length(), size2 = s2.length();
            if (s3.length() != size1 + size2) return false;

            boolean[][] dp = new boolean[size1 + 1][size2 + 1];
            dp[size1][size2] = true;

            for (int idx1 = size1; idx1 >= 0; idx1--) {
                for (int idx2 = size2; idx2 >= 0; idx2--) {
                    if (idx1 < size1 && s1.charAt(idx1) == s3.charAt(idx1 + idx2) && dp[idx1 + 1][idx2]) {
                        dp[idx1][idx2] = true;
                    } else if (idx2 < size2 && s2.charAt(idx2) == s3.charAt(idx1 + idx2) && dp[idx1][idx2 + 1]) {
                        dp[idx1][idx2] = true;
                    }
                }
            }
            return dp[0][0];
        }

        public boolean isInterleave3(String s1, String s2, String s3) {
            int size1 = s1.length(), size2 = s2.length();
            if(s3.length() != (size1 + size2)) {
                return false;
            }

            int[] frontRow = new int[size2+1];
            frontRow[size2] = 1;

            for(int idx1 = size1; idx1 >= 0; idx1--) {
                int[] currRow = new int[size2+1];
                for(int idx2 = size2; idx2 >= 0; idx2--) {
                    if(idx1 == size1 && idx2 == size2) {
                        currRow[idx2] = 1;
                    } else {
                        if (idx1 < size1
                                && s1.charAt(idx1) == s3.charAt(idx1 + idx2)
                                && frontRow[idx2] == 1) {
                            currRow[idx2] = 1;
                        } else if (idx2 < size2
                                && s2.charAt(idx2) == s3.charAt(idx1 + idx2)
                                && currRow[idx2 + 1] == 1) {
                            currRow[idx2] = 1;
                        }
                    }
                }
                frontRow = currRow;
            }

            return frontRow[0] == 1;
        }

        public boolean isInterleave2(String s1, String s2, String s3) {
            int size1 = s1.length(), size2 = s2.length();
            if(s3.length() != (size1 + size2)) {
                return false;
            }

            int[][] cache = new int[size1+1][size2+1];
            cache[size1][size2] = 1;

            for(int idx1 = size1; idx1 >= 0; idx1--) {
                for(int idx2 = size2; idx2 >= 0; idx2--) {
                    if(idx1 < size1
                            && s1.charAt(idx1) == s3.charAt(idx1+idx2)
                            && cache[idx1+1][idx2] == 1) {
                        cache[idx1][idx2] = 1;
                    } else if(idx2 < size2
                            && s2.charAt(idx2) == s3.charAt(idx1+idx2)
                            && cache[idx1][idx2+1] == 1) {
                        cache[idx1][idx2] = 1;
                    }
                }
            }

            return cache[0][0] == 1;
        }

        private int helper1(int idx1, int idx2, String s1, String s2, String s3, int size1, int size2, int[][] cache) {
            if(idx1 == size1 && idx2 == size2) {
                return 1;
            } else if(cache[idx1][idx2] != -1) {
                return cache[idx1][idx2];
            }

            int hasValid = 0;
            if(idx1 < size1
                    && s1.charAt(idx1) == s3.charAt(idx1+idx2)
                    && helper1(idx1+1, idx2, s1, s2, s3, size1, size2, cache) == 1) {
                hasValid = 1;
            } else if(idx2 < size2
                    && s2.charAt(idx2) == s3.charAt(idx1+idx2)
                    && helper1(idx1, idx2+1, s1, s2, s3, size1, size2, cache) == 1) {
                hasValid = 1;
            } else {
                hasValid = 0;
            }

            return cache[idx1][idx2] = hasValid;
        }

        public boolean isInterleave1(String s1, String s2, String s3) {
            int size1 = s1.length(), size2 = s2.length();
            if(s3.length() != (size1 + size2)) {
                return false;
            }

            int[][] cache = new int[size1+1][size2+1];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, 0, s1, s2, s3, size1, size2, cache) == 1;
        }

        private int helper0(int idx1, int idx2, String s1, String s2, String s3, int size1, int size2) {
            if(idx1 == size1 && idx2 == size2) {
                return 1;
            }

            if(idx1 < size1
                    && s1.charAt(idx1) == s3.charAt(idx1+idx2)
                    && helper0(idx1+1, idx2, s1, s2, s3, size1, size2) == 1) {
                return 1;
            } else if(idx2 < size2
                    && s2.charAt(idx2) == s3.charAt(idx1+idx2)
                    && helper0(idx1, idx2+1, s1, s2, s3, size1, size2) == 1) {
                return 1;
            } else {
                return 0;
            }
        }

        public boolean isInterleave0(String s1, String s2, String s3) {
            int size1 = s1.length(), size2 = s2.length();
            if(s3.length() != (size1 + size2)) {
                return false;
            }

            return helper0(0, 0, s1, s2, s3, size1, size2) == 1;
        }
    }
}
