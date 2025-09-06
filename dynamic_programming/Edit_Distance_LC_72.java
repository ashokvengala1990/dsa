package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/edit-distance/description/


- Explored from brute → memo → tabulation → space optimized
- Used 2-row optimization (1D array) for best space
- Bottom-up fill with base case: converting to/from empty string

TC: O(n × m)
SC: O(m)
 */

public class Edit_Distance_LC_72 {
    class Solution {
        public int minDistance3(String word1, String word2) {
            if(word1 == null && word2 != null) {
                return word2.length();
            } else if(word2 == null && word1 != null) {
                return word1.length();
            }

            int size1 = word1.length(), size2 = word2.length();
            int[] frontRow = new int[size2+1];

            for(int idx2 = 0; idx2 <= size2; idx2++) {
                frontRow[idx2] = size2 - idx2;
            }

            for(int idx1 = size1-1; idx1 >= 0; idx1--) {
                int[] currRow = new int[size2+1];
                currRow[size2] = size1 - idx1;
                for(int idx2 = size2-1; idx2 >= 0; idx2--) {
                    int minOps = 0;
                    if(word1.charAt(idx1) == word2.charAt(idx2)) {
                        minOps = frontRow[idx2+1];
                    } else {
                        minOps = 1 + Math.min(frontRow[idx2+1],
                                Math.min(frontRow[idx2],
                                        currRow[idx2+1]));
                    }

                    currRow[idx2] = minOps;
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int minDistance2(String word1, String word2) {
            if(word1 == null && word2 != null) {
                return word2.length();
            } else if(word2 == null && word1 != null) {
                return word1.length();
            }

            int size1 = word1.length(), size2 = word2.length();
            int[][] cache = new int[size1+1][size2+1];
            for(int idx1 = 0; idx1 <= size1; idx1++) {
                cache[idx1][size2] = size1- idx1;
            }

            for(int idx2 = 0; idx2 <= size2; idx2++) {
                cache[size1][idx2] = size2 - idx2;
            }

            for(int idx1 = size1-1; idx1 >= 0; idx1--) {
                for(int idx2 = size2-1; idx2 >= 0; idx2--) {
                    int minOps = 0;
                    if(word1.charAt(idx1) == word2.charAt(idx2)) {
                        minOps = cache[idx1+1][idx2+1];
                    } else {
                        minOps = 1 + Math.min(cache[idx1+1][idx2+1],
                                Math.min(cache[idx1+1][idx2],
                                        cache[idx1][idx2+1]));
                    }

                    cache[idx1][idx2] = minOps;
                }
            }

            return cache[0][0];
        }

        private int helper1(int idx1, int idx2, String word1, String word2, int size1, int size2, int[][] cache) {
            if(idx2 == size2) {
                return size1 - idx1;
            } else if(idx1 == size1) {
                return size2 - idx2;
            } else if(cache[idx1][idx2] != -1) {
                return cache[idx1][idx2];
            }

            int minOps = 0;

            if(word1.charAt(idx1) == word2.charAt(idx2)) {
                minOps = helper1(idx1+1, idx2+1, word1, word2, size1, size2, cache);
            } else {
                minOps = 1 + Math.min(helper1(idx1+1, idx2+1, word1, word2, size1, size2, cache),
                        Math.min(helper1(idx1+1, idx2, word1, word2, size1, size2, cache),
                                helper1(idx1, idx2+1, word1, word2, size1, size2, cache)));
            }

            return cache[idx1][idx2] = minOps;
        }

        public int minDistance1(String word1, String word2) {
            if(word1 == null && word2 != null) {
                return word2.length();
            } else if(word2 == null && word1 != null) {
                return word1.length();
            }

            int size1 = word1.length(), size2 = word2.length();
            int[][] cache = new int[size1][size2];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, 0, word1, word2, size1, size2, cache);
        }

        private int helper0(int idx1, int idx2, String word1, String word2, int size1, int size2) {
            if(idx2 == size2) {
                return size1 - idx1;
            } else if(idx1 == size1) {
                return size2 - idx2;
            }

            int minOps = Integer.MAX_VALUE;

            if(word1.charAt(idx1) == word2.charAt(idx2)) {
                minOps = helper0(idx1+1, idx2+1, word1, word2, size1, size2);
            } else {
                minOps = 1 + Math.min(helper0(idx1+1, idx2+1, word1, word2, size1, size2),
                        Math.min(helper0(idx1+1, idx2, word1, word2, size1, size2),
                                helper0(idx1, idx2+1, word1, word2, size1, size2)));
            }

            /*
            replace: idx1+1, idx2+1
                For example:
                word1 = hor
                word2 = ror
            remove: idx1+1, idx2
                For example:
                word1 = rose
                word2 = ros
            insert: idx1, idx2+1;
                For Example:
                 word1 = execution
                 word2 = execution
             */

            return minOps;
        }

        public int minDistance0(String word1, String word2) {
            if(word1 == null && word2 != null) {
                return word2.length();
            } else if(word2 == null && word1 != null) {
                return word1.length();
            }

            int size1 = word1.length(), size2 = word2.length();
            return helper0(0, 0, word1, word2, size1, size2);
        }
    }
}
