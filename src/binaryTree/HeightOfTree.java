package binaryTree;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class HeightOfTree {
	
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
		System.out.println("Height is : "+calculateHeight(bt.root));
	}

	private static int calculateHeight(Node root) {
		if(root == null) {
			return 0;
		}
		return 1 + Math.max(calculateHeight(root.left), calculateHeight(root.right) );
	}
}
