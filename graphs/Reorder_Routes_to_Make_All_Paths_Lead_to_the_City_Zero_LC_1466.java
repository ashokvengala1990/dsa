package neetcode.graphs;

import java.util.*;

/*
# start at city 0
# recursively check its neighbors
# count outgoing edges

 */
/*
https://leetcode.com/problems/reorder-routes-to-make-all-paths-lead-to-the-city-zero/description/

# Problem: 1466. Reorder Routes to Make All Paths Lead to the City Zero
# Goal: Count minimum number of edges to reverse so all paths lead to city 0
# Approach: DFS / BFS / Stack traversal with direction tracking


📌 Notes for Revision
1) Problem Type: Graph, DFS/BFS, Tree, Reversing edges.
2) Core Idea: Traverse all nodes starting from 0, count edges that are going away from 0.
3) DFS vs BFS: Both work; DFS can be recursive or stack-based; BFS uses queue.
4) Edge Direction Tracking:
    * Option 1: Store as (neighbor, direction) in adjacency list.
    * Option 2: Store original edges in a Set<String> and check if reversal is needed.
5) Visited Array: Prevent cycles, ensures each node processed once.
6) Return Value: Sum of edges needing reversal.

⏱ Time Complexity
DFS / BFS with adjacency list:
O(N + E)
    - N = number of nodes (cities)
    - E = number of edges (connections)

📦 Space Complexity
- Adjacency Map: O(N + E)
- Visited Array: O(N)
- Stack / Queue: O(N) in worst case
- Set for edges (optional): O(E)

*/

public class Reorder_Routes_to_Make_All_Paths_Lead_to_the_City_Zero_LC_1466 {
    class Solution {

        // BFS approach using queue
        private int bfsUsingQueue(int node, Map<Integer, List<int[]>> adjMap, boolean[] visited) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(node);
            visited[node] = true;

            int changes = 0; // counts edges that need to be reversed

            while (!queue.isEmpty()) {
                int currNode = queue.poll();

                for (int[] neighbor : adjMap.get(currNode)) {
                    int nextNode = neighbor[0], direction = neighbor[1];
                    // direction = 1 -> original edge (needs reversal if moving away from 0)
                    // direction = 0 -> already points towards 0 (no reversal needed)

                    if (!visited[nextNode]) {
                        changes += direction; // add if edge needs reversal
                        queue.offer(nextNode);
                        visited[nextNode] = true;
                    }
                }
            }

            return changes;
        }

        public int minReorder3(int n, int[][] connections) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) adjMap.put(i, new ArrayList<>());

            // Build adjacency map with direction info
            for (int[] connection : connections) {
                int u = connection[0], v = connection[1];
                adjMap.get(u).add(new int[]{v, 1}); // original direction u->v
                adjMap.get(v).add(new int[]{u, 0}); // reverse direction v->u
            }

            boolean[] visited = new boolean[n];
            return bfsUsingQueue(0, adjMap, visited);
        }

        // DFS using stack (iterative)
        private int dfsUsingStack(int node, Map<Integer, List<int[]>> adjMap, boolean[] visited) {
            Stack<Integer> stack = new Stack<>();
            stack.push(node);
            visited[node] = true;

            int changes = 0;

            while (!stack.isEmpty()) {
                int currNode = stack.pop();

                for (int[] neighbor : adjMap.get(currNode)) {
                    int nextNode = neighbor[0], direction = neighbor[1];
                    if (!visited[nextNode]) {
                        changes += direction;
                        stack.push(nextNode);
                        visited[nextNode] = true;
                    }
                }
            }

            return changes;
        }

        public int minReorder2(int n, int[][] connections) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) adjMap.put(i, new ArrayList<>());

            for (int[] connection : connections) {
                int u = connection[0], v = connection[1];
                adjMap.get(u).add(new int[]{v, 1});
                adjMap.get(v).add(new int[]{u, 0});
            }

            boolean[] visited = new boolean[n];
            return dfsUsingStack(0, adjMap, visited);
        }

        // DFS recursive
        private int dfs1(int node, Map<Integer, List<int[]>> adjMap, boolean[] visited) {
            visited[node] = true;

            int changes = 0;

            for (int[] neighbor : adjMap.get(node)) {
                int nextNode = neighbor[0], direction = neighbor[1];
                if (!visited[nextNode]) {
                    changes += direction; // count edge to reverse
                    changes += dfs1(nextNode, adjMap, visited); // explore recursively
                }
            }

            return changes;
        }

        public int minReorder1(int n, int[][] connections) {
            Map<Integer, List<int[]>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) adjMap.put(i, new ArrayList<>());

            for (int[] connection : connections) {
                int u = connection[0], v = connection[1];
                adjMap.get(u).add(new int[]{v, 1});
                adjMap.get(v).add(new int[]{u, 0});
            }

            boolean[] visited = new boolean[n];
            return dfs1(0, adjMap, visited);
        }

        // DFS with Set<String> for edge lookup
        private int dfs0(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited, Set<String> roads) {
            visited[node] = true;
            int changes = 0;

            for (int neighbor : adjMap.get(node)) {
                if (!visited[neighbor]) {
                    // If the road is not pointing towards 0, it needs reversal
                    if (!roads.contains(neighbor + "->" + node)) {
                        changes++;
                    }
                    changes += dfs0(neighbor, adjMap, visited, roads);
                }
            }

            return changes;
        }

        public int minReorder0(int n, int[][] connections) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();
            for (int i = 0; i < n; i++) adjMap.put(i, new ArrayList<>());

            Set<String> roads = new HashSet<>();
            for (int[] connection : connections) {
                int u = connection[0], v = connection[1];
                adjMap.get(u).add(v);
                adjMap.get(v).add(u);
                roads.add(u + "->" + v); // store original directed edge
            }

            boolean[] visited = new boolean[n];
            return dfs0(0, adjMap, visited, roads);
        }
    }
}
