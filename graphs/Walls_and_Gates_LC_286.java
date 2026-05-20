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
    class Revision01 {
        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};
        private static final int WALL = -1, GATE = 0, EMPTY_ROOM = 2147483647;

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        public void wallsAndGates(int[][] gates) {
            if(gates == null || gates.length == 0) {
                return;
            }

            int rows = gates.length, cols = gates[0].length, distance = 1;
            Queue<int[]> queue = new LinkedList<>();

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(gates[r][c] == GATE) {
                        queue.offer(new int[]{r, c});
                    }
                }
            }

            while (!queue.isEmpty()) {
                int levelSize = queue.size();

                for(int i = 0; i < levelSize; i++) {
                    int[] curr = queue.poll();
                    int r = curr[0], c = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int nr = r + neighbor[0], nc = c + neighbor[1];

                        if(isValid(nr, nc, rows, cols) && gates[nr][nc] == EMPTY_ROOM) {
                            gates[nr][nc] = distance;
                            queue.offer(new int[]{nr, nc});
                        }
                    }
                }

                distance++;
            }
        }
    }

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
