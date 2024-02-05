package binaryTree;

import binaryTree.StandardTree.Node;

public class Vaccine {
	/*
	 * Geek has developed an effective vaccine for Corona virus and he wants each of the N houses in 
	 * Geek Land to have access to it. Given a binary tree where each node represents a house in Geek Land,
	 *  find the minimum number of houses that should be supplied with the vaccine kit 
	 *  if one vaccine kit is sufficient for that house, its parent house and it's immediate child nodes.  
			Example 1:
			
			Input:
			    1
			   / \
			  2   3 
			        \
			         4
			          \
			           5
			            \
			             6
			
			Output:
			2
			Explanation:
			The vaccine kits should be 
			supplied to house numbers 1 and 5.
			
	The idea is to have pre defined conditions relating to if a particular node needs vaccine or not
			1 -> vaccine needed
			-1 -> not needed // already provided
			0 -> not applicable
	 * */
	public static void main(String[] args) {
		binaryTree.StandardTree.BinaryTree bt = new StandardTree().new BinaryTree(1);
		bt.root.left = new StandardTree().new Node(2);
		bt.root.right = new StandardTree().new Node(3);
		
		bt.root.right.right = new StandardTree().new Node(4);
		
		bt.root.right.right = new StandardTree().new Node(5);
		
		bt.root.right.right = new StandardTree().new Node(6);
		if(vaccineCount(bt.root) == 1) {
			vaccine++;
		}
		System.out.println(vaccine+" vaccines needed");
	}
	static int vaccine = 0;
	private static int vaccineCount(Node root) {
		if(root == null) return 0;
		
		int left = vaccineCount(root.left);
		int right = vaccineCount(root.right);
		if(left == 1 || right == 1) {// since one of the child needs vaccine
			vaccine++; // we provide vaccinated to child
			System.out.println("returning -1 for "+root.data);
			return -1; // if child are vaccinated parent doesnt need vaccine
		}
		if(left == -1 || right == -1) {// if children have been provided vaccine
			System.out.println("returning 0 for = "+root.data);
			return 0; // no need to provide vaccine to parent
		}
		System.out.println("returning 1 for "+root.data);
		return 1;
	}
	
}
