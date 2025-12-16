package neetcode.graphs;

import java.util.*;

/*
“What is the minimum water level required so that there exists a valid path from start to end?”
or it can asked like Finding the minimum ‘maximum elevation’ along any path from (0,0) to (n-1,n-1).

     * Problem: Leetcode 778 - Swim in Rising Water
     *
     * Goal:
     * Find the minimum time (t) when you can swim from top-left to bottom-right cell.
     * Water level = t at time t → you can move into any cell whose elevation <= t.
     *
     * Approach:
     * Use Dijkstra's algorithm (min-heap) to always expand the path with the minimum
     * "maximum elevation" seen so far. The cost of a path = max(elevation along path).
     *
     * Each heap element stores: {time (max elevation so far), row, col}

1. Dijkstra / Min-Heap Approach (PriorityQueue)
- Idea: Always expand the cell with the minimum "maximum elevation so far" along the path.
- Heap Element: {currentMaxElevation, row, col}
- Steps:
    1) Push start cell (0,0) into min-heap.
    2) Pop the cell with lowest currentMaxElevation.
    3) Update currentMaxElevation = max(currElevation, neighborElevation).
    4) Stop when bottom-right is reached → currentMaxElevation = min time to swim.
- Time Complexity: O(n^2 log n) (all n² cells, each push/pop in log n²)
- Space Complexity: O(n^2) (heap + visited matrix)
- Good For: Real-time “rising water” simulation, straightforward implementation.

2. Kruskal + Union-Find Approach
- Idea: Treat each cell as a node, each neighbor pair as an edge with weight = max(cell1, cell2).
- Steps:
    1) Convert 2D grid → 1D node IDs: id = r * n + c.
    2) Build edges: edge = {weight=max(cell elevations), u, v}.
    3) Sort edges by weight.
    4) Union nodes in increasing weight order.
    5) The moment start (0) and end (n*n-1) are connected → weight = min time.
- Time Complexity: O(n^2 log n) (edge sorting dominates)
- Space Complexity: O(n^2) (edges + Union-Find arrays)
- Good For: Understanding minimum spanning tree interpretation of the problem.

3. Binary Search + BFS
- Idea: Water level = time t. Reachability is monotonic: if we can reach at t, we can reach at any t' > t.
- Steps:
    1) Binary search time t from grid[0][0] → n^2-1.
    2) For mid t, do BFS/DFS to see if (n-1, n-1) is reachable.
    3) If reachable → try smaller t; else → increase t.
- Time Complexity: O(n^2 log(n^2)) ≈ O(n^2 log n) (binary search × BFS)
- Space Complexity: O(n^2) (visited + BFS queue)
- Good For: Using monotonic property of water level; easy to explain in interviews.

4. Prim’s-like / Dijkstra MST-style
- Idea: Similar to Dijkstra, but interpret grid as graph where moving to a neighbor is an “edge” weighted by neighbor elevation.
- Steps:
    1) Start from (0,0).
    2) Use a min-heap to pick the next cell with lowest elevation.
    3) Keep track of max elevation so far on the path.
    4) Stop when (n-1,n-1) is reached → max elevation = min time.
- Time Complexity: O(n^2 log n)
- Space Complexity: O(n^2)
- Good For: Visualizing Prim’s MST analogy on a grid.



*/

public class Swim_in_Rising_Water_LC_778 {
    public class Solution {
        private List<int[]> reconstructPath(int[][] parent, int n) {
            int r = n-1, c = n-1;

            List<int[]> path = new ArrayList<>();

            while (r != -1 && c != -1) {
                path.add(new int[]{r, c});
                int encoded = parent[r][c];
                if(encoded == -1) break;
                r = encoded / n;
                c = encoded % n;
            }

            Collections.reverse(path);

            return path;
        }

        public int swimInWaterPrintPath5(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return -1;
            }

            int n = grid.length;
            boolean[][] visited = new boolean[n][n];
            int[][] parent = new int[n][n];
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] -b[0]);
            minHeap.offer(new int[]{grid[0][0], -1, 0, 0});
            int time = 0;

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int wt = curr[0], pid = curr[1], r = curr[2], c = curr[3];
                time = Math.max(time, wt);

                if(r == (n-1) && c == (n-1)) {
                    reconstructPath(parent, n);
                    return time;
                } else if(visited[r][c]) {
                    continue;
                }
                visited[r][c] = true;
                parent[r][c] = pid;

                for(int[] neighbor: offsetNeighbors) {
                    int nr = r + neighbor[0], nc = c + neighbor[1];
                    if(isValid(nr, nc, n, n) && !visited[nr][nc]) {
                        minHeap.offer(new int[]{grid[nr][nc], r * n + c, nr, nc});
                    }
                }
            }

            return -1;
        }

        // Union-Find data structure to track connected cells
        class UnionFind {
            int[] parent, size;

            // Initialize Union-Find for n nodes
            UnionFind(int n) {
                parent = new int[n]; // parent[i] = parent of node i
                size = new int[n];   // size[i] = size of the tree rooted at i
                for(int i = 0; i < n; i++) {
                    parent[i] = i;   // initially, each node is its own parent
                    size[i] = 1;     // initial size is 1
                }
            }

            // Find the root parent of node with path compression
            public int find(int node) {
                if(node == parent[node]) return node;
                return parent[node] = find(parent[node]); // path compression
            }

            // Union two nodes by size (attach smaller tree to bigger)
            public void union(int u, int v) {
                int up = find(u), vp = find(v);
                if(up == vp) return; // already connected

                if(size[up] < size[vp]) {
                    parent[up] = vp;
                    size[vp] += size[up];
                } else {
                    parent[vp] = up;
                    size[up] += size[vp];
                }
            }

            // Check if two nodes are in the same connected component
            public boolean isConnected(int u, int v) {
                return find(u) == find(v);
            }
        }

        // Function to solve "Swim in Rising Water" using Kruskal + Union-Find
        public int swimInWater4(int[][] grid) {
            if(grid == null || grid.length == 0) return -1;
            if(grid.length == 1 && grid[0].length == 1) return grid[0][0];

            int n = grid.length;
            List<int[]> edges = new ArrayList<>(); // edges = {weight, node1, node2}

            // Build all possible edges (neighboring cells)
            for(int r = 0; r < n; r++) {
                for(int c = 0; c < n; c++) {
                    int id = r * n + c; // map 2D cell to 1D node
                    for(int[] neighbor: offsetNeighbors) {
                        int nr = r + neighbor[0], nc = c + neighbor[1];

                        if(isValid(nr, nc, n, n)) {
                            int nid = nr * n + nc;// neighbor's 1D id
                            int wt = Math.max(grid[r][c], grid[nr][nc]); // water level to cross
                            edges.add(new int[]{wt, id, nid});   // add edge
                        }
                    }
                }
            }

            // Sort edges by increasing weight (water level)
            edges.sort((a,b) -> a[0] - b[0]);

            // Initialize Union-Find for all cells
            UnionFind uf = new UnionFind(n * n);

            // Process edges in order of increasing water level
            for(int[] edge: edges) {
                int wt = edge[0], u = edge[1], v = edge[2];
                uf.union(u, v); // connect the two cells

                // If start (0,0) and end (n-1,n-1) are connected → answer found
                if(uf.isConnected(0, n * n - 1)) {
                    return wt;
                }
            }

            return -1; // unreachable (should not happen in valid grids)
        }

        // Kind of prims or Dijkstra's algorithms
        public int swimInWater3(int[][] grid) {
            if(grid == null || grid.length == 0) return -1;

            int n = grid.length;
            boolean[][] visited = new boolean[n][n];

            // Min-heap to always pick the next cell with lowest elevation
            // Each element: {currentElevation, row, col}
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> a[0]-b[0]);
            minHeap.offer(new int[]{grid[0][0], 0, 0});

            int time = 0; // Tracks the max elevation along the path

            while(!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int elevation = curr[0], r = curr[1], c = curr[2];

                // Update the max elevation (time) along the current path
                time = Math.max(time, elevation);

                // If we reach bottom-right cell, return the result
                if(r == n-1 && c == n-1) return time;

                // Skip already visited cells
                if(visited[r][c]) continue;

                visited[r][c] = true;

                // Explore all 4 neighbors
                for(int[] neighbor : offsetNeighbors) {
                    int nr = r + neighbor[0], nc = c + neighbor[1];

                    if(isValid(nr, nc, n, n) && !visited[nr][nc]) {
                        minHeap.offer(new int[]{grid[nr][nc], nr, nc});
                    }
                }
            }

            return -1; // unreachable (should not happen for valid grids)
        }

        /*
         * Alternate Solution: Binary Search + BFS
         *
         * Intuition:
         * ------------
         * The higher the water level (t), the easier it is to move across the grid.
         * So the condition “Can we reach the end?” becomes *monotonic*:
         *     - If we can reach at time = t, we can also reach at any time > t.
         *
         * Hence, we can binary search on time t (0 → n^2 - 1)
         * and use BFS to check if a path exists for a given water level.
         *
         * This has the same O(n² log n) complexity as Dijkstra’s algorithm.
         */
        public int swimInWater1(int[][] grid) {
            int n = grid.length;

            // Search space for water level (time)
            int left = grid[0][0];                // Minimum possible time = start elevation
            int right = n * n - 1;                // Maximum possible elevation in grid
            int answer = right;

            // Binary search for the smallest time t where we can reach (n-1, n-1)
            while (left <= right) {
                int mid = left + (right - left) / 2;

                if (canReach(grid, mid, n)) {
                    answer = mid;     // Path exists → try lower water level
                    right = mid - 1;
                } else {
                    left = mid + 1;   // No path → increase water level
                }
            }

            return answer;
        }

        // BFS to check if we can reach bottom-right at given time (water level)
        private boolean canReach(int[][] grid, int time, int n) {
            if (grid[0][0] > time) return false;   // Can't even start

            boolean[][] visited = new boolean[n][n];
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0});
            visited[0][0] = true;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int r = curr[0], c = curr[1];

                // ✅ Reached destination
                if (r == n - 1 && c == n - 1) {
                    return true;
                }

                for (int[] neighbor : offsetNeighbors) {
                    int nr = r + neighbor[0];
                    int nc = c + neighbor[1];

                    if (nr >= 0 && nr < n && nc >= 0 && nc < n &&
                            !visited[nr][nc] && grid[nr][nc] <= time) {
                        visited[nr][nc] = true;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }

            return false;
        }


        // 4 possible directions → Up, Right, Down, Left
        private int[][] offsetNeighbors = {{-1, 0},{0,1},{1,0},{0,-1}};

        // Check if the next cell is within grid boundaries
        private boolean isValid(int nextRow, int nextCol, int ROWS, int COLS) {
            return nextRow >= 0 && nextRow < ROWS && nextCol >= 0 && nextCol < COLS;
        }

        public int swimInWater0(int[][] grid) {
            if (grid == null || grid.length == 0) {
                return 0;
            }

            int ROWS = grid.length, COLS = grid[0].length;

            // To mark visited cells (like Dijkstra’s visited set)
            boolean[][] visited = new boolean[ROWS][COLS];

            /*
             * Min-heap (PriorityQueue):
             * Always process the cell that can be reached at the smallest "time".
             *
             * Each entry = [timeSoFar (max elevation along path), row, col]
             */
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);

            // Start from top-left (0,0), with initial time = its elevation
            minHeap.offer(new int[]{grid[0][0], 0, 0});

            // Dijkstra’s traversal
            while (!minHeap.isEmpty()) {

                // Get the cell reachable with the smallest current time
                int[] curr = minHeap.poll();
                int currTime = curr[0], currRow = curr[1], currCol = curr[2];

                // ✅ If we reached destination, this is the minimum time required
                if (currRow == ROWS - 1 && currCol == COLS - 1) {
                    return currTime;
                }

                // Skip if this cell was already processed
                if (visited[currRow][currCol]) {
                    continue;
                }

                visited[currRow][currCol] = true;

                // Explore all 4 neighboring cells
                for (int[] neighbor : offsetNeighbors) {
                    int nextRow = currRow + neighbor[0];
                    int nextCol = currCol + neighbor[1];

                    // Proceed if within bounds and not visited
                    if (isValid(nextRow, nextCol, ROWS, COLS) && !visited[nextRow][nextCol]) {

                        /*
                         * Water needs to be at least as high as the maximum elevation
                         * seen so far on this path. So the new "time" = max(currTime, grid[nextRow][nextCol]).
                         */
                        minHeap.offer(new int[]{
                                Math.max(currTime, grid[nextRow][nextCol]),
                                nextRow,
                                nextCol
                        });
                    }
                }
            }

            return -1; // (should never reach here)
        }
    }

}
