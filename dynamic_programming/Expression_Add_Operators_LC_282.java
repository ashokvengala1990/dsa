package neetcode.dynamic_programming;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.com/problems/expression-add-operators/description/

Using recursion
 */

public class Expression_Add_Operators_LC_282 {
    class Solution {
        private void helper0(int idx, String num, long currSum, long prevNum, String currExpr, int target, List<String> result) {
            if(idx >= num.length()) {
                if(currSum == target) {
                    result.add(currExpr);
                }
                return;
            }

            for(int i = idx; i < num.length(); i++) {
                long currNum = Long.parseLong(num.substring(idx, i+1));

                if(idx == 0) {
                    helper0(i+1, num, currSum+currNum, currNum, currExpr+currNum, target, result);
                } else {
                    helper0(i+1, num, currSum+currNum, currNum, currExpr+"+"+currNum, target, result);
                    helper0(i+1, num, currSum-currNum, -currNum, currExpr+"-"+currNum, target, result);
                    helper0(i+1, num, currSum-prevNum+(prevNum * currNum), prevNum * currNum,
                            currExpr+"*"+currNum, target, result);
                }

                if(num.charAt(idx) == '0') {
                    break;
                }
            }
        }

        public List<String> addOperators(String num, int target) {
            List<String> result = new ArrayList<>();
            if(num == null || num.isEmpty()) {
                return result;
            }

            helper0(0, num, 0, 0, "", target, result);

            return result;
        }
    }
}
