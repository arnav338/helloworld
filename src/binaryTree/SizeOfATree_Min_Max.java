package binaryTree;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class SizeOfATree_Min_Max {
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
		System.out.println("Height is : "+sizeOfTree(bt.root));
		System.out.println("Max in tree is : "+maxOfTree(bt.root));
		System.out.println("Min in tree is : "+minOfTree(bt.root));
	}

	private static int maxOfTree(Node root) {
		if(root==null) return 0;
		return Math.max(root.data, Math.max(maxOfTree(root.left), maxOfTree(root.right)));
	}
	
	private static int minOfTree(Node root) {
		if(root==null) return Integer.MAX_VALUE;
		return Math.min(root.data, Math.min(minOfTree(root.left), minOfTree(root.right)));
	}

	private static int sizeOfTree(Node root) {
		if(root==null) return 0;
		return sizeOfTree(root.left)+sizeOfTree(root.right)+1;
	}
}
