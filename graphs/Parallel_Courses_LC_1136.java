package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/parallel-courses/description/

Approach 1: BFS — Kahn's Level-by-Level (minimumSemesters2)
Key Insight: Each BFS level = one semester. All courses in same level taken in parallel.
Core addition over Course Schedule II:

int currLevel = queue.size();         // freeze current semester size
for(int i = 0; i < currLevel; i++) { // process entire level
    ...
}
minSemesters++;                       // one semester per level
```

**Cycle check:** `count == n` — nodes stuck in cycle never reach `inDegree=0`

**TC:** O(V + E) | **SC:** O(V + E)

---

## Approach 2: DFS — `visited` + `pathVisited` (`minimumSemesters0`)

**Key Insight:** Min semesters = longest path in DAG (critical chain).
```
1→3→4 = 3,  2→3→4 = 3  →  answer = 3
3 Cases per neighbor:
!visited[neighbor]       → recurse deeper
pathVisited[neighbor]    → cycle → return -1
else                     → fully computed → use memo[neighbor]
```

**Backtrack:** `pathVisited[node] = false` on exit

`memo[node] = maxDepth + 1` — caches longest path from this node

**TC:** O(V + E) | **SC:** O(V + E) + O(V) recursion stack

---

## Approach 3: DFS — Single `state[]` (`minimumSemesters1`)

**Same as Approach 2** — just cleaner with one array:
```
state = 0 → unvisited       (!visited)
state = 1 → in stack        (visited && pathVisited)
state = 2 → fully computed  (visited && !pathVisited)

3 Cases per neighbor:
state[neighbor] == 0 → recurse
state[neighbor] == 1 → cycle → return -1
state[neighbor] == 2 → use memo[neighbor]
TC: O(V + E) | SC: O(V + E) + O(V) recursion stack

Summary
Approach            Style                   Cycle Detect            Extra
BFS             Level-by-level               count == n        No recursion stack
DFS v1          visited + pathVisited     pathVisited==true    Recursion stack
DFS v2          state[]                     state==1            Recursion stack, cleaner

⚠️ Always Remember

1-indexed → arrays n+1, loops 1 to n
memo[node] = maxDepth + 1 ← the +1 counts current node itself

 */

public class Parallel_Courses_LC_1136 {
    class Solution {
        public int minimumSemesters2(int n, int[][] relations) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            int[] inDegree = new int[n+1];
            for(int[] relation: relations) {
                int prereq = relation[0], course = relation[1];
                adjMap.get(prereq).add(course);
                inDegree[course]++;
            }

            Queue<Integer> queue = new LinkedList<>();

            for(int i = 1; i <= n; i++) {
                if(inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            int minSemesters = 0, count = 0;

            while (!queue.isEmpty()) {
                int currLevel = queue.size();

                for(int i = 0; i < currLevel; i++) {
                    int curr = queue.poll();
                    count++;

                    for(int neighbor: adjMap.get(curr)) {
                        inDegree[neighbor]--;

                        if(inDegree[neighbor] == 0) {
                            queue.offer(neighbor);
                        }
                    }
                }

                minSemesters++;
            }

            return count == n ? minSemesters: - 1;
        }

        private int helperDFS1(int node, Map<Integer, List<Integer>> adjMap, int[] state, int[] memo) {
            state[node] = 1; // visited=true, pathVisited = true

            int maxDepth = 0;

            for(int neighbor: adjMap.get(node)) {
                if(state[neighbor] == 0) { // !visited
                    int length = helperDFS1(neighbor, adjMap, state, memo);
                    if(length == -1) {
                        return -1;
                    }
                    maxDepth = Math.max(maxDepth, length);
                } else if(state[neighbor] == 1) { // visited && pathVisited -> cycle
                    return -1;
                } else {
                    //state = 2 && !pathVisited -> fully computed
                    maxDepth = Math.max(maxDepth, memo[neighbor]);
                }
            }

            state[node] = 2; // pathVisited = false (backtrace)
            memo[node] = maxDepth + 1;
            return memo[node];
        }

        public int minimumSemesters1(int n, int[][] relations) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] rel: relations) {
                adjMap.get(rel[0]).add(rel[1]);
            }

            // 0 = unvisited, 1 = visiting, 2 = done
            int[] state = new int[n+1], memo = new int[n+1];
            int maxSemesters = 0;

            for(int i = 1; i <= n; i++) {
                if(state[i] == 0) { //visited
                    int length = helperDFS1(i, adjMap, state, memo);
                    if(length == -1) {
                        return -1;
                    }
                    maxSemesters = Math.max(maxSemesters, length);
                }
            }

            return maxSemesters;
        }

        private int helperDFS0(int node, Map<Integer, List<Integer>> adjMap, boolean[] visited, boolean[] pathVisited, int[] memo) {
            visited[node] = true;
            pathVisited[node] = true;

            int maxDepth = 0;

            for(int neighbor: adjMap.get(node)) {
                if(!visited[neighbor]) {
                    int result = helperDFS0(neighbor, adjMap, visited, pathVisited, memo);
                    if(result == -1) {
                        return -1;
                    }
                    maxDepth = Math.max(maxDepth, result);
                } else if(pathVisited[neighbor]) {
                    return -1;
                } else {
//                    if(memo[neighbor] != 0) { // Visited && !pathVisited --> fully computed
                    maxDepth = Math.max(maxDepth, memo[neighbor]);
                }
            }

            pathVisited[node] = false;
            memo[node] = maxDepth + 1; // +1 for current node
            return memo[node];
        }

        public int minimumSemesters0(int n, int[][] relations) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 1; i <= n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] rel: relations) {
                adjMap.get(rel[0]).add(rel[1]);
            }

            boolean[] visited = new boolean[n+1], pathVisited = new boolean[n+1];
            int[] memo = new int[n+1];
            int maxSemesters = 0;

            for(int i = 1; i <= n; i++) {
                if(!visited[i]) {
                    int length = helperDFS0(i, adjMap, visited, pathVisited, memo);
                    if(length == -1) {
                        return -1;
                    }
                    maxSemesters = Math.max(maxSemesters, length);
                }
            }

            return maxSemesters;
        }
    }
}
