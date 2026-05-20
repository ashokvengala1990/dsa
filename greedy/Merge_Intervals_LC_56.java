package neetcode.greedy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Merge_Intervals_LC_56 {
    class Solution {
        public int[][] merge(int[][] intervals) {
            if (intervals == null || intervals.length == 0) {
                return intervals;
            }

            Arrays.sort(intervals, new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    return o1[0] - o2[0];
                }
            });

            List<int[]> result = new ArrayList<>();

            for (int[] curr : intervals) {
                if (result.isEmpty() || result.get(result.size() - 1)[1] < curr[0]) {
                    result.add(new int[]{curr[0], curr[1]});
                } else {
                    int[] lastInterval = result.get(result.size() - 1);
                    lastInterval[1] = Math.max(lastInterval[1], curr[1]);
                }
            }

            return result.toArray(int[][]::new);
        }
    }
}
