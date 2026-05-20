package neetcode.greedy;

import java.util.ArrayList;
import java.util.List;

public class Insert_Interval_LC_57 {
    class Solution {
        public int[][] insert(int[][] intervals, int[] newInterval) {
            if(intervals == null || intervals.length == 0) {
                if(newInterval == null || newInterval.length == 0) {
                    return new int[][]{};
                } else {
                    return new int[][]{newInterval};
                }
            }

            int size = intervals.length;
            List<int[]> result = new ArrayList<>();

            for(int i = 0; i < size; i++) {
                int[] currInterval = intervals[i];

                if(newInterval[1] < currInterval[0]) {
                    result.add(newInterval);

                    for(int j = i; j < size; j++) {
                        result.add(intervals[j]);
                    }

                    return result.toArray(int[][]::new);
                } else if(currInterval[1] < newInterval[0]) {
                    result.add(intervals[i]);
                } else {
                    newInterval = new int[]{Math.min(newInterval[0], currInterval[0]), Math.max(newInterval[1], currInterval[1])};
                }
            }

            result.add(newInterval);
            return result.toArray(int[][]::new);
        }
    }
}
