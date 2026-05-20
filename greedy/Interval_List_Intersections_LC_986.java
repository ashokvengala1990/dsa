package neetcode.greedy;

import java.util.ArrayList;
import java.util.List;

public class Interval_List_Intersections_LC_986 {
    class Solution {
        public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
            if(firstList == null || firstList.length == 0 || secondList == null || secondList.length == 0) {
                return new int[][]{};
            }

            int size1 = firstList.length, size2 = secondList.length, i = 0, j = 0;
            List<int[]> result = new ArrayList<>();

            while (i < size1 && j < size2) {
                int[] first = firstList[i], second = secondList[j];

                if(first[1] < second[0]) {
                    i++;
                } else if(first[0] > second[1]) {
                    j++;
                } else {
                    result.add(new int[]{Math.max(first[0], second[0]), Math.min(first[1], second[1])});

                    if(first[1] < second[1]) {
                        i++;
                    } else {
                        j++;
                    }
                }
            }

            return result.toArray(int[][]::new);
        }
    }
}
