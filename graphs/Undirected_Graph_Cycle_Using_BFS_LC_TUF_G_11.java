package neetcode.graphs;

import java.util.*;

/*
TC: O(V+E)
SC: O(V+E)
 */

public class Undirected_Graph_Cycle_Using_BFS_LC_TUF_G_11 {
    private boolean bfsUsingQueue(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{node, -1});
        visited[node] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int currNode = curr[0], parentNode = curr[1];

            for(int neighbor: adjMap.get(currNode)) {
                if(!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(new int[]{neighbor, currNode});
                } else if(neighbor != parentNode) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCycle(int n, int[][] edges) {
        if(edges == null || edges.length == 0) {
            return false;
        }

        Map<Integer, List<Integer>> adjMap = new HashMap<>();
        for(int i = 0; i < n; i++) {
            adjMap.put(i, new ArrayList<>());
        }

        for(int[] edge: edges) {
            int u = edge[0], v = edge[1];
            adjMap.get(u).add(v);
            adjMap.get(v).add(u);
        }

        boolean[] visited = new boolean[n];

        for(int i = 0; i < n; i++) {
            if(!visited[i]) {
                if(bfsUsingQueue(i, adjMap, visited)) {
                    return true;
                }
            }
        }

        return false;
    }
}
