package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/network-delay-time/description/

Algorithm: Network Delay Time / Shortest Path with Paths
Goal: Find shortest path from a source node to all other nodes in a weighted graph, and reconstruct the paths.

Steps:
1) Build adjacency list
    * Map each node → list of (neighbor, weight) pairs.
    * For undirected graphs, add edges in both directions.

2) Initialize structures
    * dist: shortest distance from source to each node.
    * parent: previous node on the shortest path.
    * minHeap: priority queue (distance_so_far, parent_node, current_node) to always pick the closest unvisited node.
    * visited: set of nodes whose shortest distance is finalized.

3) Process nodes (Dijkstra main loop)
    * Pop node with smallest distance_so_far from heap.
    * Skip if already visited.
    * Mark node as visited, record distance and parent.
    * Push neighbors to heap with updated cumulative distance.

4) Compute result
    * minTime = max(distance of all visited nodes) → network delay.
    * If some nodes not visited → return -1 (unreachable).

5 Reconstruct paths
    * For each node, follow parent pointers back to source.
    * Reverse the list to get source → target path.

Key points:
* Always expands the closest unvisited node (greedy).
* Works for directed graphs; can be adapted for undirected graphs by adding reverse edges.
* Can return both shortest distances and paths.
* Complexity: O(E log V) with a min-heap.

Revision01:
Summary Table (Worst-Case)
Algorithm	            Worst-Case TC	Space Complexity                SC	Notes
DFS (pruning)	        O(N * E)	    O(N) recursion stack	    Worst-case if pruning never happens
BFS (queue)	            O(N * E)	    O(N + E) (queue + map)	    Worst-case, each edge pushed multiple times
Dijkstra (min-heap)	    O(E log N)	    O(N + E)	                Heap ensures better efficiency for weighted graph

✅ Key takeaway:
- DFS/BFS: Usually O(N + E), but in worst-case for weighted graphs with multiple paths and no pruning, it can be O(N * E).
- Dijkstra: More predictable, O(E log N) because heap prioritizes minimum distance and avoids unnecessary revisits.



======> Claude content:
LC 743 – Network Delay Time (revision notes)
Idea: Directed weighted graph. Signal sent from node k to all nodes — return time for last node to receive it, or -1 if any node unreachable.
Approach: Dijkstra's from source k. Answer = max(distance[i]) across all nodes.
Key steps:

Build adjacency list: List<List<int[]>> where each entry is {neighbor, weight}.
Run Dijkstra from k: min-heap of {cost, node}, relax edges.
After: if any distance is INF → unreachable, return -1. Else return max distance.

Why max distance?
Signal travels in parallel. Time until all nodes receive = time the slowest path completes = max shortest-path distance.
Why Dijkstra (not BFS)?
Edges have non-negative weights. BFS only works for unweighted (or uniform-weight) graphs. Bellman-Ford works but is O(V·E) — overkill since no negative weights.
Min-heap structure: {cost, node}
Cost first so PQ comparator sorts by distance. Java tip: (a,b) -> a[0]-b[0] works since costs are bounded; for safety with large values use Integer.compare(a[0], b[0]).
The stale-entry skip — if(cost > distance[node]) continue;
We push to heap on every relaxation → same node may appear multiple times with different costs. When popped, if its cost is worse than the recorded best, skip. This is the "lazy deletion" pattern — simpler than decrease-key.
Gotchas:

1-indexed nodes → size arrays as n+1 and skip index 0 when computing max.
Don't break early on first INF — need to check all nodes.
Initialize distance[src] = 0, everything else INF.
Push {0, src} to start the heap.
Don't mark nodes "visited" globally — the cost-vs-distance check handles it correctly. Marking visited on pop also works (and prevents re-processing), but the lazy check is cleaner.

Why relaxation works:
First time a node is popped, its cost equals the true shortest distance (greedy property — non-negative edges guarantee this). Later pops of the same node have stale (larger) costs and get skipped.
Complexity: O((V + E) log V) time with binary heap, O(V + E) space.
Pattern recognition:

"Shortest path from one source, non-negative weights" → Dijkstra.
"Time for signal/info to reach all nodes" → Dijkstra + max of distances.
"Cheapest/fastest path single-source" → Dijkstra.

Don't confuse with:

Negative weights → Bellman-Ford.
All-pairs shortest paths → Floyd-Warshall.
Unweighted shortest path → BFS.
≤ K stops constraint (LC 787) → modified BFS / Bellman-Ford.


======= Test cases:
 Case 1: Basic — all nodes reachable

  times = [[2,1,1],[2,3,1],[3,4,1]], n=4, k=2

          1
        1/
    2 →
        1\     1
          3 ——→ 4

  dist: {1:1, 2:0, 3:1, 4:2}
  max = 2  →  return 2

  ---
  Case 2: Unreachable node — return -1

  times = [[1,2,1]], n=3, k=1

    1 ——→ 2      3 (isolated, no edge points to it)

  dist: {1:0, 2:1, 3:∞}
  3 is unreachable  →  return -1

  ---
  Case 3: n=1, source only — return 0

  times = [], n=1, k=1

    Just node 1, already there.

  dist: {1:0}
  max = 0  →  return 0
  This is why the guard times.length == 0 → return -1 was a bug.

  ---
  Case 4: Multiple paths — Dijkstra picks shortest

  times = [[1,2,10],[1,3,1],[3,2,1]], n=3, k=1

         10
    1 ————————→ 2
    |           ↑
    1↓          1
    3 ——————————

  Path to 2:
    Direct:   1→2 = 10
    Via 3:    1→3→2 = 1+1 = 2  ✓ (shorter)

  dist: {1:0, 2:2, 3:1}
  max = 2  →  return 2

  ---
  Case 5: Source is not node 1

  times = [[3,1,5],[3,2,2],[1,2,1]], n=3, k=3

          5
    3 ————————→ 1
    |           |
    2↓          1↓
    2 ←————————
        (cost 6 via 1, but 2 direct is better)

  dist: {1:5, 2:2, 3:0}
  max = 5  →  return 5

  ---
  Case 6: Linear chain — cost accumulates

  times = [[1,2,1],[2,3,1],[3,4,1]], n=4, k=1

    1 ——1——→ 2 ——1——→ 3 ——1——→ 4

  dist: {1:0, 2:1, 3:2, 4:3}
  max = 3  →  return 3

  ---
  Summary table

  ┌──────┬──────────────────────────────────┬──────────────────┐
  │ Case │             Scenario             │      Return      │
  ├──────┼──────────────────────────────────┼──────────────────┤
  │ 1    │ All reachable, multiple branches │ max dist         │
  ├──────┼──────────────────────────────────┼──────────────────┤
  │ 2    │ Some node isolated               │ -1               │
  ├──────┼──────────────────────────────────┼──────────────────┤
  │ 3    │ n=1, no edges needed             │ 0                │
  ├──────┼──────────────────────────────────┼──────────────────┤
  │ 4    │ Multiple paths to same node      │ shortest wins    │
  ├──────┼──────────────────────────────────┼──────────────────┤
  │ 5    │ Source ≠ node 1                  │ max dist from k  │
  ├──────┼──────────────────────────────────┼──────────────────┤
  │ 6    │ Linear chain                     │ accumulated cost │
  └──────┴──────────────────────────────────┴──────────────────┘

 */

public class Network_Delay_Time_LC_743 {
    class Revision02 {
        private int[] dijkstra(int src, int n, List<List<int[]>> adjGraph) {
            int[] distance = new int[n];
            Arrays.fill(distance, Integer.MAX_VALUE);
            distance[src] = 0;

            // min-heap: {cost, node}
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> a[0]-b[0]);
            minHeap.offer(new int[]{0, src});

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int cost = curr[0], node = curr[1];

                if(cost > distance[node]) continue;

                for(int[] edge: adjGraph.get(node)) {
                    int neighbor = edge[0], weight = edge[1];
                    int newCost = cost + weight;
                    if(newCost < distance[neighbor]) {
                        distance[neighbor] = newCost;
                        minHeap.offer(new int[]{distance[neighbor], neighbor});
                    }
                }
            }

            return distance;
        }

        public int networkDelayTime(int[][] times, int n, int k) {
            if(k <= 0 || n <= 0 || times == null) {
                return -1;
            }

            List<List<int[]>> adjGraph = new ArrayList<>();

            for(int i = 0; i <= n; i++) {
                adjGraph.add(new ArrayList<>());
            }

            for(int[] time: times) {
                int u = time[0], v = time[1], wt = time[2];
                adjGraph.get(u).add(new int[]{v, wt});
            }

            int[] distance = dijkstra(k, n+1, adjGraph);

            int ans = 0;
            for(int i = 1; i <= n; i++) {
                if(distance[i] == Integer.MAX_VALUE) return -1;
                ans = Math.max(ans, distance[i]);
            }

            return ans;
        }
    }

    public static void main(String[] args) {
        int[][] times = {{2,1,1},{2,3,1},{3,4,1}};
        int n = 4, k = 2;

        System.out.println(Solution.networkDelayTimeWithPaths2(times, n, k));
    }

    class Revision01 {
        public int networkDelayTime3(int[][] times, int n, int k) {
            if(times == null || times.length == 0 || n <= 0 || k < 0) {
                return -1;
            }

            Map<Integer, List<int[]>> adjGraph = new HashMap<>();
            for(int i = 1; i <= n; i++) {
                adjGraph.put(i, new ArrayList<>());
            }

            for(int[] time: times) {
                int src = time[0], dst = time[1], dist = time[2];

                adjGraph.get(src).add(new int[]{dst, dist});
            }

            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> a[0]-b[0]);
            minHeap.offer(new int[]{k, 0});
            int[] signalReceivedAt = new int[n+1];
            Arrays.fill(signalReceivedAt, Integer.MAX_VALUE);

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int currNode = curr[0], currWt = curr[1];

                if(currWt >= signalReceivedAt[currNode]) {
                    continue;
                }

                signalReceivedAt[currNode] = currWt;

                for(int[] neighbor: adjGraph.get(currNode)) {
                    int neighborNode = neighbor[0], neighborWt = neighbor[1];

                    minHeap.offer(new int[]{neighborNode, currWt + neighborWt});
                }
            }

            int minTime = -1;
            for(int i = 1; i <= n; i++) {
                minTime = Math.max(minTime, signalReceivedAt[i]);
            }

            return minTime == Integer.MAX_VALUE ? -1 : minTime;
        }

        public int networkDelayTime2(int[][] times, int n, int k) {
            if(times == null || times.length == 0 || n < 0 || k <= 0) {
                return -1;
            }

            Map<Integer, List<int[]>> adjGraph = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjGraph.put(i, new ArrayList<>());
            }

            for(int[] time: times) {
                int src = time[0], dst = time[1], dist = time[2];

                adjGraph.get(src).add(new int[]{dst, dist});
            }

            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> a[1] - b[1]);
            minHeap.offer(new int[]{k, 0});
            Map<Integer, Integer> distance = new HashMap<>();
            int minTime = 0;

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int currNode = curr[0], currWt = curr[1];

                if(distance.containsKey(currNode)) {
                    continue;
                }

                distance.put(currNode, currWt);
                minTime = Math.max(minTime, currWt);

                for(int[] neighbor: adjGraph.get(currNode)) {
                    int neighborNode = neighbor[0], neighborWt = neighbor[1];

                    if(!distance.containsKey(neighborNode)) {
                        minHeap.offer(new int[]{neighborNode, currWt + neighborWt});
                    }
                }
            }

            return distance.size() == n ? minTime : -1;
        }

        public int networkDelayTime1(int[][] times, int n, int k) {
            if(times == null || times.length == 0 || n <= 0 || k < 0) {
                return -1;
            }

            Map<Integer, List<int[]>> adjGraph = new HashMap<>();
            for(int i = 1; i <= n; i++) {
                adjGraph.put(i, new ArrayList<>());
            }

            for(int[] time: times) {
                int src = time[0], dst = time[1], dist = time[2];

                adjGraph.get(src).add(new int[]{dst, dist});
            }

            int[] signalReceivedAt = new int[n+1];
            Arrays.fill(signalReceivedAt, Integer.MAX_VALUE);


            int minTime = -1;
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{k, 0});

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currNode = curr[0], currWt = curr[1];

                if(currWt >= signalReceivedAt[currNode]) {
                    continue;
                }

                signalReceivedAt[currNode] = currWt;

                for(int[] neighbor: adjGraph.get(currNode)) {
                    int neighborNode = neighbor[0], neighborWt = neighbor[1];

                    queue.offer(new int[]{neighborNode, currWt + neighborWt});
                }
            }

            for(int i = 1; i <= n; i++) {
                minTime = Math.max(minTime, signalReceivedAt[i]);
            }

            return minTime == Integer.MAX_VALUE ? -1 : minTime;
        }

        private void dfs(int node, int currTime, Map<Integer, List<int[]>> adjGraph, int[] signalReceivedAt) {
            if(currTime >= signalReceivedAt[node]) {
                return;
            }

            signalReceivedAt[node] = currTime;

            for(int[] neighbor: adjGraph.get(node)) {
                int neighborNode = neighbor[0], neighborWt = neighbor[1];
                dfs(neighborNode, currTime + neighborWt, adjGraph, signalReceivedAt);
            }
        }

        public int networkDelayTime0(int[][] times, int n, int k) {
            if(times == null || times.length == 0 || n <= 0 || k < 0) {
                return -1;
            }

            Map<Integer, List<int[]>> adjGraph = new HashMap<>();
            for(int i = 1; i <= n; i++) {
                adjGraph.put(i, new ArrayList<>());
            }

            for(int[] time: times) {
                int src = time[0], dst = time[1], dist = time[2];
                adjGraph.get(src).add(new int[]{dst, dist});
            }

            int[] signalReceivedAt = new int[n+1];
            Arrays.fill(signalReceivedAt, Integer.MAX_VALUE);

            dfs(k, 0, adjGraph, signalReceivedAt);

            int minTime = -1;
            for(int i = 1; i <= n; i++) {
                minTime = Math.max(minTime, signalReceivedAt[i]);
            }

            return minTime == Integer.MAX_VALUE ? -1 : minTime;
        }
    }

    static class Solution {
        public static int networkDelayTimeWithPaths2(int[][] times, int n, int k) {
            Map<Integer, List<int[]>> adjList = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjList.put(i, new ArrayList<>());
            }

            for(int[] time: times) {
                int n1 = time[0], n2 = time[1], wt = time[2];
                adjList.get(n1).add(new int[]{n2, wt});
            }

            int minTime = 0;
            Set<Integer> visited = new HashSet<>();
            Map<Integer, Integer> parent = new HashMap<>();
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeap.offer(new int[]{0, -1, k});

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int wt1 = curr[0], p = curr[1], n1 = curr[2];

                if(visited.contains(n1)) {
                    continue;
                }

                visited.add(n1);
                parent.put(n1, p);
                minTime = Math.max(minTime, wt1);

                for(int[] neighbor: adjList.get(n1)) {
                    int n2 = neighbor[0], wt2 = neighbor[1];

                    if(!visited.contains(n2)) {
                        minHeap.offer(new int[]{wt1+wt2, n1, n2});
                    }
                }
            }

            Map<Integer, List<Integer>> paths = new HashMap<>();
            for(int i = 1; i <= n; i++) {
                List<Integer> path = new ArrayList<>();
                Integer curr = i;

                while (curr != -1) {
                    path.add(curr);
                    curr = parent.get(curr);
                }
                Collections.reverse(path);
                paths.put(i, path);
            }

            System.out.println(paths);

            return visited.size() == n ? minTime : -1;
        }

        public int networkDelayTime1(int[][] times, int n, int k) {
            Map<Integer, List<int[]>> adjList = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjList.put(i, new ArrayList<>());
            }

            for(int[] time: times) {
                int n1 = time[0], n2 = time[1], wt = time[2];
                adjList.get(n1).add(new int[]{n2, wt});
            }

            int minTime = 0;
            Set<Integer> visited = new HashSet<>();
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeap.offer(new int[]{0, k});

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int wt1 = curr[0], n1 = curr[1];

                if(visited.contains(n1)) {
                    continue;
                }

                visited.add(n1);
                minTime = Math.max(minTime, wt1);

                for(int[] neighbor: adjList.get(n1)) {
                    int n2 = neighbor[0], wt2 = neighbor[1];

                    if(!visited.contains(n2)) {
                        minHeap.offer(new int[]{wt1+wt2, n2});
                    }
                }
            }

            return visited.size() == n ? minTime : -1;
        }

        public int networkDelayTime0(int[][] times, int n, int k) {
            Map<Integer, List<int[]>> adjList = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjList.put(i, new ArrayList<>());
            }

            for(int[] time : times) {
                int n1 = time[0], n2 = time[1], wt = time[2];
                adjList.get(n1).add(new int[]{n2, wt});
            }

            Map<Integer, Integer> distance = new HashMap<>();
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeap.offer(new int[]{0, k});
            int minTime = 0;

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int wt1 = curr[0], n1 = curr[1];

                if(distance.containsKey(n1)) {
                    continue;
                }

                distance.put(n1, wt1);
                minTime = Math.max(minTime, wt1);

                for(int[] neighbor: adjList.get(n1)) {
                    int n2 = neighbor[0], wt2 = neighbor[1];

                    if(!distance.containsKey(n2)) {
                        minHeap.offer(new int[]{wt1+wt2, n2});
                    }
                }
            }

            return distance.size() == n ? minTime: -1;
        }
    }
}
