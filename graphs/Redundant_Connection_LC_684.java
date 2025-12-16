package neetcode.graphs;

import java.util.*;

/*
Tree:
1) Connected
2) No Cycles

We have to remove an edge from the given graph to make it as a tree
- Remove an edges so eliminate the cycle in a graph

// 🔹 Revision Notes:
    // - Checks for path between u and v before adding edge.
    // - If path exists, this edge is redundant.
    // - Simple but inefficient (calls DFS for every edge).
    // - Works fine for small graphs or conceptual clarity.

    // 🔹 Time Complexity: O(E × (V + E))  (for each edge, may traverse full graph)
    // 🔹 Space Complexity: O(V + E)       (for adjacency list and recursion stack)

Quick Revision Summary
Approach	Key Idea	Detect Cycle Early?	Returns Last Edge?	TC	SC	Notes
DFS (Brute Force)	Run DFS before adding edge	✅	✅	O(E × (V+E))	O(V+E)	Conceptually simple but slow
Union-Find (DSU)	Union sets; if already connected → redundant	✅	✅	O(E α(V)) ≈ O(E)	O(V)	Fastest, cleanest
Global DFS (one pass)	Build full graph, find any cycle	✅	❌	O(V+E)	O(V+E)	Needs extra work to find last edge
 */


public class Redundant_Connection_LC_684 {
    class Revision01 {

        // ==============================
        // ✅ Disjoint Set Union (Union-Find) Approach
        // ==============================
        class UnionFind {
            private int[] parent;  // parent[i] → representative parent of node i
            private int[] size;    // size[i] → size of the tree rooted at i (used for union by size)

            UnionFind(int n) {
                this.parent = new int[n + 1];
                this.size = new int[n + 1];

                // Initially, each node is its own parent (self-loop)
                for (int i = 0; i <= n; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }
            }

            // Path Compression: flattens the structure for amortized O(α(N)) time
            public int find(int node) {
                if (node == parent[node]) {
                    return node;
                }
                // Recursively find and compress the path
                return parent[node] = find(parent[node]);
            }

            // Union by Size: attach smaller tree under larger one
            public boolean unionBySize(int u, int v) {
                int up = find(u), vp = find(v);

                // If both nodes have the same root, adding this edge will form a cycle
                if (up == vp) {
                    return false;
                }

                // Attach smaller tree under larger tree
                if (size[up] < size[vp]) {
                    parent[up] = vp;
                    size[vp] += size[up];
                } else {
                    parent[vp] = up;
                    size[up] += size[vp];
                }

                return true;
            }
        }

        // ======================================================
        // ✅ Optimal Approach 1: Union-Find (Disjoint Set)
        // ======================================================
        public int[] findRedundantConnection3(int[][] edges) {
            if (edges == null || edges.length == 0) {
                return new int[]{};
            }

            int n = edges.length;
            UnionFind uf = new UnionFind(n + 1);

            // Process each edge
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];

                // If union fails (same root), edge is redundant
                if (!uf.unionBySize(u, v)) {
                    return new int[]{u, v};
                }
            }

            return new int[]{};
        }

        // 🔹 Revision Notes:
        // - Detects cycle efficiently using DSU.
        // - Returns the last redundant edge in O(E α(V)) time.
        // - Preferred approach in interviews for Redundant Connection.
        // - Union-Find is typically faster than DFS/BFS for this problem.

        // 🔹 Time Complexity: O(E α(V)) ≈ O(E)
        // 🔹 Space Complexity: O(V)

        private boolean bfsUsingQueue(int src, int dst, Map<Integer, List<Integer>> adjMap, boolean[] visited) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(src);
            visited[src] = true;

            while (!queue.isEmpty()) {
                int currNode = queue.poll();

                if(currNode == dst) {
                    return true;
                }

                for(int neighbor: adjMap.get(currNode)) {
                    if(!visited[neighbor]) {
                        queue.offer(neighbor);
                        visited[neighbor] = true;
                    }
                }
            }

            return false;
        }

        // Using BFS with Queue DS
        public int[] findRedundantConnection2(int[][] edges) {
            if (edges == null || edges.length == 0) {
                return new int[]{};
            }

            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            int n = edges.length;

            // Initialize adjacency list
            for (int i = 0; i <= n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            boolean[] visited = new boolean[n + 1];

            // Try to add each edge one by one
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                Arrays.fill(visited, false);

                // If a path already exists between u and v → adding this edge forms a cycle
                if (bfsUsingQueue(u, v, adjMap, visited)) {
                    return new int[]{u, v};
                }

                // Otherwise, safely add edge to the graph
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
            }

            return new int[]{};
        }

        private boolean dfsUsingStack(int src, int dst, Map<Integer, List<Integer>> adjMap, boolean[] visited) {

            Stack<Integer> stack = new Stack<>();
            stack.push(src);
            visited[src] = true;

            while (!stack.isEmpty()) {
                int currNode = stack.pop();

                if(currNode == dst) {
                    return true;
                }

                for(int neighbor: adjMap.get(currNode)) {
                    if(!visited[neighbor]) {
                        stack.push(neighbor);
                        visited[neighbor] = true;
                    }
                }
            }

            return false;
        }

        // Using DFS with Stack
        public int[] findRedundantConnection1(int[][] edges) {
            if (edges == null || edges.length == 0) {
                return new int[]{};
            }

            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            int n = edges.length;

            // Initialize adjacency list
            for (int i = 0; i <= n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            boolean[] visited = new boolean[n + 1];

            // Try to add each edge one by one
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                Arrays.fill(visited, false);

                // If a path already exists between u and v → adding this edge forms a cycle
                if (dfsUsingStack(u, v, adjMap, visited)) {
                    return new int[]{u, v};
                }

                // Otherwise, safely add edge to the graph
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
            }

            return new int[]{};
        }

        // ======================================================
        // ✅ Brute Force DFS Approach
        // ======================================================
        private boolean hasPath(int src, int dst, Map<Integer, List<Integer>> adjMap, boolean[] visited) {
            // Base case: if we reached destination
            if (src == dst) {
                return true;
            }

            visited[src] = true;

            for (int neighbor : adjMap.get(src)) {
                // Explore unvisited neighbors
                if (!visited[neighbor] && hasPath(neighbor, dst, adjMap, visited)) {
                    return true;
                }
            }

            return false;
        }

        public int[] findRedundantConnection0(int[][] edges) {
            if (edges == null || edges.length == 0) {
                return new int[]{};
            }

            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            int n = edges.length;

            // Initialize adjacency list
            for (int i = 0; i <= n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            boolean[] visited = new boolean[n + 1];

            // Try to add each edge one by one
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                Arrays.fill(visited, false);

                // If a path already exists between u and v → adding this edge forms a cycle
                if (hasPath(u, v, adjMap, visited)) {
                    return new int[]{u, v};
                }

                // Otherwise, safely add edge to the graph
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
            }

            return new int[]{};
        }

        // 🔹 Revision Notes:
        // - Checks for path between u and v before adding edge.
        // - If path exists, this edge is redundant.
        // - Simple but inefficient (calls DFS for every edge).
        // - Works fine for small graphs or conceptual clarity.

        // 🔹 Time Complexity: O(E × (V + E))  (for each edge, may traverse full graph)
        // 🔹 Space Complexity: O(V + E)       (for adjacency list and recursion stack)
    }


    class Solution {
        private int[] answer;

        private void dfs(int node, int parent, Map<Integer, List<Integer>> adjMap, Set<Integer> visited) {
            visited.add(node);

            for(int neighbor: adjMap.get(node)) {
                if(!visited.contains(neighbor)) {
                    dfs(neighbor, node, adjMap, visited);
                } else if(neighbor != parent) {
                    answer[0] = node;
                    answer[1] = neighbor;
                }
            }
        }

        public int[] findRedundantConnection0(int[][] edges) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            int n = edges.length;

            for(int i = 1; i <= n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1];
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
            }

            answer = new int[2];
            Set<Integer> visited = new HashSet<>();

            dfs(1, 0, adjMap, visited);

            return answer;
        }
    }
}
