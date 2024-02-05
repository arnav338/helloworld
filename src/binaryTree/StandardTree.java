package binaryTree;

import java.util.ArrayList;

public class StandardTree {
	public class Node{
		int data;
		Node left,right;
		public Node(int data){
			this.data = data;
			this.left = this.right = null;
		}
	}
	public class BinaryTree {
		Node root;
		public BinaryTree(int data) {
			this.root = new Node(data);
		}
	}
	
	public static void main(String[] args) {
		BinaryTree bt = new StandardTree().new BinaryTree(10);
		bt.root.left = new StandardTree().new Node(12);
		bt.root.right = new StandardTree().new Node(2);
		bt.root.left.left = new StandardTree().new Node(24);
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
		printLeftView(bt.root);
		System.out.println("=======");
		printRightView(bt.root);
		System.out.println("=======");
		ArrayList<Integer> left = new ArrayList<>();
		ArrayList<Integer> right = new ArrayList<>();
		printTopView(bt.root,true,left,right);
		System.out.println(left);
		System.out.println(bt.root.data);
		System.out.println(right);
	}

	private static void printTopView(Node root, boolean checkLeft, ArrayList<Integer> left, ArrayList<Integer> right) {
		if(root.left!=null && checkLeft) {
			left.add(root.left.data);
			printTopView(root.left,true,left,right);
		}
		if(root.right!=null) {
			right.add(root.right.data);
			printTopView(root.right,false,left,right);
		}
	}

	private static void printRightView(Node node) {
		System.out.println(node.data);
		if(node.right==null) {
			return;
		}
		else {
			printRightView(node.right);
		}
	}

	private static void printLeftView(Node node) {
		System.out.println(node.data);
		if(node.left==null) {
			return;
		}
		else {
			printLeftView(node.left);
		}
	}
}
