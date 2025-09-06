package neetcode.dynamic_programming;

/*
https://leetcode.com/problems/count-square-submatrices-with-all-ones/description/

dp[i][j]: It indicates that how many squares end at (i,j) - Right bottom at every (i, j) indexes


This Java code defines a function, countSquares, designed to calculate the total number of square submatrices
composed entirely of ones within a given binary matrix. The function initializes by handling null or empty
input matrices, then sets up a dynamic programming (DP) approach using a dp array. It first populates the first
row and column of the dp array and adds their values to a running total. Subsequently, for the remaining cells,
if a matrix element is 0, the corresponding dp value is 0; otherwise, it computes the size of the largest square
ending at that position by considering the minimum of the three adjacent dp values (above, left, and diagonally
upper-left) and adding one, also updating the total count of squares.


 */

public class Count_Square_Submatrices_with_All_Ones_LC_1277 {
    class Solution {
        public int countSquares(int[][] matrix) {
            if(matrix == null || matrix.length == 0) {
                return 0;
            }

            int ROWS = matrix.length, COLS = matrix[0].length, totalSubmatrix  = 0;
            int[][] dp = new int[ROWS][COLS];

            for(int col = 0; col < COLS; col++) {
                dp[0][col] = matrix[0][col];
                totalSubmatrix += dp[0][col];
            }

            for(int row = 0; row < ROWS; row++) {
                dp[row][0] = matrix[row][0];
                if(row == 0) continue;

                totalSubmatrix += dp[row][0];
            }

            for(int row = 1; row < ROWS; row++) {
                for(int col = 1; col < COLS; col++) {
                    if(matrix[row][col] == 0) {
                        dp[row][col] = 0;
                    } else {
                        dp[row][col] = 1 + Math.min(dp[row-1][col], Math.min(dp[row][col-1], dp[row-1][col-1]));
                        totalSubmatrix += dp[row][col];
                    }
                }
            }

            return totalSubmatrix;
        }
    }
}
