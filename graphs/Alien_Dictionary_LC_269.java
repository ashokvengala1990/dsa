package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/alien-dictionary/description/

Alien Dictionary — Revision Notes
Core Insight
The sorted alien word list is a set of ordering clues. By comparing adjacent words, you extract character precedence rules'
(edges), then topological sort (Kahn's BFS) reveals the alien alphabet order. If a cycle exists → invalid → return "".

Step-by-Step Approach:

Step 1 — Initialize graph from all unique characters across all words.
adjMap:   each char → empty list
inDegree: each char → 0

Step 2 — Build edges by comparing each adjacent word pair. Only the first differing character gives you a valid edge. Break
immediately after finding it.
"ac" vs "ab"  →  c → b  (c comes before b)
"zc" vs "zb"  →  c → b  (same rule, duplicate edge — handled by inDegree)

⚠️ Invalid case: if word1.startsWith(word2) AND len1 > len2 → impossible ordering → return ""
e.g., ["abcd", "abc"] — a longer word cannot precede its own prefix

Step 3 — Kahn's BFS (topological sort)
- Enqueue all chars with inDegree == 0
- Process each, decrement neighbors' inDegree, enqueue when it hits 0

Step 4 — Cycle detection
- return topoOrder.length() < totalUniqueChars ? "" : topoOrder;
- If not all chars were visited → cycle existed → invalid.

Key Edge Cases to Remember
Case                        Example                                         Expected
Prefix conflict         ["abc", "abcd"]                                     valid ✅
Reverse prefix          ["abcd", "abc"]                                     "" ❌
Cycle in ordering       ["z","x","z"]                                       "" ❌
Duplicate edges         ["ac","ab","zc","zb"] → two c→b edges       handled via inDegree count

The Duplicate Edge Trap (Case 04)
For ["ac","ab","zc","zb"], both "ac" vs "ab" and "zc" vs "zb" produce edge c → b, making adjMap = {c: [b, b]}.
This is fine because inDegree of b becomes 2, and gets decremented twice during BFS traversal — it naturally
resolves. No deduplication needed.

Complexity
Let:

V = number of unique characters (nodes)
E = number of edges derived from word comparisons
N = number of words, L = max word length

        Complexity              Reason
Time    O(N × L)        Iterating all characters to build graph + BFS is O(V + E), but V + E ≤ N×L
Space   O(V + E)        adjMap + inDegree + queue + result — all bounded by unique chars and edges

In practice, V ≤ 26 (only lowercase letters), so the graph operations are effectively O(1) bounded — the
bottleneck is reading the input O(N × L).
 */
public class Alien_Dictionary_LC_269 {
    static class Solution {
        private String topologicalSort(Map<Character, List<Character>> adjMap, Map<Character, Integer> inDegree) {
            Queue<Character> queue = new LinkedList<>();

            for(char ch: inDegree.keySet()) {
                if(inDegree.get(ch) == 0) {
                    queue.offer(ch);
                }
            }

            StringBuilder topoOrder = new StringBuilder();

            while (!queue.isEmpty()) {
                char currNode = queue.poll();
                topoOrder.append(currNode);

                for(char neighbor: adjMap.get(currNode)) {
                    inDegree.put(neighbor, inDegree.get(neighbor)-1);

                    if(inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return topoOrder.toString();
        }

        public String alienOrder(String[] words) {
            if(words == null || words.length == 0) {
                return "";
            }

            Map<Character, List<Character>> adjMap = new HashMap<>();
            Map<Character, Integer> inDegree = new HashMap<>();

            for(String word: words) {
                for(char ch: word.toCharArray()) {
                    adjMap.put(ch, new ArrayList<>());
                    inDegree.put(ch, 0);
                }
            }

            int len = words.length;
            for(int i = 0; i < len-1; i++) {
                String word1 = words[i], word2 = words[i+1];
                int len1 = word1.length(), len2 = word2.length(), minLen = Math.min(len1, len2);

                if(len1 > len2 && word1.startsWith(word2)) {
                    return "";
                }

                for(int j = 0; j < minLen; j++) {
                    char ch1 = word1.charAt(j), ch2 = word2.charAt(j);

                    if(ch1 != ch2) {
                        adjMap.get(ch1).add(ch2);
                        inDegree.put(ch2, inDegree.get(ch2)+1);
                        break;
                    }
                }
            }

            int k = adjMap.size();
            String topoOrder = topologicalSort(adjMap, inDegree);

            return topoOrder.length() < k ? "" : topoOrder;
        }
    }

    public static void main(String[] args) {
//        System.out.println("abc".startsWith("abcd"));
//        System.out.println("abcd".startsWith("xyz"));

        /*
        Case 01: This is not possible when a dictionary is like this
        s1 -> abcd
        s2 -> abc

        Case 02: This is possible
        s1 -> abc
        s2 -> abcd

        Case 03: This is Acyclic dependency issue.This is also not possible when a dictionary is like this
        s1 -> abc
        s2 -> bat
        s3 -> ade

        or
        s1 -> z
        s2 -> x
        s3 -> z

        Case 04: This is possible but see the sample of adjMap and inDegree as c -> {b,b} and how it traverse check it
        and it is good example to understand this case also
        s1 -> ac
        s2 -> ab
        s3 -> zc
        s4 -> zb
         */

        Solution s = new Solution();
        String[] words = {"ac","ab","zc","zb"};
        System.out.println(s.alienOrder(words));
    }
}
