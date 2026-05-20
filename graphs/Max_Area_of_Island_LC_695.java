package neetcode.graphs;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*
- https://leetcode.com/problems/max-area-of-island/description/

- Using DFS recursion
    * TC: O(4^(n*m))
    * SC: O(n*m)

- Using BFS recursion
    * TC: O(n*m)
    * SC: O(n*m)
 */

public class Max_Area_of_Island_LC_695 {
    class Revision02 {
        class UnionFind {
            private int[] parent, size;
            private int maxComponentSize;

            UnionFind(int n) {
                parent = new int[n];
                size = new int[n];
                maxComponentSize = 0;

                for(int i = 0; i < n; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }
            }

            public int findUltimateParent(int node) {
                if(parent[node] == node) {
                    return node;
                }

                return parent[node] = findUltimateParent(parent[node]);
            }

            public boolean unionBySize(int u, int v) {
                int ulpu = findUltimateParent(u), ulpv = findUltimateParent(v);

                if(ulpu == ulpv) {
                    return false;
                }

                if(size[ulpu] < size[ulpv]) {
                    parent[ulpu] = ulpv;
                    size[ulpv] += size[ulpu];
                    maxComponentSize = Math.max(maxComponentSize, size[ulpv]);
                } else {
                    parent[ulpv] = ulpu;
                    size[ulpu] += size[ulpv];
                    maxComponentSize = Math.max(maxComponentSize, size[ulpu]);
                }

                return true;
            }

            public int getMaxNoOfComponent() {
                return maxComponentSize;
            }
        }

        private final static int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        private int dfs(int r, int c, int[][] grid, boolean[][] visited, int rows, int cols) {
            visited[r][c] = true;
            int count = 1;

            for(int[] neighbor: offsetNeighbors) {
                int nr = r + neighbor[0], nc = c + neighbor[1];

                if(isValid(nr, nc, rows, cols) && grid[nr][nc] == 1 && !visited[nr][nc]) {
                    count += dfs(nr, nc, grid, visited, rows, cols);
                }
            }

            return count;
        }

        public int maxAreaOfIsland1(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length, maxArea = 0;
            UnionFind uf = new UnionFind(rows * cols);
            boolean hasLand = false;

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(grid[r][c] == 1) {
                        int idx1 = r * cols + c;
                        hasLand = true;

                        for(int[] neighbor: offsetNeighbors) {
                            int nr = r + neighbor[0], nc = c + neighbor[1];

                            if(isValid(nr, nc, rows, cols) && grid[nr][nc] == 1) {
                                int idx2 = nr * cols + nc;
                                uf.unionBySize(idx1, idx2);
                            }
                        }
                    }
                }
            }

            return hasLand ? Math.max(1, uf.getMaxNoOfComponent()) : 0;
        }

        public int maxAreaOfIsland(int[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int maxArea = 0, rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(grid[r][c] == 1 && !visited[r][c]) {
                        maxArea = Math.max(maxArea, dfs(r, c, grid, visited, rows, cols));
                    }
                }
            }

            return maxArea;
        }
    }

    class Revision01 {
        class UnionFind {
            private final int[] parent;
            private final int[] size;
            private int maxComponent;

            UnionFind(int n) {
                this.parent = new int[n*n];
                this.size = new int[n*n];
                this.maxComponent = 0;

                for(int i = 0; i < n; i++) {
                    this.parent[i] = i;
                    this.size[i] = 1;
                }
            }

            public int find(int node) {
                if (node == parent[node]) {
                    return node;
                }

                return parent[node] = find(parent[node]);
            }

            private void unionBySize(int u, int v) {
                int up = find(u);
                int vp = find(v);

                if(up == vp) {
                    return;
                }

                if(size[up] < size[vp]) {
                    parent[up] = vp;
                    size[vp] += size[up];
                    maxComponent = Math.max(maxComponent, size[vp]);
                } else {
                    parent[vp] = up;
                    size[up] += size[vp];
                    maxComponent = Math.max(maxComponent, size[up]);
                }
            }

            public int getMaxComponent() {
                return maxComponent;
            }
        }

        public int maxAreaOfIsland3(int[][] grid) {
            int maxArea = 0;
            if (grid == null || grid.length == 0) {
                return maxArea;
            }

            int rows = grid.length, cols = grid[0].length;
            UnionFind uf = new UnionFind(rows * cols);
            boolean hasLand = false;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 1) {
                        hasLand = true;
                        for(int[] neighbor: offsetNeighbors) {
                            int nr = r + neighbor[0], nc = c + neighbor[1];

                            if(nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                                uf.unionBySize(r * cols + c, nr * cols + nc);
                            }
                        }
                    }
                }
            }

            return hasLand ? Math.max(1, uf.getMaxComponent()) : 0;
        }

        public int maxAreaOfIsland2(int[][] grid) {
            int maxArea = 0;
            if(grid == null || grid.length == 0) {
                return maxArea;
            }

            int rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(!visited[r][c] && grid[r][c] == 1) {
                        Queue<int[]> queue = new LinkedList<>();
                        queue.offer(new int[]{r, c});
                        visited[r][c] = true;
                        int count = 0;
                        while (!queue.isEmpty()) {
                            int[] curr = queue.poll();
                            int cr = curr[0], cc = curr[1];
                            count++;

                            for(int[] neighbor: offsetNeighbors) {
                                int nr = cr + neighbor[0], nc = cc + neighbor[1];

                                if(nr >= 0 && nr < rows && nc >= 0 && nc < cols
                                        && !visited[nr][nc] && grid[nr][nc] == 1) {
                                    queue.offer(new int[]{nr, nc});
                                    visited[nr][nc] = true;
                                }
                            }
                        }

                        maxArea = Math.max(maxArea, count);
                    }
                }
            }

            return maxArea;
        }

        public int maxAreaOfIsland1(int[][] grid) {
            int maxArea = 0;
            if(grid == null || grid.length == 0) {
                return maxArea;
            }

            int rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(!visited[r][c] && grid[r][c] == 1) {
                        Stack<int[]> stack = new Stack<>();
                        stack.push(new int[]{r, c});
                        visited[r][c] = true;
                        int count = 0;
                        while (!stack.isEmpty()) {
                            int[] curr = stack.pop();
                            int cr = curr[0], cc = curr[1];
                            count++;

                            for(int[] neighbor: offsetNeighbors) {
                                int nr = cr + neighbor[0], nc = cc + neighbor[1];

                                if(nr >= 0 && nr < rows && nc >= 0 && nc < cols
                                        && !visited[nr][nc] && grid[nr][nc] == 1) {
                                    stack.push(new int[]{nr, nc});
                                    visited[nr][nc] = true;
                                }
                            }
                        }

                        maxArea = Math.max(maxArea, count);
                    }
                }
            }

            return maxArea;
        }

        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1}, {1, 0},{0, -1}};

        private int dfs(int r, int c, int[][] grid, boolean[][] visited, int rows, int cols) {
            if(r < 0 || r >= rows || c < 0 || c >= cols || visited[r][c] || grid[r][c] == 0) {
                return 0;
            }

            visited[r][c] = true;
            int count = 1;

            for(int[] neighbor: offsetNeighbors) {
                int nr = r + neighbor[0], nc = c + neighbor[1];
                count += dfs(nr, nc, grid, visited, rows, cols);
            }

            return count;
        }

        public int maxAreaOfIsland0(int[][] grid) {
            int maxArea = 0;
            if(grid == null || grid.length == 0) {
                return maxArea;
            }

            int rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(!visited[r][c] && grid[r][c] == 1) {
                        maxArea = Math.max(maxArea, dfs(r, c, grid, visited, rows, cols));
                    }
                }
            }

            return maxArea;
        }
    }

    class Solution {
        private static final int[][] offsetNeighbors = {{-1,0},{0,1},{1,0},{0,-1}};

        private int bfs(int row, int col, int ROWS, int COLS, int[][] grid, boolean[][] visited) {
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{row, col});
            visited[row][col] = true;
            int count = 0;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currRow = curr[0], currCol = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int newRow = currRow + neighbor[0], newCol = currCol + neighbor[1];

                    if(newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && grid[newRow][newCol] == 1 && !visited[newRow][newCol]) {
                        queue.offer(new int[]{newRow, newCol});
                        visited[newRow][newCol] = true;
                    }
                }
                count++;
            }

            return count;
        }

        public int maxAreaOfIslandWithBFS(int[][] grid) {
            int maxArea = 0;
            if(grid == null || grid.length == 0) {
                return maxArea;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    if(grid[row][col] == 1 && !visited[row][col]) {
                        maxArea = Math.max(maxArea, bfs(row, col, ROWS, COLS, grid, visited));
                    }
                }
            }

            return maxArea;
        }

        private int dfs(int row, int col, int ROWS, int COLS, int[][] grid, boolean[][] visited) {
            if(row < 0 || col < 0 || row >= ROWS || col >= COLS || grid[row][col] == 0 || visited[row][col]) {
                return 0;
            }

            int count = 1;
            visited[row][col] = true;

            for(int[] neighbor: offsetNeighbors) {
                int newRow = row + neighbor[0], newCol = col + neighbor[1];
                count += dfs(newRow, newCol, ROWS, COLS, grid, visited);
            }

            return count;
        }

        public int maxAreaOfIslandWithDFS(int[][] grid) {
            int maxArea = 0;
            if(grid == null || grid.length == 0) {
                return maxArea;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    maxArea = Math.max(maxArea, dfs(row, col, ROWS, COLS, grid, visited));
                }
            }

            return maxArea;
        }
    }
}
