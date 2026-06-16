package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/description/

Step 1: Understand the problem
So, we are given an array of integers, we need to return min # days to wait to make m bouquets from the garden, each bouquet contains k flowers.
input: bloomday, m and k
output: min # days to form m bouquets

Step 2: Clarify edges cases
- Can I assume?
    * Can we have multiple same day for different flowers to bloom
    * Input array contains atleast number to bloom? we don't have zero or less values in it.
    * we might have less-number of flowers than m and k. For example: input array no elements = 5
    m = 3, k = 2, in that case, we need a total of 6 folowers but we have only 5 followers
    * We cannot use same flower for multiple bouquet and also can't reuse in the same bouquet also.
    * Is there a chance of multiple valid answer?
    * Positive integer value?
    * elements can't be in sorted array
    * bloomDay.length == n
    * 1 <=n <= 10^5 (100K elements)
    * value of each element in an array: 1 <= bloomDay[i] <= 10^9
           - min and max day: 1 <= 10^9
    * 1 <= m <= 10^6
    * 1 <= k <= n
    * You need to take an adjacent flowers only to form a one bouquet based how many flowers you need to pick up.


1 2 3 4 5 6 7 8 9 10
    l
      r
    m

[1, 10, 3, 10, 2]
[0, 1,  2,  3, 4]

mid = 5

3 = 3 <= 5



l = 1, r = 4, m = 2

3 == 3 <= 3

l = 3, r = 4, m = 3

l = 3, r = 2

return l;



[7,7,7,7,12,7,7], m = 2, k = 3


l = 7, r = 12, m = 9

count = 3, numBouquets = 1



l = 10, r = 12, m = 11

count = 3, numBouquet = 1

l = 12, r = 12, m = 12
return l;

Algorithm:
1) Initialize left and right with MAX, MIN, answer = -1;
2) Iterate an input to find min, max for left and right respectively
3) Iterate while until left <= right
    - find mid
    - check mid (aka number of days) can make m bouquets, k flowers from each m (canMakeMBouquetsWithKFlowers)
        answer = mid;
        then right = mid-1
      else
        left = mid+1;

   return answer; // left


canMakeMBouquetsWithKFlowers:
    - initialize countFlowers = 0, totalBouquets = 0;
    - Iterate an bloomDays array
        * currentBloomDay <= minDays:
            increment count by 1
        * else
            if count is equal to k, then increemnt totalBoquqets by 1
            reset count to 0

    - accomodate the last flower whether that form a bouqet or not by doing like
            totalBouquets += (count/k)
    - return totalBouquets >= m;

 */

public class Minimum_Number_of_Days_to_Make_m_Bouquets_LC_1482 {
    private boolean canMakeMBouquetsWithKFlowers(int[] bloomDays, int m, int k, int minDays) {
        int count = 0, totalBouquets = 0;

        for(int bloomDay: bloomDays) {
            if(bloomDay <= minDays) {
                count++;
            } else {
                totalBouquets += (count / k);
                count = 0;
            }

            if(totalBouquets >= m) {
                break;
            }
        }

        totalBouquets += (count / k);

        return totalBouquets >= m;
    }

    public int minDays(int[] bloomDays, int m, int k) {
        if(bloomDays == null) {
            return -1;
        }

        long numOfFlowers = (long) m * k;
        if(numOfFlowers > bloomDays.length) {
            return -1;
        }

        int left = Integer.MAX_VALUE, right = Integer.MIN_VALUE, answer = -1;

        for(int bloomDay: bloomDays) {
            left = Math.min(left, bloomDay);
            right = Math.max(right, bloomDay);
        }

        while (left <= right) {
            int mid = left + ((right - left)/2);

            if(canMakeMBouquetsWithKFlowers(bloomDays, m, k, mid)) {
                answer = mid;
                right = mid-1;
            } else {
                left = mid+1;
            }
        }

        return answer;
    }
}
