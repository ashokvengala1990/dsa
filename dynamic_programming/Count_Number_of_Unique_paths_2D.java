package neetcode.dynamic_programming;


public class Count_Number_of_Unique_paths_2D {
    class Solution {
        public int countUniquePaths3(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            int[] nextRow = new int[COLS+1];

            for(int row = ROWS-1; row >= 0; row--) {
                int[] currRow = new int[COLS+1];
                for(int col = COLS-1; col >= 0; col--) {
                    if(row == ROWS-1 && col == COLS-1) {
                        currRow[col] = 1;
                    } else {
                        int down = nextRow[col];
                        int right = currRow[col+1];
                        currRow[col] = down + right;
                    }
                }
                nextRow = currRow;
            }

            return nextRow[0];
        }

        public int countUniquePaths2(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            int[][] cache = new int[ROWS+1][COLS+1];

            for(int row = ROWS-1; row >=0; row--) {
                for(int col = COLS-1; col >= 0; col--) {
                    if(row == ROWS-1 && col == COLS-1) {
                        cache[row][col] = 1;
                    } else {
                        int down = cache[row+1][col];
                        int right = cache[row][col+1];
                        cache[row][col] = down+ right;
                    }
                }
            }

            return cache[0][0];
        }

        private int helper1(int row, int col, int[][] grid, int ROWS, int COLS, int[][] cache) {
            if(row == ROWS || col == COLS) {
                return 0;
            } else if(row == ROWS-1 && col == COLS-1) {
                return 1;
            } else if(cache[row][col] > 0) {
                return cache[row][col];
            }

            int down = helper1(row+1, col, grid, ROWS, COLS, cache);
            int right = helper1(row, col+1, grid, ROWS, COLS, cache);

            return cache[row][col] = down + right;
        }

        public int countUniquePaths1(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            int[][] cache = new int[ROWS][COLS];

            return helper1(0, 0, grid, ROWS, COLS, cache);
        }

        private int helper0(int row, int col, int[][] grid, int ROWS, int COLS) {
            if(row == ROWS || col == COLS) {
                return 0;
            } else if(row == ROWS-1 && col == COLS-1) {
                return 1;
            }

            int down = helper0(row+1, col, grid, ROWS, COLS);
            int right = helper0(row, col+1, grid, ROWS, COLS);

            return down + right;
        }

        public int countUniquePaths0(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            return helper0(0, 0, grid, ROWS, COLS);
        }
    }
}
