package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/path-with-minimum-effort/description/

LC 1631 — Path With Minimum Effort (Dijkstra / Minimax)
Notes (keep in one place)
- Think of each cell as a node in a graph; edges connect 4-neighbors.
- Edge “cost” between two cells = abs(height[a] - height[b]).
- Path “cost/effort” is NOT sum → it’s the maximum edge cost along the path (minimax).
- Dijkstra still works if:
    - dist[r][c] = minimum possible effort to reach (r,c)
    - Relaxation: newEffort = max(currEffort, edgeCost)
- Early exit is valid: when (rows-1, cols-1) is popped from min-heap, that effort is optimal.
- For path printing:
    - Store parent when you successfully relax to a smaller effort.
    - Use a consistent key format (you used "r-c" everywhere ✅).

Time Complexity (TC)
- V = rows * cols, E ≈ 4 * V, In grid, we have 4 neighbors for each vertex and we have V vertices. Thus, E = 4 * V.
- Dijkstra with heap: O(E log V) = O((rowscols) log(rowscols))

Space Complexity (SC)
- effortState: O(rows*cols)
- Heap (worst case): O(rows*cols)
- Parent map (optional): O(rowscols)
- Overall: **O(rowscols)**

 */

public class Path_With_Minimum_Effort_LC_1631 {
    class Revision01 {
        class UnionFind {
            private int[] parent, size;

            UnionFind(int n) {
                parent = new int[n];
                size = new int[n];

                for(int i = 0; i < n; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }
            }

            public int findUltimateParent(int node) {
                if(parent[node] == node) {
                    return node;
                }

                return parent[node] = findUltimateParent(parent[node]);
            }

            public boolean unionBySize(int u, int v) {
                int ulpu = findUltimateParent(u), ulpv = findUltimateParent(v);

                if(ulpu == ulpv) {
                    return false;
                }

                if(size[ulpu] < size[ulpv]) {
                    parent[ulpu] = ulpv;
                    size[ulpv] += size[ulpu];
                } else {
                    parent[ulpv] = ulpu;
                    size[ulpu] += size[ulpv];
                }

                return true;
            }
        }

        private static final int[][] offsetNeighbors = {{-1,0},{0,1},{1,0},{0,-1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        private List<int[]> buildPath(int[][][] prev, int rows, int cols) {
            int r = rows-1, c = cols-1;
            List<int[]> path = new ArrayList<>();

            while (r != -1 && c != -1) {
                path.add(new int[]{r, c});
                int[] parent = prev[r][c];
                r = parent[0];
                c = parent[1];
            }

            Collections.reverse(path);
            return path;
        }

        private void printPath(int[][][] prev, int rows, int cols) {
            List<int[]> path = buildPath(prev, rows, cols);
            for(int[] cell: path) {
                System.out.print("["+cell[0]+","+cell[1]+"] -> ");
            }
            System.out.println("END");
        }

        public int minimumEffortPathUsingUnionFindAlgorithm(int[][] heights) {
            if (heights == null || heights.length == 0) {
                return 0;
            }

            int rows = heights.length, cols = heights[0].length;
            List<int[]> edges = new ArrayList<>();

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(c + 1 < cols) {
                        edges.add(new int[]{Math.abs(heights[r][c] - heights[r][c+1]), r, c, r, c+1});
                    }
                    if(r + 1 < rows) {
                        edges.add(new int[]{Math.abs(heights[r][c] - heights[r+1][c]), r, c, r+1, c});
                    }
                }
            }

            edges.sort((a,b) -> Integer.compare(a[0],b[0]));

            UnionFind uf = new UnionFind(rows*cols);
            int src = 0, dst = (rows-1) * cols + (cols-1);

            for(int[] edge: edges) {
                int effort = edge[0], idx1 = edge[1] * cols + edge[2], idx2 = edge[3] * cols + edge[4];

                if(uf.unionBySize(idx1, idx2)) {
                    if(uf.findUltimateParent(src) == uf.findUltimateParent(dst)) {
                        return effort;
                    }
                }
            }

            return 0;
        }

        public int minimumEffortPathAndPrintPath(int[][] heights) {
            if(heights == null || heights.length == 0) {
                return 0;
            }

            int rows = heights.length, cols = heights[0].length;
            boolean[][] visited = new boolean[rows][cols];
            int[][][] prev = new int[rows][cols][]; // prev[r][c] = new int[]{parentRow, parentCol};
            // {maxHeightSofar,row, col}
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> Integer.compare(a[0],b[0]));
            minHeap.offer(new int[]{0,0,0,-1,-1});

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int effort = curr[0], r = curr[1], c = curr[2], pr = curr[3], pc = curr[4];

                if(visited[r][c])
                    continue;

                visited[r][c] = true;
                prev[r][c] = new int[]{pr, pc};

                if(r == (rows-1) && c == (cols-1)) {
                    printPath(prev, rows, cols);
                    return effort;
                }

                for(int[] neighbor: offsetNeighbors) {
                    int nr = r + neighbor[0], nc = c + neighbor[1];

                    if(isValid(nr, nc, rows, cols) && !visited[nr][nc]) {
                        int newEffort = Math.max(effort, Math.abs(heights[r][c] - heights[nr][nc]));
                        minHeap.offer(new int[]{newEffort, nr, nc, r, c});
                    }
                }
            }

            return 0;
        }

        public int minimumEffortPath(int[][] heights) {
            if(heights == null || heights.length == 0) {
                return 0;
            }

            int rows = heights.length, cols = heights[0].length;
            boolean[][] visited = new boolean[rows][cols];
            // {maxHeightSofar,row, col}
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> Integer.compare(a[0],b[0]));
            minHeap.offer(new int[]{0,0,0});

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int effort = curr[0], r = curr[1], c = curr[2];

                if(visited[r][c])
                    continue;

                visited[r][c] = true;
                if(r == (rows-1) && c == (cols-1)) {
                    return effort;
                }

                for(int[] neighbor: offsetNeighbors) {
                    int nr = r + neighbor[0], nc = c + neighbor[1];

                    if(isValid(nr, nc, rows, cols) && !visited[nr][nc]) {
                        int newEffort = Math.max(effort, Math.abs(heights[r][c] - heights[nr][nc]));
                        minHeap.offer(new int[]{newEffort, nr, nc});
                    }
                }
            }

            return 0;
        }
    }


    class Solution {

        // 4-direction movement: up, right, down, left
        private static final int[][] offsetNeighbors = {
                {-1, 0}, {0, 1}, {1, 0}, {0, -1}
        };

        // Bounds check for grid indices
        private boolean isBound(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        // Reconstruct & print path using child -> parent pointers.
        // Key format must match exactly what you store: "r-c"
        private void printPath(Map<String, String> childToParentMap, int[][] heights) {
            int rows = heights.length, cols = heights[0].length;

            // Start from destination
            String cellNoStr = (rows - 1) + "-" + (cols - 1);

            List<String> path = new ArrayList<>();

            // Walk backwards using parent pointers until null (source parent)
            while (cellNoStr != null) {
                path.add(cellNoStr);
                cellNoStr = childToParentMap.get(cellNoStr);
            }

            // Reverse to get source -> destination order
            Collections.reverse(path);
            System.out.println("Path: " + path);
        }

        // Dijkstra + also stores parents to print one optimal path.
        public int minimumEffortWithPath(int[][] heights) {
            if (heights == null || heights.length == 0) return -1;

            int rows = heights.length, cols = heights[0].length;

            // effortState[r][c] = minimum possible "effort" to reach this cell
            int[][] effortState = new int[rows][cols];
            for (int[] itr : effortState) Arrays.fill(itr, Integer.MAX_VALUE);

            effortState[0][0] = 0; // start cell effort is 0 (no edge taken yet)

            // Min-heap by current effort
            // heap item = [effortSoFar, row, col]
            PriorityQueue<int[]> minHeapPQ = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeapPQ.offer(new int[]{0, 0, 0});

            // Store parent pointers only when we improve effortState (relaxation)
            Map<String, String> childToParentMap = new HashMap<>();
            childToParentMap.put("0-0", null); // source has no parent

            while (!minHeapPQ.isEmpty()) {
                int[] curr = minHeapPQ.poll();
                int diff = curr[0], row = curr[1], col = curr[2];

                // Optional optimization:
                // If this heap entry is stale (we already found a better effort), skip it.
                // (Helps performance; correctness is still fine without it.)
                if (diff > effortState[row][col]) continue;

                // Early exit: first time we pop destination, it's the best possible effort
                if (row == rows - 1 && col == cols - 1) {
                    printPath(childToParentMap, heights);
                    return diff;
                }

                // Relax neighbors
                for (int[] neighbor : offsetNeighbors) {
                    int neiRow = row + neighbor[0];
                    int neiCol = col + neighbor[1];

                    if (!isBound(neiRow, neiCol, rows, cols)) continue;

                    // Edge cost between current cell and neighbor
                    int edgeCost = Math.abs(heights[row][col] - heights[neiRow][neiCol]);

                    // Path effort is the max edge cost along the path so far (minimax)
                    int newEffort = Math.max(diff, edgeCost);

                    // Standard Dijkstra relaxation (but using minimax update)
                    if (newEffort < effortState[neiRow][neiCol]) {
                        effortState[neiRow][neiCol] = newEffort;
                        minHeapPQ.offer(new int[]{newEffort, neiRow, neiCol});

                        // Record parent only when we improve the best-known effort
                        childToParentMap.put(neiRow + "-" + neiCol, row + "-" + col);
                    }
                }
            }

            return -1; // should not happen for valid grids, but safe fallback
        }

        // Same Dijkstra solution, without storing path.
        public int minimumEffortPath(int[][] heights) {
            if (heights == null || heights.length == 0) return -1;

            int rows = heights.length, cols = heights[0].length;

            int[][] effortState = new int[rows][cols];
            for (int[] itr : effortState) Arrays.fill(itr, Integer.MAX_VALUE);

            effortState[0][0] = 0;

            PriorityQueue<int[]> minHeapPQ = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeapPQ.offer(new int[]{0, 0, 0});

            while (!minHeapPQ.isEmpty()) {
                int[] curr = minHeapPQ.poll();
                int diff = curr[0], row = curr[1], col = curr[2];

                // Skip stale entries
                if (diff > effortState[row][col]) continue;

                if (row == rows - 1 && col == cols - 1) {
                    return diff;
                }

                for (int[] neighbor : offsetNeighbors) {
                    int neiRow = row + neighbor[0];
                    int neiCol = col + neighbor[1];

                    if (!isBound(neiRow, neiCol, rows, cols)) continue;

                    int edgeCost = Math.abs(heights[row][col] - heights[neiRow][neiCol]);
                    int newEffort = Math.max(diff, edgeCost);

                    if (newEffort < effortState[neiRow][neiCol]) {
                        effortState[neiRow][neiCol] = newEffort;
                        minHeapPQ.offer(new int[]{effortState[neiRow][neiCol], neiRow, neiCol});
                    }
                }
            }

            return -1;
        }
    }
}
