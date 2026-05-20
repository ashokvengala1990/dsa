package neetcode.graphs;

import java.util.*;

public class Shortest_path_in_Directed_Acyclic_Graph_TUF_27_GFG {
    class Solution {
        private void dfsTopoSort(int node, Map<Integer, List<int[]>> adjMap, boolean[] visited, int V, Stack<Integer> inStack) {
            visited[node] = true;

            for(int[] adjNeighbor: adjMap.get(node)) {
                int neighbor = adjNeighbor[0];
                if(!visited[neighbor]) {
                    dfsTopoSort(neighbor, adjMap, visited, V, inStack);
                }
            }

            inStack.push(node);
        }

        public int[] shortestPath0(int V, int e, int[][] edges) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();

            for(int i = 0; i < V; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1], wt = edge[2];
                adjMap.get(u).add(new int[]{v, wt});
            }

            Stack<Integer> inStack = new Stack<>();
            boolean[] visited = new boolean[V];

            for(int i = 0; i < V; i++) {
                if(!visited[i]) {
                    dfsTopoSort(i, adjMap, visited, V, inStack);
                }
            }

            int[] distance = new int[V];
            Arrays.fill(distance, Integer.MAX_VALUE);
            distance[0] = 0;

            while (!inStack.isEmpty()) {
                int currNode = inStack.pop();

                for(int[] adjNeighbor: adjMap.get(currNode)) {
                    int neighborNode = adjNeighbor[0], neighborWt = adjNeighbor[1];

                    if(distance[currNode] != Integer.MAX_VALUE && distance[currNode] + neighborWt < distance[neighborNode]) {
                        distance[neighborNode] = distance[currNode] + neighborWt;
                    }
                }
            }

            for(int i = 0; i < V; i++) {
                if(distance[i] == Integer.MAX_VALUE) {
                    distance[i] = -1;
                }
            }

            return distance;
        }
    }
}
