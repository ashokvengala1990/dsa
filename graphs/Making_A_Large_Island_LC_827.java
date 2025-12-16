package neetcode.graphs;

import java.util.HashSet;
import java.util.Set;

public class Making_A_Large_Island_LC_827 {
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
