package neetcode.graphs;

import java.util.LinkedList;
import java.util.Queue;

/*
- https://leetcode.com/problems/number-of-islands/description/

Approach 01: Using DFS recursion
    * TC: O(4^(n*m))
    * SC: O(n*m)

Approach 02: Using BFS recursion
    - Mark the visited cells in visited array during enqueue of the cell in queue data structure.
    This makes that we don't visit the same again. When we use BFS, cell has to be marked during
    enqueue operation, not during dequeue, whereas for PriorityQueue (Graph algorithms), you need
    to mark visited cell during dequeue, not during enqueue because of the nature of smallest edges
    we are looking into.
    * TC: O(n*m)
    * SC: O(n*m)

Approach 03: Using Union-Find DS Approach
Key Ideas:
1) Union-Find Purpose
    - Efficiently keeps track of which cells belong to the same island.
    - Supports two operations:
        * find(x) → returns the root (representative) of the set that x belongs to.
        * union(x, y) → merges two sets if they are different.
2) Grid Representation
    - Each cell (r, c) is mapped to a unique ID: id = r * cols + c.
    - This converts a 2D problem into 1D indexing for DSU.
3) Union Condition
    - Only perform union when both the current cell and its neighbor are '1' (land).
    - Use 4-directional adjacency (offsetNeighbors) to connect up, right, down, left neighbors.
4) Counting Islands
    - Initialize count with the number of '1's in the grid.
    - Each union operation merges two islands → so count-- inside unionBySize().
* TC: O(n*m) + O(4*Alpha) = O(n*m)
* SC: O(n*m) because of union find uses parent and size arrays with size = n*m)

 */

public class Number_of_Islands_LC_200 {
    class Revision02 {
        class UnionFind {
            private int[] parent, size;
            private int count;

            UnionFind(int n) {
                parent = new int[n];
                size = new int[n];
                count = 0;

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
                } else {
                    parent[ulpv] = ulpu;
                    size[ulpu] += size[ulpv];
                }

                count--;
                return true;
            }

            public int numOfComponents() {
                return count;
            }

            public void incrementCount() {
                count++;
            }
        }

        public int numIslands1(char[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length;
            UnionFind uf = new UnionFind(rows * cols);

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(grid[r][c] == '1') {
                        uf.incrementCount();
                    }
                }
            }

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    int idx1 = r * cols + c;
                    if(grid[r][c] == '1') {
                        for(int[] neighbor: offsetNeighbors) {
                            int nr = r + neighbor[0], nc = c + neighbor[1];

                            if(isValid(nr, nc, rows, cols) && grid[nr][nc] == '1') {
                                int idx2 = nr * cols + nc;
                                uf.unionBySize(idx1, idx2);
                            }
                        }
                    }
                }
            }

            return uf.numOfComponents();
        }

        private final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        private void dfs(int r, int c, char[][] grid, boolean[][] visited) {
            visited[r][c] = true;

            for(int[] neighbor: offsetNeighbors) {
                int nr = r + neighbor[0], nc = c + neighbor[1];

                if(isValid(nr, nc, grid.length, grid[0].length) && grid[nr][nc] == '1' && !visited[nr][nc]) {
                    dfs(nr, nc, grid, visited);
                }
            }
        }

        public int numIslands(char[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int count = 0, rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(!visited[r][c] && grid[r][c] == '1') {
                        dfs(r, c, grid, visited);
                        count++;
                    }
                }
            }

            return count;
        }
    }

    class Revision01 {
        // Offsets to move in four directions: up, right, down, left.
// Helps check all 4 neighboring cells of a grid position.
        private static final int[][] offsetNeighbors = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};


        // =============================
//   UNION-FIND (Disjoint Set)
// =============================
        class UnionFind {
            private int[] parent;  // parent[i] stores the representative (root) of the set containing i
            private int[] size;    // size[i] stores the size of the tree rooted at i (used for union by size)
            private int count;     // number of disjoint sets (used to track number of islands)

            // Constructor: initializes the data structures for 'n' nodes
            UnionFind(int n) {
                this.parent = new int[n];
                this.size = new int[n];
                this.count = 0;

                // Initially, every node is its own parent (self loop)
                // Each node forms an individual component of size 1
                for (int i = 0; i < n; i++) {
                    this.parent[i] = i;
                    this.size[i] = 1;
                }
            }

            // Returns the total number of disjoint sets (i.e., islands)
            public int getCount() {
                return count;
            }

            // Increments count (used when a new '1' land cell is found)
            public void incrementCount() {
                count++;
            }

            // FIND operation with path compression
            // Returns the root representative of the set containing 'node'
            // Compresses the path so that each node points directly to its root
            public int find(int node) {
                if (node == parent[node]) {
                    return node;  // Base case: node is its own parent
                }

                // Recursively find and compress the path
                return parent[node] = find(parent[node]);
            }

            // UNION operation (by size optimization)
            // Merges two sets if their roots are different
            public void unionBySize(int u, int v) {
                int up = find(u); // find root of u
                int vp = find(v); // find root of v

                // If both belong to the same set, no need to merge
                if (up == vp) {
                    return;
                }

                // Attach smaller tree under the larger tree
                if (size[up] < size[vp]) {
                    parent[up] = vp;
                    size[vp] += size[up];
                } else {
                    parent[vp] = up;
                    size[up] += size[vp];
                }

                // After union, two sets become one, so reduce island count
                count--;
            }
        }


        // ============================================
//   MAIN LOGIC: COUNT NUMBER OF ISLANDS
// ============================================
        public int numIslands(char[][] grid) {

            // Handle base case: empty grid
            if (grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length;
            int cols = grid[0].length;

            // Create a Union-Find for all cells (flattened 2D -> 1D)
            // Each cell can be mapped as index = row * cols + col
            UnionFind uf = new UnionFind(rows * cols);

            // Step 1: Count total number of land cells ('1's)
            // Each '1' initially represents a separate island
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == '1') {
                        uf.incrementCount();
                    }
                }
            }

            // Step 2: Traverse grid again to merge adjacent lands
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {

                    // Process only land cells
                    if (grid[r][c] == '1') {

                        // Check all 4 neighbors (up, right, down, left)
                        for (int[] neighbor : offsetNeighbors) {
                            int nr = r + neighbor[0];  // new row
                            int nc = c + neighbor[1];  // new col

                            // Ensure neighbor is inside the grid
                            // and also a land cell ('1')
                            if (nr >= 0 && nr < rows
                                    && nc >= 0 && nc < cols
                                    && grid[nr][nc] == '1') {

                                // Flatten 2D indices into 1D to perform union
                                uf.unionBySize(r * cols + c, nr * cols + nc);
                            }
                        }
                    }
                }
            }

            // Step 3: Return remaining number of disjoint sets (islands)
            return uf.getCount();
        }

        private void bfs(int r, int c, char[][] grid, boolean[][] visited, int rows, int cols) {
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{r, c});
            visited[r][c] = true;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int cr = curr[0], cc = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int nr = cr + neighbor[0], nc = cc + neighbor[1];

                    if(nr >= 0 && nr < rows
                            && nc >= 0 && nc < cols
                            && !visited[nr][nc] && grid[nr][nc] == '1') {
                        visited[nr][nc] = true;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
        }

        public int numIslands1(char[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length, count = 0;
            boolean[][] visited = new boolean[rows][cols];

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(!visited[r][c] && grid[r][c] == '1') {
                        bfs(r, c, grid, visited, rows, cols);
                        count++;
                    }
                }
            }

            return count;
        }

        private void dfs(int r, int c, char[][] grid, boolean[][] visited, int rows, int cols) {
            if(r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == '0' || visited[r][c]) {
                return;
            }

            visited[r][c] = true;

            for(int[] neighbor: offsetNeighbors) {
                int nr = r + neighbor[0], nc = c + neighbor[1];
                dfs(nr, nc, grid, visited, rows, cols);
            }
        }

        public int numIslands0(char[][] grid) {
            if(grid == null || grid.length == 0) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length, count = 0;
            boolean[][] visited = new boolean[rows][cols];

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(!visited[r][c] && grid[r][c] == '1') {
                        dfs(r, c, grid, visited, rows, cols);
                        count++;
                    }
                }
            }

            return count;
        }
    }

    class Solution {
        private static final int[][] offsetNeighbors = {{-1,0},{0,1},{1,0},{0,-1}};

        private void bfs(int row, int col, int ROWS, int COLS, char[][] grid, boolean[][] visited) {
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{row, col});
            visited[row][col] = true;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currRow = curr[0], currCol = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int newRow = currRow + neighbor[0], newCol = currCol + neighbor[1];

                    if(newRow >= 0 && newCol >= 0 && newRow < ROWS && newCol < COLS && grid[newRow][newCol] == '1' && !visited[newRow][newCol]) {
                        visited[newRow][newCol] = true;
                        queue.offer(new int[]{newRow, newCol});
                    }
                }
            }
        }

        public int numIslandsWithBFS(char[][] grid) {
            int count = 0;
            if(grid == null || grid.length == 0) {
                return count;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    if(grid[row][col] == '1' && !visited[row][col]) {
                        bfs(row, col, ROWS, COLS, grid, visited);
                        count++;
                    }
                }
            }

            return count;
        }

        private void dfs(int row, int col, int ROWS, int COLS, char[][] grid, boolean[][] visited) {
            if(row < 0 || col < 0 || row >= ROWS || col >= COLS || visited[row][col] || grid[row][col] == '0') {
                return;
            }

            visited[row][col] = true;

            for(int[] neighbor: offsetNeighbors) {
                int newRow = row + neighbor[0], newCol = col + neighbor[1];
                dfs(newRow, newCol, ROWS, COLS, grid, visited);
            }
        }

        public int numIslandsWithDFS(char[][] grid) {
            int count = 0;
            if(grid == null || grid.length == 0) {
                return count;
            }

            int ROWS = grid.length, COLS = grid[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    if(grid[row][col] == '1' && !visited[row][col]) {
                        dfs(row, col, ROWS, COLS, grid, visited);
                        count++;
                    }
                }
            }

            return count;
        }
    }
}
