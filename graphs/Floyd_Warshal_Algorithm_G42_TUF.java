package neetcode.graphs;

/*
https://takeuforward.org/plus/dsa/problems/floyd-warshall-algorithm

Floyd Warshal Algorithm:
- It helps multi-source the shortest path algorithm
- It also helps to detect negative cycles as well
- It states go via every node/vertex
    You want to find the shortest path from 0 to 1
    - You have to try via every node/vertex from 0 to 1.
        0 -> 0 -> 1
        0 -> 1 -> 1
        0 -> 2 -> 1
        0 -> 3 -> 1
        0 -> 4 -> 1

        d[0][1] -> min of all d[i][k] + d[k][j]

- Convert given edges (directed or undirected graph)
- We will be using adjacency matrix to store the given graph.
    - Where rows index are v1 and cols index are v2, value in each cell is considered as edge weight.
- Note: node from 0 to itself, 1 to itself, .. n to itself are zero
- We use cost matrix and initially self nodes with zero and remaining are filled with edges from the given graph in the cost
 2D array
- If given is un-directed graph, assign both places 0 to 1 and 1 to 0 with the weight.

* Move via vertex 0
* Move via vertex 1
* Move via vertex 2
....
* Move via vertex (n-1)


* How to detect a negative cycles when we use Floyd wasshal algorithm:
    - [0, 1, -2]
    - [1, 2, -3]
    - [2, 0, 2]
        => -2 -3 + 2 = -3

        cost[0][0] = 0, it could change to -3
        if cost[i][i] < 0), then negative cycles exists.


 */

public class Floyd_Warshal_Algorithm_G42_TUF {
    class Solution {
        public void shortestDistance(int[][] matrix) {
            if(matrix == null || matrix.length == 0) {
                return;
            }

            int n = matrix.length, INF = (int) 1e9;

            for(int i =0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(matrix[i][j] == -1) {
                        matrix[i][j] = INF;
                    }
                    if(i == j) {
                        matrix[i][j] = 0;
                    }
                }
            }

            for(int k = 0; k < n; k++) {
                for(int i = 0; i < n; i++) {
                    for(int j = 0; j < n; j++) {
                        if(matrix[i][k] != INF
                                && matrix[k][j] != INF
                                && matrix[i][j] > (matrix[i][k]+matrix[k][j])) {
                            matrix[i][j] = matrix[i][k]+matrix[k][j];
                        }
                    }
                }
            }

            for(int i =0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(matrix[i][j] == INF) {
                        matrix[i][j] = -1;
                    }
                }
            }

            for(int i = 0; i < n; i++) {
                if(matrix[i][i] < 0) {
                    System.out.println("Negative Cycle exists");
                }
            }
        }
    }
}
