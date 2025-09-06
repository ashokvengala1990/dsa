package neetcode.dynamic_programming;

/*
https://leetcode.com/problems/maximum-product-subarray/description/

Brute Force Approach 01:
- Two For Loops
- TC: O(n^2)
- SC: O(1)

Optimal Approach 02:
 Observations:
 1) If we have all positive elements in nums array
 2) If we have even number of negative elements and remaining positive elements in nums array.
 3) If we have odd number of negative elements and remaining positive elements in nums array.
 4) If we have zeros elements in nums array

- Calculate prefixProduct and suffixProduct by traversing nums array.
- If prefixProduct = 0 or suffixProduct = 0, then reset them to 1.

- Try out prefixProduct by traversing nums array from 0th index to size-1.
- Try out suffixProduct by traversing nums array from size-1 to 0th index.

TC: O(n)
SC: O(1)
 */

public class Maximum_Product_Subarray_LC_152 {
    class Revision01 {
        public int maxProduct2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return Integer.MIN_VALUE;
            }

            int size = nums.length, maxProd = Integer.MIN_VALUE, currMax = 1, currMin = 1;

            for(int i = 0; i < size; i++) {
                if(currMax == 0) {
                    currMax = 1;
                }
                if(currMin == 0) {
                    currMin = 1;
                }

                int local1 = currMax * nums[i], local2 = currMin * nums[i];
                currMax = Math.max(Math.max(local1,local2), nums[i]);
                currMin = Math.min(Math.min(local1, local2), nums[i]);
                maxProd = Math.max(maxProd, currMax);
            }

            return maxProd;
        }

        public int maxProduct1(int[] nums) {
            if(nums == null || nums.length == 0) {
                return Integer.MIN_VALUE;
            }

            int size = nums.length, prefixProduct = 1, suffixProduct = 1, maxProd = Integer.MIN_VALUE;

            for(int i = 0; i < size; i++) {
                if(prefixProduct == 0) {
                    prefixProduct = 1;
                }
                if(suffixProduct == 0) {
                    suffixProduct = 1;
                }

                prefixProduct = prefixProduct * nums[i];
                suffixProduct = suffixProduct * nums[size-i-1];

                maxProd = Math.max(maxProd, Math.max(prefixProduct, suffixProduct));
            }

            return maxProd;
        }

        public int maxProduct0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return Integer.MIN_VALUE;
            }

            int size = nums.length, maxProd = Integer.MIN_VALUE;

            for(int i = 0; i < size; i++) {
                int currProd = 1;
                for(int j = i; j < size; j++) {
                    currProd *= nums[j];
                    maxProd = Math.max(maxProd, currProd);
                }
            }

            return maxProd;
        }
    }

    static class Solution {
        public int maxProduct3(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length, maxProd = Integer.MIN_VALUE, currMax = Integer.MIN_VALUE, currMin = Integer.MAX_VALUE;

            for(int i = 0; i < size; i++) {
                int localMax = currMax, localMin = currMin;
                currMax = Math.max(nums[i], Math.max(localMax*nums[i], localMin*nums[i]));
                currMin = Math.min(nums[i], Math.min(localMax*nums[i], localMin*nums[i]));
                maxProd = Math.max(maxProd, currMax);
            }

            return maxProd;
        }

        public int maxProduct2(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length, maxProd = Integer.MIN_VALUE, prefixProduct = 1, suffixProduct = 1;

            for(int i = 0; i < size; i++) {
                if(prefixProduct == 0) {
                    prefixProduct = 1;
                }
                if(suffixProduct == 0) {
                    suffixProduct = 1;
                }

                prefixProduct *= nums[i];
                suffixProduct *= nums[size-i-1];
                maxProd = Math.max(maxProd, Math.max(prefixProduct, suffixProduct));
            }

            return maxProd;
        }

        private int maxProd;

        private void helper0(int idx, int currProd, int[] nums) {
            maxProd = Math.max(maxProd, currProd);

            if(idx >= nums.length) {
                return;
            }

            helper0(idx+1, currProd*nums[idx], nums);
        }

        // TLE:
        public int maxProduct1(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }

            maxProd = Integer.MIN_VALUE;
            int size = nums.length;

            for(int i = 0; i < size; i++) {
                helper0(i+1, nums[i], nums);
            }

            return maxProd;
        }

        public int maxProduct0(int[] nums) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int size = nums.length, maxProd = Integer.MIN_VALUE;

            for(int i = 0; i < size; i++) {
                int currProd = 1;
                for(int j = i; j < size; j++) {
                    currProd *= nums[j];
                    maxProd = Integer.max(maxProd, currProd);
                }
            }

            return maxProd;
        }
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        int[] nums =
//                {2, 3, -2, 4};
//                {-2, 0, -1};
//                {-1, 2, 3, 0, -1};
                {-2, 3, 4, -1, 0, -2, 3, 1, 4, 0, 4, 6, -1, 4};
        System.out.println(s.maxProduct0(nums));
    }
}
