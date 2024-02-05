package binaryTree;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class MaxPathSum {
	public static void main(String[] args) {
		/*
		Given a binary tree, find the maximum path sum. The path may start and end at any node in the tree.
		Example: 

		Input: Root of below tree
		       1
		      / \
		     2   3
		Output: 6
		
		
		For each node there can be four ways that the max path goes through the node: 
			1. Node only 
			2. Max path through Left Child + Node 
			3. Max path through Right Child + Node 
			4. Max path through Left Child + Node + Max path through Right Child
			The idea is to keep trace of four paths and pick up the max one in the end. 
			An important thing to note is, root of every subtree need to return maximum path sum 
			such that at most one child of root is involved. 
			This is needed for parent function call. In below code, 
			this sum is stored in ‘max_single’ and returned by the recursive function.
		*/	
		BinaryTree bt = new StandardTree().new BinaryTree(10);
		bt.root.left = new StandardTree().new Node(2);
		bt.root.right = new StandardTree().new Node(10);
		
		bt.root.left.left = new StandardTree().new Node(20);
		bt.root.left.right = new StandardTree().new Node(1);
		
		bt.root.right.right = new StandardTree().new Node(-25);
		
		bt.root.right.right.left = new StandardTree().new Node(3);
		bt.root.right.right.right = new StandardTree().new Node(4);
		
		/*
		 * 					 10
		 * 				   /	 \
		 * 				 2			10
		 * 				 / \	     \
		 * 			   20  1	  	  -25
		 *  					      /  \ 
		 *  					     3   4 

		 * */
		findMaxSum(bt.root);
		System.out.println("Max sum : "+temp);
	}
	static int temp = 0;
	public static int findMaxSum(Node root) {
		if(root == null) return 0;
		int left = findMaxSum(root.left);
		int right = findMaxSum(root.right);
		// path sum including atmost 1 child
		// 2 comparisons have to be made in case if both child nodes have -ve values, 
		//in that case consider only the root node(if its positive)
		int max_path = Math.max(Math.max(left, right)+root.data, root.data);
		// path sum indicating that the current node will be the root for the final path
		int root_path = Math.max(max_path, left+right+root.data);
		temp = Math.max(root_path, temp);
		return max_path; 
		// we are returning max_path as the objective of this function is to return the 
		// path sum of this particular path (see the left and right calls to this function above)
	}
}
