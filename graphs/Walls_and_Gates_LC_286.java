package neetcode.graphs;

import java.util.LinkedList;
import java.util.Queue;

/*
https://leetcode.com/problems/walls-and-gates/description/

- Using BFS, Queue and Visited Array
    * TC: O(N*M)
    * SC: O(N*M) because of visited and queue data structures
 */

public class Walls_and_Gates_LC_286 {
    class Solution {
        private static final int[][] offsetDirections = {{-1,0},{0,1},{1,0},{0,-1}};

        public void wallsAndGates(int[][] rooms) {
            if(rooms == null || rooms.length == 0) {
                return;
            }

            int ROWS = rooms.length, COLS = rooms[0].length;
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[ROWS][COLS];

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    if(rooms[row][col] == 0) {
                        visited[row][col] = true;
                        queue.offer(new int[]{row, col});
                    }
                }
            }

            int distance = 0;

            while (!queue.isEmpty()) {
                int levelSize = queue.size();

                for(int i = 0; i < levelSize; i++) {
                    int[] curr = queue.poll();
                    int row = curr[0], col = curr[1];
                    rooms[row][col] = distance;

                    for(int[] neighbor: offsetDirections) {
                        int newRow = row + neighbor[0], newCol = col + neighbor[1];

                        if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                                && rooms[newRow][newCol] != -1 && !visited[newRow][newCol]) {
                            visited[newRow][newCol] = true;
                            queue.offer(new int[]{newRow, newCol});
                        }
                    }
                }

                distance++;
            }
        }
    }
}
