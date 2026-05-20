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

                if(ulpu == ulpv) return false;

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

        public int kruskalsMSTEdges(int n, int[][] edges) {
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> Integer.compare(a[2], b[2]));
            //         return (x < y) ? -1 : ((x == y) ? 0 : 1);

            for(int[] edge: edges) {
                minHeap.offer(new int[]{edge[0], edge[1], edge[2]});
            }

            int totalCost = 0;
            List<int[]> mstEdges = new ArrayList<>();
            UnionFind uf = new UnionFind(n);

            while (!minHeap.isEmpty() && mstEdges.size() < n) {
                int[] curr = minHeap.poll();
                int u = curr[0], v = curr[1], wt = curr[2];

                if(uf.unionBySize(u, v)) {
                    totalCost += wt;
                    mstEdges.add(new int[]{u, v, wt});
                }
            }

            return mstEdges.size() == n-1 ? totalCost : -1;
        }

        public int kruskalsMST(int n, int[][] edges) {
            Arrays.sort(edges, (a, b) -> a[2] - b[2]);

            int totalCost = 0, edgesUsed = 0;
            UnionFind  uf = new UnionFind(n);

            for(int[] edge: edges) {
                if(uf.unionBySize(edge[0], edge[1])) {  // no cycle  = add to MST
                    edgesUsed++;
                    totalCost += edge[2];

                    if(edgesUsed == (n-1)) break;
                }
            }

            return edgesUsed == n-1 ? totalCost : -1; // -1 = not fully connected
        }
    }


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
