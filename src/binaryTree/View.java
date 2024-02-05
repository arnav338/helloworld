package binaryTree;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class View {

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
		
		BinaryTree bt1 = new StandardTree().new BinaryTree(2);
		bt1.root.left = new StandardTree().new Node(1);
		bt1.root.right = new StandardTree().new Node(3);
		System.out.println(isBST(bt1.root));
	}

	private static boolean isBST(Node root) {
		if(root==null) return true;
		if(root.left != null){
            if(root.left.data>root.data) return false;
        }
        if(root.right != null){
            if(root.right.data<root.data) return false;
        }
        return isBST(root.left) && isBST(root.right);
	}

}
