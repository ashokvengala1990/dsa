package neetcode.dynamic_programming;

import java.util.Arrays;

public class Last_Stone_Weight_II_LC_1049 {
    class Solution {
        public int lastStoneWeightII4(int[] stones) {
            if (stones == null || stones.length == 0) {
                return 0;
            }

            int size = stones.length, totalSum = 0;

            for (int stone : stones) {
                totalSum += stone;
            }

            int targetSum = (int) Math.ceil(totalSum / 2.0);
            int[] frontRow = new int[targetSum + 1];

            for(int currSum = 0; currSum <= targetSum; currSum++) {
                frontRow[currSum] = Math.abs(currSum - (totalSum - currSum));
            }

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[targetSum+1];
                for(int currSum = targetSum; currSum >= 0; currSum--) {
                    // Include current index element
                    int take = Integer.MAX_VALUE;
                    if(currSum + stones[idx] <= targetSum) {
                        take = frontRow[currSum+stones[idx]];
                    }

                    // Don't include current index element
                    int notTake = frontRow[currSum];

                    currRow[currSum] = Math.min(take, notTake);
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int lastStoneWeightII3(int[] stones) {
            if (stones == null || stones.length == 0) {
                return 0;
            }

            int size = stones.length, totalSum = 0;

            for (int stone : stones) {
                totalSum += stone;
            }

            int targetSum = (int) Math.ceil(totalSum / 2.0);
            int[] frontRow = new int[totalSum + 1];

            for(int currSum = 0; currSum <= totalSum; currSum++) {
                frontRow[currSum] = Math.abs(currSum - (totalSum - currSum));
            }

            for(int idx = size-1; idx >= 0; idx--) {
                int[] currRow = new int[totalSum+1];
                for(int currSum = totalSum; currSum >= 0; currSum--) {
                    // Include current index element
                    int take = Integer.MAX_VALUE;
                    if(currSum + stones[idx] <= totalSum) {
                        take = frontRow[currSum+stones[idx]];
                    }

                    // Don't include current index element
                    int notTake = frontRow[currSum];

                    currRow[currSum] = Math.min(take, notTake);
                }
                frontRow = currRow;
            }

            return frontRow[0];
        }

        public int lastStoneWeightII2(int[] stones) {
            if (stones == null || stones.length == 0) {
                return 0;
            }

            int size = stones.length, totalSum = 0;

            for (int stone : stones) {
                totalSum += stone;
            }

            int targetSum = (int) Math.ceil(totalSum / 2.0);
            int[][] cache = new int[size+1][totalSum + 1];

            for(int currSum = 0; currSum <= totalSum; currSum++) {
                cache[size][currSum] = Math.abs(currSum - (totalSum - currSum));
            }

            for(int idx = size-1; idx >= 0; idx--) {
                for(int currSum = totalSum; currSum >= 0; currSum--) {
                    // Include current index element
                    int take = Integer.MAX_VALUE;
                    if(currSum + stones[idx] <= totalSum) {
                        take = cache[idx+1][currSum+stones[idx]];
                    }

                    // Don't include current index element
                    int notTake = cache[idx+1][currSum];

                    cache[idx][currSum] = Math.min(take, notTake);
                }
            }

            return cache[0][0];
        }

        private int helper1(int idx, int currSum, int targetSum, int totalSum, int[] stones, int[][] cache) {
            if(idx >= stones.length || currSum >= targetSum) {
                return Math.abs(currSum - (totalSum - currSum));
            } else if(cache[idx][currSum] != -1) {
                return cache[idx][currSum];
            }

            // Include current index element
            int take = helper1(idx+1, currSum+stones[idx], targetSum, totalSum, stones, cache);

            // Don't include current index element
            int notTake = helper1(idx+1, currSum, targetSum, totalSum, stones, cache);

            return cache[idx][currSum] = Math.min(take, notTake);
        }

        public int lastStoneWeightII1(int[] stones) {
            if(stones == null || stones.length == 0) {
                return 0;
            }

            int size = stones.length, totalSum = 0;

            for(int stone: stones) {
                totalSum += stone;
            }

            int targetSum = (int) Math.ceil(totalSum/2.0);
            int[][] cache = new int[size][totalSum+1];
            for(int[] itr: cache) {
                Arrays.fill(itr, -1);
            }

            return helper1(0,0, targetSum, totalSum, stones, cache);
        }

        private int helper0(int idx, int currSum, int targetSum, int totalSum, int[] stones) {
            if(idx >= stones.length || currSum >= targetSum) {
                return Math.abs(currSum - (totalSum - currSum));
            }

            // Include current index
            int take = helper0(idx+1, currSum+stones[idx], targetSum, totalSum, stones);

            // Don't incldue current index
            int notTake = helper0(idx+1, currSum, targetSum, totalSum, stones);

            return Math.min(take, notTake);
        }

        public int lastStoneWeightII0(int[] stones) {
            if(stones == null || stones.length == 0) {
                return 0;
            }

            int size = stones.length, totalSum = 0;

            for(int stone: stones) {
                totalSum += stone;
            }

            int targetSum = (int) Math.ceil(totalSum/2.0);

            return helper0(0,0, targetSum, totalSum, stones);
        }
    }
}
