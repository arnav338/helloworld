package binaryTree;

import java.util.ArrayList;
import java.util.List;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class LowestCommonAncestor {
	
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
		 * Ancestors for 17 -> 17, 24 , 12, 10
		 * Ancestors for 11 -> 11, 24 , 12, 10
		 * Lowest common ancestor for (17 and 11 ) -> 10
		 * */
		Node n = lowestCommonAncestor(bt.root,15,11);
		System.out.println(n.data);
	}

	private static Node lowestCommonAncestor(Node root, int i, int j) {
		if(root==null) return null;
		if(root.data == i || root.data == j) return root;
		Node left = lowestCommonAncestor(root.left, i, j);
		Node right = lowestCommonAncestor(root.right, i, j);
		if(left==null) return right;
		if(right==null) return left;
		return root;
	}
	
}
