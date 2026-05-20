package neetcode.graphs;

import java.util.ArrayList;
import java.util.List;

/*
Core Insight
Add land positions one by one. Each new land starts as its own island, then merge with adjacent existing lands, decrementing count per successful merge.
Algorithm
For each position:
1. If already visited → skip (add current count)
2. Mark visited, count++
3. For each valid visited neighbor:
   - unionBySize → if merged (different components) → count--
4. Add count to result
Key Code Tricks
java// 2D → 1D index
int nodeNo = row * COLS + col;

// Only union ALREADY VISITED neighbors
// (unvisited = no land there yet)
if(visited[neiRow][neiCol]) {
    if(uf.unionBySize(nodeNo, neighborNodeNo))
        count--;  // merged two islands into one
}

// Duplicate position handling
if(visited[row][col]) {
    result.add(count); // island count unchanged
    continue;
}
Why count-- on union?
Before union: two separate islands → count = 2
After union:  one merged island   → count = 1
TC / SC
TC: O(k × α(m×n)) ≈ O(k)   k = number of positions
SC: O(m × n)
 */

public class Number_of_Islands_II_LC_305 {
    class Revision02 {
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

        private static int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        public List<Integer> numIslands2(int m, int n, int[][] positions) {
            List<Integer> result = new ArrayList<>();
            boolean[][] visited = new boolean[m][n];
            int count = 0, ROWS = m, COLS = n;
            UnionFind uf = new UnionFind(ROWS * COLS);

            for(int[] position: positions) {
                int row = position[0], col = position[1];
                if(visited[row][col]) {
                    result.add(count);
                    continue;
                }

                visited[row][col] = true;
                count++;
                int nodeNo = row * COLS + col;

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                    if(isValid(neiRow, neiCol, ROWS, COLS)) {
                        if(visited[neiRow][neiCol]) {
                            int neighborNodeNo = neiRow * COLS + neiCol;

                            if(uf.unionBySize(nodeNo, neighborNodeNo)) {
                                count--;
                            }
                        }
                    }
                }

                result.add(count);
            }

            return result;
        }
    }

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

    class Solution {
        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        public List<Integer> numIslands2(int m, int n, int[][] positions) {
            int ROWS = m, COLS = n;
            DisjointSet ds = new DisjointSet(ROWS * COLS);
            boolean[][] visited = new boolean[ROWS][COLS];
            List<Integer> result = new ArrayList<>();
            int count = 0;

            for(int[] position: positions) {
                int row = position[0], col = position[1];
                if (visited[row][col]) {
                    result.add(count);
                    continue;
                }

                visited[row][col] = true;
                count++;

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                    if(isValid(neiRow, neiCol, ROWS, COLS)) {
                        if(visited[neiRow][neiCol]) {
                            int nodeNo = row * COLS + col;
                            int neighNodeNo = neiRow * COLS + neiCol;

                            if(ds.unionBySize(nodeNo, neighNodeNo)) {
                                count--;
                            }
                        }
                    }
                }

                result.add(count);
            }

            return result;
        }
    }
}
