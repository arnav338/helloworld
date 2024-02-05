package binaryTree;

import java.util.HashMap;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class MaxSumOfRootToLeafPaths {
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
		 * 
		 * */
		System.out.println(findMaxPathSum(bt.root));
	}

	private static int findMaxPathSum(Node root) {
		if(root==null) return Integer.MIN_VALUE;
		if(root.left==null && root.right ==null) return root.data;
		int data = Math.max(findMaxPathSum(root.left), findMaxPathSum(root.right));
		System.out.println("max for "+root.data+" : "+data);
		return root.data + data;
	}
}
