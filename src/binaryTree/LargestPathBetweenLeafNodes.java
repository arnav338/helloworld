package binaryTree;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class LargestPathBetweenLeafNodes {
	
	private static int result=0;

	public static void main(String[] args) {
		// Diameter of tree
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
		calculateDiameter(bt.root);
		System.out.println(result);
	}

	private static int calculateDiameter(Node root) {
		if(root==null) return 0;
		int leftHeight = calculateDiameter(root.left);
		int rightHeight = calculateDiameter(root.right);
		result = Math.max(result, 1+leftHeight+rightHeight);
		return 1 + Math.max(leftHeight, rightHeight);
	}
}
