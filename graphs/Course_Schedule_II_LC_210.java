package neetcode.graphs;

import java.util.*;

/*
Notes for Quick Revision
1) DFS approach:
    * Detects cycle using pathVisited[].
    * Post-order addition gives reverse topological order.
    * Must reverse list at the end.

2) BFS (Kahn’s Algorithm):
    * Uses in-degree array and queue.
    * Works only if no cycles.
    * Natural order, no reversal needed.

⏱️ Time Complexity
- DFS: O(V + E) → Each node and edge visited once.
- BFS (Kahn’s): O(V + E) → Each node enqueued/dequeued once.

💾 Space Complexity
- DFS: O(V + E) (adjacency list + recursion stack).
- BFS: O(V + E) (adjacency list + queue + in-degree array).

https://leetcode.com/problems/course-schedule-ii/description/
 */

public class Course_Schedule_II_LC_210 {
    class Revision03 {
        private boolean dfs(int node, List<List<Integer>> graph, int[] state, LinkedList<Integer> result) {
            state[node] = 1;

            for(int neighbor: graph.get(node)) {
                if(state[neighbor] == 0) {
                    if(!dfs(neighbor, graph, state, result)) {
                        return false;
                    }
                } else if(state[neighbor] == 1) {
                    return false;
                }
            }

            state[node] = 2;
            result.addFirst(node);

            return true;
        }

        public int[] findOrder1(int numCourses, int[][] prerequisites) {
            List<List<Integer>> graph = new ArrayList<>();
            LinkedList<Integer> result = new LinkedList<>();
            int[] state = new int[numCourses]; // 0 -> not visited, 1 -> visiting, 2 -> visited

            for(int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            for(int[] prerequisite: prerequisites) {
                int course = prerequisite[0], preReq = prerequisite[1];
                graph.get(preReq).add(course);
            }

            for(int node = 0; node < numCourses; node++) {
                if(state[node] == 0) {
                    if(!dfs(node, graph, state, result)) {
                        return new int[]{};
                    }
                }
            }


            return result.size() == numCourses ? result.stream().mapToInt(Integer::intValue).toArray() : new int[]{};
        }

        public int[] findOrder0(int numCourses, int[][] prerequisites) {
            List<List<Integer>> graph = new ArrayList<>();
            int[] inDegree = new int[numCourses];
            List<Integer> result = new ArrayList<>();
            Queue<Integer> queue = new LinkedList<>();

            for(int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            for(int[] prerequisite: prerequisites) {
                int course = prerequisite[0], preReq = prerequisite[1];
                graph.get(preReq).add(course);
                inDegree[course]++;
            }

            for(int i = 0; i < numCourses; i++) {
                if(inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            while (!queue.isEmpty()) {
                int node = queue.poll();
                result.add(node);

                for(int neighbor: graph.get(node)) {
                    inDegree[neighbor]--;
                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return result.size() == numCourses ? result.stream().mapToInt(Integer::intValue).toArray() : new int[]{};
        }
    }

    class Revision02 {
        public int[] findOrderUsingTopoSortAlgorithm(int numCourses, int[][] prerequisites) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 0; i < numCourses; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            int[] inDegree = new int[numCourses];

            for(int[] prerequisite: prerequisites) {
                int course = prerequisite[0], prereq = prerequisite[1];
                adjMap.get(prereq).add(course);
                inDegree[course]++;
            }

            Queue<Integer> queue = new LinkedList<>();

            for(int i = 0; i < numCourses; i++) {
                if(inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            List<Integer> topoSort = new ArrayList<>();

            while (!queue.isEmpty()) {
                int currNode = queue.poll();
                topoSort.add(currNode);

                for(int neighbor: adjMap.get(currNode)) {
                    inDegree[neighbor]--;
                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return topoSort.size() == numCourses
                    ? topoSort.stream().mapToInt(Integer::valueOf).toArray()
                    : new int[]{};
        }

        private boolean helperDFS(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited, boolean[] pathVisited, Stack<Integer> inStack) {
            visited[node] = true;
            pathVisited[node] = true;

            for(int neighbor: adjMap.get(node)) {
                if(!visited[neighbor]) {
                    if(!helperDFS(neighbor, adjMap, visited, pathVisited, inStack)) {
                        return false;
                    }
                } else if(pathVisited[neighbor]) {
                    return false;
                }
            }

            pathVisited[node] = false;
            inStack.push(node);
            return true;
        }

        public int[] findOrderUsingDFS(int numCourses, int[][] prerequisites) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 0; i < numCourses; i++) {
                adjMap.put(i, new ArrayList<>());
            }


            for(int[] prerequisite: prerequisites) {
                int course = prerequisite[0], prereq = prerequisite[1];
                adjMap.get(prereq).add(course);
            }

            boolean[] visited = new boolean[numCourses], pathVisited = new boolean[numCourses];
            Stack<Integer> inStack = new Stack<>();

            for(int i = 0; i < numCourses; i++) {
                if(!visited[i] && !helperDFS(i, adjMap, visited, pathVisited, inStack)) {
                    return new int[]{};
                }
            }

            int[] topoSort = new int[numCourses];
            int i = 0;

            while(i < numCourses && !inStack.isEmpty()) {
                topoSort[i++] = inStack.pop();
            }

            return topoSort;
        }
    }

    class Revision01 {

        /**
         * Helper function for DFS-based Topological Sort
         *
         * @param node current node being visited
         * @param adjMap adjacency list of the graph
         * @param visited marks if a node has been completely processed
         * @param pathVisited marks if a node is part of the current DFS recursion stack
         * @param topoSort stores the topological order
         * @return false if a cycle is detected, true otherwise
         */
        private boolean dfs(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited, boolean[] pathVisited, List<Integer> topoSort) {
            // If the node is already in the current DFS stack → cycle detected
            if (pathVisited[node]) {
                return false;
            }
            // If the node is already processed completely → skip
            else if (visited[node]) {
                return true;
            }

            // Mark current node as visited and part of recursion path
            visited[node] = true;
            pathVisited[node] = true;

            // Explore all its neighbors
            for (int neighbor : adjMap.get(node)) {
                if (!dfs(neighbor, adjMap, visited, pathVisited, topoSort)) {
                    return false; // cycle found
                }
            }

            // Backtrack: remove from current path
            pathVisited[node] = false;

            // Add node after visiting all its dependencies
            topoSort.add(node);

            return true;
        }

        /**
         * DFS-based Topological Sort
         *
         * Note: We add node after all neighbors are visited (post-order)
         *       Reverse list at the end to get correct topological order.
         *
         * @param numCourses total number of nodes
         * @param prerequisites list of edges [course, prerequisite]
         * @return valid topological order or empty array if cycle detected
         */
        public int[] findOrder1(int numCourses, int[][] prerequisites) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            // Initialize adjacency list
            for (int i = 0; i < numCourses; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            // Build graph: course → prereq
            for (int[] prerequisite : prerequisites) {
                int course = prerequisite[0], prereq = prerequisite[1];
                adjMap.get(course).add(prereq);
            }

            List<Integer> topoSort = new ArrayList<>();
            boolean[] visited = new boolean[numCourses];
            boolean[] pathVisited = new boolean[numCourses];

            // Run DFS for all unvisited nodes
            for (int i = 0; i < numCourses; i++) {
                if (!dfs(i, adjMap, visited, pathVisited, topoSort)) {
                    return new int[] {}; // cycle detected
                }
            }

            // Reverse postorder list to get correct topological order
            Collections.reverse(topoSort);
            return topoSort.stream().mapToInt(Integer::valueOf).toArray();
        }

        /**
         * BFS-based Topological Sort (Kahn’s Algorithm)
         *
         * Steps:
         * 1. Build adjacency list and indegree array.
         * 2. Add all nodes with indegree 0 to queue.
         * 3. Pop from queue → add to topo order.
         * 4. Decrease indegree of neighbors, add to queue if indegree becomes 0.
         */
        public int[] findOrder0(int numCourses, int[][] prerequisites) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            // Initialize adjacency list
            for (int i = 0; i < numCourses; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            int[] inDegree = new int[numCourses];

            // Build graph: prereq → course
            for (int[] prerequisite : prerequisites) {
                int course = prerequisite[0], prereq = prerequisite[1];
                adjMap.get(prereq).add(course);
                inDegree[course]++;
            }

            // Add all nodes with indegree 0 to queue
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < numCourses; i++) {
                if (inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            List<Integer> topoSort = new ArrayList<>();

            // Process queue
            while (!queue.isEmpty()) {
                int node = queue.poll();
                topoSort.add(node);

                // Decrease indegree of neighbors
                for (int neighbor : adjMap.get(node)) {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            // If not all nodes are processed → cycle exists
            return topoSort.size() == numCourses ?
                    topoSort.stream().mapToInt(Integer::valueOf).toArray()
                    : new int[] {};
        }
    }

    class Solution {
        public int[] findOrder(int numCourses, int[][] prerequisites) {
            if(numCourses <= 0) {
                return new int[]{};
            } else if(prerequisites == null || prerequisites.length == 0) {
                return new int[]{0};
            }

            Map<Integer, List<Integer>> adjListMap = new HashMap<>();
            for(int i = 0; i < numCourses; i++) {
                adjListMap.put(i, new ArrayList<>());
            }

            int[] inDegree = new int[numCourses];
            for(int[] pre: prerequisites) {
                int node1 = pre[0], node2 = pre[1];

                adjListMap.get(node2).add(node1);
                inDegree[node1]++;
            }

            Queue<Integer> queue = new LinkedList<>();
            for(int i = 0; i < numCourses; i++) {
                if(inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            List<Integer> result = new ArrayList<>();
            while (!queue.isEmpty()) {
                int node = queue.poll();
                result.add(node);

                for(int neighbor: adjListMap.get(node)) {
                    inDegree[neighbor]--;
                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return result.size() == numCourses ? result.stream().mapToInt(Integer::intValue).toArray() : new int[]{};
        }
    }
}
