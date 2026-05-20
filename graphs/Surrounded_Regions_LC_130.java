package neetcode.graphs;

/*
- https://leetcode.com/problems/surrounded-regions/description/

Key Idea
- The 'O's connected to the border should not be flipped.
- We mark all 'O's that are connected to the border (via DFS or BFS).
- Any 'O' not marked (i.e., not reachable from border) is flipped to 'X'.

1️⃣ Recursive DFS (solve0)
Approach
- Start DFS from all border 'O' cells.
- Mark visited connected 'O's.
- After traversal, flip all unvisited 'O's to 'X'.

Pros: Simple and easy to write.
Cons: Stack overflow possible for large grids (deep recursion).

Key Fix:
✅ Base condition must check all 4 boundaries (r < 0, c < 0, etc.)
✅ Mark visited before recursive calls.

2️⃣ Iterative DFS using Stack (solve1)
Approach
- Similar to recursive DFS but avoids recursion stack.
- Use a Stack<int[]> for manual traversal.

Important Fix:
❌ Wrong variable used before: used r and c instead of cr and cc when computing neighbors.
✅ Correct:

int nr = cr + neighbor[0];
int nc = cc + neighbor[1];

Reason for Error:
- Using outer r and c meant all neighbor expansions were from the original starting point, not the current cell.

3️⃣ BFS using Queue (solve2)
Approach
- Same traversal logic as DFS but uses a queue.
- Ensures breadth-wise traversal; prevents recursion issues.

Behavior:
All three methods (DFS-recursive, DFS-iterative, BFS) will produce the same output.

💡 Common Post-processing Step
After marking all 'O's reachable from border:

if (board[r][c] == 'O' && !visited[r][c]) {
    board[r][c] = 'X';
}

This flips only surrounded regions.

Time & Space Complexity:
Approach	    Time Complexity	        Space Complexity	                    Explanation
DFS (recursive)	O(V + E) ≈ O(m × n)	O(V) (recursion stack + visited array)	Visit each cell once
DFS (stack)	O(V + E) ≈ O(m × n)	O(V) (stack + visited array)	Iterative version
BFS (queue)	O(V + E) ≈ O(m × n)	O(V) (queue + visited array)	Level-order traversal

V = m × n (total cells)
E = 4 × V (each cell has up to 4 neighbors)

====> Final Summary:
Version	Traversal	Uses Recursion	Data Structure	Notes
solve0	DFS	                ✅ Yes	        None	    Easiest but risks stack overflow
solve1	DFS	                ❌ No	        Stack	    Safe for large boards
solve2	BFS	                ❌ No	        Queue	    Most balanced and reliable
solve3  BFS (Multi-Source)  ❌ No           Queue       Most balanced and reliable
 */

import backtracking.NQueens_LC_51;

import java.util.*;

public class Surrounded_Regions_LC_130 {
    class Revision03 {
        public void solve1(char[][] board) {
            if(board == null || board.length == 0) {
                return;
            }

            int rows = board.length, cols = board[0].length;
            Queue<int[]> queue = new LinkedList<>();

            for(int col = 0; col < cols; col++) {
                if(board[0][col] == 'O') {
                    board[0][col] = 'S';
                    queue.offer(new int[]{0, col});
                }

                if(board[rows-1][col] == 'O') {
                    board[rows-1][col] = 'S';
                    queue.offer(new int[]{rows-1, col});
                }
            }

            for(int row = 0; row < rows; row++) {
                if(board[row][0] == 'O') {
                    board[row][0] = 'S';
                    queue.offer(new int[]{row, 0});
                }

                if(board[row][cols-1] == 'O') {
                    board[row][cols-1] = 'S';
                    queue.offer(new int[]{row, cols-1});
                }
            }

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int row = curr[0], col = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                    if(isValid(neiRow, neiCol, rows, cols) && board[neiRow][neiCol] == 'O') {
                        board[neiRow][neiCol] = 'S';
                        queue.offer(new int[]{neiRow, neiCol});
                    }
                }
            }

            for(int row =0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(board[row][col] == 'O') {
                        board[row][col] = 'X';
                    } else if(board[row][col] == 'S') {
                        board[row][col] = 'O';
                    }
                }
            }
        }

        private boolean isValid(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private void dfs(int row, int col, char[][] board) {
            board[row][col] = 'S';

            for(int[] neighbor: offsetNeighbors) {
                int neiRow = row + neighbor[0], neiCol = col + neighbor[1];

                if(isValid(neiRow, neiCol, board.length, board[0].length)
                        && board[neiRow][neiCol] == 'O') {
                    dfs(neiRow, neiCol, board);
                }
            }
        }

        public void solve0(char[][] board) {
            if(board == null || board.length == 0) {
                return;
            }

            int rows = board.length, cols = board[0].length;

            for(int col = 0; col < cols; col++) {
                if(board[0][col] == 'O') {
                    dfs(0, col, board);
                }

                if(board[rows-1][col] == 'O') {
                    dfs(rows-1, col, board);
                }
            }

            for(int row = 0; row < rows; row++) {
                if(board[row][0] == 'O') {
                    dfs(row, 0, board);
                }

                if(board[row][cols-1] == 'O') {
                    dfs(row, cols-1, board);
                }
            }


            for(int row =0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(board[row][col] == 'O') {
                        board[row][col] = 'X';
                    } else if(board[row][col] == 'S') {
                        board[row][col] = 'O';
                    }
                }
            }
        }
    }

    class Revision02 {
        public void solve3(char[][] board) {
            if (board == null || board.length == 0) {
                return;
            }

            int rows = board.length, cols = board[0].length;
            boolean[][] visited = new boolean[rows][cols];
            Queue<int[]> queue = new LinkedList<>();

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if((row == 0 || col == 0 || row == (rows-1) || col == (cols-1)) && board[row][col] == 'O') {
                        queue.offer(new int[]{row, col});
                        visited[row][col] = true;
                    }
                }
            }

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currRow = curr[0], currCol = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                    if(neiRow >= 0 && neiRow < rows && neiCol >= 0 && neiCol < cols
                            && board[neiRow][neiCol] == 'O' && !visited[neiRow][neiCol]) {
                        queue.offer(new int[]{neiRow, neiCol});
                        visited[neiRow][neiCol] = true;
                    }
                }
            }

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(board[row][col] == 'O' && !visited[row][col]) {
                        board[row][col] = 'X';
                    }
                }
            }
        }

        private void bfsUsingQueue(int row, int col, char[][] board, boolean[][] visited, int rows, int cols) {
            if(board[row][col] == 'X' || visited[row][col]) {
                return;
            }

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{row, col});
            visited[row][col] = true;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currRow = curr[0], currCol = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                    if(neiRow >= 0 && neiRow < rows && neiCol >= 0 && neiCol < cols
                            && board[neiRow][neiCol] == 'O' && !visited[neiRow][neiCol]) {
                        queue.offer(new int[]{neiRow, neiCol});
                        visited[neiRow][neiCol] = true;
                    }
                }
            }
        }

        public void solve2(char[][] board) {
            if (board == null || board.length == 0) {
                return;
            }

            int rows = board.length, cols = board[0].length;
            boolean[][] visited = new boolean[rows][cols];

            for(int col = 0; col < cols; col++) {
                // First row and for every column
                bfsUsingQueue(0, col, board, visited, rows, cols);

                // Last row and for every column
                bfsUsingQueue(rows-1, col, board, visited, rows, cols);
            }

            for(int row = 0; row < rows; row++) {
                // First column and for every row column
                bfsUsingQueue(row, 0, board, visited, rows, cols);

                // Last column and for every row column
                bfsUsingQueue(row, cols-1, board, visited, rows, cols);
            }

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(board[row][col] == 'O' && !visited[row][col]) {
                        board[row][col] = 'X';
                    }
                }
            }
        }

        private void dfsUsingStack(int row, int col, char[][] board, boolean[][] visited, int rows, int cols) {
            if(visited[row][col] || board[row][col] == 'X') {
                return;
            }

            Stack<int[]> stack = new Stack<>();
            stack.push(new int[]{row, col});
            visited[row][col] = true;

            while (!stack.isEmpty()) {
                int[] curr = stack.pop();
                int currRow = curr[0], currCol = curr[1];

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                    if(neiRow >= 0 && neiRow < rows && neiCol >= 0 && neiCol < cols
                            && board[neiRow][neiCol] == 'O' && !visited[neiRow][neiCol]) {
                        stack.push(new int[]{neiRow, neiCol});
                        visited[neiRow][neiCol] = true;
                    }
                }
            }
        }

        public void solve1(char[][] board) {
            if (board == null || board.length == 0) {
                return;
            }

            int rows = board.length, cols = board[0].length;
            boolean[][] visited = new boolean[rows][cols];

            for(int col = 0; col < cols; col++) {
                // First row and for every column
                dfsUsingStack(0, col, board, visited, rows, cols);

                // Last row and for every column
                dfsUsingStack(rows-1, col, board, visited, rows, cols);
            }

            for(int row = 0; row < rows; row++) {
                // First column and for every row column
                dfsUsingStack(row, 0, board, visited, rows, cols);

                // Last column and for every row column
                dfsUsingStack(row, cols-1, board, visited, rows, cols);
            }

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(board[row][col] == 'O' && !visited[row][col]) {
                        board[row][col] = 'X';
                    }
                }
            }
        }

        private static final int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

        private void dfsUsingRecursion(int row, int col, char[][] board, boolean[][] visited, int rows, int cols) {
            if(row < 0 || row >= rows || col < 0 || col >= cols || visited[row][col] || board[row][col] == 'X') {
                return;
            }

            visited[row][col] = true;

            for(int[] neighbor: offsetNeighbors) {
                int neiRow = row + neighbor[0], neiCol = col + neighbor[1];
                dfsUsingRecursion(neiRow, neiCol, board, visited, rows, cols);
            }
        }

        public void solve0(char[][] board) {
            if(board == null || board.length == 0) {
                return;
            }

            int rows = board.length, cols = board[0].length;
            boolean[][] visited = new boolean[rows][cols];

            for(int col = 0; col < cols; col++) {
                // First row and for every column
                dfsUsingRecursion(0, col, board, visited, rows, cols);

                // Last row and for every column
                dfsUsingRecursion(rows-1, col, board, visited, rows, cols);
            }

            for(int row = 0; row < rows; row++) {
                // First column and for every row column
                dfsUsingRecursion(row, 0, board, visited, rows, cols);

                // Last column and for every row column
                dfsUsingRecursion(row, cols-1, board, visited, rows, cols);
            }

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(board[row][col] == 'O' && !visited[row][col]) {
                        board[row][col] = 'X';
                    }
                }
            }
        }
    }

    class Revision01 {
        // Directions: up, right, down, left
        private static final int[][] offsetNeighbors = {{-1,0},{0,1},{1,0},{0,-1}};

        // ---------------- DFS (Recursive) ----------------
        private void dfs0(int r, int c, char[][] board, boolean[][] visited, int rows, int cols) {
            // Base case: out of bounds or invalid cell
            if (r < 0 || r >= rows || c < 0 || c >= cols || visited[r][c] || board[r][c] == 'X') return;

            visited[r][c] = true; // mark current cell as visited

            // Explore all 4 directions
            for (int[] neighbor : offsetNeighbors) {
                dfs0(r + neighbor[0], c + neighbor[1], board, visited, rows, cols);
            }
        }

        // ---------------- DFS (Iterative using Stack) ----------------
        private void dfsUsingStack0(int r, int c, char[][] board, boolean[][] visited, int rows, int cols) {
            if (board[r][c] == 'X' || visited[r][c]) return;

            Stack<int[]> stack = new Stack<>();
            stack.push(new int[]{r, c});
            visited[r][c] = true;

            while (!stack.isEmpty()) {
                int[] curr = stack.pop();
                int cr = curr[0], cc = curr[1];

                for (int[] neighbor : offsetNeighbors) {
                    int nr = cr + neighbor[0], nc = cc + neighbor[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc] && board[nr][nc] == 'O') {
                        stack.push(new int[]{nr, nc});
                        visited[nr][nc] = true;
                    }
                }
            }
        }

        // ---------------- BFS (Using Queue) ----------------
        private void bfsUsingQueue0(int r, int c, char[][] board, boolean[][] visited, int rows, int cols) {
            if (board[r][c] == 'X' || visited[r][c]) return;

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{r, c});
            visited[r][c] = true;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int cr = curr[0], cc = curr[1];

                for (int[] neighbor : offsetNeighbors) {
                    int nr = cr + neighbor[0], nc = cc + neighbor[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc] && board[nr][nc] == 'O') {
                        queue.offer(new int[]{nr, nc});
                        visited[nr][nc] = true;
                    }
                }
            }
        }

        public void solve2(char[][] board) {
            if(board == null || board.length == 0) {
                return;
            }

            int rows = board.length, cols = board[0].length;
            boolean[][] visited = new boolean[rows][cols];

            // First and last row of every column
            for(int c = 0; c < cols; c++) {
                // First row
                bfsUsingQueue0(0, c, board, visited, rows, cols);

                // Last row
                bfsUsingQueue0(rows-1, c, board, visited, rows, cols);
            }

            // First and last column of every row
            for(int r = 0; r < rows; r++) {
                // First column
                bfsUsingQueue0(r, 0, board, visited, rows, cols);

                // Last column
                bfsUsingQueue0(r, cols-1, board, visited, rows, cols);
            }

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(board[r][c] == 'O' && !visited[r][c]) {
                        board[r][c] = 'X';
                    }
                }
            }
        }

        public void solve1(char[][] board) {
            if(board == null || board.length == 0) {
                return;
            }

            int rows = board.length, cols = board[0].length;
            boolean[][] visited = new boolean[rows][cols];

            // First and last row of every column
            for(int c = 0; c < cols; c++) {
                // First row
                dfsUsingStack0(0, c, board, visited, rows, cols);

                // Last row
                dfsUsingStack0(rows-1, c, board, visited, rows, cols);
            }

            // First and last column of every row
            for(int r = 0; r < rows; r++) {
                // First column
                dfsUsingStack0(r, 0, board, visited, rows, cols);

                // Last column
                dfsUsingStack0(r, cols-1, board, visited, rows, cols);
            }

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(board[r][c] == 'O' && !visited[r][c]) {
                        board[r][c] = 'X';
                    }
                }
            }
        }

        public void solve0(char[][] board) {
            if(board == null || board.length == 0) {
                return;
            }

            int rows = board.length, cols = board[0].length;
            boolean[][] visited = new boolean[rows][cols];

            // First and last row of every column
            for(int c = 0; c < cols; c++) {
                // First row
                dfs0(0, c, board, visited, rows, cols);

                // Last row
                dfs0(rows-1, c, board, visited, rows, cols);
            }

            // First and last column of every row
            for(int r = 0; r < rows; r++) {
                // First column
                dfs0(r, 0, board, visited, rows, cols);

                // Last column
                dfs0(r, cols-1, board, visited, rows, cols);
            }

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    if(board[r][c] == 'O' && !visited[r][c]) {
                        board[r][c] = 'X';
                    }
                }
            }
        }
    }

    class Solution {
        int[][] offsetNeighbors = {{-1, 0},{1, 0},{0, 1},{0, -1}};

        private void dfs(int row, int col, char[][] board, boolean[][] visited) {
            visited[row][col] = true;

            for(int[] neighbor: offsetNeighbors) {
                int newRow = row + neighbor[0], newCol = col + neighbor[1];

                if(newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length
                        && board[newRow][newCol] == 'O' && !visited[newRow][newCol]) {
                    dfs(newRow, newCol, board, visited);
                }
            }
        }

        public void solve(char[][] board) {
            if(board == null || board.length == 0) {
                return;
            }

            int ROWS = board.length, COLS = board[0].length;
            boolean[][] visited = new boolean[ROWS][COLS];

            // First and Last row traversal for every column
            for(int col = 0; col < COLS; col++) {
                if(board[0][col] == 'O' && !visited[0][col]) {
                    dfs(0, col, board, visited);
                }

                if(board[ROWS-1][col] == 'O' && !visited[ROWS-1][col]) {
                    dfs(ROWS-1, col, board, visited);
                }
            }

            // First and Last column traversal for every row
            for(int row = 0; row < ROWS; row++) {
                if(board[row][0] == 'O' && !visited[row][0]) {
                    dfs(row, 0, board, visited);
                }

                if(board[row][COLS-1] == 'O' && !visited[row][COLS-1]) {
                    dfs(row, COLS-1, board, visited);
                }
            }

            for(int row = 0; row < ROWS; row++) {
                for(int col = 0; col < COLS; col++) {
                    if(board[row][col] == 'O' && !visited[row][col]) {
                        board[row][col] = 'X';
                    }
                }
            }
        }
    }
}
