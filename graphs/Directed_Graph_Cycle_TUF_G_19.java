package neetcode.graphs;

/*
On the same path, node has to be visited again, then we say that graph has a cycle in a directed graph
visited array + path visited array

https://www.geeksforgeeks.org/problems/detect-cycle-in-a-directed-graph/1
 */

import java.util.*;

public class Directed_Graph_Cycle_TUF_G_19 {
    class Solution {
        public boolean isCycleUsingTopoSortKhansAlgorithm4(int v, int[][] edges) {
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

            int count = 0;
            while (!queue.isEmpty()) {
                int currNode = queue.poll();
                count++;

                for(int neighbor: adjList.get(currNode)) {
                    inDegree[neighbor]--;

                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return count == v;
        }

        private boolean hasCycle3(int node, List<List<Integer>> adjList, int[] state) {
            state[node] = 1; // Mark as in current DFS path

            for(int neighbor: adjList.get(node)) {
                if(state[neighbor] == 0) {
                    if(hasCycle3(neighbor, adjList, state)) {
                        return true;
                    }
                } else if(state[neighbor] == 1) {
                    return true; // Back edge -> cycle exists
                }
                // state[neighbor] == 2 --> already fully processed, skip
            }

            state[node] = 2; // Mark as fully processed
            return false;
        }

        public boolean isCyclic3(int v, int[][] edges) {
// ⚠️ If edges is empty but V > 0, this skips everything and returns false
// which is technically correct, but the comment helps clarify intent
            if (edges == null || edges.length == 0) {
                return false; // No edges → no cycle possible
            }

            List<List<Integer>> adjList = new ArrayList<>();

            for(int i = 0; i < v; i++) {
                adjList.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                adjList.get(edge[0]).add(edge[1]);
            }

            int[] state = new int[v];

            for(int i =0; i < v; i++) {
                if(state[i] == 0 && hasCycle3(i, adjList, state)) {
                    return true;
                }
            }

            return false;
        }

        private boolean hasCycle2(int node, List<List<Integer>> adjList, boolean[] visited, boolean[] inStack) {
            visited[node] = true;
            inStack[node] = true;

            for(int neighbor: adjList.get(node)) {
                if(!visited[neighbor]) {
                    if(hasCycle2(neighbor, adjList, visited, inStack)) {
                        return true;
                    }
                } else if(inStack[neighbor]) {
                    // Back edge found -> cycle exists
                    return true;
                }
            }

            inStack[node] = false;
            return false;
        }

        public boolean isCyclic2(int v, int[][] edges) {
// ⚠️ If edges is empty but V > 0, this skips everything and returns false
// which is technically correct, but the comment helps clarify intent
            if (edges == null || edges.length == 0) {
                return false; // No edges → no cycle possible
            }

            List<List<Integer>> adjList = new ArrayList<>();
            for(int i = 0; i < v; i++) {
                adjList.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                adjList.get(edge[0]).add(edge[1]);
            }

            boolean[] visited = new boolean[v], inStack = new boolean[v];

            for(int i =0; i < v; i++) {
                if(!visited[i] && hasCycle2(i, adjList, visited, inStack)) {
                    return true;
                }
            }

            return false;
        }

        private boolean hasCycle(int node, List<Integer>[] adjList, boolean[] visited, boolean[] inStack) {
            visited[node] = true;
            inStack[node] = true;

            for(int neighbor: adjList[node]) {
                if(!visited[neighbor]) {
                    if(hasCycle(neighbor, adjList, visited, inStack)) {
                        return true;
                    }
                } else if(inStack[neighbor]) {
                    // Back edge found -> cycle exists
                    return true;
                }
            }

            inStack[node] = false;
            return false;
        }

        public boolean isCyclic1(int v, int[][] edges) {
            if(edges == null || edges.length == 0) {
                return false;
            }

            @SuppressWarnings("unchecked")
            List<Integer>[] adjList = new ArrayList[v];
            for(int i = 0; i < v; i++) {
                adjList[i] = new ArrayList<>();
            }

            for(int[] edge: edges) {
                adjList[edge[0]].add(edge[1]);
            }

            boolean[] visited = new boolean[v], inStack = new boolean[v];

            for(int i =0; i < v; i++) {
                if(!visited[i] && hasCycle(i, adjList, visited, inStack)) {
                    return true;
                }
            }

            return false;
        }

        private boolean helper0(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited, boolean[] pathVisited) {
            visited[node] = true;
            pathVisited[node] = true;

            for(Integer neighNode: adjMap.get(node)) {
                if(!visited[neighNode]) {
                    if(helper0(neighNode, adjMap, visited, pathVisited)) {
                        return true;
                    }
                } else if(pathVisited[neighNode]) {
                    return true;
                }
            }

            pathVisited[node] = false;
            return false;
        }

        public boolean isCyclic0(int v, int[][] edges) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i =0; i < v; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u1 = edge[0], u2 = edge[1];
                adjMap.get(u1).add(u2);
            }

            boolean[] visited = new boolean[v], pathVisited = new boolean[v];

            for(int i = 0; i < v; i++) {
                if(!visited[i]) {
                    if(helper0(i, adjMap, visited, pathVisited)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
