package neetcode.graphs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
Word Ladder II:
Example:
bat -> pat -> pot -> poz -> coz
bat -> bot -> pot -> poz -> coz

WordList = [pat, bot, pot, poz, coz]
beginWord = bat, endWord = coz


 */

public class Word_Ladder_II_LC_126 {
    class Solution {
        private boolean isSingleMutation(String word1, String word2) {
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

        public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
            List<List<String>> result = new ArrayList<>();

            if(wordList == null || wordList.isEmpty() || !wordList.contains(endWord)) {
                return result;
            }

            Set<String> wordSet = new HashSet<>(wordList);
            int n = wordList.size();
            Map<String, List<String>> adjMap = new HashMap<>();

            for(int i = 0; i < n; i++) {
                String word1 = wordList.get(i);

                for(int j = i + 1; j < n; j++) {
                    String word2 = wordList.get(j);
                    if(isSingleMutation(word1, word2)) {
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
                if(isSingleMutation(beginWord, word2)) {
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

            Queue<List<String>> queue = new LinkedList<>();
            queue.offer(Stream.of(beginWord).collect(Collectors.toList()));
            Set<String> usedOnLevel = new HashSet<>();

            while (!queue.isEmpty()) {
                int levelSize = queue.size();

                for(int i = 0; i < levelSize; i++) {
                    List<String> currList = queue.poll();
                    assert currList != null;
                    String currNode = currList.get(currList.size()-1);

                    if(!adjMap.containsKey(currNode)) {
                        continue;
                    }

                    if(currNode.equals(endWord)) {
                        if(result.isEmpty()) {
                            result.add(new ArrayList<>(currList));
                            continue;
                        } else if(result.get(result.size()-1).size() == currList.size()){
                            result.add(new ArrayList<>(currList));
                        } else {
                            break;
                        }
                    }

                    for (String neighbor : adjMap.get(currNode)) {
                        if(wordSet.contains(neighbor)) {
                            currList.add(neighbor);
                            usedOnLevel.add(neighbor);
                            queue.offer(new ArrayList<>(currList));
                            currList.remove(currList.size()-1);
                        }
                    }
                }

                usedOnLevel.forEach(wordSet::remove);
                usedOnLevel.clear();
            }

            return result;
        }
    }
}
