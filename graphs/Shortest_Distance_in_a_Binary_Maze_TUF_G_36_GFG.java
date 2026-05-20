package neetcode.graphs;

import java.util.*;

/*
https://www.geeksforgeeks.org/problems/shortest-path-in-a-binary-maze-1655453161/1

Revision Notes: Shortest Distance in Binary Maze (BFS)

Core Idea

Grid → treat as graph
Move in 4 directions
All edges = 1 → use BFS (level order)
✅ Approach 1 (Best / Optimal)
Use Queue + Visited
Traverse level by level → distance++
First time reaching destination = shortest path

Key Points

Mark visited when adding to queue
Use level size to track distance
Early return when destination found
🔁 Approach 2 (Distance Array - like Dijkstra-lite)
Maintain distance[][]
Relax edges: currDist + 1 < distance
Works but extra space + unnecessary for unweighted
⚡ Why BFS Works?
Equal edge weights → shortest path = minimum steps
BFS explores in layers → guarantees shortest
❌ Edge Cases
Source or destination is 0 → return -1
Source == destination → return 0
⏱️ Complexity
Approach	Time	Space
BFS (visited)	O(N × M)	O(N × M)
Distance array	O(N × M)	O(N × M)
🔑 Quick Memory Trick

“Unweighted → BFS, Weighted → Dijkstra”

 */

public class Shortest_Distance_in_a_Binary_Maze_TUF_G_36_GFG {
    class Solution {

        private void printPath(Map<String, String> childToParentMap, int[] source, int[] destination) {
            String startCell = source[0]+"-"+source[1];

            String target = destination[0]+"-"+destination[1];

            List<String> cellPath = new ArrayList<>();

            while (target != null) {
                cellPath.add(target);
                target = childToParentMap.get(target);
            }

            if(!cellPath.get(cellPath.size()-1).equals(startCell)) {
                System.out.println("No path exists");
            }

            Collections.reverse(cellPath);
            System.out.println(cellPath);
        }

        public int shortestPathPath(int[][] grid, int[] source, int[] destination) {
            if(grid == null || grid.length == 0 || source == null || source.length == 0
                    || destination == null || destination.length == 0
                    || grid[source[0]][source[1]] == 0 || grid[destination[0]][destination[1]] == 0) {
                return -1;
            } else if(source[0] == destination[0] && source[1] == destination[1]) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];
            int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{source[0], source[1]});
            visited[source[0]][source[1]] = true;
            int distance = 0;
            Map<String, String> childToParentMap = new HashMap<>();
            childToParentMap.put(source[0]+"-"+source[1], null);

            while (!queue.isEmpty()) {
                int currLevelSize = queue.size();

                for(int i = 0; i < currLevelSize; i++) {
                    int[] curr = queue.poll();
                    int currRow = curr[0], currCol = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                        if(isValidCell(neiRow, neiCol, rows, cols) && grid[neiRow][neiCol] == 1 && !visited[neiRow][neiCol]) {
                            visited[neiRow][neiCol] = true;
                            childToParentMap.put(neiRow+"-"+neiCol, currRow+"-"+currCol);

                            if(neiRow == destination[0] && neiCol == destination[1]) {
                                printPath(childToParentMap, source, destination);
                                return distance+1;
                            }
                            queue.offer(new int[]{neiRow, neiCol});
                        }
                    }
                }

                distance++;
            }

            return -1;
        }

        public int shortestPath1(int[][] grid, int[] source, int[] destination) {
            if(grid == null || grid.length == 0 || source == null || source.length == 0
                    || destination == null || destination.length == 0
                    || grid[source[0]][source[1]] == 0 || grid[destination[0]][destination[1]] == 0) {
                return -1;
            } else if(source[0] == destination[0] && source[1] == destination[1]) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];
            int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{source[0], source[1]});
            visited[source[0]][source[1]] = true;
            int distance = 0;

            while (!queue.isEmpty()) {
                int currLevelSize = queue.size();

                for(int i = 0; i < currLevelSize; i++) {
                    int[] curr = queue.poll();
                    int currRow = curr[0], currCol = curr[1];

                    for(int[] neighbor: offsetNeighbors) {
                        int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                        if(isValidCell(neiRow, neiCol, rows, cols) && grid[neiRow][neiCol] == 1 && !visited[neiRow][neiCol]) {
                            visited[neiRow][neiCol] = true;

                            if(neiRow == destination[0] && neiCol == destination[1]) {
                                return distance+1;
                            }
                            queue.offer(new int[]{neiRow, neiCol});
                        }
                    }
                }

                distance++;
            }

            return -1;
        }

        private boolean isValidCell(int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }

        public int shortestPath0(int[][] grid, int[] source, int[] destination) {
            if(grid == null || grid.length == 0 || source == null || source.length == 0
                    || destination == null || destination.length == 0
                    || grid[source[0]][source[1]] == 0 || grid[destination[0]][destination[1]] == 0) {
                return -1;
            } else if(source[0] == destination[0] && source[1] == destination[1]) {
                return 0;
            }

            int rows = grid.length, cols = grid[0].length;
            int[][] offsetNeighbors = {{-1, 0},{0, 1},{1, 0},{0, -1}};

            int[][] distance = new int[rows][cols];
            for(int[] d: distance) {
                Arrays.fill(d, Integer.MAX_VALUE);
            }

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, source[0], source[1]});
            distance[source[0]][source[1]] = 0;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int currDist = curr[0], currRow = curr[1], currCol = curr[2];

                for(int[] neighbor: offsetNeighbors) {
                    int neiRow = currRow + neighbor[0], neiCol = currCol + neighbor[1];

                    if(isValidCell(neiRow, neiCol, rows, cols) && grid[neiRow][neiCol] == 1 && currDist + 1 < distance[neiRow][neiCol]) {
                        distance[neiRow][neiCol] = currDist + 1;

                        if(neiRow == destination[0] && neiCol == destination[1]) {
                            return distance[neiRow][neiCol];
                        }

                        queue.offer(new int[]{distance[neiRow][neiCol], neiRow, neiCol});
                    }
                }
            }

            return -1;
        }
    }
}
