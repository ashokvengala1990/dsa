package binary_search.custom.binary_search;
/*
https://leetcode.com/problems/single-element-in-a-sorted-array/description/

⏺ Learnings

  - Binary search does not always search for a target value. Sometimes it searches for a position where a structural pattern breaks — here, where pair alignment flips from even-start to odd-start.
  - At mid, check three things in order: is mid the single element, is the pair aligned (single is right), is the pair misaligned (single is left). Direction depends on index parity + neighbor match, not a target comparison.

  ---
  Pattern Matching Clues

  - "Sorted array, every element appears twice except one" → the pair alignment trick applies, O(log n) is possible via parity binary search.
  - No target is given but O(log n) is expected → you are not searching for a value, you are searching for where a structural property flips.


 */

public class Single_Element_in_a_Sorted_Array_LC_540 {
    class Solution {
        public int singleNonDuplicate(int[] nums) {
            if(nums == null || nums.length == 0) {
                return -1;
            }

            int size = nums.length;
            if(size == 1) {
                return nums[0];
            } else if(nums[0] != nums[1]) {
                return nums[0];
            } else if(nums[size-2] != nums[size-1]) {
                return nums[size-1];
            }

            int left = 1, right = size-2;
            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(nums[mid-1] != nums[mid] && nums[mid] != nums[mid+1]) {
                    return nums[mid];
                } else if((mid % 2 == 1 && nums[mid-1] == nums[mid]) || (mid % 2 == 0 && nums[mid] == nums[mid+1])) {
                    left = mid+1;
                } else {
                    right = mid-1;
                }
            }

//            return left < size ? nums[left] : -1;
            return -1;
        }
    }
}
/*
            if(nums == null || nums.length == 0) {
                return -1;
            }

            int size = nums.length;
            if(size == 1) {
                return nums[0];
            } else if(nums[0] != nums[1]) {
                return nums[0];
            } else if(nums[size-2] != nums[size-1]) {
                return nums[size-1];
            }

            int left = 1, right = size-2;

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(nums[mid-1] != nums[mid] && nums[mid] != nums[mid+1]) {
                    return nums[mid];
                } else if((mid % 2 == 1 && nums[mid-1] == nums[mid]) || (mid % 2 == 0 && nums[mid] == nums[mid+1])) {
                    left = mid+1;
                } else {
                    right = mid-1;
                }
            }

            return -1;
        }
 */
