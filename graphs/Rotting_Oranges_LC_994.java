package neetcode.graphs;

import java.util.LinkedList;
import java.util.Queue;

/*
- https://leetcode.com/problems/rotting-oranges/

- Using Breadth First Search Algorithm: with Queue Data Structure and also fresh variable
- TC: O(N*M)
- SC: O(N*M) because of queue data structure
 */

public class Rotting_Oranges_LC_994 {
    class Revision02 {
        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};
        private static final int FRESH_ORANGE = 1, EMPTY = 0, ROTTEN_ORANGE = 2;

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        public int orangesRotting(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length, freshOranges = 0, minutes = 0;
            Queue<int[]> queue = new LinkedList<>();

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(grid[r][c] == 2) {
                        queue.offer(new int[]{r, c});
                    } else if(grid[r][c] == 1) {
                        freshOranges++;
                    }
                }
            }

            while (!queue.isEmpty() && freshOranges > 0) {
                int levelSize = queue.size();

                for(int i = 0; i < levelSize; i++) {
                    int[] curr = queue.poll();
                    int r = curr[0], c = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int nr = r + neighbor[0], nc = c + neighbor[1];

                        if(isValid(nr, nc, rows, cols) && grid[nr][nc] == FRESH_ORANGE) {
                            grid[nr][nc] = ROTTEN_ORANGE;
                            freshOranges--;
                            queue.offer(new int[]{nr, nc});
                        }
                    }
                }
                minutes++;
            }

            return freshOranges == 0 ? minutes : -1;
        }
    }

    class Revision01 {
        private static final int[][] offsetNeighbors = {{-1,0},{0,1},{1,0},{0,-1}};

        public int orangesRotting(int[][] grid) {
            int time = 0;
            if(grid == null || grid.length == 0) {
                return time;
            }

            int ROWS = grid.length, COLS = grid[0].length, fresh = 0;
            Queue<int[]> queue = new LinkedList<>();

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    if(grid[row][col] == 1) {
                        fresh++;
                    } else if(grid[row][col] == 2) {
                        queue.offer(new int[]{row, col});
                    }
                }
            }

            while (!queue.isEmpty() && fresh > 0) {
                int queueSize = queue.size();

                for(int i = 0; i < queueSize; i++) {
                    int[] curr = queue.poll();
                    int currRow = curr[0], currCol = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int newRow = currRow + neighbor[0], newCol = currCol + neighbor[1];

                        if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && grid[newRow][newCol] == 1) {
                            grid[newRow][newCol] = 2;
                            fresh--;
                            queue.offer(new int[]{newRow, newCol});
                        }
                    }
                }

                time++;
            }

            return fresh == 0 ? time : -1;
        }
    }

    class Solution {
        public static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        public int orangesRotting(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int ROWS = grid.length, COLS = grid[0].length, fresh = 0;
            Queue<int[]> queue = new LinkedList<>();

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    if(grid[row][col] == 1) {
                        fresh++;
                    } else if(grid[row][col] == 2) {
                        queue.offer(new int[]{row, col});
                    }
                }
            }

            int time = 0;

            while (!queue.isEmpty() && fresh > 0) {
                int currLevelSize = queue.size();

                for(int i=0; i < currLevelSize; i++) {
                    int[] curr = queue.poll();
                    int row = curr[0], col = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int newRow = row + neighbor[0], newCol = col + neighbor[1];

                        if(newRow >= 0 && newRow < ROWS
                                && newCol >= 0 && newCol < COLS
                                && grid[newRow][newCol] == 1) {
                            grid[newRow][newCol] = 2;
                            fresh--;
                            queue.offer(new int[]{newRow, newCol});
                        }
                    }
                }

                time++;
            }

            return fresh == 0 ? time : -1;
        }
    }
}
