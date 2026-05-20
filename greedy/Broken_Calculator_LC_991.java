package neetcode.greedy;

/*
https://leetcode.com/problems/broken-calculator/description

- Using Greedy and backwards approach
- Two operations: either one at a time
    * startValue * 2 = target
    * startValue - 1 = target

    If we go from startValue to reach target, then we have an issue with
    respective to base cases. So, it is not possible to solve it.

    * startValue = (target / 2)
    * startValue = target + 1
    So, if we go from target to reach startValue, then we are able to find
    the base case
    - if startValue >= target, then return startValue - target
    - Otherwise, target % 2 == 0 (even), then target = target / 2
    - Else, it is odd, then target = target + 1

    - TC: O(log(target))
    - SC: O(1)
 */

public class Broken_Calculator_LC_991 {
    class Solution {
        public int brokenCalc1(int startValue, int target) {
            int answer = 0;

            while (startValue < target) {
                answer++;

                if(target % 2 == 0) {
                    target = target / 2;
                } else {
                    target = target + 1;
                }
            }

            return answer + (startValue - target);
        }

        public int brokenCalc0(int startValue, int target) {
            if(startValue >= target) {
                return startValue - target;
            }

            if(target % 2 == 0) {
                return 1 + brokenCalc0(startValue, target / 2);
            } else {
                return 1 + brokenCalc0(startValue, target + 1);
            }
        }
    }
}
