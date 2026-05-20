package neetcode.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/*
https://leetcode.com/problems/min-cost-to-connect-all-points/description/

1) Prim's Algorithm
TC: O(V log E)
SC: O(V + E)

2) Kruskal's Algorithm
TC: O(E log E)
SC: O(V + E)

 */

public class Min_Cost_to_Connect_All_Points_LC_1584 {
    class Solution {

        // Using Prim's Algorithm
        public int minCostConnectPoints1(int[][] points) {
            if (points == null || points.length == 0) {
                return 0;
            }

            int size = points.length;

            List<List<int[]>> graph = new ArrayList<>();

            for(int i =0; i < size; i++) {
                graph.add(new ArrayList<>());
            }

            for(int i = 0; i < size; i++) {
                int[] point1 = points[i];

                for(int j = i+1; j < size; j++) {
                    int[] point2 = points[j];
                    int dist = Math.abs(point1[0] - point2[0]) + Math.abs(point1[1] - point2[1]);
                    graph.get(i).add(new int[]{j, dist});
                    graph.get(j).add(new int[]{i, dist});
                }
            }

            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> Integer.compare(a[0],b[0]));
            minHeap.offer(new int[]{0, 0});
            boolean[] inMST = new boolean[size];
            int totalCost = 0, nodesInMST = 0;

            while (!minHeap.isEmpty() && nodesInMST < size) {
                int[] curr = minHeap.poll();
                int cost = curr[0], node = curr[1];

                if(inMST[node])
                    continue;

                totalCost += cost;
                nodesInMST++;
                inMST[node] = true;

                for(int[] edge: graph.get(node)) {
                    if(!inMST[edge[0]]) {
                        minHeap.offer(new int[]{edge[1], edge[0]});
                    }
                }
            }

            return nodesInMST == size ? totalCost : -1;
        }

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

        // Using Kruskal's Algorithm
        public int minCostConnectPoints0(int[][] points) {
            if (points == null || points.length == 0) {
                return 0;
            }

            int size = points.length;
            List<int[]> edges = new ArrayList<>();


            for (int i = 0; i < size; i++) {
                int[] point1 = points[i];
                for (int j = i + 1; j < size; j++) {
                    int[] point2 = points[j];

                    int dist = Math.abs(point1[0] - point2[0]) + Math.abs(point1[1] - point2[1]);

                    edges.add(new int[]{i, j, dist});

//                edges.add(new int[]{j, i, dist}); THis doesn't require because unionFind doesn't care this as it is already added
                }
            }

            edges.sort((a, b) -> Integer.compare(a[2], b[2]));

            UnionFind uf = new UnionFind(size);
            int totalCost = 0, edgesUsed = 0;

            for(int[] edge: edges) {
                if(uf.unionBySize(edge[0], edge[1])) {
                    totalCost += edge[2];
                    edgesUsed++;

                    if(edgesUsed == size-1) break;
                }
            }

            return edgesUsed == size-1 ? totalCost : -1;
        }
    }
}
