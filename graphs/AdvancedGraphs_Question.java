package neetcode.graphs;

import java.util.*;

public class AdvancedGraphs_Question {
    static class Solution {
        /*
        - Given a connected graph represented by a list of edges, where edge[0] = src
          edge[1] = dst, edge[2] = weight, find the shortest path from src to every other
          node in the graph. There are n nodes in the graph.
        - Use MinHeap, <cost, node>
        - Output: HashMap<String, Integer>, key: node, value: minimum cost
        - Graph with V vertices or nodes
            * Maximum number of edges V^2 = E, every single node is connected to other nodes
            * As push and pop elements = number of edges
            * How many times we push and pop edges: In worst case: O(E*logE)
            * Some times, people write like: O(E * log V^2) = O(2 * E * logV) = O(E * logV)
        - TC: O(E * logV)
        - SC: O(E)
         */
        public static Map<Integer, Integer> shortestPath(int[][] edges, int n, int src) {
            Map<Integer, Integer> nodeToMinCostMap = new HashMap<>();

            if(edges == null || edges.length == 0) {
                return nodeToMinCostMap;
            }

            Map<Integer, List<int[]>> adjListMap = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjListMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int s = edge[0], d = edge[1], w = edge[2];
                adjListMap.get(s).add(new int[]{d, w});
            }

            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> a[0] - b[0]);
            minHeap.offer(new int[]{0, src});

            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int w1 = curr[0], n1 = curr[1];

                if(nodeToMinCostMap.containsKey(n1)) {
                    continue;
                }
                nodeToMinCostMap.put(n1, w1);

                for(int[] itr: adjListMap.get(n1)) {
                    int n2 = itr[0], w2 = itr[1];

                    if(!nodeToMinCostMap.containsKey(n2)) {
                        minHeap.offer(new int[]{w1+w2, n2});
                    }
                }
            }

            return nodeToMinCostMap;
        }
    }

    public static void main(String[] args) {
        int[][] edges = {{1, 2, 10},{1, 3, 3},{2,4,2},{3,2,4},{3,4,8},{3,5,2},{4,5,5}};
        int n = 5;
        int src = 1;
        System.out.println(Solution.shortestPath(edges, n, src));
    }
}
