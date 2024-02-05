package binaryTree;

import java.util.ArrayList;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class CheckIfSubtree {
	public static void main(String[] args) {
		/*
		 * The optimised approach will be :
		 * 
		 * if we have (inorder + preorder) or (inorder + postorder) traversal of 2 tress
		 * the we can uniquely identify if the second tree is subtree of 1st or not
		 * 
		 * inorder and pre order traversals take O(n) time
		 * matching also takes O(n) time
		 * 
		 * */
		BinaryTree bt = new StandardTree().new BinaryTree(10);
		bt.root.left = new StandardTree().new Node(12);
		bt.root.right = new StandardTree().new Node(2);
		
		bt.root.left.left = new StandardTree().new Node(24);
		
		bt.root.right.left = new StandardTree().new Node(15);
		bt.root.right.right = new StandardTree().new Node(18);
		/*
		 * 
		 * 					bt : 
		 * 					 10
		 * 				   /	 \
		 * 				 12			2
		 * 				 /		  /	  \
		 * 			   24		15	  18
		 * 
		 * */
		BinaryTree bt1 = new StandardTree().new BinaryTree(2);
		bt1.root.left = new StandardTree().new Node(15);
		bt1.root.right = new StandardTree().new Node(18);
		/*
		 * 
		 * 					bt1 : 
		 * 				  			2
		 * 				  		  /	  \
		 * 			    		15	  18
		 * 
		 * 
		 * 			inorder  				pre order
		 * 
		 * bt :   24 12 10 (15 2 18)		10 12 24 (2 15 18)
		 * 
		 * bt1 :  (15 2 18)					(2 15 18)
		 * 
		 * so both traversals of bt1 is there in bt hence they are subtree 
		 * 
		 * */
		//String s1 = "abcd";
		//String s2 = "ac";
		//System.out.println("--"+s1.contains(s2));
		//System.out.println("Is subtree ?  : "+isSubTree(bt.root,bt1.root));
		//efficient approach
		System.out.println("Is subtree ?  : "+isSubTree1(bt.root,bt1.root));
	}
	
	public static boolean isSubTree1(Node source, Node subtree) {
		ArrayList<Integer> inOrder = new ArrayList<Integer>();
		ArrayList<Integer> preOrder = new ArrayList<Integer>();
		ArrayList<Integer> inOrderSub = new ArrayList<Integer>();
		ArrayList<Integer> preOrderSub = new ArrayList<Integer>();
		storeInOrder(source,inOrder);
		storeInOrder(subtree,inOrderSub);
		if(!checkIfContains(inOrder,inOrderSub)) { 
			System.out.println("not equal");
			return false;
		}
		storePreOrder(source, preOrder);
		storePreOrder(subtree, preOrderSub);
		if(!checkIfContains(preOrder,preOrderSub)) { 
			System.out.println("not equal///");
			return false;
		}
		return true;
	}

	private static void storePreOrder(Node source, ArrayList<Integer> inOrder) {
		if(source == null) return;
		inOrder.add(source.data);
		storePreOrder(source.left, inOrder);
		storePreOrder(source.right, inOrder);
	}
	
	private static boolean checkIfContains(ArrayList<Integer> inOrder, ArrayList<Integer> inOrderSub) {
		int i = 0; // counter for main traversal
		int j = 0; // counter for sub traversal
		while(i < inOrder.size()) {
			if(inOrder.get(i) != inOrderSub.get(j)) {
				j = 0;
			}
			else {
				j++;
			}
			if(j == inOrderSub.size()-1) {
				return true;
			}
			i++;
		}
		return false;
	}
	
	private static void storeInOrder(Node source, ArrayList<Integer> inOrder) {
		if(source == null) return;
		storeInOrder(source.left, inOrder);
		inOrder.add(source.data);
		storeInOrder(source.right, inOrder);
	}

	// time complexity - O(n*m) as for every node of source tree we will be calling the check function
	public static boolean areEqual(Node source, Node subtree) {
		if(source == null && subtree == null) return true; // if both are null, they are same
		if(source == null || subtree == null) return false;// if only one of them is null, they are not equal
		//System.out.println("===============================================");
		//System.out.println("source: "+source.data+" || sub : "+subtree.data);
		return source.data == subtree.data && (areEqual(source.left, subtree.left)  && areEqual(source.right, subtree.right)) ;
	}
	public static boolean isSubTree(Node source, Node subtree) {
		if(source == null && subtree == null) return true; // if both are null, they are same
		if(source == null || subtree == null) return false;// if only one of them is null, they are not equal
		//System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
		//System.out.println("source: "+source.data+" || sub : "+subtree.data);
		return areEqual(source,subtree) || isSubTree(source.left, subtree) || isSubTree(source.right, subtree);
	}
}
