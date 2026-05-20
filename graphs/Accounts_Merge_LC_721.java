package neetcode.graphs;

import java.util.*;

/*
https://leetcode.com/problems/accounts-merge/description/
 */

public class Accounts_Merge_LC_721 {
    class Revision01 {
        class Solution {
            class DisjointSet {
                private int[] parent, size;

                DisjointSet(int n) {
                    parent = new int[n];
                    size = new int[n];

                    for(int i = 0; i < n; i++) {
                        parent[i] = i;
                        size[i] = 1;
                    }
                }

                public int findUltimateParent(int node) {
                    if(parent[node] == node) {
                        return node;
                    }

                    return parent[node] = findUltimateParent(parent[node]);
                }

                public void unionBySize(int u, int v) {
                    int ulpu = findUltimateParent(u), ulpv = findUltimateParent(v);

                    if(ulpu == ulpv) {
                        return;
                    }

                    if(size[ulpu] < size[ulpv]) {
                        parent[ulpu] = ulpv;
                        size[ulpv] += size[ulpu];
                    } else {
                        parent[ulpv] = ulpu;
                        size[ulpu] += size[ulpv];
                    }
                }
            }

            public List<List<String>> accountsMerge(List<List<String>> accounts) {
                if(accounts == null || accounts.isEmpty()) {
                    return new ArrayList<>();
                }

                int size = accounts.size();
                DisjointSet ds = new DisjointSet(size);
                Map<String, Integer> nodeToIdxMap = new HashMap<>();

                for(int i = 0; i < size; i++) {
                    for(int j = 1; j < accounts.get(i).size(); j++) {
                        String email = accounts.get(i).get(j);

                        if(nodeToIdxMap.containsKey(email)) {
                            int u = nodeToIdxMap.get(email);
                            ds.unionBySize(u, i);
                        } else {
                            nodeToIdxMap.put(email, i);
                        }
                    }
                }

                List<List<String>> mergedEmail = new ArrayList<>();

                for(int i = 0; i < size; i++) {
                    mergedEmail.add(new ArrayList<>());
                }

                for(Map.Entry<String, Integer> entry: nodeToIdxMap.entrySet()) {
                    String email = entry.getKey();
                    int nodeId = entry.getValue();

                    int ulParentNode = ds.findUltimateParent(nodeId);
                    mergedEmail.get(ulParentNode).add(email);
                }

                List<List<String>> result = new ArrayList<>();

                for(int i = 0; i < size; i++) {
                    List<String> currAccounts = mergedEmail.get(i);
                    if(currAccounts.isEmpty()) continue;

                    Collections.sort(currAccounts);
                    String name = accounts.get(i).get(0);
                    List<String> temp = new ArrayList<>();
                    temp.add(name);
                    temp.addAll(currAccounts);

                    result.add(temp);
                }

                return result;
            }
        }
    }

    class DisjointSet {
        private int[] parent, size;

        DisjointSet(int n) {
            this.parent = new int[n];
            this.size = new int[n];

            for(int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int findUltimateParent(int node) {
            if(node == parent[node]) {
                return node;
            }

            return parent[node] = findUltimateParent(parent[node]);
        }

        public boolean unionBySize(int u, int v) {
            int ulpU = findUltimateParent(u), ulpV = findUltimateParent(v);

            if(ulpU == ulpV) {
                return false;
            }

            if(size[ulpU] < size[ulpV]) {
                parent[ulpU] = ulpV;
                size[ulpV] += size[ulpU];
            } else {
                parent[ulpV] = ulpU;
                size[ulpU] += size[ulpV];
            }
            return true;
        }
    }

    class Solution {
        public List<List<String>> accountsMerge(List<List<String>> accounts) {
            if(accounts == null || accounts.isEmpty()) {
                return new ArrayList<>();
            }

            int size = accounts.size();
            DisjointSet ds = new DisjointSet(size);
            Map<String, Integer> emailToNodeMap = new HashMap<>();

            for(int node = 0; node < size; node++) {
                for(int j = 1; j < accounts.get(node).size(); j++) {
                    String email = accounts.get(node).get(j);
                    if(emailToNodeMap.containsKey(email)) {
                        int neighNode = emailToNodeMap.get(email);
                        ds.unionBySize(neighNode, node);
                    } else {
                        emailToNodeMap.put(email, node);
                    }
                }
            }

            List<List<String>> mergedEmail = new ArrayList<>();

            for(int i = 0; i < size; i++) {
                mergedEmail.add(new ArrayList<>());
            }

            for(Map.Entry<String, Integer> entry: emailToNodeMap.entrySet()) {
                String email = entry.getKey();
                int nodeId = entry.getValue();

                int ultimateParentNode = ds.findUltimateParent(nodeId);
                mergedEmail.get(ultimateParentNode).add(email);
            }

            List<List<String>> result = new ArrayList<>();
            for(int i =0; i < size; i++) {
                List<String> currAccounts = mergedEmail.get(i);
                if(currAccounts.isEmpty()) {
                    continue;
                }

                Collections.sort(currAccounts);

                String name = accounts.get(i).get(0);
                List<String> temp = new ArrayList<>();
                temp.add(name);
                temp.addAll(currAccounts);

                result.add(temp);
            }

            return result;
        }
    }
}
