package binaryTree;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class SwappedNodeOfBST {
	/*
	 * You are given the root of a binary search tree(BST), 
	 * where exactly two nodes were swapped by mistake. 
	 * Fix (or correct) the BST by swapping them back. 
	 * Do not change the structure of the tree.
		Note: It is guaranteed that the given input will form BST, 
		except for 2 nodes that will be wrong. 
		All changes must be reflected in the original linked list.
 
		Example 1:
		Input:
       10
     /    \
    5      8
   / \
  2   20
		Output: 1
		
		Concept is : in order traversal of BST produces sorted aray
		
	 * */
	static Node first, middle, last, prev;
	static class BinaryTree {
		Node root;
		public BinaryTree(int data) {
			this.root = new Node(data);
		}
	}
	static class Node {
		int data;
		Node left,right;
		public Node(int data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}
		public Node() {
			this.data = -1;
			this.left = null;
			this.right = null;
		}
		
	}
	public static void main(String[] args) {
		BinaryTree bt = new BinaryTree(10);
		bt.root.left = new Node(5);
		bt.root.right = new Node(8);
		
		bt.root.right.left = new Node(3);
		
		bt.root.left.left = new Node(2);
		bt.root.left.right = new Node(20);
		
		/*
		 * 					 10
		 * 				   /	 \
		 * 				 5		  8
		 * 				/ \ 	  /	\    	   
		 * 			   2  20	 12	 21
		 * 
		 * 
		 * 		 out:		 10
		 * 				   /	 \
		 * 				 5		  20
		 * 				/ \ 	  /	   	   
		 * 			   2  8	     3	 	   

		 * */
		correctBST(bt.root);
	}
	
	static void correctBSTUtil( Node root)
    {
        if( root != null )
        {
            // Recur for the left subtree
            correctBSTUtil( root.left);
 
            // If this node is smaller than the previous node, it's violating the BST rule.
            // prev != null means that there is something to compare with. the root node cant be out of order
            if (prev != null && root.data < prev.data)
            {
                // If this is first violation, mark these two nodes as 'first' and 'middle'
                if (first == null)
                {
                    first = prev;
                    middle = root;
                }
 
                // If this is second violation,mark this node as last
                else
                    last = root;
            }
 
            // Mark this node as previous
            prev = root;
 
            // Recur for the right subtree
            correctBSTUtil( root.right);
        }
    }
	
	// A function to fix a given BST where
    // two nodes are swapped. This function
    // uses correctBSTUtil() to find out
    // two nodes and swaps the nodes to
    // fix the BST
    static void correctBST( Node root )
    {
        // Initialize pointers needed for correctBSTUtil()
        first = middle = last = prev = null;
 
        // Set the pointers to find out two nodes
        correctBSTUtil( root );
         
        // Fix (or correct) the tree
        if( first != null && last != null )
        {
            int temp = first.data;
            first.data = last.data;
            last.data = temp;
        }
        // Adjacent nodes swapped
        else if( first != null && middle !=
                                    null )
        {
            int temp = first.data;
            first.data = middle.data;
            middle.data = temp;
        }
        System.out.println("============");
        printInOrder(root);
        // else nodes have not been swapped,
        // passed tree is really BST.
    }

	private static void printInOrder(Node root) {
		if(root == null) return;
		printInOrder(root.left);
		System.out.println(root.data);
		printInOrder(root.right);}
	
}
