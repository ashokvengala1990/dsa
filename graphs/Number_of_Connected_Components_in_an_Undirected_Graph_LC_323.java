package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/

Summary Table
#	Approach	Type	Uses Recursion?	Time	Space	Notes
0	DFS	Recursive	✅	O(V+E)	O(V+E)	Simple, elegant, may hit recursion limit
1	DFS	Iterative	❌	O(V+E)	O(V+E)	Avoids recursion limit
2	BFS	Queue-based	❌	O(V+E)	O(V+E)	Breadth-first traversal
3	Union-Find	DSU	❌	O(V + Eα(V))	O(V)	Most optimal & scalable

💡 Key Interview Notes
- Each connected component = one DFS/BFS/Union operation starting from an unvisited node.
- If the graph is connected → 1 component; if disconnected → multiple.
- Union-Find is best for dynamic edge additions (e.g., online graph connectivity).
- DFS/BFS are intuitive for adjacency list traversals.
- Always remember: undirected graphs must store both (u→v) and (v→u).
*/

public class Number_of_Connected_Components_in_an_Undirected_Graph_LC_323 {
    class Revision02 {
        class UnionFind {
            private int[] parent, size;
            private int numComponents;
            UnionFind(int n) {
                parent = new int[n];
                size = new int[n];
                for(int i = 0; i < n; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }
                numComponents = n;
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
                numComponents--;
            }

            public int getNumComponents() {
                return numComponents;
            }
        }
        public int countComponents2(int n, int[][] edges) {
            UnionFind uf = new UnionFind(n);

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1];
                uf.unionBySize(u, v);
            }

            return uf.getNumComponents();
        }

        private void dfs(int node, List<List<Integer>> graph, boolean[] visited) {
            visited[node] = true;

            for(int neighbor: graph.get(node)) {
                if(!visited[neighbor]) {
                    dfs(neighbor, graph, visited);;
                }
            }
        }

        private void bfsUsingQueue(int node, List<List<Integer>> graph, boolean[] visited) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(node);
            visited[node] = true;

            while (!queue.isEmpty()) {
                int currNode = queue.poll();

                for(int neighbor: graph.get(currNode)) {
                    if(!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
        }

        private void dfsUsingStack(int node, List<List<Integer>> graph, boolean[] visited) {
            Stack<Integer> stack = new Stack<>();
            stack.push(node);
            visited[node] = true;

            while (!stack.isEmpty()) {
                int currNode = stack.pop();

                for(int neighbor: graph.get(currNode)) {
                    if(!visited[neighbor]) {
                        visited[neighbor] = true;
                        stack.push(neighbor);
                    }
                }
            }
        }

        public int countComponents1(int n, int[][] edges) {
            boolean[] visited = new boolean[n];
            List<List<Integer>> graph = new ArrayList<>();
            int count = 0;

            for(int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1];
                graph.get(u).add(v);
                graph.get(v).add(u);
            }

            for(int i = 0; i < n; i++) {
                if(!visited[i]) {
                    count++;
//                    dfsUsingStack(i, graph, visited);
                    bfsUsingQueue(i, graph, visited);
                }
            }

            return count;
        }

        public int countComponents(int n, int[][] edges) {
            boolean[] visited = new boolean[n];
            List<List<Integer>> graph = new ArrayList<>();
            int count = 0;

            for(int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1];
                graph.get(u).add(v);
                graph.get(v).add(u);
            }

            for(int i = 0; i < n; i++) {
                if(!visited[i]) {
                    dfs(i, graph, visited);
                    count++;
                }
            }

            return count;
        }
    }

    class Revision01 {

        // ----------------------------------------------------------
        // ✅ Approach 3: Union-Find (Disjoint Set Union)
        // ----------------------------------------------------------
        class UnionFind {
            private int[] parent;
            private int[] size;
            private int numComponents; // Tracks how many disjoint sets remain

            UnionFind(int n) {
                this.parent = new int[n];
                this.size = new int[n];
                this.numComponents = n; // Initially, n separate components

                for (int i = 0; i < n; i++) {
                    parent[i] = i; // Each node is its own parent initially
                    size[i] = 1;
                }
            }

            // Find with path compression
            public int find(int node) {
                if (node == parent[node]) {
                    return node;
                }
                return parent[node] = find(parent[node]); // Compress path
            }

            // Union by size optimization
            public void unionBySize(int u, int v) {
                int up = find(u), vp = find(v);

                // Already in same component → no merge
                if (up == vp) {
                    return;
                }

                // Attach smaller component under larger one
                if (size[up] < size[vp]) {
                    parent[up] = vp;
                    size[vp] += size[up];
                } else {
                    parent[vp] = up;
                    size[up] += size[vp];
                }

                numComponents--; // Two sets merged → one less component
            }

            public int getComponents() {
                return numComponents;
            }
        }

        public int countComponents3(int n, int[][] edges) {
            UnionFind uf = new UnionFind(n);

            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                uf.unionBySize(u, v);
            }

            return uf.getComponents();
        }

        // ✅ Time Complexity: O(V + E * α(V)) — α(V) is inverse Ackermann (≈ constant)
        // ✅ Space Complexity: O(V) — for parent and size arrays

        // ----------------------------------------------------------
        // ✅ Approach 2: BFS (using Queue)
        // ----------------------------------------------------------
        private void bfsUsingQueue(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(node);
            visited[node] = true;

            while (!queue.isEmpty()) {
                int currNode = queue.poll();

                for (int neighbor : adjMap.get(currNode)) {
                    if (!visited[neighbor]) {
                        queue.offer(neighbor);
                        visited[neighbor] = true;
                    }
                }
            }
        }

        public int countComponents2(int n, int[][] edges) {
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

            boolean[] visited = new boolean[n];
            int count = 0; // Number of connected components

            // Run BFS from every unvisited node
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    bfsUsingQueue(i, adjMap, visited);
                    count++;
                }
            }

            return count;
        }

        // ✅ Time Complexity: O(V + E)
        // ✅ Space Complexity: O(V + E)
        // BFS explores each vertex and edge once

        // ----------------------------------------------------------
        // ✅ Approach 1: Iterative DFS (using Stack)
        // ----------------------------------------------------------
        private void dfsUsingStack(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited) {
            Stack<Integer> stack = new Stack<>();
            stack.push(node);
            visited[node] = true;

            while (!stack.isEmpty()) {
                int currNode = stack.pop();

                for (int neighbor : adjMap.get(currNode)) {
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                        visited[neighbor] = true;
                    }
                }
            }
        }

        public int countComponents1(int n, int[][] edges) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
            }

            boolean[] visited = new boolean[n];
            int count = 0;

            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    dfsUsingStack(i, adjMap, visited);
                    count++;
                }
            }

            return count;
        }

        // ✅ Time Complexity: O(V + E)
        // ✅ Space Complexity: O(V + E)

        // ----------------------------------------------------------
        // ✅ Approach 0: Recursive DFS
        // ----------------------------------------------------------
        private void dfs(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited) {
            visited[node] = true;

            for (int neighbor : adjMap.get(node)) {
                if (!visited[neighbor]) {
                    dfs(neighbor, adjMap, visited);
                }
            }
        }

        public int countComponents0(int n, int[][] edges) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
            }

            boolean[] visited = new boolean[n];
            int count = 0;

            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    dfs(i, adjMap, visited);
                    count++;
                }
            }

            return count;
        }

        // ✅ Time Complexity: O(V + E)
        // ✅ Space Complexity: O(V + E)
        // Note: recursive DFS may cause stack overflow for deep recursion
    }

    class Solution {
        private void bfs(int node, Map<Integer, List<Integer>> adjListMap, boolean[] visited) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(node);
            visited[node] = true;

            while (!queue.isEmpty()) {
                int currNode = queue.poll();

                for(int neighbor: adjListMap.get(currNode)) {
                    if(!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
        }

        public int countComponents1(int n, int[][] edges) {
            int count = 0;
            if(n <= 0 || edges == null || edges.length == 0) {
                return count;
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

            boolean[] visited = new boolean[n];

            for(int i = 0; i < n; i++) {
                if(!visited[i]) {
                    bfs(i, adjListMap, visited);
                    count++;
                }
            }

            return count;
        }

        private void dfs(int node, Map<Integer, List<Integer>> adjListMap, boolean[] visited) {
            visited[node] = true;

            for(int neighbor: adjListMap.get(node)) {
                if(!visited[neighbor]) {
                    dfs(neighbor, adjListMap, visited);
                }
            }
        }

        public int countComponents(int n, int[][] edges) {
            int count = 0;
            if(n <= 0 || edges == null || edges.length == 0) {
                return count;
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

            boolean[] visited = new boolean[n];

            for(int i = 0; i < n; i++) {
                if(!visited[i]) {
                    dfs(i, adjListMap, visited);
                    count++;
                }
            }

            return count;
        }
    }
}
