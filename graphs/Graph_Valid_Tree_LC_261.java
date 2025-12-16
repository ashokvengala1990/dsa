package neetcode.graphs;

import java.util.*;

/*
- https://leetcode.com/problems/graph-valid-tree/description/

Summary of Each Approach
#	Approach	Type	Detects Cycle	Checks Connected	Time	Space
0	Recursive DFS	Recursive	✅ Yes	✅ Yes (via visited count)	O(V+E)	O(V+E)
1	Iterative DFS	Stack-based	✅ Yes	✅ Yes	O(V+E)	O(V+E)
2	BFS	Queue-based	✅ Yes	✅ Yes	O(V+E)	O(V+E)
3	Union-Find	Disjoint Set	✅ Yes	✅ Implicit (n-1 edges)	O(V+Eα(V))	O(V)

💡 Notes for Interview
- Always check edges.length == n - 1 first — quick rejection if not.
- DFS/BFS require both cycle check + connected check.
- Union-Find implicitly guarantees connectivity when edges == n - 1.
- Mention that for directed graphs, you'd use topological sort or Kahn’s algorithm instead.
 */

public class Graph_Valid_Tree_LC_261 {
    class Revision01 {

        // ---------------------------
        // ✅ Approach 4: Union-Find (Disjoint Set Union)
        // ---------------------------
        class UnionFind {
            private int[] parent;
            private int[] size;

            // Constructor: Initialize each node as its own parent (self loop)
            UnionFind(int n) {
                this.parent = new int[n];
                this.size = new int[n];

                for (int i = 0; i < n; i++) {
                    this.parent[i] = i;
                    this.size[i] = 1; // Each component initially of size 1
                }
            }

            // Find operation with path compression optimization
            public int find(int node) {
                if (node == this.parent[node]) {
                    return node; // Found root
                }
                // Compress path so future finds are faster
                return parent[node] = find(parent[node]);
            }

            // Union by size: attach smaller tree under the larger one
            public boolean unionBySize(int u, int v) {
                int up = find(u), vp = find(v);

                // If both nodes already have the same root → cycle detected
                if (up == vp) {
                    return false;
                }

                // Attach smaller component under larger one
                if (size[up] < size[vp]) {
                    parent[up] = vp;
                    this.size[vp] += size[up];
                } else {
                    parent[vp] = up;
                    this.size[up] += size[vp];
                }

                return true; // Successfully united
            }
        }

        // Main Union-Find driver
        public boolean validTree3(int n, int[][] edges) {
            // A valid tree must have exactly n - 1 edges
            if (edges.length != (n - 1)) {
                return false;
            }

            UnionFind uf = new UnionFind(n);

            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                // If a union returns false, it means there's a cycle
                if (!uf.unionBySize(u, v)) {
                    return false;
                }
            }

            // If all unions succeeded and edge count == n - 1 → valid tree
            return true;
        }

        // ---------------------------
        // ✅ Approach 3: BFS (Queue)
        // ---------------------------
        public boolean validTree2(int n, int[][] edges) {
            if (edges.length != (n - 1)) {
                return false; // Tree must have exactly n - 1 edges
            }

            // Build adjacency list
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
            }

            Queue<int[]> queue = new LinkedList<>();
            Set<Integer> visited = new HashSet<>();

            // Start BFS from node 0 (arbitrary)
            queue.offer(new int[]{0, -1});
            visited.add(0);

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currNode = curr[0], parent = curr[1];

                // Traverse all neighbors
                for (int neighbor : adjMap.get(currNode)) {
                    if (!visited.contains(neighbor)) {
                        queue.offer(new int[]{neighbor, currNode});
                        visited.add(neighbor);
                    } else if (neighbor != parent) {
                        // If a neighbor is already visited and is NOT parent → cycle
                        return false;
                    }
                }
            }

            // After BFS, ensure all nodes were visited → connected graph
            return visited.size() == n;
        }

        // ---------------------------
        // ✅ Approach 2: Iterative DFS (Stack)
        // ---------------------------
        public boolean validTree1(int n, int[][] edges) {
            if (edges.length != (n - 1)) {
                return false;
            }

            // Build adjacency list
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
            }

            Stack<int[]> stack = new Stack<>();
            Set<Integer> visited = new HashSet<>();

            // Push starting node (0) and parent (-1)
            stack.push(new int[]{0, -1});
            visited.add(0);

            while (!stack.isEmpty()) {
                int[] curr = stack.pop();
                int currNode = curr[0], parent = curr[1];

                // Explore all neighbors
                for (int neighbor : adjMap.get(currNode)) {
                    if (!visited.contains(neighbor)) {
                        stack.push(new int[]{neighbor, currNode});
                        visited.add(neighbor);
                    } else if (neighbor != parent) {
                        // Cycle detected
                        return false;
                    }
                }
            }

            // After DFS, ensure all nodes are visited
            return visited.size() == n;
        }

        // ---------------------------
        // ✅ Approach 1: Recursive DFS
        // ---------------------------

        // Helper DFS function for recursion
        private boolean dfs0(int node, int parent, Map<Integer, List<Integer>> adjMap, Set<Integer> visited) {
            visited.add(node);

            for (int neighbor : adjMap.get(node)) {
                if (!visited.contains(neighbor)) {
                    if (!dfs0(neighbor, node, adjMap, visited)) {
                        return false; // Cycle detected in subtree
                    }
                } else if (neighbor != parent) {
                    // If we see a visited neighbor that isn't the parent → cycle
                    return false;
                }
            }

            return true;
        }

        // Main recursive DFS function
        public boolean validTree0(int n, int[][] edges) {
            if (edges.length != (n - 1)) {
                return false;
            }

            // Build adjacency list
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
            }

            Set<Integer> visited = new HashSet<>();

            // Run DFS from node 0, ensure no cycles and graph is fully connected
            return dfs0(0, -1, adjMap, visited) && visited.size() == n;
        }
    }

    class Solution {
        public boolean validTree1(int n, int[][] edges) {
            if(n <= 0 || edges == null) {
                return true;
            }

            Map<Integer, List<Integer>> adjListMap = new HashMap<>();
            for(int i = 0; i < n; i++) {
                adjListMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int node1 = edge[0], node2 = edge[1];
                adjListMap.get(node1).add(node2);
                adjListMap.get(node2).add(node1);
            }

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, -1});
            Set<Integer> visited = new HashSet<>();
            visited.add(0);

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int node = curr[0], parent = curr[1];

                for(int neighbor: adjListMap.get(node)) {
                    if(!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(new int[]{neighbor, node});
                    } else if(neighbor != parent) {
                        return false;
                    }
                }
            }

            return visited.size() == n;
        }

        private boolean dfs(int node, int prev, Map<Integer, List<Integer>> adjListMap, Set<Integer> visited) {
            visited.add(node);

            for(int neighbor: adjListMap.get(node)) {
                if(!visited.contains(neighbor)) {
                    if(!dfs(neighbor, node, adjListMap, visited)) {
                        return false;
                    }
                } else if(neighbor != prev) {
                    return false;
                }
            }

            return true;
        }

        public boolean validTree(int n, int[][] edges) {
            if(n <= 0 || edges == null) {
                return true;
            }

            Map<Integer, List<Integer>> adjListMap = new HashMap<>();
            for(int i = 0; i < n; i++) {
                adjListMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int node1 = edge[0], node2 = edge[1];
                adjListMap.get(node1).add(node2);
                adjListMap.get(node2).add(node1);
            }

            Set<Integer> visited = new HashSet<>();

            return dfs(0, -1, adjListMap, visited) && n == visited.size();
        }
    }
}
