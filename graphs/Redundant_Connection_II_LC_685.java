package neetcode.graphs;

import java.util.Arrays;

/*
Redundant Connection II — Revision Notes
Core Insight
Directed tree + 1 extra edge breaks it in 3 ways:
Case 1: Two parents, no cycle     → remove later parent edge (cand2)
For example:
1 -> 2, 2 -> 3, 1 -> 3

Case 2: Pure cycle, no two parents → remove cycle edge
For example:
1 -> 2, 2 -> 3, 3 -> 1

Case 3: Two parents + cycle        → remove earlier parent edge (cand1)
For example:
1 -> 2, 2 -> 3, 3 -> 2

Algorithm
1. Find candidates: node with inDegree == 2 → cand1 (first), cand2 (later)
2. If candidates exist:
   - Try removing cand2 → valid tree? return cand2 (Case 1)
   - Else return cand1 (Case 3)
3. No candidates → findCycleEdge with UnionFind (Case 2)
Key Code Tricks
java// Reference equality works here — same array reference from edges[]
if (edge == skip) continue;

// isValidTree: run UF skipping candidate, return false if cycle found
// findCycleEdge: run UF, return edge when union fails (same component)
Why cand2 first?
Problem says return last occurrence → try removing later edge first.
TC / SC:
O(n) time, O(n) space
 */

public class Redundant_Connection_II_LC_685 {
    class Revision02 {
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

        private boolean isValidTree(int[][] edges, int[] skip) {
            int n = edges.length;
            UnionFind uf = new UnionFind(n+1);

            for(int[] edge: edges) {
                if(Arrays.equals(edge, skip)) {
                    continue;
                }

                int u = edge[0], v = edge[1];
                if(!uf.unionBySize(u, v)) {
                    return false;
                }
            }

            return true;
        }

        private int[] findCycleEdge(int[][] edges) {
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

        public int[] findRedundantDirectedConnection(int[][] edges) {
            if(edges == null || edges.length == 0) {
                return new int[]{};
            }

            int n = edges.length;
            int[] inDegree = new int[n+1];

            for(int[] edge: edges) {
                inDegree[edge[1]]++;
            }

            int[] cand1 = null, cand2 = null;

            for(int[] edge: edges) {
                if(inDegree[edge[1]] == 2) {
                    if(cand1 == null) {
                        cand1 = edge;
                    } else {
                        cand2 = edge;
                    }
                }
            }

            if(cand1 != null) {
                if(isValidTree(edges, cand2)) {
                    return cand2;
                }
                return cand1;
            }

            return findCycleEdge(edges);
        }
    }

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

        public int[] findRedundantDirectedConnection0(int[][] edges) {
            int n = edges.length;

            // Step 1: Find candidates — node with two parents
            int[] inDegree = new int[n + 1];
            for (int[] edge : edges) inDegree[edge[1]]++;

            int[] cand1 = null, cand2 = null;
            for (int[] edge : edges) {
                if (inDegree[edge[1]] == 2) {
                    if (cand1 == null) cand1 = edge;
                    else cand2 = edge;
                }
            }

            // Step 2: If two parent candidates exist
            if (cand1 != null) {
                // Try removing cand2 (later edge) first
                if (isValidTree(edges, cand2, n)) return cand2;
                return cand1; // cand2 removal didn't work → remove cand1
            }

            // Step 3: No two parents → pure cycle, find cycle edge
            return findCycleEdge(edges, n);
        }

        private boolean isValidTree(int[][] edges, int[] skip, int n) {
            UnionFind uf = new UnionFind(n + 1);
            for (int[] edge : edges) {
                if (edge == skip) continue; // skip candidate edge
                if (!uf.unionBySize(edge[0], edge[1])) return false;
            }
            return true;
        }

        private int[] findCycleEdge(int[][] edges, int n) {
            UnionFind uf = new UnionFind(n + 1);
            for (int[] edge : edges) {
                if (!uf.unionBySize(edge[0], edge[1])) return edge;
            }
            return new int[]{};
        }
    }

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
