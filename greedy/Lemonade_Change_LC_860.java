package neetcode.greedy;

public class Lemonade_Change_LC_860 {
    class Solution {
        public boolean lemonadeChange(int[] bills) {
            if(bills == null || bills.length == 0) {
                return true;
            }

            int fivesCount = 0, tensCount = 0;

            for(int bill: bills) {
                if(bill == 5) {
                    fivesCount++;
                } else if(bill == 10) {
                    if(fivesCount > 0) {
                        fivesCount--;
                        tensCount++;
                    } else {
                        return false;
                    }
                } else {
                    if(tensCount > 0 && fivesCount > 0) {
                        tensCount--;
                        fivesCount--;
                    } else if(fivesCount >= 3) {
                        fivesCount -= 3;
                    } else {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
