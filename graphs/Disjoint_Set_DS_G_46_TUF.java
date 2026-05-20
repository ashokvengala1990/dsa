package neetcode.graphs;

public class Disjoint_Set_DS_G_46_TUF {
    class DisjointSet {
        private int[] parent, size;

        DisjointSet(int n) {
            this.parent = new int[n+1];
            this.size = new int[n+1];

            for(int i = 0; i <= n; i++) {
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
}
