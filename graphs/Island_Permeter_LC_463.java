package neetcode.graphs;

// https://leetcode.com/problems/island-perimeter/description/

public class Island_Permeter_LC_463 {
    class Solution {
        /*
         * LeetCode 463: Island Perimeter
         *
         * NOTES (Common “trick” / clean interview solution):
         * - Each land cell contributes 4 sides initially.
         * - If a land cell touches another land cell, the shared boundary is INTERNAL (not perimeter).
         * - A shared boundary is counted TWICE in "4 per cell":
         *      1) current cell side  +1
         *      2) neighbor cell side +1
         *   => so we subtract 2 for each shared edge.
         *
         * - We check only UP and LEFT to avoid double counting:
         *   When scanning row-wise (top->bottom, left->right), UP and LEFT neighbors are already visited.
         *   Each shared edge is removed exactly once.
         *
         * TC: O(rows * cols)  (optimal; must scan all cells)
         * SC: O(1)
         */
        public int islandPerimeter1(int[][] grid) {
            int perimeter = 0;
            if(grid == null || grid.length == 0) {
                return perimeter;
            }

            int rows = grid.length, cols = grid[0].length;

            for(int row = 0 ; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(grid[row][col] == 1) {
                        // Start with 4 sides for this land cell
                        perimeter += 4;

                        // Up: shared upper cell bottom edge side and also current cell upper side edge
                        // UP neighbor exists and is land:
                        // shared edge = (current top side) + (up cell bottom side) => counted twice => -2
                        if(row > 0 && grid[row-1][col] == 1) {
                            perimeter -= 2;
                        }

                        // LEFT: shared left cell right side and also current cell left side edge
                        // LEFT neighbor exists and is land:
                        // shared edge = (current left side) + (left cell right side) => counted twice => -2
                        if(col > 0 && grid[row][col-1] == 1) {
                            perimeter -= 2;
                        }
                    }
                }
            }

            return perimeter;
        }

        /*
         * NOTES (Your intuitive neighbor-count solution):
         * - For each land cell, perimeter contribution:
         *      4 - (# of land neighbors among up/down/left/right)
         * - Because each land neighbor removes exactly 1 exposed side of the current cell.
         *
         * TC: O(rows * cols)
         * SC: O(1)
         */
        public int islandPerimeter0(int[][] grid) {
            int perimeter = 0;
            if(grid == null || grid.length == 0) {
                return perimeter;
            }

            int rows = grid.length, cols = grid[0].length, up = 0, down = 0, left = 0, right = 0;

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(grid[row][col] == 1) {
                        // Up case
                        if(row == 0) {
                            up = 0;
                        } else {
                            up = grid[row-1][col];
                        }

                        // Down case
                        if(row == (rows-1)) {
                            down = 0;
                        } else {
                            down = grid[row+1][col];
                        }

                        // Left cae
                        if(col == 0) {
                            left = 0;
                        } else {
                            left = grid[row][col-1];
                        }

                        // Right case
                        if(col == (cols-1)) {
                            right = 0;
                        } else {
                            right = grid[row][col+1];
                        }

                        // Each adjacent land neighbor hides one side of this cell
                        perimeter += 4 - (up + down + left + right);
                    }
                }
            }

            return perimeter;
        }
    }
}
