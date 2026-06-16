package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/binary-search/description/

 */

public class Binary_Search_LC_704 {
    class Solution {
        public int search1(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return -1;
            }

            int size = nums.length, left = 0, right = size-1;

            while (left <= right) {
                int mid = left + ((right-left) / 2);
                if(nums[mid] == target) {
                    return mid;
                } else if(nums[mid] < target) {
                    left = mid+1;
                } else {
                    right = mid-1;
                }
            }

            return -1;
        }

        private int helper0(int left, int right, int[] nums, int target) {
            if(left > right) {
                return -1;
            }

            int mid = left + ((right-left)/2);

            if(nums[mid] == target) {
                return mid;
            } else if(nums[mid] < target) {
                return helper0(mid+1, right, nums, target);
            } else {
                return helper0(left, mid-1, nums, target);
            }
        }

        public int search0(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return -1;
            }

            int size = nums.length;

            return helper0(0, size-1, nums, target);
        }
    }
}
