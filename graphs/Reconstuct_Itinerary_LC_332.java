package neetcode.graphs;

import java.util.ArrayList;
import java.util.List;

public class Reconstuct_Itinerary_LC_332 {
    class Solution {
        public List<String> findItinerary0(List<List<String>> tickets) {
            List<String> result = new ArrayList<>();
            if(tickets == null || tickets.isEmpty()) {
                return result;
            }



            return result;
        }
    }
}

/*
class Solution:
    def findItinerary(self, tickets: List[List[str]]) -> List[str]:
        graph = defaultDict(list)

        for src, dst in sorted(tickets, reverse=true):
            graph[src].append(dst)

        stack = ["JKF"]
        itinerary = []

        while stack:
            while graph[stack[-1]]:
                stack.append(graph[stack[-1]].pop())
            itinerary.append(stack.pop())

        return reversed(itinerary)
 */