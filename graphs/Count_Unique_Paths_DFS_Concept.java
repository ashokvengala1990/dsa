package neetcode.graphs;

public class Count_Unique_Paths_DFS_Concept {
    static class Solution {
        private static int[][] offsetNeighbors = {{-1,0},{0,1},{1,0},{0,-1}};

        private int dfs(int row, int col, int ROWS, int COLS, int[][] grid, boolean[][] visited) {
            if(row < 0 || col < 0 || row >= ROWS || col >= COLS || visited[row][col] || grid[row][col] == 1) {
                return 0;
            } else if(row == ROWS-1 && col == COLS-1) {
                return 1;
            }

            visited[row][col] = true;

            int count = 0;

            for(int[] neighbor: offsetNeighbors) {
                int newRow = row + neighbor[0], newCol = col + neighbor[1];
                count += dfs(newRow, newCol, ROWS, COLS, grid, visited);
            }

            visited[row][col] = false;

            return count;
        }

        public int countUniquePaths(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];

            return dfs(0,0, ROWS, COLS,grid, visited);
        }

        public static void main(String[] args) {
            int[][] matrix = {{0, 0, 0, 0}, {1,1,0,0},{0,0,0,1},{0,1,0,0}};

            Solution s = new Solution();
            System.out.println(s.countUniquePaths(matrix));
        }
    }
}
