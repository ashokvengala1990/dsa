package neetcode.graphs;

import java.util.LinkedList;
import java.util.Queue;

/*
https://leetcode.com/problems/map-of-highest-peak/description/
Same as Leetcode 542 problem

 */

public class Map_of_Highest_Peak_LC_1765 {
    class Solution {
        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        public int[][] highestPeak(int[][] isWater) {
            if(isWater == null || isWater.length == 0) {
                return new int[][]{};
            }

            int rows = isWater.length, cols = isWater[0].length, level = 0;
            int[][] distance = new int[rows][cols];
            Queue<int[]> queue = new LinkedList<>();

            for(int row = 0; row < rows; row++) {
                for(int col = 0;  col < cols; col++) {
                    if(isWater[row][col] == 1) {
                        queue.offer(new int[]{row, col});
                        distance[row][col] = level;
                    }
                }
            }
            level++;

            while (!queue.isEmpty()) {
                int currLevelSize = queue.size();

                for(int i = 0; i < currLevelSize; i++) {
                    int[] curr = queue.poll();
                    int row = curr[0], col = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                        if(isValid(neiRow, neiCol, rows, cols)
                                && isWater[neiRow][neiCol] == 0
                                && distance[neiRow][neiCol] == 0) {
                            distance[neiRow][neiCol] = level;
                            queue.offer(new int[]{neiRow, neiCol});
                        }
                    }
                }
                level++;
            }

            return distance;
        }
    }
}
