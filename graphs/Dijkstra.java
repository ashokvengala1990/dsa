package neetcode.graphs;

import java.util.*;

/*
When you use Hashmap visited array (Neetcode) approach - which is classic Dijstra algorith, when we have a negative edge weight,
it is going to fail as it's algorithm update the node only once rather multiple shortest times like TakeuForward approach. This
neetcode hashmap doesn't works to give shortest distance properly when we have negative weights. Whereas TUF approach (distance array)
leads to TLE when we have negative cycles. So, Dijstra algorithm should not be used when we have negative edges and negative cycles. We
have to use Bellman ford algorithm (if we have single source to find other vertices shortest distance). We can use Floyd Warshall
algorithm if we multi-source to all vertext shortest path.

 */

public class Dijkstra {
    static class Revision01 {
        public static int[] printShortestPath3(int[][] edges, int n, int src, int dst) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();

            for(int i =0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], distance = edge[2];
                adjMap.get(u).add(new int[]{v, distance});
                adjMap.get(v).add(new int[]{u, distance});
            }

            PriorityQueue<int[]> minHeapPQ = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeapPQ.offer(new int[]{0, src, -1});
            Map<Integer, Integer> distanceMap = new HashMap<>();
            Map<Integer, Integer> parentMap = new HashMap<>();

            while (!minHeapPQ.isEmpty()) {
                int[] curr = minHeapPQ.poll();
                int dist = curr[0], node = curr[1], pNode = curr[2];

                if(distanceMap.containsKey(node)) {
                    continue;
                }

                distanceMap.put(node, dist);
                parentMap.put(node, pNode);

                for(int[] neighbor: adjMap.get(node)) {
                    int neighNode = neighbor[0], neighDist = neighbor[1], newDist = dist + neighDist;

                    if(!distanceMap.containsKey(neighNode)) {
                        minHeapPQ.offer(new int[]{newDist, neighNode, node});
                    }
                }
            }

            if(!distanceMap.containsKey(dst)) {
                return new int[]{};
            }

            int node = dst;
            List<Integer> result = new ArrayList<>();

            while (parentMap.containsKey(node)) { // or while (node != -1) is also correct
                result.add(node);
                node = parentMap.get(node);
            }

            return result.stream().mapToInt(Integer::intValue).toArray();
        }

        public static int[] printShortestPath2(int[][] edges, int n, int src, int dst) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();

            for(int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], distance = edge[2];
                adjMap.get(u).add(new int[]{v, distance});
                adjMap.get(v).add(new int[]{u, distance});
            }

            int[] distance = new int[n];
            Arrays.fill(distance, (int)1e9);

            int[] parent = new int[n];
            for(int i = 0; i < n; i++) {
                parent[i] = i;
            }

            PriorityQueue<int[]> minHeapPQ = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeapPQ.offer(new int[]{0, src});
            distance[src] = 0;

            while (!minHeapPQ.isEmpty()) {
                int[] curr = minHeapPQ.poll();
                int dist = curr[0], node = curr[1];

                if(dist > distance[node]) {
                    continue;
                }

                for(int[] neighbor: adjMap.get(node)) {
                    int neighNode= neighbor[0], neighDist = neighbor[1], newDist = dist + neighDist;

                    if(newDist < distance[neighNode]) {
                        minHeapPQ.offer(new int[]{newDist, neighNode});
                        distance[neighNode] = newDist;
                        parent[neighNode] = node;
                    }
                }
            }

            if(distance[dst] == (int)1e9) {
                return new int[]{};
            }

            int node = dst;
            List<Integer> result = new ArrayList<>();
            while (parent[node] != node) {
                result.add(node);
                node = parent[node];
            }
            result.add(node);
            Collections.reverse(result);

            return result.stream().mapToInt(Integer::intValue).toArray();
        }

        public static Map<Integer, Integer> shortestPath1(int[][] edges, int n, int src) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();

            for(int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], distance = edge[2];
                adjMap.get(u).add(new int[]{v, distance});
                adjMap.get(v).add(new int[]{u, distance});
            }

            PriorityQueue<int[]> minHeapPQ = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            Map<Integer, Integer> distanceMap = new HashMap<>();
            minHeapPQ.offer(new int[]{0, src});

            while (!minHeapPQ.isEmpty()) {
                int[] curr = minHeapPQ.poll();
                int dist = curr[0], node = curr[1];

                if(distanceMap.containsKey(node)) {
                    continue;
                }

                distanceMap.put(node, dist);

                for(int[] neighbor: adjMap.get(node)) {
                    int neighNode = neighbor[0], neighDist = neighbor[1], newDist = dist + neighDist;
                    if(!distanceMap.containsKey(neighNode)) {
                        minHeapPQ.offer(new int[]{newDist, neighNode});
                    }
                }
            }

            return distanceMap;
        }

//        public static Map<Integer, Integer> shortestPath(int[][] edges, int n, int src) {
        public int[] shortestPath0(int[][] edges, int n, int src) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();

            for(int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], distance = edge[2];
                adjMap.get(u).add(new int[]{v, distance});
                adjMap.get(v).add(new int[]{u, distance});
            }

            PriorityQueue<int[]> minHeapPQ = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            int[] distance = new int[n];
            Arrays.fill(distance, (int)1e9);
            distance[src] = 0;
            minHeapPQ.offer(new int[]{0, src});

            while (!minHeapPQ.isEmpty()) {
                int[] curr = minHeapPQ.poll();
                int dist = curr[0], node = curr[1];

                if(dist > distance[node]) {
                    continue;
                }

                for(int[] neighbor: adjMap.get(node)) {
                    int neighNode = neighbor[0];
                    int neighDist = neighbor[1], newDist = dist + neighDist;

                    if(newDist < distance[neighNode]) {
                        minHeapPQ.offer(new int[]{newDist, neighNode});
                        distance[neighNode] = newDist;
                    }
                }
            }

            return distance;
        }

        public int[] shortestPath00(int[][] edges, int n, int src) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();

            for(int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], distance = edge[2];
                adjMap.get(u).add(new int[]{v, distance});
                adjMap.get(v).add(new int[]{u, distance});
            }

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, src});
            int[] distance = new int[n];
            Arrays.fill(distance, (int) 1e9);;
            distance[src] = 0;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int dist = curr[0], node = curr[1];

                if(dist > distance[node]) {
                    continue;
                }

                for(int[] neighbor: adjMap.get(node)) {
                    int neighNode = neighbor[0], neighDist = neighbor[1], newDist = dist + neighDist;

                    if(newDist < distance[neighNode]) {
                        queue.offer(new int[]{newDist, neighNode});
                        distance[neighNode] = newDist;
                    }
                }
            }

            return distance;
        }
    }

    public static void main(String[] args) {
//        int[][] edges = {
//                {1, 2, 4},
//                {1, 3, 2},
//                {3, 2, 5},
//                {2, 4, 10},
//                {3, 4, 3}
//        };

//        int n =
//                4;
//        3;
//        int[][] edges = {{1,2,4},{1,3,1},{3,2,1},{2,1,1}};
//
//
//        System.out.println(Solution.shortestDistanceAndPath(edges, n, 1));
//
//        Map<Integer, Integer> map = new HashMap<>();
//        System.out.println(map.containsKey(-1));

        Revision01 r1 = new Revision01();
        int[][] edges =
                //{{0, 1, 2},{0, 2, 5},{2, 1, -10}};
                {{0, 1, 1},{0, 2, 4},{2, 1, -6}};
        int n = 3, src = 0;
        int[] ans = r1.shortestPath0(edges, n, src);
        for(int a: ans) {
            System.out.print(a+" ");
        }
        System.out.println();
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
