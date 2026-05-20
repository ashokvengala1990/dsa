package neetcode.graphs;

import java.util.*;

/*
Goal (LC 694 – Number of Distinct Islands):
Count how many different island shapes exist in a binary grid (1 = land). Two islands are the same shape if
one can be translated (shifted) to match the other (no rotate/reflect).

Key idea: “Normalize” an island shape:
When you discover a new island at (baseRow, baseCol), represent every land cell (r, c) in that island as a relative coordinate:
    - (r - baseRow, c - baseCol)
This makes the shape independent of its absolute position in the grid.

Important detail: ordering / determinism:
In your code you store List<String> as the signature. That works only if traversal produces the same relative-point order for the same shape.
- With BFS/DFS + fixed neighbor order (up, right, down, left) this is usually consistent.
- However, in some edge cases, different traversal orders could generate different lists for the same set of points.
- Most robust approach: store the relative points and then sort them (or use a canonical “path encoding”). I’ll keep your
approach but add a comment where sorting could be added for safety.

Data structures:
- visited[r][c] prevents revisiting cells.
- Set<List<String>> stores unique island signatures.

Time Complexity (TC):
Let R = rows, C = cols, N = R * C.
- Each cell is visited at most once → O(R*C).
- For each visited land cell, we check 4 neighbors → still O(R*C) total.

TC = O(R*C):
- If you add “sort the component list” per island for canonical signature, then TC becomes
O(R*C + Σ(kᵢ log kᵢ)), where kᵢ = size of island i. Worst-case O(RC log(RC)).

Space Complexity (SC):
- visited array: O(R*C)
- BFS queue / DFS stack / recursion stack: O(size of largest island) → worst-case O(R*C)
- Set of signatures: stores all island relative coordinates total across grid → worst-case O(R*C)

SC = O(R*C)
 */

/*
LC 694 - Number of Distinct Islands

Idea:
- For each unvisited land cell, traverse its island (BFS/DFS).
- Build a "shape signature" using relative coordinates (r - baseR, c - baseC).
- Put the signature into a HashSet; set size = # distinct island shapes.

Why relative coords?
- Translation-invariant: same shape anywhere in the grid produces same relative points.
*/
public class Number_of_Distinct_Islands_LC_694 {
    class Solution {
        // Fixed neighbor order is important for deterministic traversal order
        private static final int[][] offsetNeighbors = {
                {-1, 0}, // up
                {0, 1},  // right
                {1, 0},  // down
                {0, -1}  // left
        };

        /*
        BFS island traversal.
        baseRow/baseCol = starting cell of this island -> used for normalization.
        component collects normalized points like "0-0", "0-1", "1-0", ...
        */
        private void bfsUsingQueue(int row, int col,
                                   int baseRow, int baseCol,
                                   int[][] grid, boolean[][] visited,
                                   List<String> component,
                                   int rows, int cols) {

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{row, col});
            visited[row][col] = true;

            // Add normalized coordinate for the starting cell (will be "0-0")
            component.add((row - baseRow) + "-" + (col - baseCol));

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currRow = curr[0], currCol = curr[1];

                // Explore 4-direction neighbors
                for (int[] neighbor : offsetNeighbors) {
                    int neiRow = currRow + neighbor[0];
                    int neiCol = currCol + neighbor[1];

                    // Validate bounds + land + not visited
                    if (neiRow >= 0 && neiRow < rows &&
                            neiCol >= 0 && neiCol < cols &&
                            grid[neiRow][neiCol] == 1 &&
                            !visited[neiRow][neiCol]) {

                        visited[neiRow][neiCol] = true;
                        queue.offer(new int[]{neiRow, neiCol});

                        // Normalize neighbor coordinate w.r.t island's base cell
                        component.add((neiRow - baseRow) + "-" + (neiCol - baseCol));
                    }
                }
            }
        }

        /*
        Main method (BFS version).
        TC: O(R*C)
        SC: O(R*C)
        */
        public int numDistinctIslands2(int[][] grid) {
            if (grid == null || grid.length == 0) return -1;

            int rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];

            // Stores unique island "shape signatures"
            Set<List<String>> result = new HashSet<>();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    // Start a new island traversal
                    if (grid[r][c] == 1 && !visited[r][c]) {
                        List<String> component = new ArrayList<>();
                        bfsUsingQueue(r, c, r, c, grid, visited, component, rows, cols);

                        /*
                        Optional robustness improvement:
                        Sort so signature doesn't depend on traversal order.
                        Collections.sort(component);
                        */

                        result.add(component);
                    }
                }
            }

            return result.size();
        }

        /*
        Iterative DFS using stack (same signature idea).
        Note: java.util.Stack is legacy; ArrayDeque is preferred for stack behavior.
        */
        private void dfsUsingStack(int row, int col,
                                   int baseRow, int baseCol,
                                   int[][] grid, boolean[][] visited,
                                   List<String> component,
                                   int rows, int cols) {

            Stack<int[]> stack = new Stack<>();
            stack.push(new int[]{row, col});
            visited[row][col] = true;
            component.add((row - baseRow) + "-" + (col - baseCol));

            while (!stack.isEmpty()) {
                int[] curr = stack.pop();
                int currRow = curr[0], currCol = curr[1];

                for (int[] neighbor : offsetNeighbors) {
                    int neiRow = currRow + neighbor[0];
                    int neiCol = currCol + neighbor[1];

                    if (neiRow >= 0 && neiRow < rows &&
                            neiCol >= 0 && neiCol < cols &&
                            grid[neiRow][neiCol] == 1 &&
                            !visited[neiRow][neiCol]) {

                        visited[neiRow][neiCol] = true;
                        stack.push(new int[]{neiRow, neiCol});
                        component.add((neiRow - baseRow) + "-" + (neiCol - baseCol));
                    }
                }
            }
        }

        public int numDistinctIslands1(int[][] grid) {
            if (grid == null || grid.length == 0) return -1;

            int rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];
            Set<List<String>> result = new HashSet<>();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 1 && !visited[r][c]) {
                        List<String> component = new ArrayList<>();
                        dfsUsingStack(r, c, r, c, grid, visited, component, rows, cols);

                        // Optional: Collections.sort(component);
                        result.add(component);
                    }
                }
            }
            return result.size();
        }

        /*
        Recursive DFS.
        Risk: recursion depth can be O(R*C) -> stack overflow for big grids.
        */
        private void dfsUsingRecursion(int row, int col,
                                       int baseRow, int baseCol,
                                       int[][] grid, boolean[][] visited,
                                       List<String> component,
                                       int rows, int cols) {

            visited[row][col] = true;
            component.add((row - baseRow) + "-" + (col - baseCol));

            for (int[] neighbor : offsetNeighbors) {
                int neiRow = row + neighbor[0];
                int neiCol = col + neighbor[1];

                if (neiRow >= 0 && neiRow < rows &&
                        neiCol >= 0 && neiCol < cols &&
                        grid[neiRow][neiCol] == 1 &&
                        !visited[neiRow][neiCol]) {

                    dfsUsingRecursion(neiRow, neiCol, baseRow, baseCol,
                            grid, visited, component, rows, cols);
                }
            }
        }

        public int numDistinctIslands0(int[][] grid) {
            if (grid == null || grid.length == 0) return -1;

            int rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];
            Set<List<String>> result = new HashSet<>();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 1 && !visited[r][c]) {
                        List<String> component = new ArrayList<>();
                        dfsUsingRecursion(r, c, r, c, grid, visited, component, rows, cols);

                        // Optional: Collections.sort(component);
                        result.add(component);
                    }
                }
            }
            return result.size();
        }
    }
}