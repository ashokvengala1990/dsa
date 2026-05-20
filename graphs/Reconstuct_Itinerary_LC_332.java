package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/reconstruct-itinerary/description/

LC 332 – Reconstruct Itinerary (revision notes)
Idea: Use all tickets exactly once, start from "JFK", return lexicographically smallest itinerary.
Approach: Hierholzer's algorithm for Eulerian path. Greedily walk smallest-lexicographic edge each step; on dead end, prepend to result.
Key steps:

Build graph: Map<String, PriorityQueue<String>> — min-heap auto-sorts destinations lexicographically.
DFS from "JFK", always consuming the smallest available destination (poll()).
When a node has no outgoing edges left → prepend to itinerary (post-order).
Result = reverse post-order = the Eulerian path.

Why prepend on dead end (post-order)?
Greedy "always take smallest" can dead-end at a node before consuming all tickets. By recording nodes only when fully drained, dead-ends naturally fall to the end of the path. Reversing post-order yields the valid Eulerian path.
Why min-heap (PriorityQueue)?
Need lexicographically smallest edge at each step. poll() gives min in O(log E). Sorted list works too but heap handles dynamic removal cleanly.
Why addFirst on LinkedList?
Post-order recording in reverse → prepending builds the final order in one pass. O(1) head insertion.
Two equivalent implementations:

Recursive DFS: drain neighbors via while + poll, then addFirst(airport).
Iterative (stack): peek current; if has neighbors, push next; else pop and addFirst. Same logic, no recursion stack.

Gotchas:

Must use addFirst (not add) — order matters.
Recursion must drain via while, not for-each (PQ mutates during iteration → ConcurrentModificationException).
"Greedy lexicographic + reverse post-order" is non-obvious; pure greedy DFS fails — it can strand tickets.
Each ticket must be used exactly once → consume edges (poll), don't peek.

Why does this work? (Eulerian path intuition)
Problem guarantees a valid itinerary exists (Eulerian path). Hierholzer: any greedy walk that gets stuck must be stuck at the end node of the path. Recording stuck nodes first and reversing reconstructs the full path correctly even when the greedy choice "wastes" an edge early.
Complexity: O(E log E) time (heap ops), O(V + E) space.
Pattern recognition: "Use every edge exactly once" / "Eulerian path/circuit" / "traverse all tickets/edges with ordering constraint" → Hierholzer's algorithm with post-order recording.

Template skeleton:
Build graph: Map<Node, MinHeap<Node>>
DFS(node):
    while heap[node] not empty:
        DFS(heap[node].poll())
    result.addFirst(node)
Return result
 */

public class Reconstuct_Itinerary_LC_332 {
    class Revision01 {
        public List<String> findItinerary1(List<List<String>> tickets) {
            if(tickets == null || tickets.isEmpty()) {
                return new ArrayList<>();
            }

            Map<String, PriorityQueue<String>> graph = new HashMap<>();
            LinkedList<String> itinerary = new LinkedList<>();

            for(List<String> ticket: tickets) {
                graph.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).offer(ticket.get(1));
            }

            Stack<String> stack = new Stack<>();
            stack.push("JFK");

            while (!stack.isEmpty()) {
                String curr = stack.peek();
                PriorityQueue<String> neighbors = graph.get(curr);

                if(neighbors != null && !neighbors.isEmpty()) {
                    stack.push(neighbors.poll());
                } else {
                    itinerary.addFirst(stack.pop());
                }
            }

            return itinerary;
        }

        private void dfs(String airport, Map<String, PriorityQueue<String>> graph, LinkedList<String> itinerary) {
            PriorityQueue<String> neighbors = graph.get(airport);

            while (neighbors != null && !neighbors.isEmpty()) {
                dfs(neighbors.poll(), graph, itinerary);
            }

            itinerary.addFirst(airport);
        }

        public List<String> findItinerary0(List<List<String>> tickets) {
            if(tickets == null || tickets.isEmpty()) {
                return new ArrayList<>();
            }

            Map<String, PriorityQueue<String>> graph = new HashMap<>();
            LinkedList<String> itinerary = new LinkedList<>();

            for(List<String> ticket: tickets) {
                graph.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).offer(ticket.get(1));
            }

            dfs("JFK", graph, itinerary);
            return itinerary;
        }
    }

    class Solution {
        public List<String> findItinerary1(List<List<String>> tickets) {
            if(tickets == null || tickets.isEmpty()) {
                return new ArrayList<>();
            }

            Map<String, PriorityQueue<String>> graph = new HashMap<>();
            for(List<String> ticket: tickets) {
                graph.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).offer(ticket.get(1));
            }

            Stack<String> stack = new Stack<>();
            stack.push("JFK");
            LinkedList<String> itinerary = new LinkedList<>();

            while (!stack.isEmpty()) {
                String curr = stack.peek();
                PriorityQueue<String> neighbors = graph.get(curr);

                if(neighbors != null && !neighbors.isEmpty()) {
                    stack.push(neighbors.poll());
                } else {
                    itinerary.addFirst(stack.pop());
                }
            }

            return itinerary;
        }

        private void dfs(String airport, Map<String, PriorityQueue<String>> graph, LinkedList<String> itinerary) {
            PriorityQueue<String> neighbors = graph.get(airport);

            while (neighbors != null && !neighbors.isEmpty()) {
                dfs(neighbors.poll(), graph, itinerary);
            }

            itinerary.addFirst(airport);
        }

        public List<String> findItinerary0(List<List<String>> tickets) {
            if(tickets == null || tickets.isEmpty()) {
                return new ArrayList<>();
            }

            Map<String, PriorityQueue<String>> graph = new HashMap<>();
            LinkedList<String> itinerary = new LinkedList<>();

            for(List<String> ticket: tickets) {
                graph.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).offer(ticket.get(1));
            }

            dfs("JFK", graph, itinerary);

            return itinerary;
        }
    }
}

/*
===> NeetCode Solution

class Solution:
    def findItinerary(self, tickets: List[List[str]]) -> List[str]:
        adj = {src : [] for src, dst in tickets}

        tickets.sort()
        for src, dst in tickets:
            adj[src].append(dst)

        res = ["JFK"]

        def dfs(src):
            if len(res) == len(tickets) + 1:
                return True
            if src not in adj:
                return False

            temp = list(adj[src])
            for i, v in enumerate(temp):
                adj[src].pop(i)
                res.append(v)
                if dfs(v): return True
                adj[src].insert(i, v)
                res.pop()

            return False
        dfs("JKF")
        return res
 */