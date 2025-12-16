package neetcode.graphs;

import java.util.*;

public class Count_Unique_Paths_Adjacency_List_Concept_DFS_BFS {
    public void buildAdjacencyListFromDirectedEdges() {
        // Given a directed edges, build an adjacency list
        String[][] edges = {{"A","B"},{"B","C"},{"B","E"},{"C","E"},{"E","D"}};
        Map<String, List<String>> adjList = new HashMap<>();

        for(String[] edge: edges) {
            String src = edge[0], dst = edge[1];

            // If the current source does not exist, add it to the hashmap
            if(!adjList.containsKey(src)) {
                adjList.put(src, new ArrayList<>());
            }

            // If the current destination does not exist, add it to the hashmap
            if(!adjList.containsKey(dst)) {
                adjList.put(dst, new ArrayList<>());
            }

            // Retrieve the key (source) and add the destination to its list of neighbors
            adjList.get(src).add(dst);
        }
    }

    /*
     - Count the number of paths that lead from a source to destination
     - This backtracking is exponential. In the worst case, each node is connected to every other
       node in the graph. Recall the rule that E <= V^2. So, let us say that each vertex has N edges.
       If we are to create a decision tree which determines how many vertices can be visited from
       each vertex, and the height of that tree is V, then in the worst case, we will have to do N^v
       work for reasons similar to what we discussed in the matrix chapter.

       In worst case scenario, N is equal to V, so the time complexity is O(V^V)
     */
    public int dfs(String node, String target, Map<String, List<String>> adjList, Set<String> visited) {
        if(visited.contains(target)) {
            return 0;
        } else if(node == target) {
            return 1;
        }

        int count = 0;
        visited.add(node);

        for(String neighbor: adjList.get(node)) {
            count += dfs(neighbor, target, adjList, visited);
        }

        visited.remove(node);

        return count;
    }

    /*
    - Find the shortest path from source to target. By shortest path, we mean reaching the destination
    by visiting the fewest vertices possible.

    - We learned before that the number of edges in a graph is upper bounded by V^2. However, we know that
      in the worst case, we don't have self loops and we don't have the maximal number of edges. Therefore
      , we can say that the time complexity is O(V + E), where V is the number of vertices and E is the number
      of edges. This is because in the worst case, our BFS will visit every vertex and traverse every edge.
     */
    public int bfs(String source, String target, Map<String, List<String>> adjList) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(source);
        visited.add(source);
        int length = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for(int i = 0; i < levelSize; i++) {
                String node = queue.poll();
                if(node == target) {
                    return length;
                }

                for(String neighbor: adjList.get(node)) {
                    if(!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }

            length++;
        }

        return length;
    }
}
