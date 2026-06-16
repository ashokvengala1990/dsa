package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/description/
 */

public class Find_First_and_Last_Position_of_Element_in_Sorted_Array_LC_34 {
    class Solution {
        private int findFirst(int[] nums, int target) {
            int size = nums.length, left = 0, right = size-1;

            while (left + 1 < right) {
                int mid = left + ((right-left)/2);

                if(nums[mid] >= target) {
                    right = mid;
                } else {
                    left = mid;
                }
            }

            if(nums[left] == target) {
                return left;
            } else if(nums[right] == target) {
                return right;
            } else {
                return -1;
            }
        }

        private int findLast(int[] nums, int target) {
            int size = nums.length, left = 0, right = size-1;

            while (left + 1 < right) {
                int mid = left + ((right-left)/2);

                if(nums[mid] <= target) {
                    left = mid;
                } else {
                    right = mid;
                }
            }

            if(nums[right] == target) {
                return right;
            } else if(nums[left] == target) {
                return left;
            } else {
                return -1;
            }
        }

        public int[] searchRange1(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return new int[]{-1, -1};
            }

            int left = findFirst(nums, target);
            int right = findLast(nums, target);

            return new int[]{left, right};
        }

        private int helper0(int left, int right, int[] nums, int target, boolean isLeftBias) {
            int size = nums.length;

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(isLeftBias) {
                    if(target <= nums[mid]) {
                        right = mid-1;
                    } else {
                        left = mid+1;
                    }
                } else {
                    if(nums[mid] <= target) {
                        left = mid+1;
                    } else {
                        right = mid-1;
                    }
                }
            }

            if(isLeftBias) {
                if(left < size && nums[left] == target) {
                    return left;
                } else {
                    return -1;
                }
            } else {
                if(right >= 0 && nums[right] == target) {
                    return right;
                } else {
                    return -1;
                }
            }
        }

        public int[] searchRange(int[] nums, int target) {
            if(nums == null || nums.length == 0) {
                return new int[]{-1, -1};
            }

            int size = nums.length;
            int left = helper0(0, size-1, nums, target, true);
            int right = helper0(0, size-1, nums, target, false);

            return new int[]{left, right};
        }
    }
}
