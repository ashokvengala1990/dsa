package neetcode.dynamic_programming;

import java.util.*;
import java.util.stream.Stream;

/*
https://leetcode.com/problems/word-break-ii/description/

Method	Description	TC	SC
wordBreak0	Plain recursion, check every word at each index	O(m · n · R) (tries all partitions, exponential in worst case)	O(n · R) recursion depth + result storage
wordBreak1	Recursion + memoization (Map<Integer, List<String>>)	O(m · n · R) (memo avoids recomputation, still must build all sentences)	O(n · R) (cache + recursion + result storage)
wordBreak2	Iterative DP bottom-up, checking startsWith	O(m · n · R) (startsWith takes O(L), so O(m · L · n · R))	O(n · R)
wordBreak3	Recursion + substring (O(n) each)	O(m · n² · R) (extra substring)	O(n · R)
wordBreak4	Recursion + StringBuilder + HashSet	O(n² · R) (avoids substring overhead, but still recursive branching)	O(n · R)
wordBreak5	Iterative DP + HashSet	O(n² · R)	O(n · R)
wordBreak6	Recursion + Trie	O(n² · R) (each index explores forward, Trie lookup O(L))	O(n · R)
wordBreak7	Iterative DP + Trie	O(n² · R)	O(n · R)

 */

public class Word_Break_II_LC_140 {
    class Solution {
        class TrieNode {
            boolean isWord;
            Map<Character, TrieNode> children;

            TrieNode() {
                this.isWord = false;
                this.children = new HashMap<>();
            }
        }

        class Trie {
            TrieNode root;

            Trie() {
                this.root = new TrieNode();
            }

            public void addWord(String word) {
                TrieNode curr = root;

                for(int i = 0; i < word.length(); i++) {
                    char ch = word.charAt(i);

                    if(!curr.children.containsKey(ch)) {
                        curr.children.put(ch, new TrieNode());
                    }

                    curr = curr.children.get(ch);
                }

                curr.isWord = true;
            }

            public boolean search(String word) {
                TrieNode curr = root;

                for(int i = 0; i < word.length(); i++) {
                    char ch = word.charAt(i);

                    if(!curr.children.containsKey(ch)) {
                        return false;
                    }

                    curr = curr.children.get(ch);
                }

                return curr.isWord;
            }
        }

        public List<String> wordBreak7(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            Trie trie = new Trie();

            for(String startWord: wordDict) {
                trie.addWord(startWord);
            }

            int size = s.length();
            Map<Integer, List<String>> cache = new HashMap<>();
            cache.put(size, Stream.of("").toList());

            for(int idx = size-1; idx >= 0; idx--) {
                List<String> localResult = new ArrayList<>();
                StringBuilder sb = new StringBuilder();

                for(int i = idx; i < s.length(); i++) {
                    sb.append(s.charAt(i));

                    if(trie.search(sb.toString())) {
                        List<String> subWords = cache.get(i+1);

                        for(String subWord: subWords) {
                            if(subWord.isEmpty()) {
                                localResult.add(sb.toString());
                            } else {
                                localResult.add(sb.toString()+" "+subWord);
                            }
                        }
                    }
                }

                cache.put(idx, localResult);
            }

            return cache.get(0);
        }

        private List<String> helper4(int idx, String s, Trie trie) {
            if(idx >= s.length()) {
                return Stream.of("").toList();
            }

            List<String> localResult = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            for(int i = idx; i < s.length(); i++) {
                sb.append(s.charAt(i));

                if(trie.search(sb.toString())) {
                    List<String> subWords = helper4(i+1, s, trie);

                    for(String subWord: subWords) {
                        if(subWord.isEmpty()) {
                            localResult.add(sb.toString());
                        } else {
                            localResult.add(sb.toString()+" "+subWord);
                        }
                    }
                }
            }

            return localResult;
        }

        public List<String> wordBreak6(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            Trie trie = new Trie();

            for(String startWord: wordDict) {
                trie.addWord(startWord);
            }

            return helper4(0, s, trie);
        }

        public List<String> wordBreak5(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            Set<String> wordDictSet = new HashSet<>(wordDict);
            int size = s.length();
            Map<Integer, List<String>> cache = new HashMap<>();
            cache.put(size, Stream.of("").toList());

            for(int idx = size-1; idx >= 0; idx--) {
                List<String> localResult = new ArrayList<>();
                StringBuilder sb = new StringBuilder();

                for(int i = idx; i < s.length(); i++) {
                    sb.append(s.charAt(i));
                    if(wordDictSet.contains(sb.toString())) {
                        List<String> subWords = cache.get(i+1);

                        for(String subWord: subWords) {
                            if(subWord.isEmpty()) {
                                localResult.add(sb.toString());
                            } else {
                                localResult.add(sb.toString() +" "+subWord);
                            }
                        }
                    }
                }

                cache.put(idx, localResult);
            }

            return cache.get(0);
        }

        private List<String> helper3(int idx, String s, Set<String> wordDictSet) {
            if(idx >= s.length()) {
                return Stream.of("").toList();
            }

            List<String> localResult = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            for(int i = idx; i < s.length(); i++) {
                sb.append(s.charAt(i));
                if(wordDictSet.contains(sb.toString())) {
                    List<String> subWords = helper3(i+1, s, wordDictSet);

                    for(String subWord: subWords) {
                        if(subWord.isEmpty()) {
                            localResult.add(sb.toString());
                        } else {
                            localResult.add(sb.toString() +" "+subWord);
                        }
                    }
                }
            }

            return localResult;
        }

        public List<String> wordBreak4(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            Set<String> wordDictSet = new HashSet<>(wordDict);

            return helper3(0, s, wordDictSet);
        }

        private List<String> helper2(int idx, String s, Set<String> wordDictSet) {
            if(idx >= s.length()) {
                return Stream.of("").toList();
            }

            List<String> localResult = new ArrayList<>();

            for(int i = idx; i < s.length(); i++) {
                String startWord = s.substring(idx, i+1);
                if(wordDictSet.contains(startWord)) {
                    List<String> subWords = helper2(i+1, s, wordDictSet);

                    for(String subWord: subWords) {
                        if(subWord.isEmpty()) {
                            localResult.add(startWord);
                        } else {
                            localResult.add(startWord+" "+subWord);
                        }
                    }
                }
            }

            return localResult;
        }

        public List<String> wordBreak3(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            Set<String> wordDictSet = new HashSet<>(wordDict);

            return helper2(0, s, wordDictSet);
        }

        public List<String> wordBreak2(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            int size = s.length();
            Map<Integer, List<String>> cache = new HashMap<>();
            cache.put(size, Stream.of("").toList());

            for(int idx = size-1; idx >= 0; idx--) {
                List<String> localResult = new ArrayList<>();

                for(String startWord: wordDict) {
                    if(idx+startWord.length() <= s.length() && s.startsWith(startWord, idx)) {
                        List<String> subWords = cache.get(idx+startWord.length());

                        for(String subWord: subWords) {
                            if(subWord.isEmpty()) {
                                localResult.add(startWord);
                            } else {
                                localResult.add(startWord+" "+subWord);
                            }
                        }
                    }
                }

                cache.put(idx, localResult);
            }


            return cache.get(0);
        }

        private List<String> helper1(int idx, String s, List<String> wordDict, Map<Integer, List<String>> cache) {
            if(idx >= s.length()) {
                return Stream.of("").toList();
            } else if(cache.containsKey(idx)) {
                return cache.get(idx);
            }

            List<String> localResult = new ArrayList<>();

            for(String startWord: wordDict) {
                if(idx+startWord.length() <= s.length() && s.startsWith(startWord, idx)) {
                    List<String> subWords = helper1(idx+startWord.length(), s, wordDict, cache);

                    for(String subWord: subWords) {
                        if(subWord.isEmpty()) {
                            localResult.add(startWord);
                        } else {
                            localResult.add(startWord+" "+subWord);
                        }
                    }
                }
            }

            cache.put(idx, localResult);
            return localResult;
        }

        public List<String> wordBreak1(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            Map<Integer, List<String>> cache = new HashMap<>();

            return helper1(0, s, wordDict, cache);
        }

        private List<String> helper0(int idx, String s, List<String> wordDict) {
           if(idx >= s.length()) {
               return Stream.of("").toList();
           }

           List<String> localResult = new ArrayList<>();

           for(String startWord: wordDict) {
               if(idx + startWord.length() <= s.length() && s.startsWith(startWord, idx)) {
                   List<String> subWords = helper0(idx+startWord.length(), s, wordDict);

                   for(String subWord: subWords) {
                       if(subWord.isEmpty()) {
                           localResult.add(startWord);
                       } else {
                           localResult.add(startWord+" "+subWord);
                       }
                   }
               }
           }

           return localResult;
        }

        public List<String> wordBreak0(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return new ArrayList<>();
            }

            return helper0(0, s, wordDict);
        }
    }
}
