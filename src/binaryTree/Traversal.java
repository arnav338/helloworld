package binaryTree;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class Traversal {

	public static void main(String[] args) {
		BinaryTree bt = new StandardTree().new BinaryTree(10);
		bt.root.left = new StandardTree().new Node(12);
		bt.root.right = new StandardTree().new Node(2);
		bt.root.left.left = new StandardTree().new Node(24);
		bt.root.left.right = new StandardTree().new Node(30);
		bt.root.right.left = new StandardTree().new Node(15);
		bt.root.right.right = new StandardTree().new Node(18);
		/*
		 * 					 10
		 * 				   /	 \
		 * 				 12			2
		 * 				 /	\	  /	  \
		 * 			   24	30	15	  18
		 * 
		 * */
		traverseInOrder(bt.root);
		System.out.println("++++++");
		traversePreOrder(bt.root);
		System.out.println("++++++");
		traversePostOrder(bt.root);
/*		
		BinaryTree bt1 = new StandardTree().new BinaryTree(3);
		bt1.root.left = new StandardTree().new Node(5);
		bt1.root.right = new StandardTree().new Node(2);
		
		bt1.root.right.left = new StandardTree().new Node(1);
		
		bt1.root.right.left.left = new StandardTree().new Node(4);
		bt1.root.right.left.right = new StandardTree().new Node(6);
		System.out.println("/////");
		traverseInOrder(bt1.root);*/
		System.out.println("=====");
		System.out.println(isPresent(bt.root, 2)>0 ? "true" : "false");
		System.out.println("=====");
		System.out.println(sumOfTree(bt.root));
	}
	
	private static int sumOfTree(Node root) {
		if(root==null) return 0;
		return root.data+sumOfTree(root.left)+sumOfTree(root.right);
	}

	private static int isPresent(Node root,int target) {
		// N L R
		if(root==null) return 0;
		if(root.data==target) {
			return 1;
		}
		else {
			System.out.println(root.data);
			return (isPresent(root.left,target)==1 || isPresent(root.right,target)==1)==true ? 1 : 0;
		}
	}

	private static void traverseInOrder(Node root) {
		// L N R
		if(root==null) return;
		traverseInOrder(root.left);
		System.out.println(root.data);
		traverseInOrder(root.right);
	}
	
	private static void traversePreOrder(Node root) {
		// N L R
		if(root==null) return;
		System.out.println(root.data);
		traversePreOrder(root.left);
		traversePreOrder(root.right);
	}
	
	private static void traversePostOrder(Node root) {
		// L R N
		if(root==null) return;
		traversePostOrder(root.left);
		traversePostOrder(root.right);
		System.out.println(root.data);
	}
}
