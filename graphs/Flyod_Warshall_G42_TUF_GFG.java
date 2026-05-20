package neetcode.graphs;

/*
https://www.geeksforgeeks.org/problems/implementing-floyd-warshall2042/1
TC: O(n^3) or O(v^3)
 */

public class Flyod_Warshall_G42_TUF_GFG {
    class Solution {
        public void floydWarshall(int[][] dist) {

            int n = dist[0].length;
            int INF = (int) 1e8;

            // For each intermediate vertex between source and destination
            for(int k = 0; k < n; k++) {

                // Pick all vertices as source one by one
                for(int u = 0; u < n; u++) {

                    // Pick all vertices as destination for the above picked source
                    for(int v = 0; v < n; v++) {

                        // Shortest path from u to v
                        if(dist[u][k] != INF && dist[k][v] != INF) {
                            dist[u][v] = Math.min(dist[u][v], dist[u][k]+dist[k][v]);
                        }
                    }
                }
            }

            for(int i =0 ; i< n; i++) {
                if(dist[i][i] < 0) {
                    System.out.println("Negative cycle exists");
                    return;
                }
            }

            System.out.println("Able to find the shortest path from multiple sources to destination of all other vertices");
        }
    }
}
