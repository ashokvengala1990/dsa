package neetcode.graphs;

/*
Kruskal's Minimum Spanning Tree Algorithm:
- Finds MST for a connected, weighted graph.
- Idea: Sort edges by weight → pick smallest edge → add to MST if it doesn't create a cycle.
- Uses Union-Find to detect cycles efficiently.

Revision Notes:
- Union-Find (Disjoint Set Union):
    - find(x): returns root of set containing x (with path compression).
    - union(u, v): merges two sets by size (union by size/rank) → avoids tall trees.
- MST stops after n-1 edges.
- Disconnected graph → return empty MST.

Time Complexity:
- Sorting edges: O(E log E)
- Union-Find operations: O(E * α(V)) ≈ O(E) (α = inverse Ackermann function)
- Overall: O(E log E)

Space Complexity:
- O(V) for parent/size arrays
- O(E) for edge storage or min-heap


Recommendation
1) Static graph (edges known upfront, common case in interviews): use sorting edges → simpler, faster in practice.
2) Dynamic graph (edges arriving over time, or huge sparse graph): heap approach could be better.
*/

import java.util.*;

public class Kruskals {

    // ------------------------ Union-Find Data Structure ------------------------
    class UnionFind {
        private int[] parent; // parent[i] = representative of node i
        private int[] size;   // size[i] = size of set with root i

        UnionFind(int n) {
            this.parent = new int[n+1];
            this.size = new int[n+1];
            for(int i = 0; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // Path Compression: makes future finds faster
        public int find(int node) {
            if(node == parent[node]) return node;
            return parent[node] = find(parent[node]);
        }

        // Union by Size: attach smaller set under larger set
        // Returns false if already in same set (cycle), true if union performed
        public boolean unionBySize(int u, int v) {
            int rootU = find(u);
            int rootV = find(v);
            if(rootU == rootV) return false; // cycle detected

            if(size[rootU] < size[rootV]) {
                parent[rootU] = rootV;
                size[rootV] += size[rootU];
            } else {
                parent[rootV] = rootU;
                size[rootU] += size[rootV];
            }
            return true;
        }
    }

    // ------------------------ Approach 1: Sort edges ------------------------
    public List<int[]> minimumSpanningTree1(int[][] edges, int n) {
        List<int[]> mst = new ArrayList<>();
        if(edges == null || edges.length == 0) return mst;

        // Step 1: Sort edges by weight
        Arrays.sort(edges, Comparator.comparingInt(a -> a[2]));

        UnionFind uf = new UnionFind(n);
        int i = 0;

        // Step 2: Iterate edges in ascending weight
        while(i < edges.length && mst.size() < n-1) {
            int n1 = edges[i][0], n2 = edges[i][1], wt = edges[i][2];
            // Step 3: Add edge if it doesn't form cycle
            if(uf.unionBySize(n1, n2)) {
                mst.add(new int[]{n1, n2, wt});
            }
            i++;
        }

        // Return MST if graph connected, else empty
        return mst.size() == n-1 ? mst : new ArrayList<>();
    }

    // ------------------------ Approach 2: Min-Heap ------------------------
    public List<int[]> minimumSpanningTree0(int[][] edges, int n) {
        List<int[]> mst = new ArrayList<>();
        if(edges == null || edges.length == 0) return mst;

        // Step 1: Add edges to min-heap based on weight
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0]-b[0]);
        for(int[] edge : edges) {
            minHeap.offer(new int[]{edge[2], edge[0], edge[1]}); // {weight, node1, node2}
        }

        UnionFind uf = new UnionFind(n);

        // Step 2: Extract edges from min-heap
        while(!minHeap.isEmpty() && mst.size() < n-1) {
            int[] curr = minHeap.poll();
            int wt = curr[0], n1 = curr[1], n2 = curr[2];

            // Step 3: Add edge if it doesn't form cycle
            if(uf.unionBySize(n1, n2)) {
                mst.add(new int[]{n1, n2, wt});
            }
        }

        // Return MST if graph connected, else empty
        return mst.size() == n-1 ? mst : new ArrayList<>();
    }
}
