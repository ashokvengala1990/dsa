package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/minimum-time-to-repair-cars/description/
 */

public class Minimum_Time_to_Repair_Cars_LC_2594 {
    class Solution {
        private boolean canRepairsAllCars(int[] ranks, int cars, long possibleMinTime) {
            long carsRepaired = 0;

            for(int rank: ranks) {
                carsRepaired += (long) Math.sqrt(1.0 * possibleMinTime / rank);
            }

            return carsRepaired >= cars;
        }

        public long repairCars(int[] ranks, int cars) {
            if(ranks == null || ranks.length == 0) {
                return -1;
            }

            long left = 1, right = 0, answer = -1;
            // left value can also be initialized to zero

            for(int rank: ranks) {
                right = Math.max(right, rank);
            }

            right *= (long) cars * cars;

            while (left <= right) {
                long mid = left + ((right-left)/2);

                if(canRepairsAllCars(ranks, cars, mid)) {
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
ranks: [4, 2, 3, 1], cars = 10
index: [0, 1, 2, 3]
left = 1, right = 4 * 10 * 10 = 400
left = 1, right = 400

left = 1, right = 400, mid = 200
carsRepaired = 7 + 10 + 8 + 14 = 39

sqrt 200 / 4 = 7
sqrt 200 / 2 = 10
sqrt 200 / 3 = 8
sqrt 200 / 1 = 14

carsRepaired = 39 <= 10


left = 201 , right = 400, mid = 300
carsRepaired = 8 + 12 + 10 + 17 = 47
 47 <= 10

--> This is wrong as we used carsRepaired <= cars. Instead, we should use carsRepaired >= cars

ranks: [4, 2, 3, 1], cars = 10
index: [0, 1, 2, 3]
left = 1, right = 4 * 10 * 10 = 400
left = 1, right = 400

left = 1, right = 400, mid = 200
carsRepaired = 7 + 10 + 8 + 14 = 39

sqrt 200 / 4 = 7
sqrt 200 / 2 = 10
sqrt 200 / 3 = 8
sqrt 200 / 1 = 14

carsRepaired = 39 >= 10

left = 1, right = 199, mid = 100
carsRepaired = 5 + 7 + 5 + 10 = 27
27 >= 10

left = 1 , right = 99, mid = 50
carsRepaired = 3 + 5 + 4 + 7 = 19
19 >= 10

left = 1, right = 49, mid = 25
carsRepaired = 2 + 3 + 2 + 5 = 12
12 >= 10

left = 1, right = 24, mid = 12
carsRepaired = 1 + 2 + 2 + 3 = 8
8 >= 10

left = 13, right = 24, mid = 18
carsRepaired = 2 + 3 + 2 + 4 = 11
11 >= 10

left = 13, right = 17, mid = 15
carsRepaired = 1 + 2 + 2 + 3 = 8
8 <= 10

left = 16, right = 17, mid = 16
carsRepaired = 2 + 2 + 2 + 4 = 10
10 >= 10

left = 16, right = 15

return 16



ranks: [4, 2, 3, 1], cars = 10
 */

/*
[4, 2], cars = 1

left = 1, right = 4



left = 1, right = 4, mid = 2
carsRepaired = 0 + 1 = 1

1 >= 1

left = 1, right = 1, mid = 1
carsRepaired = 0 + 0 = 0
0 >= 1

left = 2, right = 1


left = 0, right = 4, mid = 2
carsRepaired = 0 + 1 = 1
1 >= 1

left = 0, right = 1, mid = 0
carsRepaired = 0 + 0 = 0
0>=1

left = 1, right = 1, mid = 1
carsRepaired = 0 + 0 = 0
0 >= 1

left =

 */
