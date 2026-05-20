package neetcode.graphs;

import java.util.*;
/*
If I remove already taken string from hashset? Will there a problem?
Ans) No. See below example.
Kolkata -> Odisha -> Hyderabad -> Kerala
Kolkata -> Bengalore -> Odisha -> Hyderabad


Approach 1: Build Full Graph (Adjacency List)
Algorithm
1. For every pair of words (word1, word2), check if they differ by 1 letter.
2. If yes → add each as neighbors in adjMap.
3. Also connect beginWord with all matching words.
4. Run BFS starting from beginWord.
5. When you reach endWord, return steps+1.

Time Complexity
- Building graph: O(N² × L) , where N = number of words in the word list, L = length of each word (average)
(Compare every pair → N², compare letters → L)
- BFS traversal: O(N²)

➡️ Total TC = O(N² × L) ❌ (Slow)

Space Complexity
- Adjacency list: O(N²)
- Queue + visited: O(N)

➡️ Total SC = O(N²) ❌ (Very large)
Use When
- Word list is very small.
- Simpler but inefficient for large input.

🧠 Approach 2: BFS + On-the-fly Neighbor Generation (Optimal)
Algorithm
1) Insert all words into a HashSet for O(1) lookup.
2) Start BFS from beginWord, steps = 0.
3) For each word in BFS:
    - Convert to char array.
    - For each index j:
    - Replace character with 'a' to 'z' (26 possibilities).
    - Form new word (neighbor).
    - If neighbor is in the set:
        * Add to queue
        * Remove from set (visited)
4) When endWord is reached → return steps + 1.

Time Complexity
    - For each word (N), for each letter (L), try 26 letters.
N = number of words in the word list

L = length of each word (average)

➡️ TC = O(N × L × 26) ≈ O(N × L) ✔ (Fast)

Space Complexity
- HashSet of words: O(N)
- BFS queue: O(N)

➡️ SC = O(N × L) ✔

Use When
- Best method for LeetCode
- Works efficiently for large word lists
- Standard optimal solution

🎯 Comparison Summary
Feature	Approach 1 (Graph Build)	Approach 2 (Optimal BFS)
Graph Preprocessing	Yes (expensive)	No
Neighbor Discovery	Compare all pairs	Modify characters dynamically
Time Complexity	❌ O(N² × L)	✅ O(N × L)
Space Complexity	❌ O(N²)	✅ O(N)
Best For	Small inputs	Large inputs / interviews
Recommended	No	Yes ✔

📝 What to Remember
- BFS gives shortest transformation path.
- Approach 2 avoids N² comparisons → huge performance gain.
- Remove neighbors from wordSet immediately to avoid revisits.
- Always check endWord in the list first → else return 0.

 */

public class Word_Ladder_LC_127 {

    class Revision01 {
        private boolean isSingleDiffLetter(String word1, String word2) {
            if(word1.length() != word2.length()) {
                return false;
            }

            int diffCount = 0;
            for(int i = 0; i < word1.length(); i++) {
                if(word1.charAt(i) != word2.charAt(i)) {
                    diffCount++;
                }
            }

            return diffCount == 1;
        }

        public int ladderLength(String beginWord, String endWord, List<String> wordList) {
            if(beginWord == null || wordList == null || wordList.isEmpty() || !wordList.contains(endWord)) {
                return 0;
            }

            Map<String, List<String>> adjMap = new HashMap<>();
            int size = wordList.size();

            for(int i = 0; i < size; i++) {
                String word1 = wordList.get(i);
                for(int j = 0; j < size; j++) {
                    String word2 = wordList.get(j);

                    if(isSingleDiffLetter(word1, word2)) {
                        if(!adjMap.containsKey(word1)) {
                            adjMap.put(word1, new ArrayList<>());
                        }

                        if(!adjMap.containsKey(word2)) {
                            adjMap.put(word2, new ArrayList<>());
                        }

                        adjMap.get(word1).add(word2);
                        adjMap.get(word2).add(word1);
                    }
                }
            }

            for(int i = 0; i < size; i++) {
                String word1 = wordList.get(i);

                if(isSingleDiffLetter(word1, beginWord)) {
                    if(!adjMap.containsKey(word1)) {
                        adjMap.put(word1, new ArrayList<>());
                    }

                    if(!adjMap.containsKey(beginWord)) {
                        adjMap.put(beginWord, new ArrayList<>());
                    }

                    adjMap.get(word1).add(beginWord);
                    adjMap.get(beginWord).add(word1);
                }
            }

            Queue<String> queue = new LinkedList<>();
            queue.offer(beginWord);
            int steps = 0;

            Set<String> visited = new HashSet<>();
            visited.add(beginWord);

            while (!queue.isEmpty()) {
                int currLevelSize = queue.size();

                for(int i = 0; i < currLevelSize; i++) {
                    String currWord = queue.poll();

                    if(currWord.equals(endWord)) {
                        return steps+1;
                    }

                    if(adjMap.containsKey(currWord)) {
                        for(String neighbor: adjMap.get(currWord)) {
                            if(!visited.contains(neighbor)) {
                                queue.offer(neighbor);
                                visited.add(neighbor);
                            }
                        }
                    }
                }

                steps++;
            }

            return 0;
        }

        public int ladderLength0(String beginWord, String endWord, List<String> wordList) {
            if(beginWord == null || wordList == null || wordList.isEmpty() || !wordList.contains(endWord)) {
                return 0;
            }

            Set<String> wordSet = new HashSet<>(wordList);

            Queue<String> queue = new LinkedList<>();
            int steps = 0;
            queue.offer(beginWord);

            while (!queue.isEmpty()) {
                int currLevelSize = queue.size();

                for(int i = 0; i < currLevelSize; i++) {
                    String currWord = queue.poll();
                    if(currWord.equals(endWord)) {
                        return steps+1;
                    }

                    char[] charWord = currWord.toCharArray();

                    for(int j = 0; j < charWord.length; j++) {
                        char originalChar = charWord[j];

                        for(char ch = 'a'; ch <= 'z'; ch++) {
                            charWord[j] = ch;

                            String neighbor = new String(charWord);
                            if(wordSet.contains(neighbor)) {
                                queue.offer(neighbor);
                                wordSet.remove(neighbor);
                            }
                        }

                        charWord[j] = originalChar;
                    }
                }

                steps++;
            }

            return 0;
        }
    }

    class Solution {
        public int ladderLength2(String beginWord, String endWord, List<String> wordList) {
            if(wordList == null || wordList.isEmpty() || !wordList.contains(endWord)) {
                return 0;
            }

            Set<String> wordSet = new HashSet<>(wordList);
            Queue<String> queue = new LinkedList<>();
            queue.offer(beginWord);
            int steps = 1;

            while (!queue.isEmpty()) {
                int levelSize = queue.size();

                for(int i = 0; i < levelSize; i++) {
                    String currNode = queue.poll();
                    if(currNode.equals(endWord)) {
                        return steps;
                    }
                    char[] chArr = currNode.toCharArray();

                    for(int j = 0; j < chArr.length; j++) {
                        char originalChar = chArr[j];

                        for(char ch = 'a'; ch <= 'z'; ch++) {
                            chArr[j] = ch;
                            String neighbor = new String(chArr);

                            if(wordSet.contains(neighbor)) {
                                queue.offer(neighbor);
                                wordSet.remove(neighbor);
                            }
                        }

                        chArr[j] = originalChar;
                    }
                }
                steps++;
            }


            return 0;
        }

        public int ladderLength1(String beginWord, String endWord, List<String> wordList) {
            if(wordList == null || wordList.isEmpty() || !wordList.contains(endWord)) {
                return 0;
            }

            Set<String> wordSet = new HashSet<>(wordList);
            Set<String> visited = new HashSet<>();
            Queue<String> queue = new LinkedList<>();

            queue.offer(beginWord);
            visited.add(beginWord);
            int steps = 1;

            while (!queue.isEmpty()) {
                int levelSize = queue.size();

                for(int i =0; i < levelSize; i++) {
                    String currNode = queue.poll();

                    if(currNode.equals(endWord)) {
                        return steps;
                    }

                    char[] currArr = currNode.toCharArray();
                    for(int j = 0; j < currArr.length; j++) {
                        char originalChar = currArr[j];

                        for(char ch = 'a'; ch <= 'z'; ch++) {
                            currArr[j] = ch;
                            String neighbor = new String(currArr);
                            if(!visited.contains(neighbor) && wordSet.contains(neighbor)) {
                                queue.offer(neighbor);
                                visited.add(neighbor);
                            }
                        }
                        currArr[j] = originalChar;
                    }
                }

                steps++;
            }

            return 0;
        }

        private boolean isSingleDiffLetter(String word1, String word2) {
            if(word1.length() != word2.length()) {
                return false;
            }

            int cnt = 0;
            for(int i = 0; i < word1.length(); i++) {
                if(word1.charAt(i) != word2.charAt(i)) {
                    cnt++;
                }
            }

            return cnt == 1;
        }

        public int ladderLength(String beginWord, String endWord, List<String> wordList) {
            if(wordList == null || wordList.isEmpty() || !wordList.contains(endWord)) {
                return 0;
            }

            Map<String, List<String>> adjMap = new HashMap<>();

            int n = wordList.size();
            for(int i = 0; i < n ; i++) {
                String word1 = wordList.get(i);
                for(int j = i + 1; j < n; j++) {
                    String word2 = wordList.get(j);
                    if(isSingleDiffLetter(word1, word2)) {
                        if(!adjMap.containsKey(word1)) {
                            adjMap.put(word1, new ArrayList<>());
                        }

                        if(!adjMap.containsKey(word2)) {
                            adjMap.put(word2, new ArrayList<>());
                        }

                        adjMap.get(word1).add(word2);
                        adjMap.get(word2).add(word1);
                    }
                }
            }

            for(int i = 0; i < n; i++) {
                String word2 = wordList.get(i);
                if(isSingleDiffLetter(beginWord, word2)) {
                    if(!adjMap.containsKey(beginWord)) {
                        adjMap.put(beginWord, new ArrayList<>());
                    }

                    if(!adjMap.containsKey(word2)) {
                        adjMap.put(word2, new ArrayList<>());
                    }

                    adjMap.get(beginWord).add(word2);
                    adjMap.get(word2).add(beginWord);
                }
            }

            Queue<String> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();
            int steps = 1;

            queue.offer(beginWord);
            visited.add(beginWord);

            while (!queue.isEmpty()) {
                int levelSize = queue.size();

                for(int i = 0; i < levelSize; i++) {
                    String currNode = queue.poll();

                    if(currNode.equals(endWord)) {
                        return steps;
                    }

                    if(adjMap.containsKey(currNode)) {
                        for (String neighbor : adjMap.get(currNode)) {
                            if (!visited.contains(neighbor)) {
                                queue.offer(neighbor);
                                visited.add(neighbor);
                            }
                        }
                    }
                }

                steps++;
            }

            return 0;
        }
    }
}
