package neetcode.graphs;

public class Number_of_Operations_to_Make_Network_Connected_LC_1319 {
    class Solution {
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

        public int makeConnected(int n, int[][] connections) {
            if(connections == null || connections.length == 0) {
                return 0;
            }

            DisjointSet ds = new DisjointSet(n);
            int extraEdges = 0;

            // E
            for(int[] edge: connections) {
                int node1 = edge[0], node2 = edge[1];

                if(!ds.unionBySize(node1, node2)) {
                    extraEdges++;
                }
            }

            int result = ds.getNumComponents()-1;

            if(extraEdges >= result) {
                return result;
            } else {
                return -1;
            }
        }
    }
}
