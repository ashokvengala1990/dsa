package neetcode.greedy;

import java.util.Arrays;

public class Maximum_Matching_of_Players_With_Trainers_LC_2410 {
    class Solution {
        public int matchPlayersAndTrainers(int[] players, int[] trainers) {
            int n = players.length, m = trainers.length, l = 0, r = 0;
            Arrays.sort(players);
            Arrays.sort(trainers);

            while (l < n && r < m) {
                if(players[l] <= trainers[r]) {
                    l++;
                }

                r++;
            }

            return l;
        }
    }
}
