package neetcode.graphs;

/*

LC 947 – Most Stones Removed with Same Row or Column (revision notes)
Idea: Stone removable if it shares row/col with another. Max removals = stones - connected_components.
Approach: DSU. Treat each row and each column as a node. For each stone, union its row-node with its column-node.
Key steps:

Find maxRow, maxCol.
DSU size = maxRow + maxCol + 2 (rows: 0..maxRow, cols: maxRow+1..maxRow+maxCol+1).
For each stone: unionBySize(row, maxRow + 1 + col).
Track touched nodes in a Set (rows/cols never used shouldn't count as components).
Count roots among touched nodes → that's component count.

Why row & col as nodes?
Two stones connected ⟺ chain of shared row/col exists. Unioning each stone's row-node with col-node captures this transitively without N² pairwise comparisons.
Why +1 offset on cols?
Prevents row 0 and col 0 from colliding into the same DSU index.
Why stoneNodes set?
DSU contains rows/cols that may not appear in any stone — they'd be counted as singleton components. Only count nodes actually touched.
Gotchas:

DSU size off-by-one: need maxRow + maxCol + 2, not + 1.
Answer is stones - components, not components.

Complexity: O(N·α) time, O(maxRow + maxCol) space.
Pattern recognition: "Group by shared attribute (row/col/category)" → DSU with attribute-as-node trick.






========> Notes from old one.
If we can visualize stones like connected visually, then it boils down to Disjoint Set Data Structure (DSU)

c1          c2          c3          c4
x1 stones   x2 stones   x3 stones   x4 stones
(x1-1)   +  (x2-1)   + (x3-1)     + (x4-1)

=> (x1 + x2 + x3 + x4) - (1+1+1+1+1)
Ans => n - number of components

I will treat row as a node in DSU. I will also treat col as a node in DSU in the next set order
A row is a node in Disjoint Set. Same for col.
col node is treated as (row+col+1), where row is the (rows-1) and col can be 0 to (cols-1)

if rows = 5, cols = 4
0th row, 1st row, second row, 3rd row, 4th row, 5th column, 6th column, 7th column, 8th column


to get into column node = col + row + 1

https://leetcode.com/problems/most-stones-removed-with-same-row-or-column/description/

Stones connected by same row or same column form a component →
From each component, we can remove all but one stone.

👉 Answer = total stones − number of connected components

💡 Core Insight
- Treat rows and columns as graph nodes
- A stone (r, c) connects:
    * rowNode = r
    * colNode = maxRow + c + 1 (to avoid overlap)
- Use DSU (Union-Find) to group connected rows & columns

🧩 Why DSU Works
- Stones in same row/column → same connected component
- In one component with x stones → removable stones = x − 1
- Summing over all components:
    * Total removable = n − number_of_components

🛠️ Implementation Notes
- DSU size = maxRow + maxCol + 2
- Track only used nodes (rows/cols that appear in stones)
- Count how many unique DSU roots exist among used nodes

✅ Algorithm Steps
- Find maxRow, maxCol
- Create DSU for rows + columns
- For each stone:
    * Union(rowNode, colNode)
    * Track used nodes
- Count unique DSU roots among used nodes
- Return stonesCount − components

⏱️ Time Complexity (TC)
- DSU operations: O(n α(n))
- α(n) ≈ constant (inverse Ackermann)
- 👉 Overall TC: O(n)

📦 Space Complexity (SC)
- DSU arrays: O(maxRow + maxCol)
- HashSet for used nodes
- 👉 Overall SC: O(maxRow + maxCol)

❗ Common Pitfalls
- Forgetting to offset column index
- Counting all DSU nodes instead of only used nodes
- Treating stones as nodes instead of rows & columns

📝 One-Line Revision Note
- LC 947 = DSU on row + column nodes → answer = total stones − connected components


“We model rows and columns as DSU nodes and stones as edges connecting them. Each connected component represents stones that
cannot all be removed, so answer = total stones − components. This is O(n) and optimal.”

 */

import java.util.*;

public class Most_Stones_Removed_with_Same_Row_or_Column_LC_947 {
    class Revision02 {
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
        }

        public int removeStones(int[][] stones) {
            if(stones == null || stones.length == 0) {
                return 0;
            }

            int noOfStones = stones.length, maxRow = 0, maxCol = 0, noOfComponents = 0;

            for(int[] stone: stones) {
                maxRow = Math.max(maxRow, stone[0]);
                maxCol = Math.max(maxCol, stone[1]);
            }

            // 0 1 2 3 4 5
            DisjointSet ds = new DisjointSet(maxRow + maxCol + 2);
            Set<Integer> stoneNodes = new HashSet<>();

            for(int[] stone: stones) {
                int nodeRow = stone[0], nodeCol = maxRow + stone[1] + 1;
                ds.unionBySize(nodeRow, nodeCol);
                stoneNodes.add(nodeRow);
                stoneNodes.add(nodeCol);
            }

            for(int node: stoneNodes) {
                if(ds.findUltimateParent(node) == node) {
                    noOfComponents++;
                }
            }

            return noOfStones - noOfComponents;
        }
    }

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
        }

        public int removeStones(int[][] stones) {
            if(stones == null || stones.length == 0) {
                return 0;
            }

            int noStones = stones.length, maxRow = 0, maxCol = 0, noOfComponents = 0;

            for(int[] stone: stones) {
                int row = stone[0], col = stone[1];
                maxRow = Math.max(maxRow,row);
                maxCol = Math.max(maxCol, col);
            }

            DisjointSet ds = new DisjointSet(maxRow + maxCol);
            Set<Integer> stoneNodes = new HashSet<>();

            for(int[] stone: stones) {
                int col = stone[1];
                int nodeRow = stone[0], nodeCol = maxRow + col + 1;
                ds.unionBySize(nodeRow, nodeCol);
                stoneNodes.add(nodeRow);
                stoneNodes.add(nodeCol);
            }

            for(int node: stoneNodes) {
                if(ds.findUltimateParent(node) == node) {
                    noOfComponents++;
                }
            }

            return noStones - noOfComponents;
        }
    }

    class DisjointSet {
        int[] parent, size;

        DisjointSet(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int findUltimateParent(int node) {
            if (node == parent[node]) {
                return node;
            }

            return parent[node] = findUltimateParent(parent[node]);
        }

        public boolean unionBySize(int u, int v) {
            int ulpU = findUltimateParent(u), ulpV = findUltimateParent(v);

            if (ulpU == ulpV) {
                return false;
            }

            if (size[ulpU] < size[ulpV]) {
                parent[ulpU] = ulpV;
                size[ulpV] += size[ulpU];
            } else {
                parent[ulpV] = ulpU;
                size[ulpU] += size[ulpV];
            }

            return true;
        }
    }

    public int removeStones1(int[][] stones) {
        if (stones == null || stones.length == 0) {
            return 0;
        }

        int noOfStones = stones.length, maxRow = 0, maxCol = 0, count = 0;

        for (int[] stone : stones) {
            maxRow = Math.max(maxRow, stone[0]);
            maxCol = Math.max(maxCol, stone[1]);
        }

        DisjointSet ds = new DisjointSet(maxRow + maxCol + 2);
        Set<Integer> stoneNodes = new HashSet<>();

        for (int[] stone : stones) {
            int nodeRow = stone[0];
            int nodeCol = maxRow + stone[1] + 1;
            ds.unionBySize(nodeRow, nodeCol);
            stoneNodes.add(nodeRow);
            stoneNodes.add(nodeCol);
        }

        for (int node : stoneNodes) {
            if (ds.findUltimateParent(node) == node) {
                count++;
            }
        }

        return noOfStones - count;
    }

    private void dfs(int idx, int[][] stones, Map<Integer, List<Integer>> rowMap, Map<Integer, List<Integer>> colMap, boolean[] visited) {
        visited[idx] = true;
        int row = stones[idx][0], col = stones[idx][1];

        // Visit all stones in same row
        for(int next: rowMap.get(row)) {
            if(!visited[next]) {
                dfs(next, stones, rowMap, colMap, visited);
            }
        }

        // Visit all stones in same column
        for(int next: colMap.get(col)) {
            if(!visited[next]) {
                dfs(next, stones, rowMap, colMap, visited);
            }
        }
    }

    public int removeStones0(int[][] stones) {
        if(stones == null || stones.length == 0) {
            return 0;
        }

        int n = stones.length;
        // row -> list of stone indices
        Map<Integer, List<Integer>> rowMap = new HashMap<>();

        // col -> list of stone indices
        Map<Integer, List<Integer>> colMap = new HashMap<>();

        for(int i = 0; i < n; i++) {
            rowMap.computeIfAbsent(stones[i][0], k -> new ArrayList<>()).add(i);
            colMap.computeIfAbsent(stones[i][1], k -> new ArrayList<>()).add(i);
        }

        boolean[] visited = new boolean[n];
        int components = 0;

        for(int i = 0; i < n; i++) {
            if(!visited[i]) {
                components++;
                dfs(i, stones, rowMap, colMap, visited);
            }
        }

        return n - components;
    }
}