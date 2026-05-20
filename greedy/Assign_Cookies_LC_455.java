package neetcode.greedy;

import java.util.Arrays;

public class Assign_Cookies_LC_455 {
    class Solution {
        public int findContentChildren(int[] g, int[] s) {
            int n = g.length, m = s.length, l = 0, r = 0;
            Arrays.sort(g);
            Arrays.sort(s);

            while (l < n && r < m) {
                if(g[l] <= s[r]) {
                    l++;
                }

                r++;
            }

            return l;
        }
    }
}
