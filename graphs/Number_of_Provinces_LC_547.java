package neetcode.graphs;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*

https://leetcode.com/problems/number-of-provinces/description/

Summary of All Approaches
Approach	Method	Time Complexity	Space Complexity	Notes
Recursive DFS	findCircleNum0	O(V²)	O(V)	Simple, easy to implement
Iterative DFS (Stack)	findCircleNum2	O(V²)	O(V)	Avoids recursion limit
BFS (Queue)	findCircleNum3	O(V²)	O(V)	Level-by-level traversal
Union-Find	findCircleNum1	O(V² × α(V)) ≈ O(V²)	O(V)	Most efficient in practice for dense graphs

Input is an adjacency matrix, and we need to do traversal differently compared to Matrix and adjacency list approach.


 */

public class Number_of_Provinces_LC_547 {
    class Revision03 {
        class DisjointSet {
            private int[] parent, size;
            private int noOfComponents;

            DisjointSet(int n) {
                parent = new int[n+1];
                size = new int[n+1];

                for(int i = 0; i <= n; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }

                noOfComponents = n;
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

                noOfComponents--;
                return true;
            }

            public int getNoOfComponents() {
                return noOfComponents;
            }
        }

        public int findCircleNum(int[][] isConnected) {
            int n = isConnected.length;
            DisjointSet ds = new DisjointSet(n);

            for(int u = 0; u < n; u++) {
                for(int v = 0; v < n; v++) {
                    if(isConnected[u][v] == 1) {
                        ds.unionBySize(u, v);
                    }
                }
            }

            return ds.getNoOfComponents();
        }
    }

    class Revision02 {
        class DisjointSet {
            int[] parent, size;
            int numComponents;

            DisjointSet(int n) {
                this.parent = new int[n];
                this.size = new int[n];

                for(int i = 0; i < n; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }
                numComponents = n;
            }

            public int findUltimateParent(int node) {
                if(node == parent[node]) {
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
                    size[ulpv] += size[ulpu];
                    parent[ulpu] = ulpv;
                } else {
                    size[ulpu] += size[ulpv];
                    parent[ulpv] = ulpu;
                }

                numComponents--;
            }

            public int getNumComponents() {
                return numComponents;
            }
        }

        public int findCircleNum3(int[][] isConnected) {
            if(isConnected == null || isConnected.length == 0) {
                return 0;
            }

            int size = isConnected.length;
            DisjointSet ds = new DisjointSet(size);

            for(int node = 0; node < size; node++) {
                for(int neighborNode = 0; neighborNode < size; neighborNode++) {
                    if(isConnected[node][neighborNode] == 1) {
                        ds.unionBySize(node, neighborNode);
                    }
                }
            }

            return ds.getNumComponents();
        }

        private void bfsUsingQueue(int node, int[][] isConnected, boolean[] visited) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(node);
            visited[node] = true;

            int size = isConnected.length;

            while (!queue.isEmpty()) {
                int currNode = queue.poll();

                for(int neighborNode = 0; neighborNode < size; neighborNode++) {
                    if(isConnected[currNode][neighborNode] == 1 && !visited[neighborNode]) {
                        queue.offer(neighborNode);
                        visited[neighborNode] = true;
                    }
                }
            }
        }

        public int findCircleNum2(int[][] isConnected) {
            int numProvinces = 0;
            if(isConnected == null || isConnected.length == 0) {
                return numProvinces;
            }

            int size = isConnected.length;
            boolean[] visited = new boolean[size];

            for(int node = 0; node < size; node++) {
                if(!visited[node]) {
                    bfsUsingQueue(node, isConnected, visited);
                    numProvinces++;
                }
            }

            return numProvinces;
        }

        private void dfsUsingStack(int node, int[][] isConnected, boolean[] visited) {
            Stack<Integer> stack = new Stack<>();
            stack.push(node);
            visited[node] = true;

            int size = isConnected.length;

            while (!stack.isEmpty()) {
                int currNode = stack.pop();

                for(int neighborNode = 0; neighborNode < size; neighborNode++) {
                    if(isConnected[currNode][neighborNode] == 1 && !visited[neighborNode]) {
                        stack.push(neighborNode);
                        visited[neighborNode] = true;
                    }
                }
            }
        }

        public int findCircleNum1(int[][] isConnected) {
            int numProvinces = 0;
            if(isConnected == null || isConnected.length == 0) {
                return numProvinces;
            }

            int size = isConnected.length;
            boolean[] visited = new boolean[size];

            for(int node = 0; node < size; node++) {
                if(!visited[node]) {
                    dfsUsingStack(node, isConnected, visited);
                    numProvinces++;
                }
            }

            return numProvinces;
        }

        private void dfs(int node, int[][] isConnected, boolean[] visited) {
            visited[node] = true;

            for(int neighborNode = 0; neighborNode < isConnected.length; neighborNode++) {
                if(isConnected[node][neighborNode] == 1 && !visited[neighborNode]) {
                    dfs(neighborNode, isConnected, visited);
                }
            }
        }

        public int findCircleNum0(int[][] isConnected) {
            int numProvinces = 0;
            if(isConnected == null || isConnected.length == 0) {
                return numProvinces;
            }

            int size = isConnected.length;
            boolean[] visited = new boolean[size];

            for(int node = 0; node < size; node++) {
                if(!visited[node]) {
                    dfs(node, isConnected, visited);
                    numProvinces++;
                }
            }

            return numProvinces;
        }
    }

    class DisjointSet {
        private int[] parent, size;
        private int numComponents;

        DisjointSet(int n) {
            this.parent = new int[n];
            this.size = new int[n];

            for(int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }

             numComponents = n;
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

            numComponents--;

            return true;
        }

        public int getNumComponents() {
            return numComponents;
        }
    }

    class Revision01 {
        public int findCircleNum3(int[][] isConnected) {
            if(isConnected == null || isConnected.length == 0) {
                return 0;
            }

            int size = isConnected.length;
            DisjointSet ds = new DisjointSet(size);

            for(int node = 0; node < size; node++) {
                for(int neighNode = 0; neighNode < size; neighNode++) {
                    if(isConnected[node][neighNode] == 1) {
                        ds.unionBySize(node, neighNode);
                    }
                }
            }

            return ds.getNumComponents();
        }

        private void bfs(int node, int[][] isConnected, boolean[] visited) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(node);
            visited[node] = true;
            int size = isConnected.length;

            while (!queue.isEmpty()) {
                int currNode = queue.poll();

                for(int neighNode = 0; neighNode < size; neighNode++) {
                    if(isConnected[currNode][neighNode] == 1 && !visited[neighNode]) {
                        queue.offer(neighNode);
                        visited[neighNode] = true;
                    }
                }
            }
        }

        public int findCircleNum2(int[][] isConnected) {
            int numProvinces = 0;
            if(isConnected == null || isConnected.length == 0) {
                return numProvinces;
            }

            int size = isConnected.length;
            boolean[] visited = new boolean[size];

            for(int node = 0; node < size; node++) {
                if(!visited[node]) {
                    bfs(node, isConnected, visited);
                    numProvinces++;
                }
            }

            return numProvinces;
        }

        private static final int[][] offsetDirections = {{-1, 0},{0, 1},{0, 1},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        private void dfsUsingStack(int node, int[][] isConnected, boolean[] visited) {
            Stack<Integer> stack = new Stack<>();
            stack.push(node);
            visited[node] = true;
            int size = isConnected.length;

            while (!stack.isEmpty()) {
                int currNode = stack.pop();

                for(int neighNode = 0; neighNode < size; neighNode++) {
                    if(isConnected[currNode][neighNode] == 1 && !visited[neighNode]) {
                        stack.push(neighNode);
                        visited[neighNode] = true;
                    }
                }
            }
        }

        public int findCircleNum1(int[][] isConnected) {
            int numProvinces = 0;
            if(isConnected == null || isConnected.length == 0) {
                return numProvinces;
            }

            int size = isConnected.length;
            boolean[] visited = new boolean[size];

            for(int node = 0; node < size; node++) {
                if(!visited[node]) {
                    dfsUsingStack(node, isConnected, visited);
                    numProvinces++;
                }
            }

            return numProvinces;
        }

        private void dfs(int node, int[][] isConnected, boolean[] visited) {
            visited[node] = true;

            for(int neighNode = 0 ; neighNode < isConnected.length; neighNode++) {
                if(isConnected[node][neighNode] == 1 && !visited[neighNode]) {
                    dfs(neighNode, isConnected, visited);
                }
            }
        }

        public int findCircleNum0(int[][] isConnected) {
            if(isConnected == null || isConnected.length == 0) {
                return 0;
            }

            int size = isConnected.length;
            boolean[] visited = new boolean[size];
            int numProvinces = 0;

            for(int node = 0; node < size; node++) {
                if(!visited[node]) {
                    dfs(node, isConnected, visited);
                    numProvinces++;
                }
            }

            return numProvinces;
        }
    }

     class Solution {
            /*
             * ------------------------------
             * ✅ UNION-FIND (Disjoint Set)
             * ------------------------------
             * Idea:
             * - Each city is a node.
             * - If isConnected[i][j] == 1, merge (union) them in the same component.
             * - The number of disjoint sets after processing all edges = number of provinces.
             *
             * TC: O(V^2 * α(V)) ≈ O(V^2)
             * SC: O(V)
             */
            class UnionFind {
                private int[] parent;
                private int[] size;
                private int numComponents;

                UnionFind(int n) {
                    this.parent = new int[n];
                    this.size = new int[n];
                    this.numComponents = n; // Initially, each node is its own province

                    for (int i = 0; i < n; i++) {
                        parent[i] = i;
                        size[i] = 1;
                    }
                }

                // Path compression to flatten the tree
                public int find(int node) {
                    if (node == parent[node]) {
                        return node;
                    }
                    return parent[node] = find(parent[node]);
                }

                // Union by size (attach smaller tree under larger tree)
                public void unionBySize(int u, int v) {
                    int up = find(u), vp = find(v);

                    if (up == vp) {
                        return; // already in same component
                    }

                    if (size[up] < size[vp]) {
                        parent[up] = vp;
                        size[vp] += size[up];
                    } else {
                        parent[vp] = up;
                        size[up] += size[vp];
                    }

                    numComponents--; // merging two components reduces total count by 1
                }

                public int getNumComponents() {
                    return numComponents;
                }
            }

            // -----------------------------
            // 🔹 1️⃣ Union-Find Approach
            // -----------------------------
            public int findCircleNum1(int[][] isConnected) {
                if (isConnected == null || isConnected.length == 0) return 0;

                int n = isConnected.length;
                UnionFind uf = new UnionFind(n);

                for (int r = 0; r < n; r++) {
                    for (int c = 0; c < n; c++) {
                        if (r != c && isConnected[r][c] == 1) {
                            uf.unionBySize(r, c);
                        }
                    }
                }
                return uf.getNumComponents();
            }

            /*
             * -----------------------------------
             * ✅ BFS Approach (Using Queue)
             * -----------------------------------
             * Idea:
             * - Traverse each unvisited node (city)
             * - Start BFS → mark all reachable cities as visited
             * - Each BFS traversal = 1 new province
             *
             * TC: O(V^2) since adjacency matrix has V^2 entries
             * SC: O(V) for visited array and queue
             */
            private void bfsUsingQueue(int node, int[][] isConnected, boolean[] visited) {
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(node);
                visited[node] = true;

                while (!queue.isEmpty()) {
                    int currNode = queue.poll();

                    for (int i = 0; i < isConnected.length; i++) {
                        // Visit all connected, unvisited cities
                        if (isConnected[currNode][i] == 1 && !visited[i]) {
                            queue.offer(i);
                            visited[i] = true;
                        }
                    }
                }
            }

            public int findCircleNum3(int[][] isConnected) {
                if (isConnected == null || isConnected.length == 0) return 0;

                int n = isConnected.length, numComponents = 0;
                boolean[] visited = new boolean[n];

                for (int i = 0; i < n; i++) {
                    if (!visited[i]) {
                        numComponents++;
                        bfsUsingQueue(i, isConnected, visited);
                    }
                }
                return numComponents;
            }

            /*
             * -----------------------------------
             * ✅ DFS (Iterative using Stack)
             * -----------------------------------
             * Similar to BFS but uses stack (LIFO)
             *
             * TC: O(V^2)
             * SC: O(V)
             */
            private void dfsUsingStack(int node, int[][] isConnected, boolean[] visited) {
                Stack<Integer> stack = new Stack<>();
                stack.push(node);
                visited[node] = true;

                while (!stack.isEmpty()) {
                    int currNode = stack.pop();

                    for (int i = 0; i < isConnected.length; i++) {
                        if (isConnected[currNode][i] == 1 && !visited[i]) {
                            stack.push(i);
                            visited[i] = true;
                        }
                    }
                }
            }

            public int findCircleNum2(int[][] isConnected) {
                if (isConnected == null || isConnected.length == 0) return 0;

                int n = isConnected.length, numComponents = 0;
                boolean[] visited = new boolean[n];

                for (int i = 0; i < n; i++) {
                    if (!visited[i]) {
                        numComponents++;
                        dfsUsingStack(i, isConnected, visited);
                    }
                }

                return numComponents;
            }

            /*
             * -----------------------------------
             * ✅ DFS (Recursive)
             * -----------------------------------
             * TC: O(V^2)
             * SC: O(V) recursion stack
             */
            private void dfs(int node, int[][] isConnected, boolean[] visited) {
                visited[node] = true;

                for (int i = 0; i < isConnected.length; i++) {
                    if (isConnected[node][i] == 1 && !visited[i]) {
                        dfs(i, isConnected, visited);
                    }
                }
            }

            public int findCircleNum0(int[][] isConnected) {
                if (isConnected == null || isConnected.length == 0) return 0;

                int n = isConnected.length, numComponents = 0;
                boolean[] visited = new boolean[n];

                for (int i = 0; i < n; i++) {
                    if (!visited[i]) {
                        numComponents++;
                        dfs(i, isConnected, visited);
                    }
                }

                return numComponents;
            }
     }
}
