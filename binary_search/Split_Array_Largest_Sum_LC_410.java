package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/split-array-largest-sum/

1) Understanding the problem
So, we are given an input array of integers and k split and we return the minimum largest sum of the split. There are multiple answer we will
return minimized sum value of the split.

2) Edges cases:
- all elements are positive integers
- not sorted
- it can be duplicate elements in nums array
- k range : 1 to 50 and what is k is largest than the size of the nums array
- nums array element limit: 10^6
- array empty

3) Identify pattern:
The array is not sorted, and we are looking minimum largest sum of the split, so, if we have search
space left and right and apply binary search on answer pattern would likely to work here.
- Binary search on answer
- minimum largest sum could be = min(nums array)
- maximum largest sum could be = sum(nums array)

4) Write the algorithm as Step-by-Step Instructions:
4.1) Initialize variables left = max, right = 0, answer = 0
4.2) Iterate input nums array to left and right values by min(left, nums[i]) and right += num[i]
4.3) Use a helper function canSplitPossible(nums, k, possibleMInLargeSum) to check if the array can be split
into at most k sub-arrays where each subarray's sum is within the given limit.
        - Inside the helper function, iterate through nums while tracking currSum and splits. If adding the
        current element would exceed the limit, start a new partition by setting curr_sum to the current element
        and increasing splits. Otherwise, keep accumulating into curr_sum
        - return true if splits <= k otherwise false
4.4) Apply binary search between left and right. At each step, compute mid = left + ((right-left)/2) as the candidate minimum
    largest sum.
4.5) Call callSplitPossible with mid. If it return true, the candaite is valid - save mid as the current answer and move right
to mid-1 to search the smallest valid answer
4.6) If it returns false, mid is too small to allow a valid split - move left to mid+1;
4.7) Return answer once the binary search ends. This is minimized largest sum.

5) Code only after the approach is clear
- See below

6) Dry run before submitting
Examples:
- [7, 2, 5, 10, 8], k = 2
- [1, 4, 4], k = 3
- [1, 2, 3, 4, 5], k = 2

7) Verify time and space complexity
TC: O(log (sum(nums[i] - max(nums[i]) * N)
8) Submit only after verifying

 */

public class Split_Array_Largest_Sum_LC_410 {
    class Solution {
        private boolean canPartitionPossible(int[] nums, int k, int possibleMinLargeSum) {
            int currSum = 0, splits = 1;

            for(int num: nums) {
                if(currSum + num > possibleMinLargeSum) {
                    currSum = num;
                    splits++;
                } else {
                    currSum += num;
                }
            }

            return splits <= k;
        }

        public int splitArray(int[] nums, int k) {
            if(nums == null || nums.length == 0) {
                return 0;
            }

            int left = Integer.MIN_VALUE, right = 0, answer = 0;

            for(int num: nums) {
                left = Math.max(left, num);
                right += num;
            }

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(canPartitionPossible(nums, k, mid)) {
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

/*
[1, 4, 4], k = 3
left = 4, right = 9, mid = 6
currSum = 1 + 4 , split = 1
currSum = 4

left = 4, right = 5, mid = 4
currSum = 1 , split = 1
currSum = 4, split = 2
currSum = 4

left = 4, right = 3


left = 2
right = 32
mid = 17

[7, 2, 5, 10, 8]

sum = 7 + 2 + 5
pieces = 1

sum = 10
pieces = 2

sum = 8
pieces = 3

pieces >= k

left = 18, right = 32, mid = 25
sum = 7 + 2 + 5 + 10, pieces = 1

sum = 8
pieces = 2
pieces <= k
left = 18, right = 24, mid = 21
sum = 7 + 2 + 5, pieces = 1

sum = 10 + 8
pieces = 2

left = 18, right = 20, mid = 19
sum = 7 + 2 + 5, pieces = 1
sum = 10 + 8, pieces = 2

left = 18, right = 18
sum = 7 + 2 + 5, pieces = 1
sum = 10 + 8, pieces = 2

nums = [1, 2, 3, 4, 5], k = 2
left = 1, right = 15

left = 1, right = 15, mid = 8
sum = 1 + 2 + 3, pieces = 1
sum = 4, pieces = 2
sum = 5, pieces = 3

pieces <= 2

left = 9, right = 15, mid = 12
sum = 1 + 2 + 3 + 4, pieces = 1
sum = 5, pieces = 2

left = 9, right = 11, mid = 10
sum = 1 + 2 + 3, pieces = 1
sum = 4 + 5, pieces = 2

left = 9, right = 9
sum = 1 + 2 + 3, pieces = 1
sum = 4 + 5, pieces = 2

Example:
[7, 2, 5, 10, 8], k = 2
left = 10, right = 32, mid = 21
currSum = 7 + 2 + 5 , split = 1
currSum = 10 + 8

left = 10, right = 20, mid = 15
currSum = 7 + 2 + 5, split = 1
currSum = 10, split = 2
currSum = 8

left = 10, right = 14, mid = 12
currSum = 7 + 2 , split = 1
currSum = 5, split = 2
currSum = 10, split = 3
currSum = 8

left = 13, right = 14, mid = 13

left = 14, right = 14, mid = 14

[7, 2, 5]
[10]
[8]
which is wrong.
So, we need to initoalize
 */
