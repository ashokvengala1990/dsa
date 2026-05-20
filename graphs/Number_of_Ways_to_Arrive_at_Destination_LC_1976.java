package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/number-of-ways-to-arrive-at-destination/description/

Problem Pattern:
Find the number of shortest paths from node 0 to node n-1 in a weighted graph.

Approach 01: Brute Force
Why is this exponential?
Key reason:
- DFS is exploring ALL simple paths from node 0 to node n-1.

Worst-case intuition (important for revision)
- At each node, you can go to many unvisited neighbors
- Depth of recursion can go up to V
- Choices multiply at every level

So number of paths ≈
(V − 1) × (V − 2) × (V − 3) × ... × 1


That is:
≈ (V − 1)!   → factorial growth

Your question: is it like (V-1)^V?
✔️ Close intuition, but more accurate is:
- Upper bound: O(V!)
- Because DFS explores permutations of nodes (simple paths)

Both (V-1)^V and V! mean:
Explodes extremely fast

⏱️ Time & Space Complexity
Time Complexity (Worst Case)
O(V!)
- All simple paths explored
- Pruning helps a little, but worst case still factorial

Space Complexity
O(V)

- Recursion stack
- visited[] array

⚠️ Why this TLEs on LeetCode
Constraints:
n ≤ 200
edges ≤ n*(n-1)/2
- Number of paths is astronomical
- DFS brute force is impossible even for n ≈ 20



Approach 02: Optimal
Core Idea
- Use Dijkstra to compute shortest distances
- Along with distance, maintain:
    * ways[i] = number of shortest paths to reach node i

Algorithm Rules
1. Initialization
    * distance[i] = ∞ (Long.MAX_VALUE)
    * distance[0] = 0
    * ways[0] = 1
2. Relaxation Logic
    * If newDist < distance[v]
        - Update distance
        - ways[v] = ways[u]
    * If newDist == distance[v]
        - ways[v] += ways[u] (mod 1e9+7)

3. Priority Queue
- Min-heap ordered by distance
- Skip outdated entries:
    * if (dist > distance[node]) continue;

Why long Distance is Mandatory
- Edge weight ≤ 1e9
- Path length can exceed 2^31 − 1
- Using int causes overflow → wrong Dijkstra

Common Mistakes (EXAM / LC TRAPS)
❌ Using int for distance
❌ Forgetting Arrays.fill(distance, Long.MAX_VALUE)
❌ Not handling newDist == distance[v] case
❌ Missing outdated PQ check

Time & Space Complexity
Time: O(E log V)
Space: O(V + E)

Short Revision Summary (Focused)
Can DFS be used?
- ✅ Yes, logically correct
- ❌ Not scalable

Why it fails?
- DFS enumerates all simple paths
- Number of paths grows ~ V!

When DFS is OK
- Very small graphs
- Educational / brute-force reasoning

Correct scalable solution
- Dijkstra + DP (ways[])
- Time: O(E log V)

Memory hook
- “Shortest paths → weighted graph → Dijkstra, never DFS brute force.”


--> countPathsWithPath: this will print all paths from 0 to n-1 which has shortest distance.
 */

public class Number_of_Ways_to_Arrive_at_Destination_LC_1976 {
    class Revision01 {

        private void helper(int node, List<List<Integer>> parents, List<Integer> currPath, List<List<Integer>> result) {
            if(node == 0) {
                List<Integer> tmp = new ArrayList<>(currPath);
                tmp.add(node);
                Collections.reverse(tmp);
                result.add(tmp);
                return;
            }

            currPath.add(node);

            for(int p: parents.get(node)) {
                helper(p, parents, currPath, result);
            }

            currPath.remove(currPath.size()-1);
        }

        private void printAllPaths(List<List<Integer>> parents, int n) {
            List<List<Integer>> result = new ArrayList<>();

            helper(n-1, parents, new ArrayList<>(), result);
            System.out.println(result);
        }

        public int countPathsWithPath(int n, int[][] roads) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();
            for(int i = 0; i < n ; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] road: roads) {
                int u = road[0], v = road[1], time = road[2];
                adjMap.get(u).add(new int[]{v, time});
                adjMap.get(v).add(new int[]{u, time});
            }

            long[] distance = new long[n];
            int[] ways = new int[n];
            List<List<Integer>> parents = new ArrayList<>();

            Arrays.fill(distance, Long.MAX_VALUE);
            distance[0] = 0;
            ways[0] = 1;
            for(int i = 0; i < n; i++) {
                parents.add(new ArrayList<>());
            }

            PriorityQueue<long[]> minHeapPQ = new PriorityQueue<>((a,b) -> Long.compare(a[0], b[0]));
            minHeapPQ.offer(new long[]{0, 0, });

            while (!minHeapPQ.isEmpty()) {
                long[] curr = minHeapPQ.poll();
                long currTime = curr[0];
                int currNode = (int) curr[1];

                if(currTime > distance[currNode]) {
                    continue;
                }

                for(int[] neighbor: adjMap.get(currNode)) {
                    int nextNode = neighbor[0], edgeTime = neighbor[1];
                    long newTime = currTime + edgeTime;

                    if(newTime < distance[nextNode]) {
                        ways[nextNode] = ways[currNode];
                        distance[nextNode] = newTime;
                        minHeapPQ.offer(new long[]{newTime, nextNode});
                        parents.get(nextNode).clear();
                        parents.get(nextNode).add(currNode);
                    } else if(newTime == distance[nextNode]) {
                        ways[nextNode] = (ways[currNode] + ways[nextNode]) % mod;
                        parents.get(nextNode).add(currNode);
                    }
                }
            }

            printAllPaths(parents, n);

            return ways[n-1];
        }

        public int countPaths(int n, int[][] roads) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();
            for(int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] road: roads) {
                int u = road[0], v = road[1], time = road[2];
                adjMap.get(u).add(new int[]{v, time});
                adjMap.get(v).add(new int[]{u, time});
            }

            long[] distance = new long[n];
            int[] ways = new int[n];

            Arrays.fill(distance, Long.MAX_VALUE);
            distance[0] = 0;
            ways[0] = 1;

            PriorityQueue<long[]> minHeapPQ = new PriorityQueue<>((a, b) -> (Long.compare(a[0], b[0])));
            minHeapPQ.offer(new long[]{0, 0});

            while (!minHeapPQ.isEmpty()) {
                long[] curr = minHeapPQ.poll();
                long currTime = curr[0];
                int currNode = (int) curr[1];

                if(currTime > distance[currNode]) {
                    continue;
                }

                for(int[] neighbor: adjMap.get(currNode)) {
                    int nextNode = neighbor[0], edgeTime = neighbor[1];
                    long newTime = currTime + edgeTime;

                    if(newTime < distance[nextNode]) {
                        ways[nextNode] = ways[currNode];
                        distance[nextNode] = newTime;
                        minHeapPQ.offer(new long[]{newTime, nextNode});
                    } else if(newTime == distance[nextNode]) {
                        ways[nextNode] = (ways[nextNode] + ways[currNode]) % mod;
                    }
                }
            }

            return ways[n-1];
        }

        private long minTime;
        private int ways;
        private int mod = (int)(1e9+7);
        private int dest;

        private void dfs(int node, Map<Integer, List<int[]>> adjMap, boolean[] visited, long currTime) {
            if(currTime > minTime) {
                return;
            }
            if(node == dest) {
                if(currTime < minTime) {
                    minTime = currTime;
                    ways = 1;
                } else if(currTime == minTime) {
                    ways = (ways + 1) % mod;
                }
                return;
            }

            visited[node]  = true;

            for(int[] neighbor: adjMap.get(node)) {
                int nextNode = neighbor[0], edgeTime = neighbor[1];
                if(!visited[nextNode]) {
                    dfs(nextNode, adjMap, visited, currTime + edgeTime);
                }
            }
            visited[node] = false;
        }

        /*
        - TC iS exponential
        - For every node, we can use the node or not
        - V! or 2^V scenarios like trying out all paths
         */
        public int countPaths0(int n, int[][] roads) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();
            for(int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] road: roads) {
                int u = road[0], v = road[1], time = road[2];
                adjMap.get(u).add(new int[]{v, time});
                adjMap.get(v).add(new int[]{u, time});
            }

            minTime = Long.MAX_VALUE;
            ways = 0;
            dest = n-1;

            boolean[] visited = new boolean[n];

            dfs(0, adjMap, visited, 0);

            return ways;
        }
    }

    class Solution {
        // Dijkstra + Path Counting
// LC 1976: Number of Ways to Arrive at Destination
        public int countPaths1(int n, int[][] roads) {

            // Edge case: no roads
            if (roads == null) return 0;

            // Adjacency list:
            // node -> list of {neighbor, travelTime}
            Map<Integer, List<int[]>> adjMap = new HashMap<>();

            for (int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            // Build undirected graph
            for (int[] road : roads) {
                int u = road[0], v = road[1], time = road[2];
                adjMap.get(u).add(new int[]{v, time});
                adjMap.get(v).add(new int[]{u, time});
            }

            // distance[i] = shortest distance from node 0 to i
            // Use LONG to avoid overflow (weights can be up to 1e9)
            long[] distance = new long[n];

            // ways[i] = number of shortest paths to reach node i
            int[] ways = new int[n];

            // Min-heap: {currentDistance, node}
            PriorityQueue<long[]> minHeapPQ =
                    new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));

            // Initialize distances to infinity
            Arrays.fill(distance, Long.MAX_VALUE);

            // Source initialization
            distance[0] = 0;
            ways[0] = 1;

            // Start from node 0
            minHeapPQ.offer(new long[]{0, 0});

            int mod = (int) (1e9 + 7);

            // Standard Dijkstra loop
            while (!minHeapPQ.isEmpty()) {

                long[] curr = minHeapPQ.poll();
                long dist = curr[0];
                int currNode = (int) curr[1];

                // Skip outdated heap entries
                if (dist > distance[currNode]) continue;

                // Relax neighbors
                for (int[] neighbor : adjMap.get(currNode)) {
                    int neiNode = neighbor[0];
                    int neiDist = neighbor[1];

                    long newDist = dist + neiDist;

                    // Found strictly shorter path
                    if (newDist < distance[neiNode]) {
                        distance[neiNode] = newDist;
                        ways[neiNode] = ways[currNode]; // inherit path count
                        minHeapPQ.offer(new long[]{newDist, neiNode});

                        // Found another shortest path
                    } else if (newDist == distance[neiNode]) {
                        ways[neiNode] = (ways[neiNode] + ways[currNode]) % mod;
                    }
                }
            }

            // Number of shortest paths to destination node (n-1)
            return ways[n - 1];
        }

        // Global variables to track result
        private int ways;          // number of shortest paths
        private long minDist;      // global minimum distance found
        private static final int mod = (int)(1e9 + 7);
        private int dest;          // destination node (n - 1)

        // DFS explores ALL simple paths from source to destination
        private void dfs(
                int node,
                long currDist,
                Map<Integer, List<int[]>> adjMap,
                boolean[] visited
        ) {
            // Pruning:
            // If current path already exceeds known shortest distance,
            // no need to continue further
            if (currDist > minDist) {
                return;
            }

            // Base case: reached destination
            if (node == dest) {
                if (currDist < minDist) {
                    // Found a strictly shorter path
                    minDist = currDist;
                    ways = 1;
                } else if (currDist == minDist) {
                    // Found another shortest path
                    ways = (ways + 1) % mod;
                }
                return;
            }

            // Mark current node as visited (avoid cycles)
            visited[node] = true;

            // Try all neighbors
            for (int[] neighbor : adjMap.get(node)) {
                int neiNode = neighbor[0];
                int neiDist = neighbor[1];

                if (!visited[neiNode]) {
                    dfs(neiNode, currDist + neiDist, adjMap, visited);
                }
            }

            // Backtrack: allow node to be reused in other paths
            visited[node] = false;
        }

        // Brute Force
        public int countPaths0(int n, int[][] roads) {
            if (roads == null) return 0;

            ways = 0;
            minDist = Long.MAX_VALUE;
            dest = n - 1;

            // Build adjacency list
            Map<Integer, List<int[]>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for (int[] road : roads) {
                int u = road[0], v = road[1], time = road[2];
                adjMap.get(u).add(new int[]{v, time});
                adjMap.get(v).add(new int[]{u, time});
            }

            boolean[] visited = new boolean[n];

            // Start DFS from source (0)
            dfs(0, 0L, adjMap, visited);

            return ways;
        }
    }
}