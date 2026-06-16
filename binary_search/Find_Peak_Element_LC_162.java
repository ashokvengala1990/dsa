package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/find-peak-element/description/
 */

public class Find_Peak_Element_LC_162 {
    class Solution {
        public int findLocalMinima(int[] nums) {
            if(nums == null || nums.length == 0) {
                return -1;
            }

            int size = nums.length;
            if(size == 1) {
                return 0;
            } else if(nums[0] <= nums[1]) {
                return 0;
            } else if(nums[size-2] >= nums[size-1]) {
                return size-1;
            }

            int left = 1, right = size-2;

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(nums[mid-1] >= nums[mid] && nums[mid] <= nums[mid+1]) {
                    return mid;
                } else if(nums[mid] < nums[mid+1]) {
                    right = mid-1;
                } else {
                    left = mid+1;
                }
            }

            return -1;
        }

        public int findPeakElement(int[] nums) {
            if(nums == null || nums.length == 0) {
                return -1;
            }

            int size = nums.length;
            if(size == 1) {
                return 0;
            } else if(nums[0] > nums[1]) {
                return 0;
            } else if(nums[size-2] < nums[size-1]) {
                return size-1;
            }

            int left = 1, right = size-2;

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(nums[mid-1] < nums[mid] && nums[mid] > nums[mid+1]) {
                    return mid;
                } else if(nums[mid] < nums[mid+1]) {
                    left = mid+1;
                } else {
                    right = mid-1;
                }
            }

            return -1;
        }
    }
}
