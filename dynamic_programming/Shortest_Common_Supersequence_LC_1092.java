package neetcode.dynamic_programming;

import java.util.HashMap;
import java.util.Map;

public class Shortest_Common_Supersequence_LC_1092 {
    class Solution {
        public String shortestCommonSupersequence2(String str1, String str2) {
            if(str1 == null || str1.isEmpty()) {
                return str2;
            } else if(str2 == null || str2.isEmpty()) {
                return str1;
            }

            int size1 = str1.length(), size2 = str2.length();
            Map<String, String> cache = new HashMap<>();
            for(int idx1=0; idx1 <= size1; idx1++) {
                cache.put(idx1+"-"+size2, str1.substring(idx1));
            }

            for(int idx2 = 0; idx2 <= size2; idx2++) {
                cache.put(size1+"-"+idx2, str2.substring(idx2));
            }

            cache.put(size1+"-"+size2, "");

            for(int idx1 = size1-1; idx1 >= 0; idx1--) {
                for(int idx2 = size2-1; idx2 >= 0; idx2--) {
                    String key = idx1 +"-"+idx2;

                    String shortStr = "";
                    if(str1.charAt(idx1) == str2.charAt(idx2)) {
                        shortStr = str1.charAt(idx1) + cache.get((idx1+1)+"-"+(idx2+1));
                    } else {
                        String s1 = str1.charAt(idx1) + cache.get((idx1+1)+"-"+idx2);
                        String s2 = str2.charAt(idx2) + cache.get(idx1+"-"+ (idx2+1));
                        if(s1.length() < s2.length()) {
                            shortStr = s1;
                        } else {
                            shortStr = s2;
                        }
                    }

                    cache.put(key, shortStr);
                }
            }

            return cache.get(0+"-"+0);
        }

        private String helper1(int idx1, int idx2, String str1, String str2, int size1,  int size2, Map<String, String> cache) {
            if(idx1 == size1 && idx2 == size2) {
                return "";
            } else if(idx1 == size1) {
                return str2.substring(idx2);
            } else if(idx2 == size2) {
                return str1.substring(idx1);
            }
            String key = idx1 +"-"+idx2;
            if(cache.containsKey(key)) {
                return cache.get(key);
            }

            String shortStr = "";
            if(str1.charAt(idx1) == str2.charAt(idx2)) {
                shortStr = str1.charAt(idx1) + helper1(idx1+1, idx2+1, str1, str2, size1, size2, cache);
            } else {
                String s1 = str1.charAt(idx1) + helper1(idx1+1, idx2, str1, str2, size1, size2, cache);
                String s2 = str2.charAt(idx2) + helper1(idx1, idx2+1, str1, str2, size1, size2, cache);
                if(s1.length() < s2.length()) {
                    shortStr = s1;
                } else {
                    shortStr = s2;
                }
            }

            cache.put(key, shortStr);
            return shortStr;
        }

        public String shortestCommonSupersequence1(String str1, String str2) {
            if(str1 == null || str1.isEmpty()) {
                return str2;
            } else if(str2 == null || str2.isEmpty()) {
                return str1;
            }

            int size1 = str1.length(), size2 = str2.length();
            Map<String, String> cache = new HashMap<>();

            return helper1(0, 0, str1, str2, size1, size2, cache);
        }

        private String helper0(int idx1, int idx2, String str1, String str2, int size1,  int size2) {
            if(idx1 == size1 && idx2 == size2) {
                return "";
            } else if(idx1 == size1) {
                return str2.substring(idx2);
            } else if(idx2 == size2) {
                return str1.substring(idx1);
            }

            if(str1.charAt(idx1) == str2.charAt(idx2)) {
                return str1.charAt(idx1) + helper0(idx1+1, idx2+1, str1, str2, size1, size2);
            } else {
                String s1 = str1.charAt(idx1) + helper0(idx1+1, idx2, str1, str2, size1, size2);
                String s2 = str2.charAt(idx2) + helper0(idx1, idx2+1, str1, str2, size1, size2);
                if(s1.length() < s2.length()) {
                    return s1;
                } else {
                    return s2;
                }
            }
        }

        public String shortestCommonSupersequence0(String str1, String str2) {
            if(str1 == null || str1.isEmpty()) {
                return str2;
            } else if(str2 == null || str2.isEmpty()) {
                return str1;
            }

            int size1 = str1.length(), size2 = str2.length();

            return helper0(0, 0, str1, str2, size1, size2);
        }
    }
}
