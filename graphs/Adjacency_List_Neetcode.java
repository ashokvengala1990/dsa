package neetcode.graphs;

import java.util.*;

public class Adjacency_List_Neetcode {
    static class Basic {
        class GraphNode {
            int val;
            List<GraphNode> neighbors;

            GraphNode(int val) {
                this.val = val;
                this.neighbors = new ArrayList<>();
            }
        }

        public Map<String, List<String>> setup() {
            Map<String, List<String>> adjList = new HashMap<>();
            // Given directed edges, build an adjacency list
            String[][] edges = {{"A","B"},{"B","C"},{"B","E"},{"C","E"},{"E","D"}};

            for(String[] edge: edges) {
                String src = edge[0], dst = edge[1];

                // If the current source node does not exist, add it to the hashMap
                if(!adjList.containsKey(src)) {
                    adjList.put(src, new ArrayList<>());
                }

                // If the current destination node does not exist, add it to the hashMap
                if(!adjList.containsKey(dst)) {
                    adjList.put(dst, new ArrayList<>());
                }

                // Retrieve the key (souce) and add the destination to it's list of neighbors.
                adjList.get(src).add(dst);
            }

            return adjList;
        }

        public int bfs(String node, String target, Map<String, List<String>> adjList) {
            Set<String> visited = new HashSet<>();
            Queue<String> queue = new LinkedList<>();
            queue.offer(node);
            visited.add(node);
            int length = 0;

            while (!queue.isEmpty()) {
                int queueLength = queue.size();

                for(int i = 0; i < queueLength; i++) {
                    String currNode = queue.poll();
                    if(currNode.equals(target)) {
                        return length;
                    }

                    for(String neighbor: adjList.get(currNode)) {
                        if(!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.offer(neighbor);
                        }
                    }
                }

                length++;
            }

            return -1;
        }

        public int dfs(String node, String target, Map<String, List<String>> adjList, Set<String> visited) {
            if(visited.contains(node)) {
                return 0;
            } else if(node.equals(target)) {
                return 1;
            }

            int count = 0;
            visited.add(node);

            for(String neighbor: adjList.get(node)) {
                dfs(neighbor, target, adjList, visited);
            }

            visited.remove(node);
            return count;
        }
    }

    public static void main(String[] args) {
        Basic basic = new Basic();
        Map<String, List<String>> adjListMap = basic.setup();

        System.out.println(basic.dfs("A","E", adjListMap, new HashSet<>()));
    }
}
