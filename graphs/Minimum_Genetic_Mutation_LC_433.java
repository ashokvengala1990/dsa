package neetcode.graphs;

import java.util.*;
import java.util.stream.Collectors;

/*
🧠 Notes
Aspect	minMutation1 (Graph Build)	minMutation0 (On-the-fly BFS)
Graph Build	Explicitly builds all edges by comparing all pairs in bank.	No explicit graph — generates neighbors dynamically.
Preprocessing Time	O(n² × L) to build adjacency list.	O(1) preprocessing — direct lookup in bankSet.
BFS Traversal Time	O(V + E) but E can be O(n²).	O(4 × L × n) (for each string, try all positions and 4 mutations).
Space Complexity	O(n²) for adjacency list + O(n) for visited.	O(n) for bankSet + O(n) for visited.
Overall	Less optimal for large bank.	✅ More optimal and simpler — recommended.
✅ Final Recommendation

Use minMutation0 — it’s the cleaner and optimal BFS solution for LeetCode 433.

minMutation1 is fine conceptually but inefficient because it builds a full adjacency list via all pair comparisons.

 */

public class Minimum_Genetic_Mutation_LC_433 {
    class Solution {
        // ✅ Helper function: Checks if two gene strings differ by exactly one character.
        private boolean isSingleMutation(String node1, String node2) {
            if (node1.length() != node2.length()) {
                return false;
            }

            int n = node1.length(), cnt = 0;
            for (int i = 0; i < n; i++) {
                if (node1.charAt(i) != node2.charAt(i)) {
                    cnt++;
                }
            }

            // Valid mutation only if exactly one character differs.
            return cnt == 1;
        }

        // ✅ BFS Approach (Adjacency List Built by Comparing All Pairs)
        public int minMutation2(String startGene, String endGene, String[] bank) {
            if (bank == null || bank.length == 0) {
                return -1;
            }

            // Step 1: Build adjacency list (graph)
            Map<String, List<String>> adjMap = new HashMap<>();
            int n = bank.length;

            // Compare every pair of genes in the bank and connect if one mutation apart
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (isSingleMutation(bank[i], bank[j])) {
                        adjMap.computeIfAbsent(bank[i], k -> new ArrayList<>()).add(bank[j]);
                        adjMap.computeIfAbsent(bank[j], k -> new ArrayList<>()).add(bank[i]);
                    }
                }
            }

            // Also connect startGene with all 1-step mutations from it in bank
            for (int i = 0; i < n; i++) {
                if (isSingleMutation(bank[i], startGene)) {
                    adjMap.computeIfAbsent(bank[i], k -> new ArrayList<>()).add(startGene);
                    adjMap.computeIfAbsent(startGene, k -> new ArrayList<>()).add(bank[i]);
                }
            }

            // Step 2: BFS from startGene
            Queue<String> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();
            queue.offer(startGene);
            visited.add(startGene);
            int steps = 0;

            while (!queue.isEmpty()) {
                int levelSize = queue.size();

                for (int i = 0; i < levelSize; i++) {
                    String currNode = queue.poll();

                    // Found the target gene
                    if (currNode.equals(endGene)) {
                        return steps;
                    }

                    // Traverse neighbors (connected 1-mutation genes)
                    for (String neighbor : adjMap.getOrDefault(currNode, new ArrayList<>())) {
                        if (!visited.contains(neighbor)) {
                            queue.offer(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }

                // Increment mutation step after exploring one level
                steps++;
            }

            // If BFS completes with no path found
            return -1;
        }

        public int minMutation1(String startGene, String endGene, String[] bank) {
            if (bank == null || bank.length == 0) {
                return -1;
            }

            Queue<String> queue = new LinkedList<>();
            Set<String> bankSet = Arrays.stream(bank).collect(Collectors.toSet()); // for O(1) lookups

            queue.offer(startGene);
            int steps = 0;

            // BFS traversal
            while (!queue.isEmpty()) {
                int levelSize = queue.size();

                for (int i = 0; i < levelSize; i++) {
                    String currNode = queue.poll();

                    // Found end gene
                    if (currNode.equals(endGene)) {
                        return steps;
                    }

                    // Try mutating each position to A, C, G, T
                    for (char c : new char[]{'A', 'C', 'G', 'T'}) {
                        for (int j = 0; j < currNode.length(); j++) {
                            // Create a possible mutated gene
                            String neighbor = currNode.substring(0, j) + c + currNode.substring(j + 1);

                            // If valid mutation in bank and not yet visited
                            if (bankSet.contains(neighbor)) {
                                queue.offer(neighbor);
                                bankSet.remove(neighbor);
                            }
                        }
                    }
                }

                steps++;
            }

            // Not reachable
            return -1;
        }

        /**
         * ✅ More optimal BFS version:
         * Instead of pre-building a full adjacency list (O(n^2) comparisons),
         * we generate all possible 1-character mutations on the fly.
         */
        public int minMutation0(String startGene, String endGene, String[] bank) {
            if (bank == null || bank.length == 0) {
                return -1;
            }

            Queue<String> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();
            Set<String> bankSet = Arrays.stream(bank).collect(Collectors.toSet()); // for O(1) lookups

            queue.offer(startGene);
            visited.add(startGene);
            int steps = 0;

            // BFS traversal
            while (!queue.isEmpty()) {
                int levelSize = queue.size();

                for (int i = 0; i < levelSize; i++) {
                    String currNode = queue.poll();

                    // Found end gene
                    if (currNode.equals(endGene)) {
                        return steps;
                    }

                    // Try mutating each position to A, C, G, T
                    for (char c : new char[]{'A', 'C', 'G', 'T'}) {
                        for (int j = 0; j < currNode.length(); j++) {
                            // Create a possible mutated gene
                            String neighbor = currNode.substring(0, j) + c + currNode.substring(j + 1);

                            // If valid mutation in bank and not yet visited
                            if (!visited.contains(neighbor) && bankSet.contains(neighbor)) {
                                queue.offer(neighbor);
                                visited.add(neighbor);
                            }
                        }
                    }
                }

                steps++;
            }

            // Not reachable
            return -1;
        }
    }
}
