package neetcode.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/*
https://leetcode.com/problems/critical-connections-in-a-network/
 */

/*
 * CRITICAL CONNECTIONS / BRIDGES (Tarjan's Algorithm)  -- LC 1192
 * ==============================================================
 *
 * PROBLEM: Find all edges whose removal disconnects the graph
 * (or increases the number of connected components).
 * These edges are called "bridges" or "critical connections."
 *
 * CORE IDEA:
 * During DFS, track two timestamps per node:
 *   - discoveryTime[u] = when DFS first visited u
 *   - minimumTime[u]   = lowest discoveryTime reachable from u's subtree
 *                        using tree edges + AT MOST ONE back edge
 *
 * An edge (u, v) where v is a DFS-tree child of u is a BRIDGE iff:
 *     discoveryTime[u] < minimumTime[v]
 * Meaning: v's subtree has NO back edge that reaches u or above. So
 * the only way out of v's subtree is through this edge -- remove it,
 * and the subtree is disconnected.
 *
 * KEY CONDITION -- STRICT < (different from articulation points!):
 *   - Bridge:    discoveryTime[u] <  minimumTime[v]   (strict)
 *   - Art. pt.:  discoveryTime[u] <= minimumTime[v]   (non-strict)
 *   Reason: removing an EDGE is weaker than removing a NODE. For a
 *   bridge, the child only needs to be unable to bypass the edge;
 *   reaching u itself is fine (u is still there). For an articulation
 *   point, u is being removed, so the child must reach STRICTLY ABOVE u.
 *
 * BACK EDGE UPDATE -- TEXTBOOK SAYS USE discoveryTime, NOT minimumTime:
 *   if (visited[neighbor])
 *       minimumTime[u] = min(minimumTime[u], discoveryTime[neighbor]);
 *   For BRIDGES SPECIFICALLY, both forms happen to give the right
 *   answer (the bridge condition only reads minimumTime[child] right
 *   after recursion returns, when it's fully finalized). But the
 *   textbook form uses discoveryTime because it generalizes correctly
 *   to articulation points and SCCs, where minimumTime[neighbor] of
 *   an in-progress ancestor isn't safe to read.
 *   ==> Always write discoveryTime[neighbor] here. It's right for
 *       bridges and necessary for the related algorithms.
 *
 * TREE EDGE UPDATE -- USE minimumTime (it's finalized after recursion):
 *   minimumTime[u] = min(minimumTime[u], minimumTime[neighbor]);
 *
 * NO ROOT SPECIAL CASE (unlike articulation points):
 *   Bridges don't care whether u is the DFS root. The strict-inequality
 *   condition handles it naturally -- if v's subtree can reach u via a
 *   back edge, minimumTime[v] <= discoveryTime[u], and the edge isn't a
 *   bridge. No childCount tracking needed.
 *
 * EDGE CASES:
 *   - Disconnected graph: outer loop runs DFS from each unvisited node.
 *   - Multi-edges: parent-vertex check is wrong; track parent EDGE ID
 *     instead. (Not needed if problem guarantees simple graph -- LC 1192
 *     does guarantee this.)
 *   - Self-loops: skipped naturally; never bridges.
 *   - Empty edge list: no bridges, return empty list.
 *
 * COMPLEXITY: O(V + E) time, O(V) space (excluding adjacency list).
 *
 * COMMON FOLLOW-UP: "How many components after removing all bridges?"
 *   Answer: originalComponents + bridges.size()
 *   Each bridge independently splits its component into exactly 2.
 *   To compute: count components in the outer loop (increment when
 *   starting a new DFS), then add bridges.size().
 *
 * COMMON MISTAKES (for myself):
 *   1. Using <= instead of < in the bridge condition (that's the
 *      articulation point condition, not bridges).
 *   2. Using minimumTime[neighbor] in the back-edge branch -- works
 *      for bridges but is non-standard and breaks for art. points/SCCs.
 *      Always use discoveryTime[neighbor].
 *   3. Forgetting the disconnected-graph outer loop.
 *   4. Adding a root special case from the articulation points template
 *      -- bridges don't need one.
 *
 * RELATED:
 *   - Articulation Points: same skeleton, <= condition, root needs
 *     childCount > 1 special case.
 *   - Tarjan's SCC: directed graphs, explicit stack, on-stack tracking.
 *   - Bridge tree: contract 2-edge-connected components -- CP-only.
 *
 * MY CODE NOTE:
 *   Current back-edge line uses minimumTime[neighbor], which works for
 *   this problem but should be discoveryTime[neighbor] by convention.
 *   Fix on next revision.
 */


public class Critical_Connections_in_a_Network_LC_1192 {
    class Solution {
        private int timer;

        private void dfs(int node, int parent, List<List<Integer>> adjList, boolean[] visited, int[] discoveryTime, int[] minimumTime, List<List<Integer>> bridges) {
            visited[node] = true;
            discoveryTime[node] = minimumTime[node] = timer;
            timer++;

            for(int neighbor: adjList.get(node)) {
                if(neighbor == parent) continue;

                if(visited[neighbor]) {
                    minimumTime[node] = Math.min(minimumTime[node], minimumTime[neighbor]); // minimumTime[node] = Math.min(minimumTime[node], minimumTime[neighbor]);
                } else {
                    dfs(neighbor, node, adjList, visited, discoveryTime, minimumTime, bridges);
                    minimumTime[node] = Math.min(minimumTime[node], minimumTime[neighbor]);

                    // node -- neighbor
                    if(discoveryTime[node] < minimumTime[neighbor]) {
                        bridges.add(Stream.of(node, neighbor).toList());
                    }
                }
            }
        }

        public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
            if(connections == null || connections.isEmpty()) {
                return new ArrayList<>();
            }

            timer = 1;

            List<List<Integer>> adjList = new ArrayList<>();

            for(int i = 0; i < n; i++) {
                adjList.add(new ArrayList<>());
            }

            for(List<Integer> connection: connections) {
                if(connection == null) continue;

                int u = connection.get(0), v = connection.get(1);
                adjList.get(u).add(v);
                adjList.get(v).add(u);
            }

            boolean[] visited = new boolean[n];
            int[] discoveryTime = new int[n], minimumTime = new int[n];
            List<List<Integer>> bridges = new ArrayList<>();

            for(int i = 0; i < n; i++) {
                if(!visited[i]) {
                    dfs(i, -1, adjList, visited, discoveryTime, minimumTime, bridges);
                }
            }

            return bridges;
        }
    }
}
