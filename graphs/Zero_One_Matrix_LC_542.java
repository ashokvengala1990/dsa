package neetcode.graphs;

import java.util.LinkedList;
import java.util.Queue;

/*
https://leetcode.com/problems/01-matrix/description/

MultiSource BFS Algorithm using Queue Data Structure
- Initial distance 2D array, queue data structures
- Iterate matrix and fill the queue where matrix[row][col] == 0 and distance[row][col] = 0
- Iterate queue until it is not empty

TC: O(n*m) + O(n*m) = O(n*m)
SC: O(n*m) for queue and distance data structure

Core Idea (Multi-Source BFS):
- Treat each cell as a graph node
- All 0 cells are sources → enqueue them first
- BFS expands layer-by-layer (level)
- First time we visit a 1 cell → shortest distance to a 0

✅ Why BFS works:
- Graph is unweighted
- BFS guarantees minimum distance
- Multi-source BFS handles nearest zero naturally

📌 Key Observations in This Implementation
- distance[row][col] == 0 + matrix[row][col] == 1 ⇒ unvisited 1-cell
- level represents distance from nearest zero
- Distance is updated when enqueuing (first visit = shortest path)
 */

public class Zero_One_Matrix_LC_542 {
    class Solution {
        private static final int[][] offsetNeighbors = {
                {-1, 0},  // up
                {0, 1},  // right
                {1, 0},  // down
                {0, -1}  // left
        };

        private boolean isValid(int row, int col, int rows, int cols) {
            // bounds check for matrix
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        public int[][] updateMatrix(int[][] matrix) {
            if(matrix == null || matrix.length == 0) {
                return matrix;
            }

            int rows = matrix.length, cols = matrix[0].length, level = 0; // distance from nearest 0
            Queue<int[]> queue = new LinkedList<>(); // BFS queue
            int[][] distance = new int[rows][cols]; // result matrix

            // Step 1: multi-source initialization
            // enqueue all 0-cells
            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(matrix[row][col] == 0) {
                        queue.offer(new int[]{row, col});
                        distance[row][col] = level; // distance to itself is 0
                    }
                }
            }
            level++; // move to level = 1

            // Step 2: BFS level-by-level
            while (!queue.isEmpty()) {
                int currLevelSize = queue.size(); // nodes at current level

                for(int i = 0; i < currLevelSize; i++) {
                    int[] curr = queue.poll();
                    int row = curr[0], col = curr[1];

                    // explore 4-directional neighbors
                    for(int[] neighbor: offsetNeighbors) {
                        int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                        // conditions:
                        // 1) inside grid
                        // 2) must be a 1-cell
                        // 3) must be unvisited (distance == 0)
                        if(isValid(neiRow, neiCol, rows, cols)
                                && matrix[neiRow][neiCol] == 1
                                && distance[neiRow][neiCol] == 0) {
                            distance[neiRow][neiCol] = level; // shortest distance
                            queue.offer(new int[]{neiRow, neiCol});
                        }
                    }
                }

                level++; // move to next level layer
            }

            return distance;
        }
    }
}
