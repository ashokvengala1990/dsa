package neetcode.graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

/*
https://leetcode.com/problems/pacific-atlantic-water-flow/description/

LeetCode 417 – Pacific Atlantic Water Flow (REVISION NOTES)
Problem Summary

Given a m × n matrix of heights, water can flow from a cell to its neighboring cell (up, down, left, right) if the neighbor’s height is less than or equal to the current cell’s height.
The Pacific Ocean touches the top and left borders.
The Atlantic Ocean touches the bottom and right borders.
Return all cells from which water can flow to both oceans.

Core Insight (Most Important)
- Instead of checking from each cell to the oceans (brute force), reverse the thinking:
    * Start from the oceans and move inward to higher or equal height cells.
- This avoids repeated DFS and gives an optimal solution.

Approach 0 — Brute Force DFS (Baseline / TLE)
Idea
- For each cell (r, c):
    * DFS downhill (height >= nextHeight)
    * Check if it can reach:
        - Any Pacific edge (top or left)
        - Any Atlantic edge (bottom or right)

Flow Condition:
heights[curr] >= heights[next]

Time Complexity: O((m × n)²)

Space Complexity: O(m × n)   // visited array per DFS

Verdict
- ❌ Too slow, but useful to explain naïve thinking.

Approach 1 — Multi-Source BFS from Oceans (Optimal)
Key Insight
If a cell can reach an ocean, then that ocean can also reach the cell by reversing the flow.

Steps
1. Add all Pacific border cells to a queue.
2. BFS inward, moving only uphill or flat.
3. Mark reachable cells.
4. Repeat for Atlantic.
5. Take intersection of both reachable matrices.

Reverse Flow Condition:
heights[curr] <= heights[next]

Time Complexity: O(m × n)

Space Complexity: O(m × n)

Verdict:
✅ Optimal, efficient, and interview-ready.

Approach 2 — DFS from Oceans (Optimal & Cleaner)
Same Logic as BFS, Different Traversal
    - DFS from all Pacific borders
    - DFS from all Atlantic borders
    - Move only uphill
    - Intersection = answer

Reverse Flow Condition:
heights[curr] <= heights[next]

Time Complexity: O(m × n)

Space Complexity: O(m × n)   // recursion stack + visited

Verdict:
✅ Preferred for clarity and simplicity.

Height Condition Cheat Sheet:
Context	                     Condition
Brute force (cell → ocean)	    >=
Optimized (ocean → cell)	    <=


Comparison Summary:
Method	                  TC	      SC	Status
Brute Force DFS	        O((mn)²)	O(mn)	 ❌
BFS from Oceans	         O(mn)	    O(mn)	 ✅
DFS from Oceans	        O(mn)	    O(mn)	✅

Interview One-Line Explanations:
Brute Force:
- “I DFS from every cell and check if it can reach both oceans.”

Optimized Insight:
- “I reverse the water flow and start DFS/BFS from ocean borders.”

Why Optimal:
- “Each cell is visited once per ocean, avoiding repeated work.”

Final Takeaway (Ultra-Compact)
- Reverse flow ✅
- Start from borders ✅
- Move uphill ✅
- Intersect results ✅
O(m × n) ✅

 */

public class Pacific_Atlantic_Water_Flow_LC_417 {
    class Revision01 {
        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        private void bfs(Queue<int[]> queue, boolean[][] canReachable, int[][] heights) {
            int rows = heights.length, cols = heights[0].length;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int row = curr[0], col = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                    if(isValid(neiRow, neiCol, rows, cols) && !canReachable[neiRow][neiCol] && heights[row][col] <= heights[neiRow][neiCol]) {
                        canReachable[neiRow][neiCol] = true;
                        queue.offer(new int[]{neiRow, neiCol});
                    }
                }
            }
        }

        public List<List<Integer>> pacificAtlantic1(int[][] heights) {
            if(heights == null || heights.length == 0) {
                return new ArrayList<>();
            }

            int rows = heights.length, cols = heights[0].length;
            boolean[][] canReachPacific = new boolean[rows][cols], canReachAtlantic = new boolean[rows][cols];
            Queue<int[]> pacificQueue = new LinkedList<>(), atlanticQueue = new LinkedList<>();
            List<List<Integer>> result = new ArrayList<>();

            for(int col = 0; col < cols; col++) {
                if(!canReachPacific[0][col]) {
                    canReachPacific[0][col] = true;
                    pacificQueue.offer(new int[]{0, col});
                }

                if(!canReachAtlantic[rows-1][col]) {
                    canReachAtlantic[rows-1][col] = true;
                    atlanticQueue.offer(new int[]{rows - 1, col});
                }
            }

            for(int row = 0; row < rows; row++) {
                if(!canReachPacific[row][0]) {
                    canReachPacific[row][0] = true;
                    pacificQueue.offer(new int[]{row, 0});
                }

                if(!canReachAtlantic[row][cols-1]) {
                    canReachAtlantic[row][cols-1] = true;
                    atlanticQueue.offer(new int[]{row, cols - 1});
                }
            }

            bfs(pacificQueue, canReachPacific, heights);
            bfs(atlanticQueue, canReachAtlantic, heights);

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(canReachPacific[row][col] && canReachAtlantic[row][col]) {
                        result.add(List.of(row, col));
                    }
                }
            }

            return result;
        }

        private void dfs(int row, int col, int[][] heights, boolean[][] canReachable, int rows, int cols) {
            canReachable[row][col] = true;

            for(int[] neighbor: offsetNeighbors) {
                int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                if(isValid(neiRow, neiCol, rows, cols) && !canReachable[neiRow][neiCol] && heights[row][col] <= heights[neiRow][neiCol]) {
                    dfs(neiRow, neiCol, heights, canReachable, rows, cols);
                }
            }
        }

        public List<List<Integer>> pacificAtlantic(int[][] heights) {
            if(heights == null || heights.length == 0) {
                return new ArrayList<>();
            }

            int rows = heights.length, cols = heights[0].length;
            boolean[][] canReachPacific = new boolean[rows][cols], canReachAtlantic = new boolean[rows][cols];
            List<List<Integer>> result = new ArrayList<>();

            // Up, Bottom
            for(int col = 0; col < cols; col++) {
                dfs(0, col, heights, canReachPacific, rows, cols);
                dfs(rows-1, col, heights, canReachAtlantic, rows, cols);
            }

            // Left, Right
            for(int row = 0; row < rows; row++) {
                dfs(row, 0, heights, canReachPacific, rows, cols);
                dfs(row, cols-1, heights, canReachAtlantic, rows, cols);
            }

            for(int row =0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(canReachPacific[row][col] && canReachAtlantic[row][col]) {
                        result.add(List.of(row, col));
                    }
                }
            }

            return result;
        }
    }

    class Solution {
        private void dfs1(int row, int col, int[][] heights, boolean[][] canReachable, int rows, int cols) {
            canReachable[row][col] = true;

            for(int[] neighbor: offsetDirections) {
                int newRow = row + neighbor[0], newCol = col + neighbor[1];

                if(isValid(newRow, newCol, rows, cols)
                        && !canReachable[newRow][newCol]
                        && heights[row][col] <= heights[newRow][newCol]) {
                    dfs1(newRow, newCol, heights, canReachable, rows, cols);
                }
            }
        }

        public List<List<Integer>> pacificAtlantic2(int[][] heights) {
            List<List<Integer>> commonCells = new ArrayList<>();

            if(heights == null || heights.length == 0) {
                return commonCells;
            }

            int rows = heights.length, cols = heights[0].length;
            boolean[][] canReachablePacific = new boolean[rows][cols], canReachableAtlantic = new boolean[rows][cols];

            for(int col = 0; col < cols; col++) {
                dfs1(0, col, heights, canReachablePacific, rows, cols);
                dfs1(rows-1, col, heights, canReachableAtlantic, rows, cols);
            }

            for(int row = 0; row < rows; row++) {
                dfs1(row, 0, heights, canReachablePacific, rows, cols);
                dfs1(row, cols-1, heights, canReachableAtlantic, rows, cols);
            }

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(canReachablePacific[row][col] && canReachableAtlantic[row][col]) {
                        commonCells.add(Stream.of(row, col).toList());
                    }
                }
            }

            return commonCells;
        }

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows
                    && col >= 0 && col < cols;
        }

        private boolean[][] bfs1(int[][] heights, Queue<int[]> queue) {
            int rows = heights.length, cols = heights[0].length;
            boolean[][] reachable = new boolean[rows][cols];

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int row = curr[0], col = curr[1];
                if(reachable[row][col]) {
                    continue;
                }

                reachable[row][col] = true;

                for(int[] neighbor: offsetDirections) {
                    int newRow = row + neighbor[0], newCol = col + neighbor[1];

                    if(isValid(newRow, newCol, rows, cols)
                            && !reachable[newRow][newCol] && heights[row][col] <= heights[newRow][newCol]) {
                        queue.offer(new int[]{newRow, newCol});
                    }
                }
            }

            return reachable;
        }
        private static final int[][] offsetDirections = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        public List<List<Integer>> pacificAtlantic1(int[][] heights) {
            List<List<Integer>> commonCells = new ArrayList<>();
            if(heights == null || heights.length == 0) {
                return commonCells;
            }

            int rows = heights.length, cols = heights[0].length;
            Queue<int[]> pacificQueue = new LinkedList<>(), atlanticQueue = new LinkedList<>();

            for(int col = 0; col < cols; col++) {
                pacificQueue.add(new int[]{0, col});
                atlanticQueue.add(new int[]{rows-1, col});
            }

            for(int row = 0; row < rows; row++) {
                pacificQueue.add(new int[]{row, 0});
                atlanticQueue.add(new int[]{row, cols-1});
            }

            boolean[][] pacificReachable = bfs1(heights, pacificQueue);
            boolean[][] atlanticReachable = bfs1(heights, atlanticQueue);

            for(int row = 0; row < rows; row++) {
                for(int col = 0 ; col < cols; col++) {
                    if (pacificReachable[row][col] && atlanticReachable[row][col]) {
                        commonCells.add(Stream.of(row, col).toList());
                    }
                }
            }

            return commonCells;
        }

        private boolean canReachPacific, canReachAtlantic;

        private void dfs(int row, int col, int[][] heights, boolean[][] visited, int rows, int cols) {
            if(visited[row][col]) {
                return;
            }

            visited[row][col] = true;

            if(row == 0 || col == 0) {
                canReachPacific = true;
            }

            if(row == (rows-1) || col == (cols-1)) {
                canReachAtlantic = true;
            }

            if(canReachPacific && canReachAtlantic) {
                return;
            }

            for(int[] neighbor: offsetDirections) {
                int newRow = row + neighbor[0], newCol = col + neighbor[1];

                if(newRow >= 0 && newRow < rows
                        && newCol >= 0 && newCol < cols
                        && !visited[newRow][newCol] && heights[row][col] >= heights[newRow][newCol]) {
                    dfs(newRow, newCol, heights, visited, rows, cols);
                }
            }
        }

        public List<List<Integer>> pacificAtlantic0(int[][] heights) {
            List<List<Integer>> commonCells = new ArrayList<>();
            if(heights == null || heights.length == 0) {
                return commonCells;
            }

            int rows = heights.length, cols = heights[0].length;
            this.canReachAtlantic = false;
            this.canReachPacific = false;

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    this.canReachAtlantic = false;
                    this.canReachPacific = false;

                    boolean[][] visited = new boolean[rows][cols];
                    dfs(row, col, heights, visited, rows, cols);

                    if(canReachPacific && canReachAtlantic) {
                        commonCells.add(Stream.of(row, col).toList());
                    }
                }
            }

            return commonCells;
        }
    }
}
