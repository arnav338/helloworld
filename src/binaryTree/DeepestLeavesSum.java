package binaryTree;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class DeepestLeavesSum {
	
	public static void main(String[] args) {
		BinaryTree bt = new StandardTree().new BinaryTree(1);
		bt.root.left = new StandardTree().new Node(2);
		bt.root.right = new StandardTree().new Node(3);
		
		bt.root.left.left = new StandardTree().new Node(4);
		bt.root.left.right = new StandardTree().new Node(5);
		
		bt.root.left.left.left = new StandardTree().new Node(7);
		
		bt.root.left.right.right = new StandardTree().new Node(9);
		
		bt.root.right.right = new StandardTree().new Node(6);
		bt.root.right.right.right = new StandardTree().new Node(8);
		
		/*
		 * 					 1
		 * 				   /	 \
		 * 				 2			3
		 * 				 /\ 	  	  \
		 * 			   4	5		  6
		 * 				/  	\		   \ 
		 * 			  7      9          8
		 * 				     
		 * 					  
		 * Deepest leaves sum = 11
		 * 
		 * */
		
		deepestLeaves(bt.root.left, 0);
		int left = result;
		result = 0;
		System.out.println("+++++");
		deepestLeaves(bt.root.right, 0);
		System.out.println("->"+(result+left));
	}
	
	public static int maxHieght = 0;
	public static int result = 0;
	public static int temp = 0;
	
	private static void deepestLeaves(Node root, int level) {
		if(root==null) return;
		
		if(root.left == null && root.right==null) { // condition for leaf node
			if(level>maxHieght) { // if we have encountered a leaf node at a hieght which is more than prev, 
				// then we have to clear the result variable and add the newly found max height
				result = 0;
				result += root.data;
				maxHieght = level;
			}
			else if(level == maxHieght) {
				result += root.data;
			}
		}
		
		deepestLeaves(root.left, level+1);
		deepestLeaves(root.right, level+1);
	}
	
	/*
	private static void deepestLeaves(Node root, HashMap<Integer, Integer> left, HashMap<Integer, Integer> right, int i,int level) {
		if(root==null) return;
		if(root.left==null && root.right==null) {
				if(i==0) {
					if(left.containsKey(level)) {
							left.put(level, Math.max(root.data, left.get(level)));
					}
					else {
						left.put(level, root.data);
					}
				}
				else {
					if(right.containsKey(level)) {
						right.put(level, Math.max(root.data, right.get(level)));
					}
					else {
						right.put(level, root.data);
					}
				}
		}
		deepestLeaves(root.left, left, right, 0,level+1);
		deepestLeaves(root.right, left, right, 1,level+1);
	}
	*/
}
