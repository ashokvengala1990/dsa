package neetcode.graphs;

import java.util.HashSet;
import java.util.Set;

/*
LC 827 – Making a Large Island (revision notes)
Idea: Grid of 0s/1s. Flip at most one 0 → 1. Return largest island size.
Approach: DSU to group all 1s into islands. For each 0, sum sizes of unique adjacent islands + 1.
Key steps:

Flatten grid: nodeNo = row * cols + col.
Pass 1: union every 1 cell with its 1 neighbors (build islands).
Pass 2: for each 0, collect unique roots of adjacent 1s in a Set, sum their sizes, +1 for the flipped cell.
Edge case: all 1s (no 0 to flip) → final loop tracks max island size from DSU directly.

Why Set<Integer> for roots?
A 0 can touch the same island from multiple sides (e.g. 0 surrounded by a ring of 1s). Without dedup, you'd count one island 2–4 times.
Gotchas:

isValid(neiRow, neiCol, rows, cols) — easy typo to pass col instead of cols.
All-1s grid needs the final loop, else returns 0.
All-0s grid: Set empty → totalSize = 0 + 1 = 1 ✓.
getSizeByNodeNo only meaningful on roots → always call after findUltimateParent.

Complexity: O(R·C·α) time, O(R·C) space.
Pattern recognition: "Connected components on grid + one modification" → DSU with flattened indexing + neighbor root dedup.
 */

public class Making_A_Large_Island_LC_827 {
    class Revision01 {
        class DisjointSet {
            private int[] parent, size;

            DisjointSet(int n) {
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

            public void unionBySize(int u, int v) {
                int ulpu = findUltimateParent(u), ulpv = findUltimateParent(v);

                if(ulpu == ulpv) {
                    return;
                }

                if(size[ulpu] < size[ulpv]) {
                    parent[ulpu] = ulpv;
                    size[ulpv] += size[ulpu];
                } else {
                    parent[ulpv] = ulpu;
                    size[ulpu] += size[ulpv];
                }
            }

            public int getSizeByNodeNo(int node) {
                return size[node];
            }
        }

        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        public int largestIsland(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length, maxSizeIsland = 0;
            DisjointSet ds = new DisjointSet(rows * cols);

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(grid[row][col] == 1) {
                        for(int[] neighbor: offsetNeighbors) {
                            int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                            if(isValid(neiRow, neiCol, rows, cols) && grid[neiRow][neiCol] == 1) {
                                int nodeNo = row * cols + col;
                                int neiNodeNo = neiRow * cols + neiCol;

                                ds.unionBySize(nodeNo, neiNodeNo);
                            }
                        }
                    }
                }
            }

            for(int row = 0 ; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(grid[row][col] == 0) {
                        Set<Integer> components = new HashSet<>();

                        for(int[] neighbor: offsetNeighbors) {
                            int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                            if(isValid(neiRow, neiCol, rows, cols) && grid[neiRow][neiCol] == 1) {
                                int neiNodeNo = neiRow * cols + neiCol;
                                components.add(ds.findUltimateParent(neiNodeNo));
                            }
                        }

                        int totalSize = 0;

                        for(int itr: components) {
                            totalSize += ds.getSizeByNodeNo(itr);
                        }

                        // As we are changing 0 to 1 and this need to be count in the size
                        totalSize++;

                        maxSizeIsland = Math.max(maxSizeIsland, totalSize);
                    }
                }
            }

            for(int nodeNo = 0; nodeNo < rows * cols; nodeNo++) {
                maxSizeIsland = Math.max(maxSizeIsland, ds.getSizeByNodeNo(ds.findUltimateParent(nodeNo)));
            }

            return maxSizeIsland;
        }
    }

    class Solution {
        class DisjointSet {
            private int[] parent, size;

            DisjointSet(int n) {
                this.parent = new int[n];
                this.size = new int[n];

                for(int i = 0; i < n; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }
            }

            public int findUltimateParent(int node) {
                if(node == parent[node]) {
                    return node;
                }

                return parent[node] = findUltimateParent(parent[node]);
            }

            public boolean unionBySize(int u, int v) {
                int ulpU = findUltimateParent(u), ulpV = findUltimateParent(v);

                if(ulpU == ulpV) {
                    return false;
                }

                if(size[ulpU] < size[ulpV]) {
                    parent[ulpU] = ulpV;
                    size[ulpV] += size[ulpU];
                } else {
                    parent[ulpV] = ulpU;
                    size[ulpU] += size[ulpV];
                }
                return true;
            }
        }

        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        public int largestIsland(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int maxArea = 0, rows = grid.length, cols = grid[0].length;
            DisjointSet ds = new DisjointSet(rows * cols);

            for(int row = 0 ; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(grid[row][col] == 0) {
                        continue;
                    }

                    int nodeId = row * cols + col;
                    for(int[] neighbor: offsetNeighbors) {
                        int newRow = row + neighbor[0], newCol = col + neighbor[1];

                        if(isValid(newRow, newCol, rows, cols) && grid[newRow][newCol] == 1) {
                            int neighNode = newRow * cols + newCol;
                            ds.unionBySize(nodeId, neighNode);
                        }
                    }
                }
            }

            for(int row =0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(grid[row][col] == 1) {
                        continue;
                    }

                    Set<Integer> components = new HashSet<>();
                    int totalSize = 1;

                    for(int[] neighbor: offsetNeighbors) {
                        int newRow = row + neighbor[0], newCol = col + neighbor[1];

                        if(isValid(newRow, newCol, rows, cols) && grid[newRow][newCol] == 1) {
                            int neighNode = newRow * cols + newCol;
                            components.add(ds.findUltimateParent(neighNode));
                        }
                    }

                    for(int itr: components) {
                        totalSize += ds.size[ds.findUltimateParent(itr)];
                    }

                    maxArea = Math.max(maxArea, totalSize);
                }
            }

            for(int nodeId = 0; nodeId < rows * cols; nodeId++) {
                maxArea = Math.max(maxArea, ds.size[ds.findUltimateParent(nodeId)]);
            }

            return maxArea;
        }
    }
}
