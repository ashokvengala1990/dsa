package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/cheapest-flights-within-k-stops/description/
Notes (LC 787 – Cheapest Flights Within K Stops)
Idea (BFS by stops + relaxation):
- Build adjacency list: from -> [(to, cost)]
- Do a BFS-style traversal where each queue state is (stopsUsed, node, totalCostSoFar).
- Only expand a state if stopsUsed <= k (since we can take at most k stops).
- Relax edges: if totalCostSoFar + edgeCost improves best known cost to nextNode, update and push the new state with stopsUsed + 1.

Important detail:
- stops <= k inside the relaxation if is redundant if you already skip states with stops > k at dequeue time.

Gotcha (for interviews):
- This “single distance[node]” approach can miss some cases because same node with different stop counts can matter. Safer versions:
    * Bellman-Ford for k+1 iterations, or
    * Dijkstra-like with state (node, stops) / dist[stops][node].

Time Complexity (TC):
- Build graph: O(E)
- BFS relaxations: in worst case, nodes can be re-enqueued many times; upper bound commonly stated as O(E * (k+1)) (stop-limited relaxations).
- So overall: O(E * k) (dominant term).

Space Complexity (SC):
- Adjacency list: O(E)
- Distance array: O(V)
- Queue can hold up to O(V * k) states in worst case.
- Total: O(E + V * k)
 */

public class Cheapest_Flights_Within_K_Stops_LC_787 {
    class Revision01 {
        private void printPath(int[] childToParentNode, int dst, int src, int[] prices) {
            // ✅ Fix 1: Guard BEFORE the loop using prices array
            if (prices[dst] == Integer.MAX_VALUE) {
                System.out.println("No path exists");
                return;
            }

            int node = dst;
            List<Integer> path = new ArrayList<>();

            while (node != -1) {
                path.add(node);
                node = childToParentNode[node];
            }

            Collections.reverse(path);
            System.out.println("======= Print Path=======");
            System.out.println(path);
        }

        public int findCheapestPriceAndPrintPath(int n, int[][] flights, int src, int dst, int k) {
            List<List<int[]>> graph = new ArrayList<>();

            for(int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for(int[] flight: flights) {
                graph.get(flight[0]).add(new int[]{flight[1], flight[2]});
            }

            // {price, parentNode, node}
            Queue<int[]> queue = new ArrayDeque<>();
            queue.offer(new int[]{0, -1, src});
            int[] prices = new int[n];
            Arrays.fill(prices, Integer.MAX_VALUE);
            prices[src] = 0;
            int[] childToParent = new int[n];
            Arrays.fill(childToParent, -1);

            int level = 0;

            while (!queue.isEmpty()) {
                int size = queue.size();

                for(int i = 0; i < size; i++) {
                    int[] curr = queue.poll();
                    int price = curr[0], node = curr[2];

                    if(level > k)
                        continue;

                    for(int[] edge: graph.get(node)) {
                        int neighborNode = edge[0], weight = edge[1], newPrice = price + weight;

                        if(newPrice < prices[neighborNode]) {
                            prices[neighborNode] = newPrice;
                            queue.offer(new int[]{prices[neighborNode], node, neighborNode});
                            childToParent[neighborNode] = node;
                        }
                    }
                }

                level++;
            }

/*
            // Find the path
            printPath(childToParent, dst, src, prices);

            for(int p: prices) {
                System.out.print(p+" ");
            }
            System.out.println();
*/

            return prices[dst] == Integer.MAX_VALUE ? -1 : prices[dst];
        }

        public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
            List<List<int[]>> graph = new ArrayList<>();

            for(int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for(int[] flight: flights) {
                graph.get(flight[0]).add(new int[]{flight[1], flight[2]});
            }

            // {price, node}
            Queue<int[]> queue = new ArrayDeque<>();
            queue.offer(new int[]{0, src});
            int[] prices = new int[n];
            Arrays.fill(prices, Integer.MAX_VALUE);
            prices[src] = 0;
            int level = 0;

            while (!queue.isEmpty()) {
                int size = queue.size();

                for(int i = 0; i < size; i++) {
                    int[] curr = queue.poll();
                    int price = curr[0], node = curr[1];

                    if(level > k)
                        continue;

                    for(int[] edge: graph.get(node)) {
                        int neighborNode = edge[0], weight = edge[1], newPrice = price + weight;

                        if(newPrice < prices[neighborNode]) {
                            prices[neighborNode] = newPrice;
                            queue.offer(new int[]{prices[neighborNode], neighborNode});
                        }
                    }
                }

                level++;
            }

            return prices[dst] == Integer.MAX_VALUE ? -1 : prices[dst];
        }
    }

    class Solution {

        private void printPath(Map<Integer, Integer> childToParentMap, int dst) {
            int nodeNo = dst;
            List<Integer> result = new ArrayList<>();
            while (nodeNo != -1) {
                result.add(nodeNo);
                nodeNo = childToParentMap.get(nodeNo);
            }

            Collections.reverse(result);
            System.out.println("-----> "+ result);
        }

        public int findCheapestPriceWithPath(int n, int[][] flights, int src, int dst, int k) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();
            for(int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] flight: flights) {
                int from = flight[0], to = flight[1], cost = flight[2];
                adjMap.get(from).add(new int[]{to, cost});
            }

            int[] distance = new int[n];
            Arrays.fill(distance, Integer.MAX_VALUE);
            distance[src] = 0;
            Map<Integer, Integer> childToParentMap = new HashMap<>();
            childToParentMap.put(src, -1);

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, src, 0});

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int stops = curr[0], node = curr[1], costSoFar = curr[2];

                if(stops > k) {
                    continue;
                }

                for(int[] neighbor: adjMap.get(node)) {
                    int nextNode = neighbor[0], edgeCost = neighbor[1], newCost = costSoFar + edgeCost;

                    if(newCost < distance[nextNode]) {
                        distance[nextNode] = newCost;
                        queue.offer(new int[]{stops+1, nextNode, newCost});
                        childToParentMap.put(nextNode, node);
                    }
                }
            }

            if(distance[dst] != Integer.MAX_VALUE) {
                printPath(childToParentMap, dst);
                return distance[dst];
            }else {
                System.out.println("No Path exists");
                return -1;
            }
        }

        public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {

            // Build adjacency list: from -> list of {to, cost}
            Map<Integer, List<int[]>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for (int[] flight : flights) {
                int from = flight[0], to = flight[1], cost = flight[2];
                adjMap.get(from).add(new int[]{to, cost});
            }

            // distance[node] = best (lowest) cost found so far to reach node
            int[] distance = new int[n];
            Arrays.fill(distance, Integer.MAX_VALUE);
            distance[src] = 0;

            // BFS queue holds: {stopsUsed, node, totalCostSoFar}
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, src, 0});

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int stops = curr[0], node = curr[1], costSoFar = curr[2];

                // If we've already used more than k stops, don't expand this path
                if (stops > k) continue;

                // Try all outgoing flights from current node
                for (int[] nei : adjMap.get(node)) {
                    int nextNode = nei[0];
                    int edgeCost = nei[1];
                    int newCost = costSoFar + edgeCost;

                    // Relaxation: found a cheaper cost to nextNode
                    // (No need to check "stops <= k" here since we already ensured it above)
                    if (newCost < distance[nextNode]) {
                        distance[nextNode] = newCost;

                        // Take this flight: stopsUsed increases by 1
                        queue.offer(new int[]{stops + 1, nextNode, newCost});
                    }
                }
            }

            // If destination is still unreachable, return -1
            return distance[dst] != Integer.MAX_VALUE ? distance[dst] : -1;
        }
    }
}
