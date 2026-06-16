package binary_search.custom.binary_search;

public class Find_Minimum_in_Rotated_Sorted_Array_II_LC_154 {
    class Solution {
        public int findMin(int[] nums) {
            if(nums == null || nums.length == 0) {
                return Integer.MAX_VALUE;
            }

            int size = nums.length, left = 0, right = size-1, minElement = Integer.MAX_VALUE;

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(nums[left] == nums[mid] && nums[mid] == nums[right]) {
                    minElement = Math.min(minElement, nums[left]);
                    left++;
                    right--;
                } else if(nums[left] <= nums[mid]) {
                    minElement = Math.min(minElement, nums[left]);
                    left = mid+1;
                } else {
                    minElement = Math.min(minElement, nums[mid]);
                    right = mid-1;
                }
            }

            return minElement;
        }
    }
}
