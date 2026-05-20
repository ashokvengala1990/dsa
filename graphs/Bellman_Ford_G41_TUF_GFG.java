package neetcode.graphs;

import java.util.Arrays;

/*
https://www.geeksforgeeks.org/problems/distance-from-the-source-bellman-ford-algorithm/1
 */

public class Bellman_Ford_G41_TUF_GFG {
    class Solution {
        public int[] bellmanFord(int V, int[][] edges, int src) {

            int[] distance = new int[V];
            Arrays.fill(distance, (int)1e8);
            distance[src] = 0;

            for(int i = 0; i < (V-1); i++) {
                for(int[] edge: edges) {
                    int u = edge[0], v = edge[1], wt = edge[2];

                    if(distance[u] != (int)1e8 && distance[u] + wt < distance[v]) {
                        distance[v] = distance[u] + wt;
                    }
                }
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], wt = edge[2];

                if(distance[u] != (int)1e8 && distance[u] + wt < distance[v]) {
                    return new int[]{-1};
                }
            }

            return distance;
        }
    }
}
