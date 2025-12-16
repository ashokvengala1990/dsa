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
 */

public class Network_Delay_Time_LC_743 {
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
