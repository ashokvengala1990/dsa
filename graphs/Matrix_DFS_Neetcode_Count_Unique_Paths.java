package neetcode.graphs;

import java.util.ArrayList;
import java.util.List;

/*
Q: Count and print all unique paths from the top-left (0,0)
   to the bottom-right (ROWS-1, COLS-1) of a matrix grid.

Rules:
- You can move in 4 directions: up, right, down, left.
- You can move only on 0s (free cells).
- You cannot visit the same cell more than once in a path.
- Return both:
   1. The total number of unique paths.
   2. The list of all unique paths (each path is a list of coordinates).
*/

public class Matrix_DFS_Neetcode_Count_Unique_Paths {
    static class Solution {

        // All 4 possible movements (up, right, down, left)
        private static final int[][] offsetNeighbors = {{-1,0},{0,1},{1,0},{0,-1}};

        /* --------------------------------------------------------------------------------
         *  METHOD 1: DFS + BACKTRACKING TO PRINT ALL UNIQUE PATHS
         * --------------------------------------------------------------------------------
         *  r, c -> current cell position
         *  currPath -> stores the path being built
         *  allPaths -> stores all valid paths found
         * -------------------------------------------------------------------------------- */
        private void dfs1(int r, int c, int[][] grid, int ROWS, int COLS,
                          boolean[][] visited, List<int[]> currPath, List<List<int[]>> allPaths) {

            // Boundary + obstacle + already visited check
            if (r < 0 || r >= ROWS || c < 0 || c >= COLS || visited[r][c] || grid[r][c] == 1) {
                return;
            }

            // ✅ Found a valid path (reached bottom-right)
            if (r == ROWS - 1 && c == COLS - 1) {
                List<int[]> currRes = new ArrayList<>(currPath);
                currRes.add(new int[]{r, c}); // add destination
                allPaths.add(currRes);
                return;
            }

            // Mark cell as visited and add to current path
            visited[r][c] = true;
            currPath.add(new int[]{r, c});

            // Explore all 4 neighbors recursively
            for (int[] neighbor : offsetNeighbors) {
                int nr = r + neighbor[0], nc = c + neighbor[1];
                dfs1(nr, nc, grid, ROWS, COLS, visited, currPath, allPaths);
            }

            // Backtrack: unmark cell and remove last coordinate from path
            currPath.remove(currPath.size() - 1);
            visited[r][c] = false;
        }

        // Public function to find all unique paths
        public List<List<int[]>> findUniquePaths0(int[][] grid) {
            List<List<int[]>> allPaths = new ArrayList<>();
            if (grid == null || grid.length == 0) {
                return allPaths;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];
            List<int[]> currPath = new ArrayList<>();

            dfs1(0, 0, grid, ROWS, COLS, visited, currPath, allPaths);

            return allPaths;
        }

        /* --------------------------------------------------------------------------------
         *  METHOD 2: DFS + BACKTRACKING TO COUNT ONLY TOTAL UNIQUE PATHS
         * -------------------------------------------------------------------------------- */
        private int dfs0(int r, int c, int[][] grid, int ROWS, int COLS, boolean[][] visited) {
            if (r < 0 || r >= ROWS || c < 0 || c >= COLS || visited[r][c] || grid[r][c] == 1) {
                return 0;
            } else if (r == (ROWS - 1) && c == (COLS - 1)) {
                return 1; // Found a valid path
            }

            int count = 0;
            visited[r][c] = true;

            for (int[] neighbor : offsetNeighbors) {
                int nr = r + neighbor[0], nc = c + neighbor[1];
                count += dfs0(nr, nc, grid, ROWS, COLS, visited);
            }

            visited[r][c] = false; // backtrack
            return count;
        }

        public int countUniquePaths0(int[][] grid) {
            if (grid == null || grid.length == 0) {
                return 0;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];

            return dfs0(0, 0, grid, ROWS, COLS, visited);
        }
    }

    // -----------------------------------------------
    // Driver Code
    // -----------------------------------------------
    public static void main(String[] args) {
        Solution s = new Solution();

        int[][] grid = {
                {0, 0, 0, 0},
                {1, 1, 0, 0},
                {0, 0, 0, 1},
                {0, 1, 0, 0}
        };

        // Count total unique paths
        System.out.println("Total Unique Paths: " + s.countUniquePaths0(grid));
        System.out.println();

        // Print all unique paths
        List<List<int[]>> allPaths = s.findUniquePaths0(grid);
        System.out.println("All Unique Paths:");
        for (List<int[]> path : allPaths) {
            for (int[] cell : path) {
                System.out.print("(" + cell[0] + "," + cell[1] + ") ");
            }
            System.out.println();
        }
    }
}

/*
--------------------------------
🧭 REVISION NOTES
--------------------------------
1️⃣ Type: DFS + Backtracking
2️⃣ Moves allowed: Up, Right, Down, Left
3️⃣ Avoid revisiting the same cell (use visited[][])
4️⃣ Obstacle: cell with value 1 cannot be visited
5️⃣ Base cases:
     - Out of bounds / visited / obstacle → stop path
     - Destination reached → record or count path
6️⃣ Backtrack after exploring each direction (unmark visited)

--------------------------------
⏱️ TIME COMPLEXITY
--------------------------------
O(4^(M*N)) in the worst case (when all cells = 0, i.e., open paths)
→ Each cell can go in 4 directions.

--------------------------------
💾 SPACE COMPLEXITY
--------------------------------
O(M*N) for recursion stack + visited[][] + path tracking

--------------------------------
✅ SUMMARY
--------------------------------
- `countUniquePaths0()` → returns total number of valid paths.
- `findUniquePaths0()` → prints all valid unique paths.
- Both use DFS + backtracking, differing only in how results are collected.
*/
