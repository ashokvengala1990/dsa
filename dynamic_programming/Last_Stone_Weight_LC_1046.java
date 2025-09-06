package neetcode.dynamic_programming;

import java.util.PriorityQueue;

/*
https://leetcode.com/problems/last-stone-weight/description/

- Using PriorityQueue Data structure
- TC: O(n * logn)
- SC: o(n)
 */

public class Last_Stone_Weight_LC_1046 {
    class Solution {
        public int lastStoneWeight(int[] stones) {
            if(stones == null || stones.length == 0) {
                return 0;
            }

            PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a,b) -> b-a);

            for(int stone: stones) {
                maxHeap.offer(stone);
            }

            while (maxHeap.size() >= 2) {
                int y = maxHeap.poll(), x = maxHeap.poll();

                if(x != y) {
                    maxHeap.offer(y-x);
                }
            }

            return maxHeap.isEmpty() ? 0 : maxHeap.poll();
        }
    }
}
