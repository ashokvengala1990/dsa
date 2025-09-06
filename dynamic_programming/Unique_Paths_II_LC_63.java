package neetcode.dynamic_programming;

import java.util.Arrays;

/*
https://leetcode.com/problems/unique-paths-ii/description/

 */

public class Unique_Paths_II_LC_63 {
    class Solution {
        public int uniquePathsWithObstacles3(int[][] obstacleGrid) {
            if(obstacleGrid == null || obstacleGrid.length == 0) {
                return 0;
            }

            int ROWS = obstacleGrid.length, COLS = obstacleGrid[0].length;
            int[] frontRow = new int[COLS+1];

            for(int row = ROWS-1; row >= 0; row--) {
                int[] currRow = new int[COLS+1];
                for(int col = COLS-1; col >= 0; col--) {
                    if(obstacleGrid[row][col] == 1) {
                        continue;
                    } else if(row == (ROWS-1) && col == (COLS-1)) {
                        currRow[col] = 1;
                    } else {
                        int down = frontRow[col];
                        int right = currRow[col+1];

                        currRow[col] = down + right;
                    }
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int uniquePathsWithObstacles2(int[][] obstacleGrid) {
            if(obstacleGrid == null || obstacleGrid.length == 0) {
                return 0;
            }

            int ROWS = obstacleGrid.length, COLS = obstacleGrid[0].length;
            int[][] cache = new int[ROWS+1][COLS+1];

            for(int row = ROWS-1; row >= 0; row--) {
                for(int col = COLS-1; col >= 0; col--) {
                    if(obstacleGrid[row][col] == 1) {
                        continue;
                    } else if(row == (ROWS-1) && col == (COLS-1)) {
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

        private int helper1(int row, int col, int ROWS, int COLS, int[][] obstacleGrid, int[][] cache) {
            if(row >= ROWS || col >= COLS || obstacleGrid[row][col] == 1) {
                return 0;
            } else if(row == (ROWS-1) && col == (COLS-1)) {
                return 1;
            } else if(cache[row][col] != -1) {
                return cache[row][col];
            }

            int down = helper1(row+1, col, ROWS, COLS, obstacleGrid, cache);
            int right = helper1(row, col+1, ROWS, COLS, obstacleGrid, cache);

            return cache[row][col] = down + right;
        }

        public int uniquePathsWithObstacles1(int[][] obstacleGrid) {
            if(obstacleGrid == null || obstacleGrid.length == 0) {
                return 0;
            }

            int ROWS = obstacleGrid.length, COLS = obstacleGrid[0].length;
            int[][] cache = new int[ROWS][COLS];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0, 0, ROWS, COLS, obstacleGrid, cache);
        }

        private int helper0(int row, int col, int ROWS, int COLS, int[][] obstacleGrid) {
            if(row >= ROWS || col >= COLS || obstacleGrid[row][col] == 1) {
                return 0;
            } else if(row == (ROWS-1) && col == (COLS-1)) {
                return 1;
            }

            int down = helper0(row+1, col, ROWS, COLS, obstacleGrid);
            int right = helper0(row, col+1, ROWS, COLS, obstacleGrid);

            return down + right;
        }

        public int uniquePathsWithObstacles0(int[][] obstacleGrid) {
            if(obstacleGrid == null || obstacleGrid.length == 0) {
                return 0;
            }

            int ROWS = obstacleGrid.length, COLS = obstacleGrid[0].length;

            return helper0(0, 0, ROWS, COLS, obstacleGrid);
        }
    }
}
