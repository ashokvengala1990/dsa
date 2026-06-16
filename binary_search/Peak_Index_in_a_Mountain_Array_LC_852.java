package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/peak-index-in-a-mountain-array/description/
 */

public class Peak_Index_in_a_Mountain_Array_LC_852 {
    class Solution {
        public int peakIndexInMountainArray(int[] nums) {
            if(nums == null || nums.length <= 2) {
                return -1;
            }

            int size = nums.length, left = 1, right = size-2;

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
