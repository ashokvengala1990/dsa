package neetcode.dynamic_programming;

import java.util.Arrays;
/*
https://leetcode.com/problems/unique-paths/description/

 */

public class Unique_Paths_LC_62 {
    class Solution {
        public int uniquePaths3(int m, int n) {
            if (m <= 0 || n <= 0) {
                return 0;
            }

            int ROWS = m, COLS = n;
            int[] frontRow = new int[COLS + 1];

            for (int row = ROWS - 1; row >= 0; row--) {
                int[] currRow = new int[COLS + 1];
                for (int col = COLS - 1; col >= 0; col--) {
                    if (row == (ROWS - 1) && col == (COLS - 1)) {
                        currRow[col] = 1;
                    } else {
                        int down = frontRow[col];
                        int right = currRow[col + 1];

                        currRow[col] = down + right;
                    }
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int uniquePaths2(int m, int n) {
            if(m <= 0 || n <= 0) {
                return 0;
            }

            int ROWS = m, COLS = n;
            int[][] cache = new int[ROWS+1][COLS+1];

            for(int row = ROWS-1; row >= 0; row--) {
                for(int col = COLS-1; col >= 0; col--) {
                    if(row == (ROWS-1) && col == (COLS-1)) {
                        cache[row][col] = 1;
                    } else {
                        int down = cache[row+1][col];
                        int right = cache[row][col+1];

                        cache[row][col] = down + right;
                    }
                }
            }

            return cache[0][0];
        }

        private int helper1(int row, int col, int ROWS, int COLS, int[][] cache) {
            if(row >= ROWS || col >= COLS) {
                return 0;
            } else if(row == (ROWS-1) && col == (COLS-1)) {
                return 1;
            } else if(cache[row][col] != -1) {
                return cache[row][col];
            }

            int down = helper1(row+1, col, ROWS, COLS, cache);
            int right = helper1(row, col+1, ROWS, COLS, cache);

            return cache[row][col] = down + right;
        }

        public int uniquePaths1(int m, int n) {
            if(m <= 0 || n <= 0) {
                return 0;
            }

            int[][] cache = new int[m][n];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }
            return helper1(0, 0, m, n, cache);
        }

        private int helper0(int row, int col, int ROWS, int COLS) {
            if(row >= ROWS || col >= COLS) {
                return 0;
            } else if(row == (ROWS-1) && col == (COLS-1)) {
                return 1;
            }

            int down = helper0(row+1, col, ROWS, COLS);
            int right = helper0(row, col+1, ROWS, COLS);

            return down + right;
        }

        public int uniquePaths0(int m, int n) {
            if(m <= 0 || n <= 0) {
                return 0;
            }

            return helper0(0, 0, m, n);
        }
    }
}
