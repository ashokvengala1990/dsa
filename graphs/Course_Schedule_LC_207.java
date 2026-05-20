package neetcode.graphs;

import java.util.*;

/*
Revision02==>

Notes
1) DFS Version (canFinish1)
- Uses recursion and two arrays:
    * visited[] → Marks nodes already processed.
    * pathVisited[] → Marks nodes currently in recursion stack.
- Detects back edges, which indicate cycles.
- Works purely for cycle detection (not for producing topological order).

2) BFS Version (canFinish0)
- Implements Kahn’s Algorithm (topological sort using in-degrees).
- If all nodes are processed (count == numCourses) → no cycle.
- Intuitive when building the actual course order.

Time & Space Complexity
Method	Time Complexity	Space Complexity	Notes
DFS (canFinish1)	O(V + E)	O(V + E) (adjacency list + recursion stack + visited arrays)	Each node and edge visited once
BFS (canFinish0)	O(V + E)	O(V + E) (adjacency list + in-degree + queue)	Each edge processed once

Where:
V = numCourses
E = prerequisites.length

✅ Summary
- Use canFinish1() when you prefer recursion (DFS) and don’t need explicit order.
- Use canFinish0() (Kahn’s Algorithm) when you also want the topological order of courses.
- Both effectively solve Course Schedule (Leetcode 207).

https://leetcode.com/problems/course-schedule/description/
 */

public class Course_Schedule_LC_207 {
    class Revision04 {
        private boolean dfs(int node, List<List<Integer>> graph, int[] state) {
            state[node] = 1;

            for(int neighbor: graph.get(node)) {
                if(state[neighbor] == 0) {
                    if(!dfs(neighbor, graph, state)) {
                        return false;
                    }
                } else if(state[neighbor] == 1) {
                    return false;
                }
            }

            state[node] = 2;
            return true;
        }

        public boolean canFinish1(int numCourses, int[][] prerequisites) {
            List<List<Integer>> graph = new ArrayList<>();

            for(int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            for(int[] prerequisite: prerequisites) {
                int course = prerequisite[0], preReq = prerequisite[1];
                graph.get(preReq).add(course);
            }

            int[] state = new int[numCourses];

            for(int i = 0; i < numCourses; i++) {
                if(state[i] == 0) {
                    if(!dfs(i, graph, state)) {
                        return false;
                    }
                }
            }

            return true;
        }

        public boolean canFinish(int numCourses, int[][] prerequisites) {
            List<List<Integer>> graph = new ArrayList<>();
            int[] inDegree = new int[numCourses];
            Queue<Integer> queue = new LinkedList<>();
            int count = 0;

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
                int course = queue.poll();
                count++;

                for(int neighbor: graph.get(course)) {
                    inDegree[neighbor]--;
                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return count == numCourses;
        }
    }

    class Revision03 {
        public boolean canFinishUsingTopoSort(int numCourses, int[][] prerequisites) {
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

            int count = 0;
            while (!queue.isEmpty()) {
                int currNode = queue.poll();
                count++;

                for(int neighbor: adjMap.get(currNode)) {
                    inDegree[neighbor]--;
                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return count == numCourses;
        }

        private boolean helperDFS(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited, boolean[] pathVisited) {
            visited[node] = true;
            pathVisited[node] = true;

            for(int neighbor: adjMap.get(node)) {
                if(!visited[neighbor]) {
                    if(!helperDFS(neighbor, adjMap, visited, pathVisited)) {
                        return false;
                    }
                } else if(pathVisited[neighbor]) {
                    return false;
                }
            }

            pathVisited[node] = false;
            return true;
        }

        public boolean canFinishUsingDFS(int numCourses, int[][] prerequisites) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 0; i < numCourses; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] prerequisite: prerequisites) {
                int course = prerequisite[0], prereq = prerequisite[1];
                adjMap.get(prereq).add(course);
            }

            boolean[] visited = new boolean[numCourses], pathVisited = new boolean[numCourses];

            for(int i = 0; i < numCourses; i++) {
                if(!visited[i] && !helperDFS(i, adjMap, visited, pathVisited)) {
                    return false;
                }
            }

            return true;
        }
    }

    class Revision02 {

        // ------------------- DFS-based Cycle Detection -------------------
        // Detects cycles in a directed graph (Course Schedule problem)
        // If a cycle exists → return false (cannot finish all courses)

        private boolean dfs(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited, boolean[] pathVisited) {
            // If this node is already in the current recursion stack (path), a cycle exists
            if (pathVisited[node]) {
                return false;
            }
            // If node is already fully processed in a previous DFS call, skip it
            else if (visited[node]) {
                return true;
            }

            // Mark the node as visited globally and in the current recursion path
            visited[node] = true;
            pathVisited[node] = true;

            // Explore all the neighbors (dependent courses)
            for (int neighbor : adjMap.get(node)) {
                if (!dfs(neighbor, adjMap, visited, pathVisited)) {
                    return false;  // Cycle detected
                }
            }

            // Remove the node from the current recursion stack before returning
            pathVisited[node] = false;

            // No cycle found in this path
            return true;
        }

        public boolean canFinish1(int numCourses, int[][] prerequisites) {
            // Build adjacency list → course → [list of prerequisites]
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for (int i = 0; i < numCourses; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            // Build graph edges
            for (int[] prerequisite : prerequisites) {
                int course = prerequisite[0];
                int prereq = prerequisite[1];
                adjMap.get(course).add(prereq); // Edge: course → prereq (reversed direction)
            }

            boolean[] visited = new boolean[numCourses];     // Marks nodes that are fully processed
            boolean[] pathVisited = new boolean[numCourses]; // Marks nodes currently in recursion stack

            // Perform DFS from every unvisited node
            for (int i = 0; i < numCourses; i++) {
                if (!dfs(i, adjMap, visited, pathVisited)) {
                    return false; // Cycle detected
                }
            }

            // If no cycles found → all courses can be completed
            return true;
        }

        // ------------------- BFS-based Topological Sort (Kahn’s Algorithm) -------------------
        // Detects cycles using in-degree method
        // If all nodes can be processed (count == numCourses) → no cycle

        public boolean canFinish0(int numCourses, int[][] prerequisites) {
            Map<Integer, List<Integer>> adjList = new HashMap<>();

            // Build adjacency list → prereq → [list of courses depending on it]
            for (int i = 0; i < numCourses; i++) {
                adjList.put(i, new ArrayList<>());
            }

            int[] inDegree = new int[numCourses]; // inDegree[i] = number of prerequisites for course i

            for (int[] prerequisite : prerequisites) {
                int course = prerequisite[0];
                int prereq = prerequisite[1];
                adjList.get(prereq).add(course); // Edge: prereq → course
                inDegree[course]++;               // Increase indegree of dependent course
            }

            // Add all courses with no prerequisites to queue
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < numCourses; i++) {
                if (inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            int count = 0; // Count of courses that can be completed

            // Process nodes in topological order
            while (!queue.isEmpty()) {
                int node = queue.poll();
                count++;

                for (int neighbor : adjList.get(node)) {
                    inDegree[neighbor]--; // One prerequisite completed
                    if (inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            // If all courses were processed → no cycle exists
            return count == numCourses;
        }
    }

    class Revision01 {
        public boolean dfs(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited, boolean[] pathVisited) {
            if(pathVisited[node]) {
                return false;
            } else if(visited[node]) {
                return true;
            }

            visited[node] = true;
            pathVisited[node] = true;

            for(int neighbor: adjMap.get(node)) {
                if(!dfs(neighbor, adjMap, visited, pathVisited)) {
                    return false;
                }
            }

            pathVisited[node] = false;
            return true;
        }

        public boolean canFinish1(int numCourses, int[][] prerequisites) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 0; i < numCourses; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] prerequisite: prerequisites) {
                int course = prerequisite[0], prereq = prerequisite[1];
                adjMap.get(course).add(prereq);
            }

            boolean[] visited = new boolean[numCourses], pathVisited = new boolean[numCourses];

            for(int i = 0; i < numCourses; i++) {
                if(!dfs(i, adjMap, visited, pathVisited)) {
                    return false;
                }
            }

            return true;
        }

        public boolean canFinish0(int numCourses, int[][] prerequisites) {
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

            int count = 0;
            while (!queue.isEmpty()) {
                int node = queue.poll();
                count++;

                for(int neighbor: adjMap.get(node)) {
                    inDegree[neighbor]--;

                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return count == numCourses;
        }
    }

    class Solution {
        public boolean canFinish1(int numCourses, int[][] prerequisites) {
            if(numCourses <= 0 || prerequisites == null || prerequisites.length == 0) {
                return true;
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

            int count = 0;
            while (!queue.isEmpty()) {
                int node = queue.poll();
                count++;

                for(int neighbor: adjListMap.get(node)) {
                    inDegree[neighbor]--;

                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return count == numCourses;
        }

        private boolean dfs(int node, Map<Integer, List<Integer>> adjListMap, Set<Integer> visited) {
            if(visited.contains(node)) {
                return false;
            }

            if(adjListMap.get(node).isEmpty()) {
                return true;
            }

            visited.add(node);

            for(int neighbor: adjListMap.get(node)) {
                if(!dfs(neighbor, adjListMap, visited)) {
                    return false;
                }
            }

            visited.remove(node);
            adjListMap.get(node).clear();
            return true;
        }

        public boolean canFinish0(int numCourses, int[][] prerequisites) {
            if(numCourses <= 0 || prerequisites == null || prerequisites.length == 0) {
                return true;
            }

            Map<Integer, List<Integer>> adjListMap = new HashMap<>();
            for(int i = 0; i < numCourses; i++) {
                adjListMap.put(i, new ArrayList<>());
            }

            for(int[] pre: prerequisites) {
                int node1 = pre[0], node2 = pre[1];
                adjListMap.get(node1).add(node2);
            }

            Set<Integer> visited = new HashSet<>();

            for(int i = 0; i < numCourses; i++) {
                if(!dfs(i, adjListMap,visited)) {
                    return false;
                }
            }

            return true;
        }
    }
}
