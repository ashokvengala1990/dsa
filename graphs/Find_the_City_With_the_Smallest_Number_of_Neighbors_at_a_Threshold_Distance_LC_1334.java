package neetcode.graphs;

import java.util.Arrays;
import java.util.Map;

/*
Convert the given input edges to Adjacency matrix format, where rows and columns are the nodes or vertices and each cell
value indicates the edge weight between v1 and v2 vertices.

https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/description/

Floyd–Warshall Summary Notes (LeetCode 1334):
- Used for all-pairs shortest path
- Works with non-negative & negative edges
- Time: O(n³), Space: O(n²)

Initialization
- dist[i][j] = INF → no path
- dist[i][i] = 0 → self distance
- dist[u][v] = wt, dist[v][u] = wt → edge

🚫 Never use distanceThreshold as INF:
Core Formula:
dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

Threshold Usage:
- Applied after shortest paths computed
- Used only for counting reachable cities

Tie Rule:
- If multiple cities have same count → return largest index

When to Use
- Need shortest paths between all cities
- n ≤ 100
- Simpler than running Dijkstra multiple times

Common Mistakes:
- Using threshold as initial distance
- Forgetting dist[i][i] = 0
- Integer overflow with Integer.MAX_VALUE

One-Line Recall:
- Compute all-pairs shortest paths first, then count cities within threshold and break ties by index.
 */
public class Find_the_City_With_the_Smallest_Number_of_Neighbors_at_a_Threshold_Distance_LC_1334 {
    class Revision01 {
        public int findTheCity(int n, int[][] edges, int distanceThreshold) {
            int[][] distance = new int[n][n];
            for(int[] d: distance) {
                Arrays.fill(d, Integer.MAX_VALUE);
            }
            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], wt = edge[2];
                distance[u][v] = wt;
                distance[v][u] = wt;
            }

            for(int i = 0; i < n; i++) {
                distance[i][i] = 0;
            }

            for(int k = 0; k < n; k++) {
                for(int u = 0; u < n; u++) {
                    for(int v = 0; v < n; v++) {
                        if(distance[u][k] != Integer.MAX_VALUE && distance[k][v] != Integer.MAX_VALUE) {
                            distance[u][v] = Math.min(distance[u][v], distance[u][k] + distance[k][v]);
                        }
                    }
                }
            }

            int cityCnt = n, cityNo = -1;

            for(int city = 0; city < n; city++) {
                int currCityCnt = 0;

                for(int adjCity = 0; adjCity < n; adjCity++) {
                    if(distance[city][adjCity] <= distanceThreshold) {
                        currCityCnt++;
                    }
                }

                if(currCityCnt <= cityCnt) {
                    cityCnt = currCityCnt;
                    cityNo = city;
                }
            }

            return cityNo;
        }
    }

    class Solution {
        public int findTheCity(int n, int[][] edges, int distanceThreshold) {

            // Step 1: distance[i][j] = shortest distance from i to j
            int[][] distance = new int[n][n];

            // Initialize all distances as INF (no path)
            for (int i = 0; i < n; i++) {
                Arrays.fill(distance[i], Integer.MAX_VALUE);
            }

            // Distance to self is 0
            for (int i = 0; i < n; i++) {
                distance[i][i] = 0;
            }

            // Fill direct edge weights (undirected graph)
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], wt = edge[2];
                distance[u][v] = wt;
                distance[v][u] = wt;
            }

            // Step 2: Floyd–Warshall Algorithm
            // Try every node as an intermediate
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {

                        // Only relax if paths exist
                        if (distance[i][k] != Integer.MAX_VALUE &&
                                distance[k][j] != Integer.MAX_VALUE &&
                                distance[i][k] + distance[k][j] < distance[i][j]) {

                            distance[i][j] = distance[i][k] + distance[k][j];
                        }
                    }
                }
            }

            // Step 3: Find city with minimum reachable cities within threshold
            int cityCnt = n;
            int cityNo = -1;

            for (int city = 0; city < n; city++) {
                int currCityCnt = 0;

                for (int adjCity = 0; adjCity < n; adjCity++) {
                    if (distance[city][adjCity] <= distanceThreshold) {
                        currCityCnt++;
                    }
                }

                // If tie, pick city with greater index
                if (currCityCnt <= cityCnt) {
                    cityCnt = currCityCnt;
                    cityNo = city;
                }
            }

            return cityNo;
        }
    }
}
