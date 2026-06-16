package binary_search.custom.binary_search;

/*
1) Understand the problem
So, we are given an input time array and totaltrips required to complete and find the minimumtime required to complete atleast
totalTrips trips

2) Edges cases:
totalTrips: is positive integer?
trips array: positive, negative, zero
array: can be empty or null?
array: unique, duplicate or unsorted or sorted?
1 <= time.length <= 10^5
1 <= time[i], totalTrips <= 10^7

3) identity the pattern

What are searching for? we are looking for minimumTime required to complete at-least totalTrips
What can be smallest value or time required? - 1 or 0
What can be largest value or time required?
what does mid represent: possible minimTime to complete totalTrips

So, we probably need to apply Binary Search on answers pattern

4) Write the algorithm as step-by-step instructions:
left = 1, right =

time: [2] , totalTrips = 2

left = 2, right = 4, mid = 3

total = 1

1 >= 2

left = 4, right = 4, mid = 4
total = 2
2 >= 2
answer = 4

left = 4, right = 3, break the loop, return answer which is 4


Now let's take an example of
time = [1,2, 3], totalTrips = 5

left = 1, right = 15, mid = 8
total = 8 + 4 + 2 = 14

14 >= 5

left = 1, right = 7, mid = 4
total = 4 + 2 + 1 = 7

7 >= 5

left = 1, right = 3, mid = 2
total = 2 + 1 + 0 = 3
3 >= 5

left = 3, right = 3, mid = 3
total = 3 + 1 + 1 = 5
5 >= 5

left = 3, right = 2
return 3 as answer

- Initialize left and right to 1 and max(time) * totalTrips
- Apply binary search untile left is less than equal to right
- use a helper function to calcualte the totalTripsTaken with the possibleMinTime by iterating an time array
- possibleMinTime / each element in time and add to totalCompleteTrip variable and after the iteration, check
  totalCompleteTrip >= totalTrips and it returns true or false accoridngly
- Find mid = left + ((right - left)/2); and check can mid be the possibleMinTime to complete atleast totalTrips. if so, move
the right pointer to left by updating right = mid-1 and answer = mid else move the left pointer to mid+1 to consider large
time to satify the totalTrips as minimum Required
- After binary search, return answer variable

5) Code only after the approach is clear

⏺ Learnings

  - "Minimum time" + multiple workers each completing tasks at fixed rates → binary search on time. In mid minutes, worker i completes floor(mid / time[i]) trips. Sum across all workers, check if total >= totalTrips.
  - When constraints show max(time) * totalTrips can exceed 2 × 10⁹, use long for left, right, mid, and answer. Multiply constraints first to decide int vs long before writing code.

  ---
  Pattern Matching Clues

  - "Minimum time" + each worker has a fixed rate → binary search on time, feasibility check sums how much each worker contributes in mid time.
  - This problem and Repair Cars are the same pattern — the only difference is the feasibility formula: here floor(mid / time[i]), there floor(sqrt(mid / rank[i])). Recognize the pattern from "minimize time across parallel workers."

https://leetcode.com/problems/minimum-time-to-complete-trips/description/
 */

public class Minimum_Time_to_Complete_Trips_LC_2187 {
    static class Solution {
        private boolean canCompleteTotalTrips(int[] time, int totalTrips, long possibleMinTime) {
            long totalCompleteTrip = 0;

            for(int t: time) {
                totalCompleteTrip += (possibleMinTime / t);
            }

            return totalCompleteTrip >= totalTrips;
        }

        public long minimumTime(int[] time, int totalTrips) {
            if(time == null || time.length == 0) {
                return -1;
            }

            long left = 1, right = 0, answer = -1;

            for(int t: time) {
                right = Math.max(right, t);
            }

            right *= totalTrips;

            while (left <= right) {
                long mid = left + ((right-left)/2);

                if(canCompleteTotalTrips(time, totalTrips, mid)) {
                    answer = mid;
                    right = mid-1;
                } else {
                    left = mid+1;
                }
            }

            return answer;
        }
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        int[] time = {2};
                //{1, 2, 3};
        int totalTrips = 2;
                //5;
        System.out.println(s.minimumTime(time, totalTrips));
    }
}
