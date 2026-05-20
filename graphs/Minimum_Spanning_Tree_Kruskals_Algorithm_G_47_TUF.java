package neetcode.graphs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Minimum_Spanning_Tree_Kruskals_Algorithm_G_47_TUF {
    class Solution {
        class DisjointSet {
            private int[] parent, size;

            DisjointSet(int n) {
                parent = new int[n+1];
                size = new int[n+1];

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

        public int kruskalsMST(int n, int[][] edges) {
            List<int[]> newEdges = new ArrayList<>();

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], wt = edge[2];
                newEdges.add(new int[]{wt, u, v});
            }

            newEdges.sort(new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    return o1[0] - o2[0];
                }
            });

            DisjointSet ds = new DisjointSet(n);
            int mstWt = 0;

            for(int[] edge: newEdges) {
                int wt = edge[0], u = edge[1], v = edge[2];

                if(ds.unionBySize(u, v)) {
                    mstWt += wt;
                }
            }

            return mstWt;
        }
    }
}
