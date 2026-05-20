package neetcode.graphs;

import java.util.Arrays;

/*

https://www.geeksforgeeks.org/problems/distance-from-the-source-bellman-ford-algorithm/1

 */

public class Bellman_Ford_Algorithm {
    class Solution {
        public int[] bellmanFord(int n, int[][] edges, int src) {
            if(edges == null || edges.length == 0) {
                return new int[]{};
            }

            int[] distance = new int[n];
            Arrays.fill(distance, (int) 1e8);
            distance[src] = 0;

            for(int i =0; i < (n-1); i++) {
                for(int[] edge: edges) {
                    int u = edge[0], v = edge[1], wt = edge[2];

                    if(distance[u] != 1e8 && distance[u] + wt < distance[v]) {
                        distance[v] = distance[u] + wt;
                    }
                }
            }

            // Nth iteration helps to detect negative cycles.
            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], wt = edge[2];

                if(distance[u] != 1e8 && distance[u] + wt < distance[v]) {
                    return new int[]{-1};
                }
            }

            return distance;
        }
    }
}
