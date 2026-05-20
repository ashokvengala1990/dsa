package neetcode.graphs;

import java.util.*;

/*
https://www.geeksforgeeks.org/problems/detect-cycle-in-an-undirected-graph/1

TC: O(V+E)
SC: O(V+E)
 */

public class Undirected_Graph_Cycle_Using_DFS_LC_TUF_G_12 {
    class Solution {
        public boolean dfsUsingStack(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited) {
            Stack<int[]> stack = new Stack<>();
            stack.push(new int[]{node, -1});
            visited[node] = true;

            while (!stack.isEmpty()) {
                int[] curr = stack.pop();
                int currNode = curr[0], parentNode = curr[1];

                for(int neighbor: adjMap.get(currNode)) {
                    if(!visited[neighbor]) {
                        visited[neighbor] = true;
                        stack.push(new int[]{neighbor, currNode});
                    } else if(neighbor != parentNode) {
                        return true;
                    }
                }
            }

            return false;
        }

        public boolean isCycle1(int n, int[][] edges) {
            if(edges == null || edges.length == 0) {
                return false;
            }

            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for(int i = 0; i < n ; i++) {
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
                    if(dfsUsingStack(i, adjMap, visited)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean dfsUsingRecursion(int node, int parent, Map<Integer, List<Integer>> adjMap, boolean[] visited) {
            visited[node] = true;

            for(int neighbor: adjMap.get(node)) {
                if(!visited[neighbor]) {
                    if(dfsUsingRecursion(neighbor, node, adjMap, visited)) {
                        return true;
                    }
                } else if(neighbor != parent) {
                    return true;
                }
            }

            return false;
        }

        public boolean isCycle0(int n, int[][] edges) {
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
                    if(dfsUsingRecursion(i, -1, adjMap, visited)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
