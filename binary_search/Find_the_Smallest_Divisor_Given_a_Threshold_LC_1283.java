package binary_search.custom.binary_search;

/*

 */

public class Find_the_Smallest_Divisor_Given_a_Threshold_LC_1283 {
    class Solution {
        private boolean canPossible(int[] nums, int threshold, int divisor) {
            int sum = 0;

            for(int num: nums) {
                sum += (int) Math.ceil((double) num / divisor);
            }

            return sum <= threshold;
        }

        public int smallestDivisor(int[] nums, int threshold) {
            if(nums == null || nums.length == 0) {
                 return -1;
            }

            int left = 1, right = Integer.MIN_VALUE, answer = -1;

            for(int num: nums) {
                right = Math.max(right, num);
            }

            while (left <= right) {
                int mid = left + ((right - left)/2);

                if(canPossible(nums, threshold, mid)) {
                    answer = mid;
                    right = mid-1;
                } else {
                    left = mid+1;
                }
            }

            return answer;
        }
    }
}
