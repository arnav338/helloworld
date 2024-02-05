package binaryTree;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class FlattenABinaryTree {
	
	public static void main(String[] args) {
		
		BinaryTree bt1 = new StandardTree().new BinaryTree(3);
		bt1.root.left = new StandardTree().new Node(5);
		bt1.root.right = new StandardTree().new Node(2);
		
		bt1.root.right.left = new StandardTree().new Node(1);
		
		bt1.root.right.left.left = new StandardTree().new Node(4);
		bt1.root.right.left.right = new StandardTree().new Node(6);
		/*
		 * 		3
		 * 	   / \ 
		 *    5   2
		 *        /   	
		 *        1
		 *       / \ 
		 *       4 6  
		 *      
		 *      */
		inorder(bt1.root);
		System.out.println("-----");
		binaryTree.Node prev = new binaryTree.Node();
		binaryTree.Node head = new binaryTree.Node();
		flatten(bt1.root,prev,head);
		while(head!=null) {
			System.out.println("->"+head.data);
			head = head.left;
		}
	}

	private static void flatten(Node root, binaryTree.Node prev, binaryTree.Node head) {
		
		/*if(root==null) return;
		flatten(root.left,prev,head);
		if(prev==null) {
			head = root;
		}
		else {
			root.left = prev;
			prev.right = root;
		}
		prev = root;
		flatten(root.right,prev,head); 
		*/
	}
	
	private static void inorder(Node root) {
		if(root==null) return;
		inorder(root.left);
		System.out.println(root.data);
		inorder(root.right);
	}
}
