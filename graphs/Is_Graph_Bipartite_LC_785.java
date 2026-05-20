package neetcode.graphs;

import java.util.*;

/*
This problem is same as Cycle detection for an undirected graph but with a small variation on the array. Instead of visited array
- true/false, we need to consider colorArr[] = -1 not colored, 0 - red color or set A partition and 1 - blue color or set B partition
- TC: O(V+E)
- SC: O(V+E)

* Using DFS With Recursion Function
* Using DFS With Stack Data Structure
* Using BFS With Queue Data Structure

https://leetcode.com/problems/is-graph-bipartite/description/
 */

public class Is_Graph_Bipartite_LC_785 {
    class Solution {
        private boolean dfsUsingRecursion(int currNode, int currColor, Map<Integer, List<Integer>> adjMap, int[] colorArr) {
            colorArr[currNode] = currColor;

            for(int neighbor: adjMap.get(currNode)) {
                if(colorArr[neighbor] == -1) {
                    if(!dfsUsingRecursion(neighbor, 1 - colorArr[currNode], adjMap, colorArr)) {
                        return false;
                    }
                } else if(colorArr[currNode] == colorArr[neighbor]) {
                    return false;
                }
            }

            return true;
        }

        private boolean bfsUsingQueue(int node, int currColor, Map<Integer, List<Integer>> adjMap, int[] colorArr) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(node);
            colorArr[node] = currColor;

            while (!queue.isEmpty()) {
                int currNode = queue.poll();

                for(int neighbor: adjMap.get(currNode)) {
                    // If the adjacent node is yet not colored
                    // You will give the opposite color of the node
                    if(colorArr[neighbor] == -1) {
                        colorArr[neighbor] = 1 - colorArr[currNode];
                        queue.offer(neighbor);
                    }
                    // Is the adjacent guy having the someone did color it on some other path
                    else if(colorArr[currNode] == colorArr[neighbor]) {
                        return false;
                    }
                }
            }

            return true;
        }

        private boolean dfsUsingStack(int initialNode, int currColor, Map<Integer, List<Integer>> adjMap, int[] colorArr) {
            Stack<Integer> stack = new Stack<>();
            stack.push(initialNode);
            colorArr[initialNode] = currColor;

            while (!stack.isEmpty()) {
                int currNode = stack.pop();

                for(int neighbor: adjMap.get(currNode)) {
                    if(colorArr[neighbor] == -1) {
                        colorArr[neighbor] = 1 - colorArr[currNode];
                        stack.push(neighbor);
                    } else if(colorArr[currNode] == colorArr[neighbor]) {
                        return false;
                    }
                }
            }

            return true;
        }

        public boolean isBipartite2(int[][] graph) {
            if(graph == null || graph.length == 0) {
                return true;
            }

            int size = graph.length;
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for(int node = 0; node < size; node++) {
                adjMap.put(node, new ArrayList<>());
            }

            for(int node = 0; node < size; node++) {
                int[] neighbors = graph[node];
                for(int neighbor: neighbors) {
                    adjMap.get(node).add(neighbor);
                }
            }

            int[] colorArr = new int[size];
            Arrays.fill(colorArr, -1);
            for(int node = 0; node < size; node++) {
                if(colorArr[node] == -1 && !dfsUsingStack(node, 0, adjMap, colorArr)) {
                    return false;
                }
            }

            return true;
        }

        public boolean isBipartite1(int[][] graph) {
            if(graph == null || graph.length == 0) {
                return true;
            }

            int size = graph.length;
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for(int node = 0; node < size; node++) {
                adjMap.put(node, new ArrayList<>());
            }

            for(int node = 0; node < size; node++) {
                int[] neighbors = graph[node];
                for(int neighbor: neighbors) {
                    adjMap.get(node).add(neighbor);
                }
            }

            int[] colorArr = new int[size];
            Arrays.fill(colorArr, -1);

            for(int node = 0; node < size; node++) {
                if(colorArr[node] == -1 && !bfsUsingQueue(node, 0, adjMap, colorArr)) {
                    return false;
                }
            }

            return true;
        }

        public boolean isBipartite0(int[][] graph) {
            if(graph == null || graph.length == 0) {
                return true;
            }

            int size = graph.length;
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int node = 0; node < size; node++) {
                adjMap.put(node, new ArrayList<>());
            }

            for(int node = 0; node < size; node++) {
                int[] neighbors = graph[node];
                for(int neighbor: neighbors) {
                    adjMap.get(node).add(neighbor);
                }
            }

            /*
            not colored = -1
            red color = 0
            blue color = 1
             */
            int[] colorArr = new int[size];
            Arrays.fill(colorArr, -1);

            for(int node = 0; node < size; node++) {
                if(colorArr[node] == -1 && !dfsUsingRecursion(node, 0, adjMap, colorArr)) {
                    return false;
                }
            }

            return true;
        }
    }
}
