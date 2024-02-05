package binaryTree;

import java.util.HashMap;
import java.util.Map;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class BurnATree {
	
	static int result=0;
	static Map<Integer, Integer> map = new HashMap<Integer, Integer>();

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
		 * 5 sec
		 * 
		 * Approach : 
		 * 
		 * At any point of time, we will try to find the location of the source/target leaf (leaf to be burnt)
		 * 1. Either the leaf will be in the left sub tree or in the right
		 *    if its in left ->  find distance of the leaf from current node in left tree and height of right sub tree
		 *    if its in right ->  find distance of the leaf from current node in right tree and height of left sub tree
		 * 2. take max of the 2 and add up through recursion   
		 * */
		int source = 11;
		burn(bt.root,source);
		System.out.println("->->"+result);
	}
	private static void burn(Node root, int source) {
		height(root);
		traverse(root,source);
	}
	private static int traverse(Node root, int source) { // returns distance of root from source
		if(root==null) return 0;
		if(root.data==source) {
			return 1;
		}
		int val = traverse(root.left, source);
		if(val!=0) {
			if(root.right!=null) {
				result = Math.max(result, val+map.get(root.right.data));
			}
			return val+1;
		}
		val = traverse(root.right, source);
		if(val!=0) {
			if(root.left!=null) {
				result = Math.max(result, val+map.get(root.left.data));
			}
			return val+1;
		}
		return 0;
	}
	private static int height(Node root) {
		if(root==null) return 0;
		int left = height(root.left);
		int right = height(root.right);
		map.put(root.data, 1 + Math.max(left, right));
		return 1 + Math.max(left, right);
	}
}
