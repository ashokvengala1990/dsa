package neetcode.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/*
https://www.geeksforgeeks.org/problems/strongly-connected-components-kosarajus-algo/1

Approach: Two DFS passes + transpose graph.
Key steps:

DFS pass 1 on original graph → push nodes to stack on finish (post-order).
Transpose the graph (reverse every edge).
DFS pass 2 on transposed graph, popping from stack → each DFS tree = one SCC.

Why finish-time order?
Last-finished node is guaranteed to be in a source SCC of the original. In the transposed graph, source ↔ sink, so DFS from it can't escape its own SCC. Iterating 0..V-1 doesn't give this guarantee — DFS may leak across SCCs via reversed bridges.
Why transpose?
In transposed graph, edges between SCCs flip direction. Starting DFS from a "now-sink" SCC traps the traversal inside that one SCC.
Gotchas:

Both DFS functions need if(!visited[neighbor]) before recursing — outer loop's !visited[i] only guards entry points, not internal recursion. Cycles → StackOverflow without inner check.
Don't return early on empty edges — n isolated nodes = n SCCs, not 0.
Transpose builds a separate adjacency list — don't mutate the original.
Reset visited[] between the two passes.

Complexity: O(V + E) time, O(V + E) space.
Pattern recognition: "Mutual reachability in directed graph" / "group nodes that can all reach each other" / "condense directed graph into DAG" → Kosaraju (or Tarjan).
Don't confuse with:

Undirected components → plain DFS/BFS/DSU.
Complete components (LC 2685) → undirected CC + edge-count check.
Bridges/articulation points → Tarjan's variant, not Kosaraju.

Template skeleton:
DFS1 on original (with !visited check) → push on finish → stack
Build transpose
Reset visited
While stack not empty:
    pop node
    if !visited: DFS2 on transpose → one SCC, increment count
 */

public class Count_Strongly_Connected_TUF_G_54 {
    class Revision01 {
        private void dfs0(int node, List<List<Integer>> adjList, boolean[] visited, Stack<Integer> stack) {
            visited[node] = true;

            for(int neighbor: adjList.get(node)) {
                if(!visited[neighbor]) {
                    dfs0(neighbor, adjList, visited, stack);
                }
            }

            stack.push(node);
        }

        private void dfs1(int node, List<List<Integer>> adjListTranpose, boolean[] visited, List<Integer> currSccValues) {
            visited[node] = true;
            currSccValues.add(node);

            for(int neighbor: adjListTranpose.get(node)) {
                if(!visited[neighbor]) {
                    dfs1(neighbor, adjListTranpose, visited, currSccValues);
                }
            }
        }

        public int kosaraju(int n, int[][] edges) {
            if(edges == null || edges.length == 0) {
                return 0;
            }

            List<List<Integer>> adjList = new ArrayList<>();

            for(int i = 0; i < n; i++) {
                adjList.add(new ArrayList<>());
            }

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1];
                adjList.get(u).add(v);
            }

            Stack<Integer> stack = new Stack<>();
            boolean[] visited = new boolean[n];

            for(int i = 0; i < n; i++) {
                if(!visited[i]) {
                    dfs0(i, adjList, visited, stack);
                }
            }

            List<List<Integer>> adjListTranpose = new ArrayList<>(), resultSccValues = new ArrayList<>();
            int sccCount = 0;

            for(int i = 0; i < n; i++) {
                adjListTranpose.add(new ArrayList<>());
            }

            Arrays.fill(visited, false);

            for(int[] edge: edges) {
                int u = edge[0], v = edge[1];
                adjListTranpose.get(v).add(u);
            }

            while (!stack.isEmpty()) {
                int currNode = stack.pop();

                if(!visited[currNode]) {
                    List<Integer> currSccValues = new ArrayList<>();
                    dfs1(currNode, adjListTranpose, visited, currSccValues);
                    sccCount++;
                    resultSccValues.add(currSccValues);
                }
            }

            System.out.println(resultSccValues);

            return sccCount;
        }
    }

    class Solution {
        private void dfs(int node, List<List<Integer>> adjList, boolean[] visited) {
            visited[node] = true;

            for(int neighNode: adjList.get(node)) {
                if(!visited[neighNode]) {
                    dfs(neighNode, adjList, visited);
                }
            }
        }

        public int kosaraju(int n, int[][] edges) {
            List<List<Integer>> adjList = new ArrayList<>();

            for(int i =0; i < n; i++) {
                adjList.add(new ArrayList<>());
            }

            // Reverse the edges when building the graph
            for(int[] edge: edges) {
                int u = edge[0], v = edge[1];
                adjList.get(v).add(u);
            }

            boolean[] visited = new boolean[n];
            int sccCount = 0;

            for(int i = 0; i < n; i++) {
                if(!visited[i]) {
                    dfs(i, adjList, visited);
                    sccCount++;
                }
            }

            return sccCount;
        }
    }
}
