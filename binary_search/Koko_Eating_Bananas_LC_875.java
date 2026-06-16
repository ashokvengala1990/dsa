package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/koko-eating-bananas/description/
 */

public class Koko_Eating_Bananas_LC_875 {
    class Solution {
        private boolean canEatBanana(int[] piles, int h, int bananaPerHour) {
            int totalHrs = 0;

            for(int pile: piles) {
                totalHrs += Math.ceil((double) pile / bananaPerHour);
            }

            return totalHrs <= h;
        }

        public int minEatingSpeed(int[] piles, int h) {
            if(piles == null || piles.length == 0) {
                return 0;
            }

            int left = 1, right = Integer.MIN_VALUE, answer = 0;

            for(int pile: piles) {
                right = Math.max(right, pile);
            }

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(canEatBanana(piles, h, mid)) {
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