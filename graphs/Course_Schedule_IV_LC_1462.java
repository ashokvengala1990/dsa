package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/course-schedule-iv/description/

checkIfPrerequisiteUsingTopoSortBFS:
✅ Build adjacency list (prereq → course)
✅ Track inDegree
✅ Seed queue with inDegree=0 nodes
✅ Direct edge: reachable[currNode][neighbor] = true
✅ Propagate ancestors: reachable[i][neighbor] via reachable[i][currNode]
✅ Kahn's BFS: inDegree[neighbor]--, enqueue when hits 0
✅ Answer queries in O(1)

TC and SC
TC: O(V² + E·V)
- Outer while loop processes each node once: O(V)
- Per node, iterates neighbors: O(E) total across all nodes
- Per neighbor, inner loop scans all nodes: O(V)
- Total: O(E·V) + O(V+E) setup ≈ O(E·V) → worst case O(V³) when E ≈ V²

SC: O(V² + E)
- reachable[][] matrix: O(V²) — dominates
- adjMap: O(V + E)
- inDegree, queue: O(V)


checkIfPrerequisiteUsingDFS:
Algorithm: DFS + Precompute Reachability Matrix

Key Insight:
- Not about ordering (like II) or cycle detection (like I)
- About transitive reachability — can node A reach node B?
- Precompute all reachability upfront → answer each query in O(1)


Edge Direction:
- prerequisites[i] = [ai, bi] → ai must come before bi → ai → bi
- adjMap.get(prereq).add(course)  // ai → bi

Core Trick — Two DFS params:
- src = who started the journey (never changes)
- curr = where we are now (moves deeper)
- reachable[src][neighbor] = true → src can reach neighbor


3 Steps:
1) Build adjacency list (ai → bi)
2) DFS from every node → fill boolean[][] reachable (N×N matrix)
3) Each query → reachable[u][v] in O(1)


TC: O(V · (V + E)) → worst case O(V³) when E ≈ V²
SC: O(V²) — reachability matrix dominates

Constraint safety: V ≤ 100 → V³ = 1,000,000 ✅

DFS vs Topo BFS — Quick Comparison
                        DFS                         Topo BFS (your code)
   TC                O(V·(V+E))                           O(V·E) → same
   SC          O(V²) + recursion stack              O(V²), no recursion stack ✅
   Style              Recursive                           Iterative ✅
*/
public class Course_Schedule_IV_LC_1462 {
    class Solution {
        public List<Boolean> checkIfPrerequisiteUsingTopoSortBFS(int numCourses, int[][] prerequisites, int[][] queries) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 0; i < numCourses; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            int[] inDegree = new int[numCourses];

            for(int[] prerequisite: prerequisites) {
                int prereq = prerequisite[0], course = prerequisite[1];
                adjMap.get(prereq).add(course);
                inDegree[course]++;
            }

            Queue<Integer> queue = new LinkedList<>();

            for(int i = 0; i < numCourses; i++) {
                if(inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            boolean[][] reachable = new boolean[numCourses][numCourses];

            while (!queue.isEmpty()) {
                int currNode = queue.poll();

                for(int neighbor: adjMap.get(currNode)) {
                    reachable[currNode][neighbor] = true;

                    for(int i = 0; i < numCourses; i++) {
                        if(reachable[i][currNode]) {
                            reachable[i][neighbor] = true;
                        }
                    }

                    inDegree[neighbor]--;
                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            List<Boolean> result = new ArrayList<>();

            for(int[] query: queries) {
                result.add(reachable[query[0]][query[1]]);
            }

            return result;
        }

        // From src, walk all reachable nodes and mark src -> neighbor = true
        private void helperDFS0(int src, int curr, Map<Integer, List<Integer>> adjMap, boolean[][] reachable) {
            for(int neighbor: adjMap.get(curr)) {
                if(!reachable[src][neighbor]) {
                    reachable[src][neighbor] = true; // src can reach neighbor
                    helperDFS0(src, neighbor, adjMap, reachable);
                }
            }
        }

        public List<Boolean> checkIfPrerequisiteUsingDFS(int numCourses, int[][] prerequisites, int[][] queries) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 0; i < numCourses; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            // Step 1: Build adjacency list
            // Edge direction: ai -> bi (ai is prereq of bi)
            for(int[] prerequisite: prerequisites) {
                int prereq = prerequisite[0], course = prerequisite[1];
                adjMap.get(prereq).add(course); // ai -> bi
            }

            // Step 2: Precompute reachability matrix
            // reachable[i][i] = true means i is a prerequisite of j (i can reach j)
            boolean[][] reachable = new boolean[numCourses][numCourses];

            for(int i = 0; i < numCourses; i++) {
                helperDFS0(i, i, adjMap, reachable);
            }

            List<Boolean> result = new ArrayList<>();

            // Step 3: Answer each query in O(1)
            for(int[] query: queries) {
                result.add(reachable[query[0]][query[1]]); // is query[0] prereq of query[1]?
            }

            return result;
        }
    }
}