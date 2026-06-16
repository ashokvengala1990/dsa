package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/search-a-2d-matrix/description/
 */

public class Search_a_2D_Matrix_LC_74 {
    class Solution {
        public boolean searchMatrix1(int[][] matrix, int target) {
            if(matrix == null || matrix.length == 0) {
                return false;
            }

            int rows = matrix.length, cols = matrix[0].length, left = 0, right = rows * cols - 1;

            while (left <= right) {
                int mid = left + ((right - left)/2);
                int row = mid / cols, col = mid % cols;

                if(matrix[row][col] == target) {
                    return true;
                } else if(matrix[row][col] < target) {
                    left = mid+1;
                } else {
                    right = mid-1;
                }
            }

            return false;
        }

        private boolean search1DMatrix(int[] nums, int target) {
            int size = nums.length, left = 0, right = size-1;

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(nums[mid] == target) {
                    return true;
                } else if(nums[mid] > target) {
                    right = mid-1;
                } else {
                    left = mid+1;
                }
            }

            return false;
        }

        public boolean searchMatrix0(int[][] matrix, int target) {
            if(matrix == null || matrix.length == 0) {
                return false;
            }

            int rows = matrix.length, cols = matrix[0].length, topLeft = 0, bottomLeft = rows-1;

            while (topLeft <= bottomLeft) {
                int mid = topLeft + ((bottomLeft-topLeft)/2);

                if(matrix[mid][0] > target) {
                    bottomLeft = mid-1;
                } else if(matrix[mid][cols-1] < target) {
                    topLeft = mid+1;
                } else {
                    return search1DMatrix(matrix[mid], target);
                }
            }

            return false;
        }
    }
}
