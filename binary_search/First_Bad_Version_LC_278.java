package binary_search.custom.binary_search;

/*
https://leetcode.com/problems/first-bad-version/description/
 */

public class First_Bad_Version_LC_278 {
    class VersionControl {
        public boolean isBadVersion(int version) {
            return true;
        }
    }
    class Solution2 extends VersionControl {
        public int firstBadVersion(int n) {
            int left = 1, right = n;

            while (left + 1 < right) {
                int mid = left + ((right-left)/2);

                if(isBadVersion(mid)) {
                    right = mid;
                } else {
                    left = mid;
                }
            }

            if(isBadVersion(left)) {
                return left;
            } else if(isBadVersion(right)) {
                return right;
            }
            return -1;
        }
    }

    class Solution1 extends VersionControl {
        public int firstBadVersion(int n) {
            int ans = helper(1, n);
            if (ans <= n && isBadVersion(ans)) {
                return ans;
            } else {
                return -1;
            }
        }

        private int helper(int left, int right) {
            if (left > right) return left;

            int mid = left + (right - left) / 2;

            if (isBadVersion(mid)) {
                return helper(left, mid - 1);  // search left half
            } else {
                return helper(mid + 1, right); // search right half
            }
        }
    }

    class Solution extends VersionControl {
        public int firstBadVersion(int n) {
            int left = 1, right = n;

            while (left <= right) {
                int mid = left + ((right-left)/2);

                if(isBadVersion(mid)) {
                    right = mid-1;
                } else {
                    left = mid+1;
                }
            }

            if(left <= n && isBadVersion(left)) {
                return left;
            } else {
                return left;
            }
        }
    }
}
