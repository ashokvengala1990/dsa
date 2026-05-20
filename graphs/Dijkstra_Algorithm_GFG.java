package neetcode.graphs;

/*
Given a weighted undirected graph and a source vertex src. We need to find the shortest path distances from the source vertex to all
other vertices in the graph.
Note: The given graph does not contain any negative edge.


Try this example and it leads to infinite loop using Dijsktra algorithm:
Directed graph:
(u, v, wt)
(0, 1, 1)
(1, 2, -3)
(2, 0, 1)

We can also solve below problem using BFS algorithm but it takes longer time to calculate the distance.


Dijkstra doesn't not works when we have:
- Negative weight
- Negative cycle

For example:
0 -> 1 with weight = -2


TC: O(E log V)
E = Total number of edges and V is the number of nodes.


In Summary:
- Find the shortest distance from a given src node to all other vertices/nodes
- Use Dijkstra algorithm:
    - Use Distance Array by initializing Integer.MAX_VALUE initially
    - Use Min-Heap where type of the value is {distance, node} and apply minHeap on distance
    -

https://www.geeksforgeeks.org/problems/implementing-dijkstra-set-1-adjacency-matrix/1

1. Build adjacency list
Size V list of lists. Each entry = [neighNode, weight]. Add both directions (undirected). Guard: throw on wt < 0.

2. Init distance array
int[] dist = new int[V], fill with Integer.MAX_VALUE. Then set dist[src] = 0.

3. Set up min-heap
PriorityQueue<int[]> sorted by a[0]-b[0]. Offer {0, src}. Heap stores [distance, node] — distance first.

4. Poll + stale check
Pop [currDist, currNode]. If currDist > dist[currNode] → continue. This is lazy deletion — skips outdated entries.

5. Relax neighbours
For each neighbour: if currDist + neighWt < dist[neigh] → update dist[neigh] and push {dist[neigh], neigh} to heap.

6. Return dist[]
Unreachable nodes stay at Integer.MAX_VALUE. Caller must check before using (overflow risk if you add to it).

Complexity
Time
O((V + E) log V)
Each node/edge processed once. Each heap op = O(log V). Heap can hold O(E) entries with lazy deletion → O(E log E) = O(E log V).
Space
O(V + E)
Adj list O(V+E). dist[] O(V). Heap O(E) worst case — no decrease-key, so stale entries pile up.

Things to get right:
- Heap comparator sorts by index [0]
Heap entry is {distance, node}. Comparator = (a,b) -> a[0]-b[0]. Swapping to a[1] sorts by node ID — silent, wrong.

- Don't skip the stale check
Without if (currDist > dist[currNode]) continue, you re-relax already-settled nodes and push duplicates, inflating the heap.

- Negative weights break correctness
Once a node is settled, Dijkstra assumes its distance is final. A negative edge can make a "settled" node reachable more cheaply. Use Bellman-Ford instead.

- dist[src] = 0 before the loop
Set it explicitly. The heap offer of {0, src} alone doesn't update the array — without this, the stale check at step 4 still passes, but it's fragile to rely on.

- Both directions for undirected
Add u→v and v→u. Missing one direction gives a directed graph — easy bug to miss on graph problems.


Problem 02:
https://www.geeksforgeeks.org/problems/shortest-path-in-weighted-undirected-graph/1
Find the path from src to dst and also include distance of the dst in the output.
- Same Dijkstra algorithm. Along with that, we need to use parent array to mark where I came from in case I need to update
the distance array.

 */

import java.util.*;

public class Dijkstra_Algorithm_GFG {
    class Revision01 {
        public List<Integer> shortestPath(int n, int m, int[][] edges) {
            List<List<int[]>> adjList = new ArrayList<>();

            for(int i = 0; i <= n ; i++) {
                adjList.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], wt = edge[2];
                if(wt < 0) {
                    throw new IllegalArgumentException("Negative weight found : "+ wt+ "between "+u+" and "+v);
                }
                adjList.get(u).add(new int[]{v, wt});
                adjList.get(v).add(new int[]{u, wt});
            }

            int[] distance = new int[n+1];
            int[] parent = new int[n+1];
            Arrays.fill(distance, Integer.MAX_VALUE);

            for(int i = 0; i <= n; i++) {
                parent[i] = i;
            }

            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeap.offer(new int[]{0, 1});
            distance[1] = 0;

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int currDistance = curr[0], currNode = curr[1];

                if (currDistance > distance[currNode]) {
                    continue;
                }

                for(int[] neighbor: adjList.get(currNode)) {
                    int neighNode = neighbor[0], neighWt = neighbor[1];

                    if(currDistance + neighWt < distance[neighNode]) {
                        distance[neighNode] = currDistance + neighWt;
                        minHeap.offer(new int[]{distance[neighNode], neighNode});
                        parent[neighNode] = currNode;
                    }
                }
            }

            if(distance[n] == Integer.MAX_VALUE) {
                return List.of(-1);
            }

            List<Integer> result = new ArrayList<>();

            int node = n;
            while (parent[node] != node) {
                result.add(node);
                node = parent[node];
            }

            result.add(1);
            result.add(distance[n]);
            Collections.reverse(result);
            return result;
        }

        public int[] dijkstra0(int V, int[][] edges, int src) {
            List<List<int[]>> adjList = new ArrayList<>();

            for(int i = 0; i < V; i++) {
                adjList.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], wt = edge[2];
                if(wt < 0) {
                    throw new IllegalArgumentException("Negative weight found : "+ wt+ "between "+u+" and "+v);
                }
                adjList.get(u).add(new int[]{v, wt});
                adjList.get(v).add(new int[]{u, wt});
            }

            int[] distance = new int[V];
            Arrays.fill(distance, Integer.MAX_VALUE);

            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            minHeap.offer(new int[]{0, src});
            distance[src] = 0;

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int currDistance = curr[0], currNode = curr[1];

                if(currDistance > distance[currNode]) {
                    continue;
                }

                for(int[] neighbor: adjList.get(currNode)) {
                    int neighNode = neighbor[0], neighWt = neighbor[1];

                    if(currDistance + neighWt < distance[neighNode]) {
                        distance[neighNode] = currDistance + neighWt;
                        minHeap.offer(new int[]{distance[neighNode], neighNode});
                    }
                }
            }

            return distance;
        }
    }

    public int[] dijkstraWithPath(int V, int[][] edges, int src) {
        List<List<int[]>> adj = new ArrayList<>();

        for(int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        for(int[] edge: edges) {
            int u = edge[0], v = edge[1], wt = edge[2];
            if(wt < 0) {
                throw new IllegalArgumentException("Negative weight fund: "+ edge[2] + "between u : "+u + " and v : "+ v);
            }
            adj.get(u).add(new int[]{v, wt});
            adj.get(v).add(new int[]{u, wt});
        }

        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        int[] parent = new int[V];
        Arrays.fill(parent, -1);

        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        minHeap.offer(new int[]{0, src});
        dist[src] = 0;

        while (!minHeap.isEmpty()) {
            int[] top = minHeap.poll();
            int d = top[0], u = top[1];

            if(d > dist[u]) {
                continue;
            }

            for(int[] nei: adj.get(u)) {
                int v = nei[0] , wt = nei[1];

                if(dist[u] + wt < dist[v]) {
                    dist[v] = dist[u] + wt;
                    minHeap.offer(new int[]{dist[v], v});
                    parent[v] = u;
                }
            }
        }

        for(int dest = 0; dest < V; dest++) {
            System.out.print("Path "+src+" -> "+dest+" (cost="+dist[dest]+"): ");
//            printPath(parent, src, dest);
            printPathIterative(parent, src, dest);
            System.out.println();
        }

        return dist;
    }

    private void printPath(int[] parent, int src, int dest) {
        if(dest == src) {
            System.out.println(src);
            return;
        } else if(parent[dest] == -1) {
            System.out.println("No Path");
            return;
        }
        printPath(parent, src, parent[dest]);
        System.out.println(" -> "+dest);
    }

    private void printPathIterative(int[] parent, int src, int dest) {
        if(dest == src) {
            System.out.println(src);
            return;
        } else if(parent[dest] == -1) {
            System.out.println("No Path");
            return;
        }

        List<Integer> path = new ArrayList<>();
        int curr = dest;

        // Traverse from dest -> src
        while (curr != -1) {
            path.add(curr);
            if(curr == src) break;
            curr = parent[curr];
        }

        // If we didn't reach src -> no path
        if(path.get(path.size()-1) != src) {
            System.out.println("No Path");
            return;
        }

        Collections.reverse(path);
        // Print
        for(int i = 0; i < path.size(); i++) {
            if(i > 0) System.out.println(" -> ");
            System.out.println(path.get(i));
        }
    }

    public int[] dijkstra(int V, int[][] edges, int src) {
        List<List<int[]>> adj = new ArrayList<>();

        for(int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        for(int[] edge: edges) {
            int u = edge[0], v = edge[1], wt = edge[2];
            if(wt < 0) {
                throw new IllegalArgumentException("Negative weight fund: "+ edge[2] + "between u : "+u + " and v : "+ v);
            }
            adj.get(u).add(new int[]{v, wt});
            adj.get(v).add(new int[]{u, wt});
        }

        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);

        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        minHeap.offer(new int[]{0, src});
        dist[src] = 0;

        while (!minHeap.isEmpty()) {
            int[] top = minHeap.poll();
            int d = top[0], u = top[1];

            if(d > dist[u]) {
                continue;
            }

            for(int[] nei: adj.get(u)) {
                int v = nei[0] , wt = nei[1];

                if(dist[u] + wt < dist[v]) {
                    dist[v] = dist[u] + wt;
                    minHeap.offer(new int[]{dist[v], v});
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        Dijkstra_Algorithm_GFG d = new Dijkstra_Algorithm_GFG();
        int[][] edges = new int[][]{{0, 1, 1},{1, 2, 3},{0, 2, 6}};
        int V = 3, src = 2;

        System.out.println(d.dijkstraWithPath(V, edges, src));
    }
}
