package neetcode.graphs;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/*
Quick Summary Notes (for Revision)
✅ Problem:
You are given n rooms, each room i may contain keys to other rooms.
You can enter room 0 initially.
Return true if you can visit all rooms.

⚙️ Core Idea:
This is a graph traversal problem:
    - Each room is a node.
    - Each key in a room represents a directed edge to another node.
We just need to check reachability of all nodes from node 0.

🧩 4 Approaches:
Method	Technique	Traversal Type	Code
canVisitAllRooms0	Recursive DFS (void)	DFS	Simpler version
canVisitAllRooms1	Recursive DFS (returns count)	DFS	Counts reachable nodes
canVisitAllRooms2	Iterative DFS (stack)	DFS	Avoids recursion stack
canVisitAllRooms3	BFS (queue)	BFS	Level-order
🕒 Time Complexity: O(V + E), Each room (V) and key (E) is visited once.

💾 Space Complexity: O(V)
For visited array, and queue/stack recursion depth.

🧩 DFS vs BFS Notes:
Aspect	                DFS	                                BFS
Implementation	Recursive or Stack	                       Queue
Use Case	    Depth-first exploration	                Level-by-level exploration
Space	        O(V) recursion/stack	                    O(V) queue
Order	        May go deep before visiting siblings	Visits closer rooms first
Result	          Same correctness	                       Same correctness

✅ Summary
- All 4 methods solve the same problem.
- Prefer BFS (Queue) or Iterative DFS (Stack) for large input sizes to avoid stack overflow.
- DFS (recursive) is clean and intuitive for smaller input constraints.

 */

public class Keys_and_Rooms_LC_841 {
    class Solution {
        // ✅ BFS Using Queue
        private boolean bfsUsingQueue(int node, List<List<Integer>> rooms) {
            int size = rooms.size(), count = 0;
            Queue<Integer> queue = new LinkedList<>();
            boolean[] visited = new boolean[size];

            // Start from room 0
            queue.offer(node);
            visited[node] = true;

            while (!queue.isEmpty()) {
                int currNode = queue.poll(); // take one room
                count++; // mark it as visited count

                // Explore all keys in current room
                for (int neighbor : rooms.get(currNode)) {
                    if (!visited[neighbor]) {
                        queue.offer(neighbor);
                        visited[neighbor] = true;
                    }
                }
            }

            // If all rooms visited, return true
            return count == size;
        }

        public boolean canVisitAllRooms3(List<List<Integer>> rooms) {
            if (rooms == null || rooms.isEmpty()) {
                return true;
            }

            return bfsUsingQueue(0, rooms);
        }

        // ✅ DFS Using Stack (Iterative)
        private boolean dfsUsingStack(int node, List<List<Integer>> rooms) {
            int size = rooms.size(), count = 0;
            Stack<Integer> stack = new Stack<>();
            boolean[] visited = new boolean[size];

            // Start from room 0
            stack.push(node);
            visited[node] = true;

            while (!stack.isEmpty()) {
                int currNode = stack.pop();
                count++;

                // Visit all rooms that the current room's keys unlock
                for (int neighbor : rooms.get(currNode)) {
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                        visited[neighbor] = true;
                    }
                }
            }

            return count == size;
        }

        public boolean canVisitAllRooms2(List<List<Integer>> rooms) {
            if (rooms == null || rooms.isEmpty()) {
                return true;
            }

            return dfsUsingStack(0, rooms);
        }

        // ✅ DFS Recursive - returns count of rooms visited
        private int dfs1(int node, List<List<Integer>> rooms, boolean[] visited) {
            visited[node] = true;
            int count = 1; // count this room

            // Recurse for all unvisited neighbors
            for (int neighbor : rooms.get(node)) {
                if (!visited[neighbor]) {
                    count += dfs1(neighbor, rooms, visited);
                }
            }

            return count;
        }

        public boolean canVisitAllRooms1(List<List<Integer>> rooms) {
            if (rooms == null || rooms.isEmpty()) {
                return true;
            }

            int size = rooms.size();
            boolean[] visited = new boolean[size];

            // Start DFS from room 0
            return dfs1(0, rooms, visited) == size;
        }

        // ✅ DFS Recursive (no return, simple version)
        private void dfs0(int node, List<List<Integer>> rooms, boolean[] visited) {
            visited[node] = true;

            for (int neighbor : rooms.get(node)) {
                if (!visited[neighbor]) {
                    dfs0(neighbor, rooms, visited);
                }
            }
        }

        public boolean canVisitAllRooms0(List<List<Integer>> rooms) {
            if (rooms == null || rooms.isEmpty()) {
                return true;
            }

            int size = rooms.size();
            boolean[] visited = new boolean[size];

            // Perform DFS from room 0
            dfs0(0, rooms, visited);

            // Check if all rooms were visited
            int count = 0;
            for (int i = 0; i < size; i++) {
                if (visited[i]) {
                    count++;
                }
            }

            return count == size;
        }
    }
}
