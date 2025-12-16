package neetcode.graphs;

import java.util.*;

/*
----------------------------------------------
PROBLEM
----------------------------------------------
Find the shortest path from top-left (0,0) to bottom-right (ROWS-1, COLS-1)
in a binary grid/matrix:
- 0 = empty cell (can move)
- 1 = obstacle (cannot move)
- Allowed moves: Up, Down, Left, Right

----------------------------------------------
APPROACH
----------------------------------------------
1. Use BFS (Breadth First Search) to find the shortest path:
   - BFS guarantees shortest path in an unweighted graph/grid.
   - Each BFS level represents distance (number of moves) from the source.
2. Use a visited matrix to avoid revisiting cells.
3. Use a parent array to reconstruct the actual path after BFS.

----------------------------------------------
TIME COMPLEXITY (TC)
----------------------------------------------
- BFS traversal: O(ROWS * COLS)
  Each cell is visited at most once. Checking 4 neighbors is O(1).
- Path reconstruction: O(ROWS + COLS) in worst case
- Overall: O(ROWS * COLS)

----------------------------------------------
SPACE COMPLEXITY (SC)
----------------------------------------------
- visited[][]: O(ROWS * COLS)
- queue for BFS: up to O(ROWS * COLS)
- parent[][][]: O(ROWS * COLS)
- Overall: O(ROWS * COLS)

----------------------------------------------
REVISION NOTES
----------------------------------------------
- BFS is level-order traversal: increment path length after processing each level.
- Mark visited during enqueue to prevent duplicates.
- Use parent array to reconstruct path from destination to source.
- If destination not reachable → return -1 or empty path.



✅ Key Revision Notes for Interviews
1. BFS → guarantees shortest path in unweighted grids.
2. Mark visited during enqueue to avoid duplicate processing.
3. Parent array is used to reconstruct the actual path.
4. Path length = number of BFS levels until destination is reached.
5. Edge cases:
    - Empty grid → return 0
    - Destination blocked → return -1
*/

public class Matrix_BFS_Length_Of_Shortest_Path_Neetcode {

    static class Solution {
        // 4 possible moves: up, right, down, left
        private static final int[][] offsetNeighbors = {{-1,0},{0,1},{1,0},{0,-1}};

        // Check if the cell (r,c) is inside the grid
        private boolean isValid(int r, int c, int ROWS, int COLS) {
            return r >= 0 && r < ROWS && c >= 0 && c < COLS;
        }

        /*
         * BFS to find the actual shortest path
         * Returns list of coordinates representing path from (0,0) -> (ROWS-1, COLS-1)
         */
        public List<int[]> findPathForShortestPathInGrid(int[][] grid) {
            List<int[]> path = new ArrayList<>();
            if(grid == null || grid.length == 0) return path;

            int ROWS = grid.length, COLS = grid[0].length;
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[ROWS][COLS];

            // Parent array stores the previous cell for each visited cell
            int[][][] parent = new int[ROWS][COLS][2];

            queue.offer(new int[]{0,0});
            visited[0][0] = true;
            parent[0][0] = new int[]{-1, -1}; // start has no parent

            boolean found = false;

            // BFS traversal
            while(!queue.isEmpty() && !found) {
                int queueLength = queue.size();

                for(int i=0; i<queueLength; i++) {
                    int[] curr = queue.poll();
                    int r = curr[0], c = curr[1];

                    // If reached destination, break
                    if(r == ROWS-1 && c == COLS-1) {
                        found = true;
                        break;
                    }

                    // Explore 4 directions
                    for(int[] neighbor: offsetNeighbors) {
                        int nr = r + neighbor[0], nc = c + neighbor[1];

                        if(isValid(nr, nc, ROWS, COLS) && grid[nr][nc]==0 && !visited[nr][nc]) {
                            visited[nr][nc] = true;
                            queue.offer(new int[]{nr,nc});
                            parent[nr][nc] = new int[]{r, c}; // store parent
                        }
                    }
                }
            }

            // Reconstruct path from destination to source
            int r = ROWS-1, c = COLS-1;
            while(r != -1 && c != -1) {
                path.add(new int[]{r, c});
                int[] p = parent[r][c];
                r = p[0]; c = p[1];
            }
            Collections.reverse(path); // source -> destination
            return path;
        }

        /*
         * BFS to find only the length of shortest path
         * Returns -1 if destination not reachable
         */
        public int lengthOfShortestPathInGrid(int[][] grid) {
            if(grid == null || grid.length == 0) return 0;

            int ROWS = grid.length, COLS = grid[0].length;
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[ROWS][COLS];

            queue.offer(new int[]{0,0});
            visited[0][0] = true;
            int length = 0; // distance from start

            while(!queue.isEmpty()) {
                int queueLength = queue.size();

                for(int i=0; i<queueLength; i++) {
                    int[] curr = queue.poll();
                    int r = curr[0], c = curr[1];

                    // Reached destination
                    if(r == ROWS-1 && c == COLS-1) return length;

                    // Explore neighbors
                    for(int[] neighbor: offsetNeighbors) {
                        int nr = r + neighbor[0], nc = c + neighbor[1];
                        if(isValid(nr, nc, ROWS, COLS) && grid[nr][nc]==0 && !visited[nr][nc]) {
                            visited[nr][nc] = true; // mark visited during enqueue
                            queue.offer(new int[]{nr,nc});
                        }
                    }
                }
                length++; // increment distance after each BFS level
            }
            return -1; // destination not reachable
        }
    }

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0, 0},
                {1, 1, 0, 0},
                {0, 0, 0, 1},
                {0, 1, 0, 0}
        };

        Solution s = new Solution();

        // Get path
        List<int[]> path = s.findPathForShortestPathInGrid(grid);
        System.out.print("Shortest Path: ");
        for(int[] cell: path) {
            System.out.print("["+cell[0]+","+cell[1]+"] ");
        }
        System.out.println();

        // Get length
        int len = s.lengthOfShortestPathInGrid(grid);
        System.out.println("Length of Shortest Path: " + len);
    }
}
