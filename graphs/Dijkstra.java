package neetcode.graphs;

import java.util.*;

public class Dijkstra {
    public static void main(String[] args) {
//        int[][] edges = {
//                {1, 2, 4},
//                {1, 3, 2},
//                {3, 2, 5},
//                {2, 4, 10},
//                {3, 4, 3}
//        };

        int n =
//                4;
        3;
        int[][] edges = {{1,2,4},{1,3,1},{3,2,1},{2,1,1}};


        System.out.println(Solution.shortestDistanceAndPath(edges, n, 1));
    }

    static class Solution {
        public static Map<Integer, Integer> shortestDistanceAndPath(int[][] edges, int n, int src) {
            Map<Integer, List<int[]>> adjList = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjList.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int s = edge[0], d = edge[1], wt = edge[2];
                adjList.get(s).add(new int[]{d, wt});
            }

            Map<Integer, Integer> dist = new HashMap<>();
            Map<Integer, Integer> parent = new HashMap<>();

            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0]-b[0]);
            minHeap.offer(new int[]{0, -1, src});

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int wt1 = curr[0], p = curr[1], n1 = curr[2];

                if(dist.containsKey(n1)) {
                    continue;
                }

                dist.put(n1, wt1);
                parent.put(n1, p);

                for(int[] neighbor: adjList.get(n1)) {
                    int nextNode = neighbor[0], wt2 = neighbor[1];

                    if(!dist.containsKey(nextNode)) {
                        minHeap.offer(new int[]{wt1+wt2, n1, nextNode});
                    }
                }
            }

            Map<Integer, List<Integer>> paths = new HashMap<>();

            for(int node = 1; node <= n; node++) {
                if(!dist.containsKey(node)) {
                    paths.put(node, new ArrayList<>());
                }

                List<Integer> path = new ArrayList<>();
                Integer curr = node;
                while (curr != -1) {
                    path.add(curr);
                    curr = parent.get(curr);
                }
                Collections.reverse(path);
                paths.put(node, path);
            }

            System.out.println(paths);
            return dist;
        }

        public static Map<Integer, Integer> shortestPath(int[][] edges, int n, int src) {
            Map<Integer, List<int[]>> adjList = new HashMap<>();

            // Step 1: Initialize adjacency list
            for (int i = 1; i <= n; i++) {
                adjList.put(i, new ArrayList<>());
            }

            // Step 2: Fill adjacency list
            for (int[] edge : edges) {
                int s = edge[0], d = edge[1], w = edge[2];
                adjList.get(s).add(new int[]{d, w});
            }

            // Step 3: Initialize shortest-path map
            Map<Integer, Integer> shortest = new HashMap<>();

            // Step 4: Min-heap with (distance, node)
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeap.offer(new int[]{0, src});

            // Step 5: Process heap
            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int w1 = curr[0];  // distance so far
                int n1 = curr[1];  // current node

                if (shortest.containsKey(n1)) {
                    continue; // already visited
                }

                shortest.put(n1, w1);

                // Step 6: Explore neighbors
                for (int[] adj : adjList.get(n1)) {
                    int n2 = adj[0], w2 = adj[1];
                    if (!shortest.containsKey(n2)) {
                        minHeap.offer(new int[]{w1 + w2, n2});
                    }
                }
            }

            return shortest;
        }
    }
}
