package neetcode.dynamic_programming;

import java.util.ArrayList;
import java.util.*;
import java.util.stream.Stream;

/*

https://www.jointaro.com/interviews/amazon/
https://www.jointaro.com/interviews/amazon/palindrome-pairs/
https://leetcode.com/problems/palindrome-pairs/description/

TODO: Come back to this problem
Need to solve in Optimal way.

 */

public class Palindrome_Pairs_LC_336 {

    class Solution01 {
        private boolean isPalindrome(String s) {
            int left = 0, right = s.length() - 1;
            while (left < right) {
                if (s.charAt(left++) != s.charAt(right--)) {
                    return false;
                }
            }
            return true;
        }

        public List<List<Integer>> palindromePairs(String[] words) {
            Map<String, Integer> wordMap = new HashMap<>();
            List<List<Integer>> result = new ArrayList<>();

            // Step 1: Map each word to its index
            for (int i = 0; i < words.length; i++) {
                wordMap.put(words[i], i);
            }

            // Step 2: Iterate through all words
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                int n = word.length();

                // Step 3: Try all possible splits
                for (int j = 0; j <= n; j++) {
                    String prefix = word.substring(0, j);
                    String suffix = word.substring(j);

                    // Case 1: suffix is palindrome, check for reversed prefix
                    if (isPalindrome(suffix)) {
                        String reversedPrefix = new StringBuilder(prefix).reverse().toString();
                        if (wordMap.containsKey(reversedPrefix) && wordMap.get(reversedPrefix) != i) {
                            result.add(Arrays.asList(i, wordMap.get(reversedPrefix)));
                        }
                    }

                    // Case 2: prefix is palindrome (j > 0 to avoid duplicates), check for reversed suffix
                    if (j > 0 && isPalindrome(prefix)) {
                        String reversedSuffix = new StringBuilder(suffix).reverse().toString();
                        if (wordMap.containsKey(reversedSuffix) && wordMap.get(reversedSuffix) != i) {
                            result.add(Arrays.asList(wordMap.get(reversedSuffix), i));
                        }
                    }
                }
            }

            return result;
        }
    }


    class Solution {
        private boolean isPalindrome(String word1, String word2) {
            String newWord1 = word1 + word2;
            int left = 0, right = newWord1.length()-1;

            while (left < right) {
                if(newWord1.charAt(left) != newWord1.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }

        public List<List<Integer>> palindromePairs0(String[] words) {
            List<List<Integer>> result = new ArrayList<>();
            if(words == null || words.length == 0) {
                return result;
            }

            int size = words.length;

            for(int i = 0; i < size; i++) {
                String word1 = words[i];
                for(int j = i+1; j < size; j++) {
                    String word2 = words[j];

                    if(isPalindrome(word1, word2)) {
                        result.add(Stream.of(i, j).toList());
                    }
                    if(isPalindrome(word2, word1)) {
                        result.add(Stream.of(j, i).toList());
                    }
                }
            }

            return result;
        }
    }
}
