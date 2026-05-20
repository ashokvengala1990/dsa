package neetcode.graphs;

/*
https://leetcode.com/problems/flood-fill/description/

Flood Fill (LC 733) — Notes (one place)
Goal:
* Starting from (sr, sc), recolor the entire 4-directionally connected component that has the same original color as image[sr][sc].

Core Idea (DFS/BFS):
* Use DFS (or BFS) to visit neighbors {up, right, down, left}.
* Base/guard checks:
    - Out of bounds → stop
    - Cell color != initialColor → stop
* Mark visited by recoloring the cell to newColor.

Critical Edge Case:
* If initialColor == newColor, doing DFS will loop forever (because recoloring doesn’t change value).
* So always:
    - if (initialColor == newColor) return image;

Two variants:
1. In-place (recommended): modify image directly (no extra grid).
2. Copy + fill: create result copy and fill result (extra space, but keeps input unchanged).

TC / SC:
Let R = rows, C = cols.

In-place DFS (floodFill1):
* TC: O(R * C) worst case (entire grid visited once)
* SC: O(R * C) worst case recursion stack (if component covers whole grid)

Copy + DFS (floodFill0):
* TC: O(R * C) to copy + O(R * C) fill ⇒ O(R * C)
* SC: O(R * C) for result + O(R * C) recursion stack ⇒ O(R * C)
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Flood_Fill_LC_733 {
    class Solution {
        private void bfsUsingQueue(int row, int col, int[][] image, int initialColor, int newColor) {
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{row, col});
            image[row][col] = newColor;
            int rows = image.length, cols = image[0].length;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currRow = curr[0], currCol = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                    if(isValid(neiRow, neiCol, rows, cols)
                            && image[neiRow][neiCol] == initialColor) {
                        queue.offer(new int[]{neiRow, neiCol});
                        image[neiRow][neiCol] = newColor;
                    }
                }
            }
        }

        public int[][] floodFill3(int[][] image, int sr, int sc, int color) {
            int initialColor = image[sr][sc];

            if(initialColor != color) {
                bfsUsingQueue(sr, sc, image, initialColor, color);
            }

            return image;
        }

        private void dfsUsingStack(int row, int col, int[][] image, int initialColor, int newColor) {
            Stack<int[]> stack = new Stack<>();
            stack.push(new int[]{row, col});
            image[row][col] = newColor;
            int rows = image.length, cols = image[0].length;

            while (!stack.isEmpty()) {
                int[] curr = stack.pop();
                int currRow = curr[0], currCol = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                    if(isValid(neiRow, neiCol, rows, cols)
                            && image[neiRow][neiCol] == initialColor) {
                        stack.push(new int[]{neiRow, neiCol});
                        image[neiRow][neiCol] = newColor;
                    }
                }
            }
        }

        public int[][] floodFill2(int[][] image, int sr, int sc, int color) {
            int initialColor = image[sr][sc];

            if(initialColor != color) {
                dfsUsingStack(sr, sc, image, initialColor, color);
            }

            return image;
        }

        private void dfs1(int row, int col, int[][] image, int initialColor, int newColor) {
            image[row][col] = newColor;

            for(int[] neighbor: offsetNeighbors) {
                int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                if(isValid(neiRow, neiCol, image.length, image[0].length)
                        && image[neiRow][neiCol] == initialColor) {
                    dfs1(neiRow, neiCol, image, initialColor, newColor);
                }
            }
        }

        public int[][] floodFill1(int[][] image, int sr, int sc, int color) {
            int initialColor = image[sr][sc];

            if(initialColor != color) {
                dfs1(sr, sc, image, initialColor, color);
            }

            return image;
        }

        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        private void dfs0(int row, int col, int initialColor, int populateColor, int[][] result) {
            result[row][col] = populateColor;

            for(int[] neighbor: offsetNeighbors) {
                int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                if(isValid(neiRow, neiCol, result.length, result[0].length)
                        && result[neiRow][neiCol] == initialColor) {
                    dfs0(neiRow, neiCol, initialColor, populateColor, result);
                }
            }
        }

        public int[][] floodFill0(int[][] image, int sr, int sc, int color) {
            int initialColor = image[sr][sc];
            if(initialColor == color) {
                return image;
            }

            int rows = image.length, cols = image[0].length;
            int[][] result = new int[rows][cols];

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    result[row][col] = image[row][col];
                }
            }

            dfs0(sr, sc, initialColor, color, result);

            return result;
        }
    }
}
