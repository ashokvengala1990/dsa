package neetcode.graphs;

import java.util.*;

/*

1. Goal:
    - Find the subset of edges forming a tree that connects all vertices with the minimum total weight, without cycles.

2. Data Structures Used:
    - adjList: Stores graph as adjacency list -> note -> [neighbor, weight].
    - visited: Keeps track of nodes already included in MST.
    - minHeap: Picks the smallest-weight edge at each step.
    - mst: Stores the resulting MST edges

3. Algorithm Steps:
    - Build adjacency list (add both directions for undirected graph).
    - Pick a starting node (here, node 1) and mark it visited.
    - Add all its edges to the min-heap (sorted by weight).
    - While heap not empty and MST not complete:
        * Pop the smallest-weight edge.
        * If the destination node isn’t visited:
        * Add edge to MST.
        * Mark node as visited.
        * Push all unvisited neighbors to the heap.
    - Continue until all nodes are connected.
4. Time Complexity:
    - O(E log V) — Each edge is processed once via the priority queue.

5. Space Complexity:
    - O(V + E) — For adjacency list, heap, and visited set.

6. Edge Cases:
    - Graph must be connected; otherwise, MST won’t cover all nodes.
    - Skip self-loops or repeated edges.

Closing Notes:
Prim's algorithm does not work on disconnected graphs is because there is no way to create a spanning tree with a disconnected graph.
Even if there are two components within a disconnected graph, we can only find the spanning tree for each individual component.


Note:
Algorithm:
- You correctly initialize the adjacency list
- You correctly add both directions for each edge (since Prim's is for undirected graphs).
- You use a min-heap (PriorityQueue<int[]>) to always pick the next smallest edge.
- You maintain a visited set to avoid cycles
- You collect MST edges in mst.
 */

public class Prims {
    public static void main(String[] args) {
        int[][] edges = {{1,2,10},{1,3,3},{2,3,4},{2,4,1},{3,4,4},{3,5,4},{4,5,2}};
        int n = 5;
        List<int[]> mst = Revision01.mst(edges, n);

        for(int[] edge: mst) {
            System.out.println(edge[0]+" -- "+ edge[1]);
        }
    }

    class Revision02 {
        public int primMSTEdges(int n, int[][] edges) {
            List<List<int[]>> graph = new ArrayList<>();

            for(int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], wt = edge[2];
                graph.get(u).add(new int[]{v, wt});
                graph.get(v).add(new int[]{u, wt});
            }

            int totalCost = 0;
            List<int[]> mstEdges = new ArrayList<>();

            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> a[0] - b[0]);
            minHeap.offer(new int[]{0, -1, 0}); // {weight, parentNode, node}
            boolean[] inMST = new boolean[n];

            while (!minHeap.isEmpty() && mstEdges.size() < n-1) {
                int[] curr = minHeap.poll();
                int cost = curr[0], parentNode = curr[1], node = curr[2];

                if(inMST[node]) continue;

                inMST[node] = true;
                totalCost += cost;
                if(parentNode != -1) {
                    mstEdges.add(new int[]{parentNode, node});
                }

                for(int[] edge: graph.get(node)) {
                    if(!inMST[edge[0]]) {
                        minHeap.offer(new int[]{edge[1], node, edge[0]});
                    }
                }
            }
/*
            System.out.println("====== MST Edges ======");
            for(int[] edge: mstEdges) {
                System.out.println(edge[0]+"<->"+edge[1]);
            }
            System.out.println();
 */

            return mstEdges.size() == n-1 ? totalCost : -1;
        }

        public int primMST(int n, int[][] edges) {
            List<List<int[]>> graph = new ArrayList<>();

            for(int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], wt = edge[2];
                graph.get(u).add(new int[]{v, wt});
                graph.get(v).add(new int[]{u, wt});
            }

            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]); // {weight, node}
            minHeap.offer(new int[]{0, 0});
            int totalCost = 0, nodesInMST = 0;
            boolean[] inMST = new boolean[n];

            while (!minHeap.isEmpty()) { // && edgesUsed < n : This condition is optional
                int[] curr = minHeap.poll();
                int cost = curr[0], node = curr[1];

                if(inMST[node]) continue;

                inMST[node] = true;
                totalCost += cost;
                nodesInMST++;

                for(int[] edge: graph.get(node)) {
                    if(!inMST[edge[0]]) {
                        minHeap.offer(new int[]{edge[1], edge[0]});
                    }
                }
            }

            return nodesInMST == n ? totalCost : -1;
        }
    }

    static class Revision01 {
        public static List<int[]> mst(int[][] edges, int n) {
            // Create adjacency list to store the graph (node -> list of [neighbor, weight])
            Map<Integer, List<int[]>> adjList = new HashMap<>();

            // Initialize adjacency list entries for all nodes
            for(int i = 1; i <= n; i++) {
                adjList.put(i, new ArrayList<>());
            }

            // Build the undirected weighted graph from edge list
            for(int[] edge: edges) {
                int s = edge[0], d = edge[1], wt = edge[2];
                adjList.get(s).add(new int[]{d, wt}); // Add edge s -> d
                adjList.get(d).add(new int[]{s, wt}); // Add reverse edge d -> s (undirected)
            }

            // Track visited nodes (nodes already included in MST)
            // Use a Set for O(1) lookup when checking if a node is visited
            Set<Integer> visited = new HashSet<>();

            // Min-heap to always pick the smallest weighted edge
            // Each element: [weight, fromNode, toNode]
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);

            // Start MST from node 1: add all its edges to the priority queue
            for(int[] neighbor: adjList.get(1)) {
                int d = neighbor[0], wt = neighbor[1];
                minHeap.offer(new int[]{wt, 1, d});
            }

            // Mark node 1 as visited
            visited.add(1);

            // List to store the resulting MST edges
            List<int[]> mst = new ArrayList<>();

            // Process edges in increasing order of weight
            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll(); // Extract the smallest edge
                int w1 = curr[0], n1 = curr[1], n2 = curr[2];

                // If destination node already in MST, skip it
                if(visited.contains(n2)) {
                    continue;
                }

                // Include the new edge in MST and mark n2 in visited
                visited.add(n2);
                mst.add(new int[]{n1, n2});

                // Explore all neighbors of the newly added node
                for(int[] neighbor: adjList.get(n2)) {
                    int nextNode = neighbor[0], w2 = neighbor[1];

                    if(!visited.contains(nextNode)) {
                        minHeap.offer(new int[]{w2, n2, nextNode});
                    }
                }
            }

            // Return the list of edges forming the Minimum Spanning Tree
            return mst;
        }
    }

    class Solution {
        public static List<int[]> mst(int[][] edges, int n) {
            Map<Integer, List<int[]>> adjList = new HashMap<>();

            for (int i = 1; i <= n; i++) {
                adjList.put(i, new ArrayList<>());
            }

            for (int[] edge : edges) {
                int s = edge[0], d = edge[1], wt = edge[2];
                adjList.get(s).add(new int[]{d, wt});
                adjList.get(d).add(new int[]{s, wt});
            }

            List<int[]> mst = new ArrayList<>();
            Set<Integer> visited = new HashSet<>();
            visited.add(1);
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);

            for (int[] neighbor : adjList.get(1)) {
                int d = neighbor[0], wt = neighbor[1];
                minHeap.offer(new int[]{wt, 1, d});
            }

            while (visited.size() < n && !minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int w1 = curr[0], n1 = curr[1], n2 = curr[2];

                if (visited.contains(n2)) {
                    continue;
                }

                visited.add(n2);
                mst.add(new int[]{n1, n2});

                for (int[] adj : adjList.get(n2)) {
                    int neighbor = adj[0], wt = adj[1];

                    if (!visited.contains(neighbor)) {
                        minHeap.offer(new int[]{wt, n2, neighbor});
                    }
                }
            }

            return mst;
        }
    }
}
