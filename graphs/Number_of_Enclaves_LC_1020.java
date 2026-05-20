package neetcode.graphs;

import java.util.LinkedList;
import java.util.Queue;

/*
https://leetcode.com/problems/number-of-enclaves/description/

Same as LeetCode 130 Surrounded Regions problem.

 */

public class Number_of_Enclaves_LC_1020 {
    class Solution {
        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        public int numEnclaves(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length, countEnclaves = 0;
            boolean[][] visited = new boolean[rows][cols];
            Queue<int[]> queue = new LinkedList<>();

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if((row == 0 || row == (rows-1) || col == 0 || col == (cols-1)) && grid[row][col] == 1) {
                        queue.offer(new int[]{row, col});
                        visited[row][col] = true;
                    }
                }
            }

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currRow = curr[0], currCol = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                    if(neiRow >= 0 && neiRow < rows && neiCol >= 0 && neiCol < cols
                            && grid[neiRow][neiCol] == 1 && !visited[neiRow][neiCol]) {
                        queue.offer(new int[]{neiRow, neiCol});
                        visited[neiRow][neiCol] = true;
                    }
                }
            }

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(grid[row][col] == 1 && !visited[row][col]) {
                        countEnclaves++;
                    }
                }
            }

            return countEnclaves;
        }
    }
}
