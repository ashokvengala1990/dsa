package neetcode.graphs;

/*
https://leetcode.com/problems/find-eventual-safe-states/description/
 */

import java.util.ArrayList;
import java.util.List;

public class Find_Eventual_Safe_States_LC_802 {
    class Solution {
        private boolean detectCycle(int node, List<List<Integer>> adjList, boolean[] visited, boolean[] pathVisited) {
            visited[node] = true;
            pathVisited[node] = true;

            for(int neighbor: adjList.get(node)) {
                if(!visited[neighbor]) {
                    if(detectCycle(neighbor, adjList, visited, pathVisited)) {
                        return true;
                    }
                } else if(pathVisited[neighbor]) {
                    return true;
                }
            }

            pathVisited[node] = false;
            return false;
        }

        public List<Integer> eventualSafeNodes(int[][] graph) {
            if(graph == null || graph.length == 0) {
                return new ArrayList<>();
            }

            int v = graph.length;
            List<List<Integer>> adjList = new ArrayList<>();

            for(int i = 0; i < v; i++) {
                adjList.add(new ArrayList<>());
            }

            for(int i = 0; i < v; i++) {
                int[] neighbors = graph[i];

                for(int neighbor: neighbors) {
                    adjList.get(i).add(neighbor);
                }
            }

            boolean[] visited = new boolean[v], pathVisited = new boolean[v];

            for(int i = 0; i < v; i++) {
                if(!visited[i]) {
                    detectCycle(i, adjList, visited, pathVisited);
                }
            }

            List<Integer> safeNodes = new ArrayList<>();

            for(int i = 0; i < v; i++) {
                if(!pathVisited[i]) {
                    safeNodes.add(i);
                }
            }

            return safeNodes;
        }
    }
}
