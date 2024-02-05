package binaryTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class BFS_DFS {

	public static void main(String[] args) {
		BinaryTree bt = new StandardTree().new BinaryTree(10);
		bt.root.left = new StandardTree().new Node(12);
		bt.root.right = new StandardTree().new Node(2);
		
		bt.root.left.left = new StandardTree().new Node(24);
		
		bt.root.left.left.left = new StandardTree().new Node(17);
		bt.root.left.left.right = new StandardTree().new Node(11);
		
		bt.root.left.left.right.right = new StandardTree().new Node(19);
		
		bt.root.right.left = new StandardTree().new Node(15);
		bt.root.right.right = new StandardTree().new Node(18);
		/*
		 * 					 10
		 * 				   /	 \
		 * 				 12			2
		 * 				 /		  /	  \
		 * 			   24		15	  18
		 * 				/ \ 
		 * 			  17  11
		 * 				   \ 
		 * 				   19
		 * */
		List<Integer> a = DFS(bt.root);
		a.stream().forEach(System.out::println);
		System.out.println("=========");
		List<Integer> b = new ArrayList<Integer>();
		DFSrecursion(bt.root,b);
		b.stream().forEach(System.out::println);
	}

	private static List<Integer> DFSrecursion(Node root, List<Integer> result) {
		if(root==null) return null;
		result.add(root.data);
		DFSrecursion(root.left, result);
		DFSrecursion(root.right, result);
		return result;
	}

	private static List<Integer> DFS(Node root) {
		List<Integer> result = new ArrayList<Integer>();
		Stack<Node> stack = new Stack<Node>();
		stack.add(root);
		DFSUtil(root,result,stack);
		return result;
	}

	private static void DFSUtil(Node root, List<Integer> result, Stack<Node> stack) {
		if(root==null || stack.isEmpty()) return;
		while(!stack.isEmpty()) {
			Node node = stack.pop();
			result.add(node.data);
			if(node.right!=null) {
				stack.add(node.right);
			}
			if(node.left!=null) {
				stack.add(node.left);
			}
		}
		return;
	}
}
