package neetcode.graphs;

import java.util.ArrayList;

/*
* https://www.geeksforgeeks.org/problems/articulation-point-1/1
*
 * ARTICULATION POINTS (Tarjan's Algorithm)
 * ========================================
 *
 * PROBLEM: Find all vertices whose removal increases the number of
 * connected components in an undirected graph.
 *
 * CORE IDEA:
 * During DFS, track two timestamps per node:
 *   - discoveryTime[u] = when DFS first visited u
 *   - minimumTime[u]   = lowest discoveryTime reachable from u's subtree
 *                        using tree edges + AT MOST ONE back edge
 *
 * If a child's subtree cannot reach above u (without going through u),
 * then removing u disconnects that subtree => u is an articulation point.
 *
 * TWO CASES FOR ARTICULATION POINT:
 *   1. Non-root u: discoveryTime[u] <= minimumTime[neighbor]
 *      (note: <=, NOT <  -- different from bridges!)
 *   2. Root u: has 2+ DFS-tree children
 *      (root has no ancestors, so the <= rule doesn't apply)
 *
 * BRIDGE vs ARTICULATION POINT CONDITION:
 *   - Bridge:    discoveryTime[u] <  minimumTime[neighbor]   (strict)
 *   - Art. pt.:  discoveryTime[u] <= minimumTime[neighbor]   (non-strict)
 *   Reason: for a bridge, the edge itself is being removed, so the child
 *   only needs to be unable to bypass the EDGE. For an articulation point,
 *   the node is being removed, so reaching the node itself isn't enough --
 *   the child must reach STRICTLY ABOVE it.
 *
 * BACK EDGE UPDATE -- USE discoveryTime, NOT minimumTime:
 *   if (visited[neighbor])
 *       minimumTime[u] = min(minimumTime[u], discoveryTime[neighbor]);
 *   Reason: minimumTime[neighbor] may not be finalized yet (neighbor is
 *   an ancestor still on the DFS stack). For bridges this happens to give
 *   the right answer either way, but for articulation points and SCCs it
 *   does NOT -- always use discoveryTime here.
 *
 * TREE EDGE UPDATE -- USE minimumTime (it's finalized after recursion):
 *   minimumTime[u] = min(minimumTime[u], minimumTime[neighbor]);
 *
 * WHY childCount FOR ROOT:
 *   Root has no parent, so the <= condition doesn't apply to it.
 *   But if the root has 2+ DFS-tree children, those children's subtrees
 *   can only connect to each other THROUGH the root (otherwise they'd
 *   have been in the same subtree). So removing the root disconnects them.
 *   One DFS-tree child = root is NOT an articulation point.
 *
 * EDGE CASES:
 *   - Disconnected graph: outer loop runs DFS from each unvisited node.
 *   - Multi-edges: parent-vertex tracking is wrong; track parent EDGE ID
 *     instead. (Not needed if problem guarantees simple graph.)
 *   - Self-loops: skipped naturally by parent check; harmless.
 *   - Use boolean[] to dedupe -- a node can satisfy the condition multiple
 *     times (once per qualifying child).
 *
 * COMPLEXITY: O(V + E) time, O(V) space (excluding adjacency list).
 *
 * COMMON MISTAKES (for myself):
 *   1. Using < instead of <= in the non-root condition.
 *   2. Using minimumTime[neighbor] in the back-edge branch instead of
 *      discoveryTime[neighbor].
 *   3. Forgetting the root special case (childCount > 1).
 *   4. Applying the <= rule to the root (parent != -1 guard prevents this).
 *   5. Not handling disconnected graphs in the outer loop.
 *
 * RELATED:
 *   - Bridges (LC 1192): same skeleton, strict < condition, no root case.
 *   - Tarjan's SCC: directed graphs, uses an explicit stack.
 *   - Bridge tree / block-cut tree: advanced, CP-only.
 */

public class Articulation_Point_G_56_TUF {
    class Solution {
        private int timer;

        private void dfs(int node, int parent, ArrayList<ArrayList<Integer>> adjList, boolean[] visited, int[] discoveryTime, int[] minimumTime, boolean[] isArticulationPoint) {
            visited[node] = true;
            discoveryTime[node] = minimumTime[node] = timer;
            timer++;
            int childCount = 0; // Only matters for root check

            for(int neighbor: adjList.get(node)) {
                if(neighbor == parent) continue;

                if(visited[neighbor]) {
                    minimumTime[node] = Math.min(minimumTime[node], discoveryTime[neighbor]);
                } else {
                    childCount++;
                    dfs(neighbor, node, adjList, visited, discoveryTime, minimumTime, isArticulationPoint);
                    minimumTime[node] = Math.min(minimumTime[node], minimumTime[neighbor]);

                    if(parent != -1 && discoveryTime[node] <= minimumTime[neighbor]) {
                        isArticulationPoint[node] = true;
                    }
                }
            }

            if(parent == -1 && childCount > 1) {
                isArticulationPoint[node] = true;
            }
        }

        public ArrayList<Integer> articulationPoints(int n, ArrayList<ArrayList<Integer>> adj) {
            if(adj == null || adj.isEmpty()) {
                return new ArrayList<>();
            }

            timer = 1;

            boolean[] visited = new boolean[n], isArticulationPoint = new boolean[n];
            int[] discoveryTime = new int[n], minimumTime = new int[n];


            for(int i = 0; i < n; i++) {
                if(!visited[i]) {
                    dfs(i, -1, adj, visited, discoveryTime, minimumTime, isArticulationPoint);
                }
            }

            ArrayList<Integer> result = new ArrayList<>();

            for(int i = 0; i < n; i++) {
                if(isArticulationPoint[i]) {
                    result.add(i);
                }
            }

            if(result.isEmpty()) {
                result.add(-1);
            }

            return result;
        }
    }
}
