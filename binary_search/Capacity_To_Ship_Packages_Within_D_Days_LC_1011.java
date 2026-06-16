package binary_search.custom.binary_search;

public class Capacity_To_Ship_Packages_Within_D_Days_LC_1011 {
    class Solution {
        private boolean canShipPackages(int[] weights, int days, int capacityOfShip) {
            int daysNeeded = 1, loadWeight = 0;

            for(int weight: weights) {
                if(loadWeight + weight > capacityOfShip) {
                    daysNeeded++;
                    loadWeight = weight;
                } else {
                    loadWeight += weight;
                }
            }

            return daysNeeded <= days;
        }

        public int shipWithinDays(int[] weights,int days) {
            if(weights == null || weights.length == 0) {
                return 0;
            }

            int size = weights.length, left = 0, right = 0, answer = 0;

            for(int weight: weights) {
                left = Math.max(left, weight);
                right += weight;
            }

            while (left <= right) {
                int mid = left + ((right - left)/2);

                if(canShipPackages(weights, days, mid)) {
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
