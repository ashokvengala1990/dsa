package neetcode.graphs;

import lld.sanket.Patterns.BuilderPattern.Assignment.PartA.Itinerary;

import java.util.*;

/*

https://leetcode.com/problems/shortest-path-in-binary-matrix/description/
https://www.geeksforgeeks.org/problems/shortest-path-in-a-binary-maze-1655453161/1

Revision Summary (Important)
Algorithm:
- BFS (level-order traversal)
- Each BFS level = path length
- First time reaching destination = shortest path

Why BFS?
- Unweighted grid
- BFS guarantees shortest path

Time Complexity:
- TC: O(rows × cols)
    * Each cell visited once, 8 neighbors max

Space Complexity
- SC: O(rows × cols)
    * visited + queue
    * Extra Map only when path printing is needed

Key Notes (remember):
- Mark visited when adding to queue
- Use currQueueLength to track BFS levels
- Path reconstruction = store child → parent
- Diagonal movement enabled via offsetNeighbors


===> GeeksForGeeks: https://www.geeksforgeeks.org/problems/shortest-path-in-a-binary-maze-1655453161/1
shortestPathBinaryMatrix2 is to solve for generic source and destination, where shortestPathBinaryMatrix1 and shortestPathBinaryMatrix0 are specific
source and destination.

Key Notes for Revision:
1. BFS + Queue → first time we visit a cell = shortest path to that cell.
2. Check destination during push (when adding neighbor to queue) → safe in BFS.
3. Level-order BFS → length incremented after finishing each layer.
4. 8-direction moves → diagonal moves allowed in LeetCode 1091.
5. Visited matrix prevents revisiting cells → O(m*n) nodes processed at most once.
6. Edge cases:
    - Start/end blocked → return -1
    - Source = destination → return 1 (includes the start itself)

⏱ Time Complexity (TC)
- BFS visits each cell at most once → O(m*n), m = rows, n = cols

💾 Space Complexity (SC)
- Queue + visited matrix → O(m*n)

 */

public class Shortest_Path_in_Binary_Matrix_LC_1091 {
    class Revision02 {
        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1},{-1,-1},{-1,1},{1,-1},{1,1}};

        private boolean isValidCell(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        private void printPath(Map<String, String> childToParentMap, int rows, int cols) {
            String target = (rows-1)+"-"+(cols-1);

            List<String> path = new ArrayList<>();

            while (target != null && childToParentMap.containsKey(target)) {
                path.add(target);
                target = childToParentMap.get(target);
            }

            Collections.reverse(path);
            System.out.println(path);
        }

        public int shortestPathBinaryMatrix1(int[][] grid) {
            if(grid == null || grid.length == 0 || grid[0][0] == 1 || grid[grid.length-1][grid[0].length-1] == 1) {
                return -1;
            }

            int rows = grid.length, cols = grid[0].length;
            if(rows == 1 && cols == 1) {
                return 1;
            }

            boolean[][] visited = new boolean[rows][cols];
            Queue<int[]> queue = new LinkedList<>();
            Map<String, String> childToParentMap = new HashMap<>();
            queue.offer(new int[]{0, 0});
            visited[0][0] = true;
            int distance = 1;


            while (!queue.isEmpty()) {
                int currLevelSize = queue.size();

                for(int i = 0; i < currLevelSize; i++) {
                    int[] curr = queue.poll();
                    int currRow = curr[0], currCol = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                        if(isValidCell(neiRow, neiCol, rows, cols) && grid[neiRow][neiCol] == 0 && !visited[neiRow][neiCol]) {
                            visited[neiRow][neiCol] = true;
                            childToParentMap.put(neiRow+"-"+neiCol, currRow+"-"+currCol);

                            if(neiRow == rows-1 && neiCol == cols-1) {
                                printPath(childToParentMap, rows, cols);
                                return distance+1;
                            }

                            queue.offer(new int[]{neiRow, neiCol});
                        }
                    }
                }

                distance++;
            }

            return -1;
        }

        public int shortestPathBinaryMatrix0(int[][] grid) {
            if(grid == null || grid.length == 0 || grid[0][0] == 1 || grid[grid.length-1][grid[0].length-1] == 1) {
                return -1;
            }

            int rows = grid.length, cols = grid[0].length;
            if(rows == 1 && cols == 1) {
                return 1;
            }

            boolean[][] visited = new boolean[rows][cols];
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0});
            visited[0][0] = true;
            int distance = 1;

            while (!queue.isEmpty()) {
                int currLevelSize = queue.size();

                for(int i = 0; i < currLevelSize; i++) {
                    int[] curr = queue.poll();
                    int currRow = curr[0], currCol = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                        if(isValidCell(neiRow, neiCol, rows, cols)
                                && grid[neiRow][neiCol] == 0
                                && !visited[neiRow][neiCol]) {
                            visited[neiRow][neiCol] = true;

                            if(neiRow == rows-1 && neiCol == cols-1) {
                                return distance+1;
                            }

                            queue.offer(new int[]{neiRow, neiCol});
                        }
                    }
                }

                distance++;
            }

            return -1;
        }
    }

    class Revision01 {
        /**
         * Helper method to find shortest path in a binary grid from source -> destination
         * BFS approach (level-order)
         *
         * @param grid       binary grid (0 = open, 1 = blocked)
         * @param source     starting cell {row, col}
         * @param destination target cell {row, col}
         * @return minimum number of steps to reach destination, -1 if impossible
         *
         * TC: O(m*n) → each cell visited at most once
         * SC: O(m*n) → queue + visited matrix
         */
        private int shortestPathBinaryMazeGFG(int[][] grid, int[] source, int[] destination) {
            // Edge cases: invalid grid, blocked start/end
            if(grid == null || grid.length == 0 || grid[0].length == 0
                    || source == null || source.length == 0 || grid[source[0]][source[1]] == 1
                    || destination == null || destination.length == 0 || grid[destination[0]][destination[1]] == 1) {
                return -1;
            }
            // Source is already destination
            else if(source[0] == destination[0] && source[1] == destination[1]) {
                return 1; // path length = 1 (start itself)
            }

            // 8 possible moves (including diagonals)
            final int[][] offsetNeighbors = {
                    {-1, 0},{0, 1},{1, 0},{0, -1},
                    {-1,-1},{1,1},{-1,1},{1,-1}
            };

            int rows = grid.length, cols = grid[0].length;
            int length = 1; // initial path length
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{source[0], source[1]});

            boolean[][] visited = new boolean[rows][cols];
            visited[source[0]][source[1]] = true;

            // BFS traversal
            while (!queue.isEmpty()) {
                int currLevelSize = queue.size(); // number of nodes at current BFS level

                for(int i = 0; i < currLevelSize; i++) {
                    int[] curr = queue.poll();
                    int currRow = curr[0], currCol = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int neiRow = currRow + neighbor[0];
                        int neiCol = currCol + neighbor[1];

                        // valid, unvisited, open cell
                        if(isValid(neiRow, neiCol, rows, cols) && grid[neiRow][neiCol] == 0
                                && !visited[neiRow][neiCol]) {
                            visited[neiRow][neiCol] = true;

                            // Destination reached → return path length
                            if(neiRow == destination[0] && neiCol == destination[1]) {
                                return length + 1;
                            }

                            queue.offer(new int[]{neiRow, neiCol});
                        }
                    }
                }

                length++; // increase level after finishing current BFS layer
            }

            return -1; // destination unreachable
        }

        /**
         * Main method to call for LeetCode 1091 style
         *
         * @param grid binary matrix
         * @return shortest path length from top-left to bottom-right
         */
        public int shortestPathBinaryMatrix2(int[][] grid) {
            if(grid == null || grid.length == 0 || grid[0].length == 0) {
                return -1;
            }
            return shortestPathBinaryMazeGFG(
                    grid,
                    new int[]{0, 0},
                    new int[]{grid.length - 1, grid[0].length - 1}
            );
        }

        // 8-direction movement (up, right, down, left + 4 diagonals)
        private static final int[][] offsetNeighbors = {
                {-1, 0}, {0, 1}, {1, 0}, {0, -1},
                {1, 1}, {-1, -1}, {-1, 1}, {1, -1}
        };

        // Bounds check helper
        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        // Reconstruct path using child -> parent mapping
// Used only when path printing is required
        private void printPath(int rows, int cols, Map<String, String> childToParentMap) {
            String currCell = (rows - 1) + "-" + (cols - 1);
            List<String> result = new ArrayList<>();

            // Traverse back until source (parent = null)
            while (currCell != null) {
                result.add(currCell);
                currCell = childToParentMap.get(currCell);
            }

            Collections.reverse(result); // reverse to get src -> dest
            System.out.println(result);
        }

        // BFS with path reconstruction
        public int shortestPathBinaryMatrix1(int[][] grid) {
            // Invalid start/end
            if (grid == null || grid.length == 0 ||
                    grid[0][0] == 1 || grid[grid.length - 1][grid[0].length - 1] == 1) {
                return -1;
            }

            int rows = grid.length, cols = grid[0].length;
            int length = 1; // BFS level = path length

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0});

            boolean[][] visited = new boolean[rows][cols];
            visited[0][0] = true;

            // Stores parent of each cell for path reconstruction
            Map<String, String> childToParentMap = new HashMap<>();
            childToParentMap.put("0-0", null);

            // Standard BFS level order traversal
            while (!queue.isEmpty()) {
                int currQueueLength = queue.size();

                for (int i = 0; i < currQueueLength; i++) {
                    int[] curr = queue.poll();
                    int currRow = curr[0], currCol = curr[1];

                    // Destination reached
                    if (currRow == rows - 1 && currCol == cols - 1) {
                        printPath(rows, cols, childToParentMap);
                        return length;
                    }

                    // Explore all 8 neighbors
                    for (int[] neighbor : offsetNeighbors) {
                        int neiRow = currRow + neighbor[0];
                        int neiCol = currCol + neighbor[1];

                        if (isValid(neiRow, neiCol, rows, cols)
                                && grid[neiRow][neiCol] == 0
                                && !visited[neiRow][neiCol]) {

                            queue.offer(new int[]{neiRow, neiCol});
                            visited[neiRow][neiCol] = true;

                            // Record parent for path reconstruction
                            childToParentMap.put(neiRow + "-" + neiCol,
                                    currRow + "-" + currCol);
                        }
                    }
                }
                length++; // increment after finishing one BFS level
            }
            return -1;
        }

        // BFS without path reconstruction (only shortest length)
        public int shortestPathBinaryMatrix0(int[][] grid) {
            if (grid == null || grid.length == 0 ||
                    grid[0][0] == 1 || grid[grid.length - 1][grid[0].length - 1] == 1) {
                return -1;
            }

            int rows = grid.length, cols = grid[0].length;
            int length = 1;

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0});

            boolean[][] visited = new boolean[rows][cols];
            visited[0][0] = true;

            while (!queue.isEmpty()) {
                int currQueueLength = queue.size();

                for (int i = 0; i < currQueueLength; i++) {
                    int[] curr = queue.poll();
                    int currRow = curr[0], currCol = curr[1];

                    if (currRow == rows - 1 && currCol == cols - 1) {
                        return length;
                    }

                    for (int[] neighbor : offsetNeighbors) {
                        int neiRow = currRow + neighbor[0];
                        int neiCol = currCol + neighbor[1];

                        if (isValid(neiRow, neiCol, rows, cols)
                                && grid[neiRow][neiCol] == 0
                                && !visited[neiRow][neiCol]) {

                            queue.offer(new int[]{neiRow, neiCol});
                            visited[neiRow][neiCol] = true;
                        }
                    }
                }
                length++;
            }
            return -1;
        }
    }

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
