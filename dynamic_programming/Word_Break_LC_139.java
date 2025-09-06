package neetcode.dynamic_programming;

import java.util.*;

/*
https://leetcode.com/problems/word-break/description/


 */

public class Word_Break_LC_139 {
    class Solution {
        class TrieNode {
            boolean isWord;
            Map<Character, TrieNode> children;

            TrieNode() {
                this.isWord = false;
                this.children = new HashMap<>();
            }

            TrieNode(boolean _isWord) {
                this.isWord = _isWord;
                this.children = new HashMap<>();
            }

            TrieNode(boolean _isWord, Map<Character, TrieNode> _children) {
                this.isWord = _isWord;
                this.children = _children;
            }
        }

        class Trie {
            TrieNode root;

            Trie() {
                this.root = new TrieNode();
            }

            public void  addWord(String word) {
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

            public boolean search(String s, int idx, int i) {
                TrieNode curr = root;
                for(int i1 = idx; i1 <=i; i1++) {
                    char ch = s.charAt(i1);
                    if(!curr.children.containsKey(ch)) {
                        return false;
                    }
                    curr = curr.children.get(ch);
                }

                return curr.isWord;
            }
        }

        // use min(maxWordLen or s.length()) to traverse s string in the inner for loop
        public boolean wordBreak14(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            Trie trie = new Trie();

            int maxWordLen = Integer.MIN_VALUE;
            for(String word: wordDict) {
                trie.addWord(word);
                maxWordLen = Integer.max(maxWordLen, word.length());
            }

            int size = s.length();
            boolean[] cache = new boolean[size+1];
            cache[size] = true;

            for(int idx = size-1; idx >= 0; idx--) {
                for(int i = idx; i < Math.min(s.length(), idx+maxWordLen); i++) {
                    if(trie.search(s, idx, i) && cache[i+1]) {
                        cache[idx] = true;
                        break;
                    }
                }
            }

            return cache[0];
        }

        // Using Iterative True Dynamic Programming + Trie Data Structure
        // Instead of HashSet + Substring, equals method, trie is better
        public boolean wordBreak13(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            Trie trie = new Trie();

            for(String word: wordDict) {
                trie.addWord(word);
            }

            int size = s.length();
            boolean[] cache = new boolean[size+1];
            cache[size] = true;

            for(int idx = size-1; idx >= 0; idx--) {
                for(int i = idx; i < s.length(); i++) {
                    if(trie.search(s, idx, i) && cache[i+1]) {
                        cache[idx] = true;
                        break;
                    }
                }
            }

            return cache[0];
        }

        private boolean helper5(int idx, String s, Trie trie, Boolean[] cache) {
            if(idx >= s.length()) {
                return true;
            } else if(cache[idx] != null) {
                return cache[idx];
            }

            for(int i = idx; i < s.length(); i++) {
                if(trie.search(s, idx, i) && helper5(i+1, s, trie, cache)) {
                    return cache[idx] = true;
                }
            }

            return cache[idx] = false;
        }

        // Using Recursion Memoization + Trie Data Structure
        // Instead of HashSet + Substring, equals method, trie is better
        public boolean wordBreak12(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            Trie trie = new Trie();

            for(String startWord: wordDict) {
                trie.addWord(startWord);
            }

            int size = s.length();
            Boolean[] cache = new Boolean[size];

            return helper5(0, s, trie, cache);
        }

        public boolean wordBreak11(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            Queue<String> queue = new LinkedList<>();
            queue.offer(s);
            Set<String> visited = new HashSet<>();

            while (!queue.isEmpty()) {
                String word = queue.poll();

                if(visited.contains(word)) {
                    continue;
                } else {
                    visited.add(word);
                    if(word.isEmpty()) {
                        return true;
                    }

                    for(String startWord: wordDict) {
                        if(word.startsWith(startWord)) {
                            queue.offer(word.substring(startWord.length()));
                        }
                    }
                }
            }

            return false;
        }

        public boolean wordBreak9(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            int size = s.length();
            boolean[] cache = new boolean[size+1];
            cache[size] = true;

            for(int idx = size-1; idx >= 0; idx--) {
                for(String startWord: wordDict) {
                    if(idx + startWord.length() <= s.length() && startWord.equals(s.substring(idx, idx+startWord.length()))
                            && cache[idx+startWord.length()]) {
                        cache[idx] = true;
                        break;
                    }
                }
            }

            return cache[0];
        }

        private boolean helper4(int idx, String s, List<String> wordDict, Boolean[] cache) {
            if(idx >= s.length()) {
                return true;
            } else if(cache[idx] != null) {
                return cache[idx];
            }

            for(String startWord: wordDict) {
                if(idx + startWord.length() <= s.length() && startWord.equals(s.substring(idx, idx+startWord.length()))
                        && helper4(idx+startWord.length(), s, wordDict, cache)) {
                    return cache[idx] = true;
                }
            }

            return cache[idx] = false;
        }

        public boolean wordBreak8(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            int size = s.length();
            Boolean[] cache = new Boolean[size];

            return helper4(0, s, wordDict, cache);
        }

        private boolean helper3(int idx, String s, List<String> wordDict) {
            if(idx >= s.length()) {
                return true;
            }

            for(String startWord: wordDict) {
                if(idx + startWord.length() <= s.length() && startWord.equals(s.substring(idx, idx+startWord.length()))
                        && helper3(idx+startWord.length(), s, wordDict)) {
                    return true;
                }
            }

            return false;
        }

        public boolean wordBreak7(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            return helper3(0, s, wordDict);
        }

        public boolean wordBreak6(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            int size = s.length();
            boolean[] cache = new boolean[size+1];
            cache[size] = true;
            Set<String> wordDictSet = new HashSet<>(wordDict);

            for(int idx = size-1; idx >= 0; idx--) {
                StringBuilder sb = new StringBuilder();
                for(int i = idx; i < s.length(); i++) {
                    sb.append(s.charAt(i));
                    if(wordDictSet.contains(sb.toString()) && cache[i+1]) {
                        cache[idx] = true;
                        break;
                    }
                }
            }

            return cache[0];
        }

        public boolean wordBreak5(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            int size = s.length();
            Boolean[] cache = new Boolean[size+1];
            cache[size] = true;
            Set<String> wordDictSet = new HashSet<>(wordDict);

            for(int idx = size-1; idx >= 0; idx--) {
                StringBuilder sb = new StringBuilder();
                for(int i = idx; i < s.length(); i++) {
                    sb.append(s.charAt(i));
                    if(wordDictSet.contains(sb.toString()) && cache[i+1]) {
                        cache[idx] = true;
                        break;
                    }
                }

                if(cache[idx] == null) {
                    cache[idx] = false;
                }
            }

            return cache[0];
        }

        /*
        Optimize recursion with memoization using string builder instead of getting substring and checking inside the hashset
        data structure
         */
        private boolean helper2(int idx, String s, Set<String> wordDictSet, Boolean[] cache) {
            if(idx >= s.length()) {
                return true;
            } else if(cache[idx] != null) {
                return cache[idx];
            }

            StringBuilder sb = new StringBuilder();
            for(int i = idx; i < s.length(); i++) {
                sb.append(s.charAt(i));
                if(wordDictSet.contains(sb.toString()) && helper2(i+1, s, wordDictSet, cache)) {
                    return cache[idx] = true;
                }
            }

            return cache[idx] = false;
        }

        public boolean wordBreak4(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            int size = s.length();
            Boolean[] cache = new Boolean[size];
            Set<String> wordDictSet = new HashSet<>();

            return helper2(0, s, wordDictSet, cache);
        }

        public boolean wordBreak2(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            Set<String> wordDictSet = new HashSet<>(wordDict);

            int size = s.length();
            Boolean[] cache = new Boolean[size+1];
            cache[size] = true;

            for(int idx = size-1; idx >= 0; idx--) {
                for(int i = idx; i < s.length(); i++) {
                    if(wordDictSet.contains(s.substring(idx, i+1)) && cache[i+1]) {
                        cache[idx] = true;
                        break;
                    }
                }

                if(cache[idx] == null) {
                    cache[idx] = false;
                }
            }

            return cache[0];
        }

        private boolean helper1(int idx, String s, Set<String> wordDictSet, Boolean[] cache) {
            if(idx >= s.length()) {
                return true;
            } else if(cache[idx] != null) {
                return cache[idx];
            }

            for(int i = idx; i < s.length(); i++) {
                if(wordDictSet.contains(s.substring(idx, i+1))) {
                    if(helper1(i+1, s, wordDictSet, cache)) {
                        return cache[idx] = true;
                    }
                }
            }

            return cache[idx] = false;
        }

        public boolean wordBreak1(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            Set<String> wordDictSet = new HashSet<>(wordDict);

            int size = s.length();
            Boolean[] cache = new Boolean[size];

            return helper1(0, s, wordDictSet, cache);
        }


        private boolean helper0(int idx, String s, Set<String> wordDictSet) {
            if(idx >= s.length()) {
                return true;
            }

            for(int i = idx; i < s.length(); i++) {
                if(wordDictSet.contains(s.substring(idx, i+1))) {
                    if(helper0(i+1, s, wordDictSet)) {
                        return true;
                    }
                }
            }

            return false;
        }

        public boolean wordBreak0(String s, List<String> wordDict) {
            if(s == null || s.isEmpty()) {
                return true;
            }

            Set<String> wordDictSet = new HashSet<>(wordDict);

            int size = s.length();
            return helper0(0, s, wordDictSet);
        }
    }
}
