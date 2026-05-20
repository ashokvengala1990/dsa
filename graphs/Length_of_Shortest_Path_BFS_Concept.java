package neetcode.graphs;

import java.util.LinkedList;
import java.util.Queue;

public class Length_of_Shortest_Path_BFS_Concept {
    static class Solution {
        private static final int[][] offsetNeighbors = {{-1, 0},{0,1},{1,0},{0,-1}};

        public int bfs(int[][] grid) {
            int length = 0;
            if(grid == null || grid.length == 0) {
                return length;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0});
            visited[0][0] = true;

            while (!queue.isEmpty()) {
                int queueSize = queue.size();

                for(int i =0; i < queueSize; i++) {
                    int[] curr = queue.poll();
                    int row = curr[0], col = curr[1];

                    if(row == ROWS-1 && col == COLS-1) {
                        return length;
                    }

                    for(int[] neighbor: offsetNeighbors) {
                        int newRow = row + neighbor[0], newCol = col + neighbor[1];
                        if(newRow >= 0 && newCol >= 0 && newRow < ROWS && newCol < COLS
                                && !visited[newRow][newCol] && grid[newRow][newCol] == 0) {
                            queue.offer(new int[]{newRow, newCol});
                            visited[newRow][newCol] = true;
                        }
                    }
                }

                length++;
            }

            return -1;
        }

        public static void main(String[] args) {
            Solution s = new Solution();
            int[][] grid = {{0, 0, 0, 0},{1,1,0,0},{0,0,0,1},{0,1,0,0,}};

            System.out.println(s.bfs(grid));
        }
    }
}
