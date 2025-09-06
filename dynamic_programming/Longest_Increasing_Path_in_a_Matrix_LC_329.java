package neetcode.dynamic_programming;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
- Using DFS recursion with Cache
- Using BFS + Topological Sorting

https://leetcode.com/problems/longest-increasing-path-in-a-matrix/description/
 */

public class Longest_Increasing_Path_in_a_Matrix_LC_329 {
    public static void main(String[] args) {
        Solution s = new Solution();
        int[][] matrix = {{9,9,4},{6,6,8},{2,1,1}};
        System.out.println(s.longestIncreasingPath2(matrix));
    }

    static class Solution {
        /*
        BFS - Kahn's Algorithm
            * inDegree 2D array
            * Queue
         */
        public int longestIncreasingPath3(int[][] matrix) {
            if(matrix == null || matrix.length == 0) {
                return 0;
            }

            int[][] offsetNeighbors = {{-1, 0},{1, 0},{0, 1},{0, -1}};

            int ROWS = matrix.length, COLS = matrix[0].length;
            int[][] inDegree = new int[ROWS][COLS];

            // Step 1: Compute in-degrees
            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    for(int[] neighbor: offsetNeighbors) {
                        int newRow = row + neighbor[0], newCol = col + neighbor[1];

                        if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                                && matrix[row][col] < matrix[newRow][newCol]) {
                            inDegree[newRow][newCol]++;
                        }
                    }
                }
            }

            // Step 2: Initialize queue with 0 in-degree cells
            Queue<int[]> queue = new LinkedList<>();
            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    if(inDegree[row][col] == 0) {
                        queue.offer(new int[]{row, col});
                    }
                }
            }

            // Step 3: BFS Topological Sort
            int length = 0;
            while (!queue.isEmpty()) {
                int size = queue.size();
                length++; // Each BFS level is one step in the increasing path

                for(int i = 0; i < size; i++) {
                    int[] cell = queue.poll();
                    int row = cell[0], col = cell[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int newRow = row + neighbor[0], newCol = col + neighbor[1];
                        if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                                && matrix[row][col] < matrix[newRow][newCol]) {
                            inDegree[newRow][newCol]--;
                            if(inDegree[newRow][newCol] == 0) {
                                queue.offer(new int[]{newRow, newCol});
                            }
                        }
                    }
                }
            }

            return length;
        }

        private int helper2(int row, int col, int[][] matrix, int[][] cache, int[][][] parent, int ROWS, int COLS) {
            if(cache[row][col] > 0) {
                return cache[row][col];
            }

            int currMaxLIP = 1;
            for(int[] neighbor: offsetNeighbors) {
                int newRow = row + neighbor[0], newCol = col + neighbor[1];

                if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                        && matrix[row][col] < matrix[newRow][newCol]) {
                    int len = 1 + helper2(newRow, newCol, matrix, cache, parent, ROWS, COLS);
                    if(len > currMaxLIP) {
                        currMaxLIP = len;
                        parent[row][col] = new int[]{newRow, newCol};
                    }
                }
            }

            return cache[row][col] = currMaxLIP;
        }

        public int longestIncreasingPath2(int[][] matrix) {
            if(matrix == null || matrix.length == 0) {
                return 0;
            }

            int ROWS = matrix.length, COLS = matrix[0].length, maxLIP = 0, startRow = -1, startCol = -1;
            int[][] cache = new int[ROWS][COLS];
            int[][][] parent = new int[ROWS][COLS][];

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    int len = helper2(row, col, matrix, cache, parent, ROWS, COLS);
                    if(len > maxLIP) {
                        maxLIP = len;
                        startRow = row;
                        startCol = col;
                    }
                }
            }

            List<Integer> path = new ArrayList<>();
            int r = startRow, c = startCol;

            while (parent[r][c] != null) {
                path.add(matrix[r][c]);
                int[] next = parent[r][c];
                r = next[0];
                c = next[1];
            }

            path.add(matrix[r][c]);

            System.out.println(path);
            return maxLIP;
        }


        /*
        Print the "Longest Increasing Path":
            * Walking using cache values
            * Easy to implement
            * No extra space beyond cache and the path list
            * Cons:
                - You may need to scan all neighbors each time to find the next step.
                - In some edge cases, if multiple neighbors have some cache value, it might be ambiguous
                *
         */
        private int helper1(int row, int col, int[][] matrix, int[][] cache, int ROWS, int COLS) {
            if(cache[row][col] > 0) {
                return cache[row][col];
            }

            int currMaxLIP = 1;
            for(int[] neighbor: offsetNeighbors) {
                int newRow = row + neighbor[0], newCol = col + neighbor[1];

                if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                        && matrix[row][col] < matrix[newRow][newCol]) {
                    currMaxLIP = Math.max(currMaxLIP, 1 + helper1(newRow, newCol, matrix, cache, ROWS, COLS));
                }
            }

            return cache[row][col] = currMaxLIP;
        }

        public int longestIncreasingPath1(int[][] matrix) {
            if(matrix == null || matrix.length == 0) {
                return 0;
            }

            int ROWS = matrix.length, COLS = matrix[0].length, maxLIP = 0, startRow = -1, startCol = -1;
            int[][] cache = new int[ROWS][COLS];

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    int len = helper1(row, col, matrix, cache, ROWS, COLS);
                    if(len > maxLIP) {
                        maxLIP = len;
                        startRow = row;
                        startCol = col;
                    }
                }
            }

            // Step 2: Trace the path
            List<int[]> path = new ArrayList<>();
            int currRow = startRow, currCol = startCol;
            path.add(new int[]{currRow, currCol});

            while (cache[currRow][currCol] > 1) {
                for(int[] neighbor: offsetNeighbors) {
                    int newRow = currRow + neighbor[0], newCol = currCol + neighbor[1];

                    if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                            && matrix[currRow][currCol] < matrix[newRow][newCol]
                            && cache[currRow][currCol] -1 == cache[newRow][newCol]) {
                        path.add(new int[]{newRow, newCol});
                        currRow = newRow;
                        currCol = newCol;
                        break;
                    }
                }
            }

            // Print path
            System.out.println("Longest Increasing Path Length: "+ maxLIP);
            System.out.println("Path:");
            for(int[] pos: path) {
                System.out.print(matrix[pos[0]][pos[1]]+ " ");
            }

            System.out.println();

            return maxLIP;
        }

        private static final int[][] offsetNeighbors = {{-1, 0}, {0, 1}, {1, 0},{0, -1}};

        private int helper0(int row, int col, int[][] matrix, int[][] cache, int ROWS, int COLS) {
            if(cache[row][col] != 0) {
                return cache[row][col];
            }

            int currMaxLIP = 1;
            for(int[] neighbor: offsetNeighbors) {
                int newRow = row + neighbor[0], newCol = col + neighbor[1];

                if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                        && matrix[row][col] < matrix[newRow][newCol]) {
                    currMaxLIP = Math.max(currMaxLIP, 1 + helper0(newRow, newCol, matrix, cache, ROWS, COLS));
                }
            }

            return cache[row][col] = currMaxLIP;
        }

        public int longestIncreasingPath(int[][] matrix) {
            if(matrix == null || matrix.length == 0) {
                return 0;
            }

            int ROWS = matrix.length, COLS = matrix[0].length, maxLIP = 0;
            int[][] cache = new int[ROWS][COLS];

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    maxLIP = Math.max(maxLIP, helper0(row, col, matrix, cache, ROWS, COLS));
                }
            }

            return maxLIP;
        }
    }
}
