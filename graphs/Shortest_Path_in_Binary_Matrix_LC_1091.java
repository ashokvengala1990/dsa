package neetcode.graphs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Shortest_Path_in_Binary_Matrix_LC_1091 {
    class Solution {
        private static final int[][] offsetNeighbors = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};

        public int shortestPathBinaryMatrixWithPath(int[][] grid) {
            if(grid == null || grid.length == 0 || grid[0][0] == 1 || grid[grid.length-1][grid[0].length-1] == 1) {
                System.out.println("No Path exists");
                return -1;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];
            Queue<int[]> queue = new LinkedList<>();
            Map<String, String> childToParentPath = new HashMap<>();
            queue.offer(new int[]{0,0});
            visited[0][0] = true;
            childToParentPath.put("0-0",null);
            int length = 1;

            while (!queue.isEmpty()) {
                int queueSize = queue.size();

                for(int i = 0 ; i < queueSize; i++) {
                    int[] curr = queue.poll();
                    int currRow = curr[0], currCol = curr[1];

                    if(currRow == ROWS - 1 && currCol == COLS - 1) {
                        LinkedList<String> result = new LinkedList<>();
                        String target = (ROWS-1) +"-"+(COLS-1);
                        while (childToParentPath.containsKey(target)) {
                            result.addFirst(target);
                            target = childToParentPath.get(target);
                        }
                        System.out.println(result);
                        return length;
                    }

                    for(int[] neighbor: offsetNeighbors) {
                        int newRow = currRow + neighbor[0], newCol = currCol + neighbor[1];

                        if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && grid[newRow][newCol] == 0 && !visited[newRow][newCol]) {
                            childToParentPath.put(newRow+"-"+newCol,currRow+"-"+currCol);
                            visited[newRow][newCol] = true;
                            queue.offer(new int[]{newRow, newCol});
                        }
                    }
                }
                length++;
            }

            return -1;
        }

        public int shortestPathBinaryMatrix(int[][] grid) {
            if(grid == null || grid.length == 0 || grid[0][0] == 1 || grid[grid.length-1][grid[0].length-1] == 1) {
                return -1;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0});
            boolean[][] visited = new boolean[ROWS][COLS];
            visited[0][0] = true;
            int length = 1;

            while (!queue.isEmpty()) {
                int queueSize = queue.size();

                for(int i = 0; i < queueSize; i++) {
                    int[] curr = queue.poll();
                    int row = curr[0], col = curr[1];

                    if(row == ROWS-1 && col == COLS-1) {
                        return length;
                    }

                    for(int[] neighbor: offsetNeighbors) {
                        int newRow = row + neighbor[0], newCol = col + neighbor[1];

                        if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && grid[newRow][newCol] == 0 && !visited[newRow][newCol]) {
                            queue.offer(new int[]{newRow, newCol});
                            visited[newRow][newCol] = true;
                        }
                    }
                }

                length++;
            }

            return -1;
        }
    }
}
