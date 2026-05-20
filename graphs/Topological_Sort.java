package neetcode.graphs;

import java.util.*;

/*
https://www.geeksforgeeks.org/problems/topological-sort/1

- Given a directed Acyclical graph, return a valid topological ordering of the graph.


GFG_Topo_Sort:
topoSortUsingDFS0:
    - Using DFS + Visited + Stack
    - This doesn't gracefully that it has a cycle
topoSortUsingDFS1:
    - Using DFS + Visited + PathVisited + Stack
    - This tells gracefully that it has a cycle

topoSortUsingBFS:
    - Using BFS + InDegree
    - This tells gracefully that it has a cycle in the condition at the last

TC: O(V + E)
SC: O(V)

- Topo Sort is applicable to only on DAG - Directed Acyclic Graph
- We can this algorithm to detect cycles and also we can find Topological Ordering
 */

public class Topological_Sort {
    class GFG_Topo_Sort {
        public ArrayList<Integer> topoSortUsingBFS(int v, int[][] edges) {
            List<List<Integer>> adjList = new ArrayList<>();

            for(int i = 0; i < v; i++) {
                adjList.add(new ArrayList<>());
            }

            int[] inDegree = new int[v];

            for(int[] edge: edges) {
                adjList.get(edge[0]).add(edge[1]);
                inDegree[edge[1]]++;
            }

            Queue<Integer> queue = new LinkedList<>();

            for(int i = 0; i < v; i++) {
                if(inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            ArrayList<Integer> topoSort = new ArrayList<>();

            while (!queue.isEmpty()) {
                int currNode = queue.poll();
                topoSort.add(currNode);

                for(int neighbor: adjList.get(currNode)) {
                    inDegree[neighbor]--;
                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return topoSort.size() == v ? topoSort : new ArrayList<>();
        }

        private void helperTopoSortDFS1(int node, List<List<Integer>> adjList, boolean[] visited, boolean[] pathVisited, Stack<Integer> stack) {
            visited[node] = true;
            pathVisited[node] = true;

            for(int neighbor: adjList.get(node)) {
                if(!visited[neighbor]) {
                    helperTopoSortDFS1(neighbor, adjList, visited, pathVisited, stack);
                } else if(pathVisited[neighbor]) {
                    return;
                }
            }

            stack.push(node);
            pathVisited[node] = false;
        }

        // Include pathVisited to return when there is a cycle
        public ArrayList<Integer> topoSortUsingDFS1(int v, int[][] edges) {
            List<List<Integer>> adjList = new ArrayList<>();

            for(int i =0; i < v; i++) {
                adjList.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                adjList.get(edge[0]).add(edge[1]);
            }

            boolean[] visited = new boolean[v], pathVisited = new boolean[v];
            Stack<Integer> stack = new Stack<>();

            for(int i = 0; i < v; i++) {
                if(!visited[i]) {
                    helperTopoSortDFS1(i, adjList, visited, pathVisited, stack);
                }
            }

            ArrayList<Integer> result = new ArrayList<>();
            if(stack.size() == v) {
                while (!stack.isEmpty()) {
                    result.add(stack.pop());
                }
            }

            return result;
        }

        private void helperTopoSortDFS0(int node, List<List<Integer>> adjList, boolean[] visited, Stack<Integer> stack) {
            visited[node] = true;

            for(int neighbor: adjList.get(node)) {
                if(!visited[neighbor]) {
                    helperTopoSortDFS0(neighbor, adjList, visited, stack);
                }
            }

            stack.push(node);
        }

        public ArrayList<Integer> topoSortUsingDFS0(int v, int[][] edges) {
            List<List<Integer>> adjList = new ArrayList<>();

            for(int i =0; i < v; i++) {
                adjList.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                adjList.get(edge[0]).add(edge[1]);
            }

            boolean[] visited = new boolean[v];
            Stack<Integer> stack = new Stack<>();

            for(int i = 0; i < v; i++) {
                if(!visited[i]) {
                    helperTopoSortDFS0(i, adjList, visited, stack);
                }
            }

            ArrayList<Integer> result = new ArrayList<>();
            if(stack.size() == v) {
                while (!stack.isEmpty()) {
                    result.add(stack.pop());
                }
            }

            return result;
        }
    }

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
