package neetcode.graphs;

import java.util.Arrays;

/*
https://www.geeksforgeeks.org/problems/distance-from-the-source-bellman-ford-algorithm/1

📘 Bellman–Ford Algorithm — One-Place Revision Notes
🔹 What Bellman–Ford Solves
- Single-source shortest paths
- Works with negative edge weights
- Detects negative cycles

🔹 Key Idea (MEMORIZE)
- A shortest path can have at most (V − 1) edges.
- So relax all edges (V − 1) times.

🔹 Core Relaxation Rule
- For each edge (u → v, wt):
if dist[u] + wt < dist[v]:
    dist[v] = dist[u] + wt

🔹 Algorithm Steps
1) Initialize all distances as INF
2) Set dist[src] = 0
3) Repeat V−1 times:
    - Relax all edges
4) Do one extra iteration:
    - If any distance improves → negative cycle exists

🔹 Negative Cycle Detection (VERY IMPORTANT)
- If relaxation is still possible in the Nth iteration
- → shortest path does not exist
- → return -1

🔹 When to Use Bellman–Ford
Situation	Use
Negative edges	✅
Negative cycle detection	✅
Single source	✅
All-pairs shortest path	❌ (use Floyd–Warshall)
⏱️ Time Complexity (TC): O(V × E)

🧮 Space Complexity (SC): O(V)

🔑 Common Mistakes
❌ Forgetting the Nth iteration
❌ Using Dijkstra with negative edges
❌ Not checking distance[u] != INF
❌ Thinking Bellman–Ford is faster than Dijkstra

⭐ One-Line Interview Answer

Bellman–Ford computes shortest paths by repeatedly relaxing all edges and detects negative cycles when distances still improve
after V−1 iterations.
 */

/*
Bellman–Ford Algorithm
- Handles negative edge weights
- Detects negative cycles
- Time: O(V * E)
- Space: O(V)
*/
public class Bellman_Ford_Algorithm_G41_TUF {

    static class Solution {

        public int[] bellmanFord(int n, int[][] edges, int src) {

            // Distance array
            int[] distance = new int[n];

            // Initialize all distances as INF
            Arrays.fill(distance, Integer.MAX_VALUE);

            // Distance from source to itself is 0
            distance[src] = 0;

            // Step 1: Relax all edges (V - 1) times
            for (int i = 0; i < n - 1; i++) {
                for (int[] edge : edges) {

                    int u = edge[0];
                    int v = edge[1];
                    int wt = edge[2];

                    // Relax edge if possible
                    if (distance[u] != Integer.MAX_VALUE &&
                            distance[u] + wt < distance[v]) {

                        distance[v] = distance[u] + wt;
                    }
                }
            }

            // Step 2: Extra iteration to detect negative cycle
            for (int[] edge : edges) {

                int u = edge[0];
                int v = edge[1];
                int wt = edge[2];

                // If relaxation is still possible, negative cycle exists
                if (distance[u] != Integer.MAX_VALUE &&
                        distance[u] + wt < distance[v]) {

                    return new int[]{-1}; // Negative cycle detected
                }
            }

            return distance;
        }
    }

    public static void main(String[] args) {

        Solution s = new Solution();

        int n = 3, src = 0;

        // Graph with a negative cycle
        int[][] edges = {
                {0, 1, -2},
                {1, 2, -3},
                {2, 0, 2}
        };

        int[] result = s.bellmanFord(n, edges, src);

        for (int d : result) {
            System.out.print(d + " ");
        }
    }
}
