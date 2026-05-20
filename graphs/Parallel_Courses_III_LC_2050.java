package neetcode.graphs;

import java.util.*;

public class Parallel_Courses_III_LC_2050 {
    class Solution {
        public int minimumTime1(int n, int[][] relations, int[] time) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            int[] inDegree = new int[n];
            for(int[] rel: relations) {
                adjMap.get(rel[0]-1).add(rel[1]-1);
                inDegree[rel[1]-1]++;
            }

            Queue<Integer> queue = new LinkedList<>();
            int[] earliest = new int[n];

            for(int i = 0; i < n; i++) {
                if(inDegree[i] == 0) {
                    queue.offer(i);
                    earliest[i] = time[i];
                }
            }

            while (!queue.isEmpty()) {
                int node = queue.poll();

                for(int neighbor: adjMap.get(node)) {
                    earliest[neighbor] = Math.max(earliest[neighbor], earliest[node] + time[neighbor]);

                    inDegree[neighbor]--;

                    if(inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            int ans = 0;

            for(int i = 0; i < n; i++) {
                ans = Math.max(ans, earliest[i]);
            }

            return ans;
        }

        private int helperDFS0(int node, Map<Integer, List<Integer>> adjMap, int[] time, int[] memo) {
            if(memo[node] != -1) {
                return memo[node];
            }

            int ans = time[node];

            for(int neighbor: adjMap.get(node)) {
                ans = Math.max(ans, time[node] + helperDFS0(neighbor, adjMap, time, memo));
            }

            memo[node] = ans;
            return memo[node];
        }

        public int minimumTime0(int n, int[][] relations, int[] time) {
            Map<Integer, List<Integer>> adjMap = new HashMap<>();

            for(int i = 0; i < n; i++) {
                adjMap.put(i, new ArrayList<>());
            }

            for(int[] rel: relations) {
                adjMap.get(rel[0]-1).add(rel[1]-1);
            }

            int[] memo = new int[n];
            Arrays.fill(memo, -1);

            int ans = 0;

            for(int i = 0; i < n; i++) {
                ans = Math.max(ans, helperDFS0(i, adjMap, time, memo));
            }

            return ans;
        }
    }
}
