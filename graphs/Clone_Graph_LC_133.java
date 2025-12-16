package neetcode.graphs;

import java.util.*;

/*
- https://leetcode.com/problems/clone-graph/description/

- Using DFS or Stack Data Structure and Hashmap Data structure
    * TC: O(
 */

public class Clone_Graph_LC_133 {
    class Node {
        int val;
        List<Node> neighbors;

        Node(){}

        Node(int _val) {
            this.val = _val;
            this.neighbors = new ArrayList<>();
        }
    }

    public Node cloneGraph1(Node node) {
        if(node == null) {
            return node;
        }

        Map<Node, Node> oldToNewNodeMap = new HashMap<>();
        Stack<Node> stack = new Stack<>();
        stack.push(node);
        Node newNodeHead = new Node(node.val);
        oldToNewNodeMap.put(node, newNodeHead);

        while (!stack.isEmpty()) {
            Node currNode = stack.pop();

            for(Node neighbor: currNode.neighbors) {
                if(!oldToNewNodeMap.containsKey(neighbor)) {
                    oldToNewNodeMap.put(neighbor, new Node(neighbor.val));
                    stack.push(neighbor);
                }

                oldToNewNodeMap.get(currNode).neighbors.add(oldToNewNodeMap.get(neighbor));
            }
        }

        return newNodeHead;
    }

    private Node dfs(Node node, Map<Node, Node> oldToNewNodeMap) {
        if(oldToNewNodeMap.containsKey(node)) {
            return oldToNewNodeMap.get(node);
        }

        Node newNode = new Node(node.val);
        oldToNewNodeMap.put(node, newNode);

        for(Node neighbor: node.neighbors) {
            newNode.neighbors.add(dfs(neighbor, oldToNewNodeMap));
        }

        return newNode;
    }

    public Node cloneGraph0(Node node) {
        if(node == null) {
            return node;
        }

        Map<Node, Node> oldToNewNodeMap = new HashMap<>();
        return dfs(node, oldToNewNodeMap);
    }
}
