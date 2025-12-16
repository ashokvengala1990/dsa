package neetcode.graphs;

import java.util.*;

/*
- Given a directed acyclical graph, return a valid topological ordering of the graph.

 */

public class Topological_Sort {
    class Solution {
        private boolean dfsForTopoSortAndCycleDetection(int node, Map<Integer, List<Integer>> adjList
                , Set<Integer> visited, Set<Integer> pathVisited, List<Integer> topoSort) {
            if(pathVisited.contains(node)) {
                return false;
            } else if(visited.contains(node)) {
                return true;
            }

            visited.add(node);
            pathVisited.add(node);

            for(int neighbor: adjList.get(node)) {
                if(!dfsForTopoSortAndCycleDetection(neighbor, adjList, visited, pathVisited, topoSort)) {
                    return false;
                }
            }

            pathVisited.remove(node);
            topoSort.add(node);

            return true;
        }

        public List<Integer> topologicalSortForCycleDetectionUsingDFS(int[][] edges, int n) {
            List<Integer> topoSort = new ArrayList<>();
            Map<Integer, List<Integer>> adjList = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjList.put(i, new ArrayList<>());
            }

            Set<Integer> visited = new HashSet<>();
            Set<Integer> pathVisited = new HashSet<>();

            for(int i = 1; i <= n; i++) {
                if(!dfsForTopoSortAndCycleDetection(i, adjList, visited, pathVisited, topoSort)) {
                    return new ArrayList<>();
                }
            }

            return topoSort;
        }

        private void dfsForTopoSort(int node, Map<Integer, List<Integer>> adjList, Set<Integer> visited, List<Integer> topoSort) {
            if(visited.contains(node)) {
                return;
            }

            visited.add(node);

            for(int neighbor: adjList.get(node)) {
                dfsForTopoSort(neighbor, adjList, visited, topoSort);
            }

            topoSort.add(node);
        }

        public List<Integer> topologicalSortUsingDFS(int[][] edges, int n) {
            Map<Integer, List<Integer>> adjList = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjList.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                adjList.get(edge[0]).add(edge[1]);
            }

            List<Integer> topoSort = new ArrayList<>();
            Set<Integer> visited = new HashSet<>();

            for(int i = 1; i <= n; i++) {
                dfsForTopoSort(i, adjList, visited, topoSort);
            }

            return topoSort;
        }
    }
}
