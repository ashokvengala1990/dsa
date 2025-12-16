package neetcode.graphs;

public class Redundant_Connection_II_LC_685 {
    class Solution {
        class UnionFind {
            private int[] parent;
            private int[] size;

            UnionFind(int n) {
                this.parent = new int[n];
                this.size = new int[n];

                for(int i = 0; i <= n; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }
            }

            public int find(int node) {
                if(node == parent[node]) {
                    return node;
                }

                return parent[node] = find(parent[node]);
            }

            public boolean unionBySize(int u, int v) {
                int up = find(u), vp = find(v);

                if(up == vp) {
                    return false;
                }

                if(size[up] < size[vp]) {
                    parent[up] = vp;
                    size[vp] += size[up];
                } else {
                    parent[vp] = up;
                    size[up] += size[vp];
                }

                return true;
            }
        }

        public int[] findRedundantDirectedConnection1(int[][] edges) {
            if(edges == null || edges.length == 0) {
                return new int[]{0};
            }

            int n = edges.length;
            UnionFind uf = new UnionFind(n+1);

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1];

                if(!uf.unionBySize(u, v)) {
                    return new int[]{u, v};
                }
            }

            return new int[]{};
        }
    }
}
