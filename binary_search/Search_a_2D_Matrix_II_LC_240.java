package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/search-a-2d-matrix-ii/
 */

public class Search_a_2D_Matrix_II_LC_240 {
    class Solution {
        // ⏺ searchMatrix2 is the Staircase / Elimination approach — TC: O(M + N)
        /*
          ---
  Intuition — start from top-right corner:

  The top-right element has a unique property:
  - It is the largest in its row (row is sorted left to right)
  - It is the smallest in its column (column is sorted top to bottom)

  This gives you a clear elimination rule at every step:
  - target > current → entire current row is too small, move down
  - target < current → entire current column is too large, move left
  - target == current → found it

  You eliminate one full row or column per step → O(M + N).
         */
        public boolean searchMatrix2(int[][] matrix, int target) {
            if(matrix == null || matrix.length == 0) {
                return false;
            }

            int rows = matrix.length, cols = matrix[0].length, row = 0, col = cols-1;

            while (row < rows && col >= 0) {
                if(matrix[row][col] < target) {
                    row++;
                } else if(matrix[row][col] > target) {
                    col--;
                } else {
                    return true;
                }
            }

            return false;
        }

        private boolean isExists(int[] nums, int target) {
            int size = nums.length, left = 0, right = size-1;

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(nums[mid] == target) {
                    return true;
                } else if(nums[mid] < target) {
                    left = mid+1;
                } else {
                    right = mid-1;
                }
            }

            return false;
        }

        public boolean searchMatrix1(int[][] matrix, int target) {
            if(matrix == null || matrix.length == 0) {
                return false;
            }

            int rows = matrix.length;

            for(int row = 0; row < rows; row++) {
                if(isExists(matrix[row], target)) {
                    return true;
                }
            }

            return false;
        }

        public boolean searchMatrix0(int[][] matrix, int target) {
            if(matrix == null || matrix.length == 0) {
                return false;
            }

            int rows = matrix.length, cols = matrix[0].length;

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(matrix[row][col] == target) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
