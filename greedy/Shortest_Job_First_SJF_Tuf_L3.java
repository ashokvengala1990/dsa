package neetcode.greedy;

/*
Shortest Job First (SJF) -> Scheduling policy that select the waiting process with the smallest execution time to execute next.

- This problem assumes that all processes comes at the same arrival time
- Sort the input
- waitTime, totalTime
- TC: O(N) + O(NlogN) = O(NlogN)
- SC: O(1)
 */

import java.util.Arrays;

public class Shortest_Job_First_SJF_Tuf_L3 {
    class Solution {
        public int calculateAverageWaitTime(int[] jobs) {
            if(jobs == null || jobs.length == 0) {
                return 0;
            }

            int n = jobs.length, waitTime = 0, totalTime = 0;
            Arrays.sort(jobs);

            for(int i = 0; i < n; i++) {
                waitTime += totalTime;
                totalTime += jobs[i];
            }

            return waitTime / n;
        }
    }
}
