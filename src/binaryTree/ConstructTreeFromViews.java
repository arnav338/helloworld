package binaryTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class ConstructTreeFromViews {
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
		List<Integer> pre = new ArrayList<Integer>();
		List<Integer> post = new ArrayList<Integer>();
		List<Integer> in = new ArrayList<Integer>();
		traverseInOrder(bt.root, in);
		System.out.println("============");
		traversePreOrder(bt.root, pre);
		System.out.println("============");
		in.stream().forEach(b -> System.out.print(b+" "));
		System.out.println("==========");
		pre.stream().forEach(b -> System.out.print(b+" "));
		constructFromInAndPre(in,pre);
		
	}
	private static void constructFromInAndPre(List<Integer> in, List<Integer> pre) {
		HashMap<Integer, Integer> inmap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> premap = new HashMap<Integer, Integer>();
		for(int i=0; i<in.size(); i++) {
			inmap.put(in.get(i), i);
		}
		for(int i=0; i<pre.size(); i++) {
			premap.put(pre.get(i), i);
		}
		construct(inmap,premap);
	}
	private static void construct(HashMap<Integer, Integer> inmap, HashMap<Integer, Integer> premap) {
		for(int i=0; i<premap.size();i++) {
			
		}
		
	}
	private static void traverseInOrder(Node root,List<Integer> list) {
		// L N R
		if(root==null) return;
		traverseInOrder(root.left,list);
		System.out.println(root.data);
		list.add(root.data);
		traverseInOrder(root.right,list);
	}
	
	private static void traversePreOrder(Node root,List<Integer> list) {
		// N L R
		if(root==null) return;
		System.out.println(root.data);
		list.add(root.data);
		traversePreOrder(root.left,list);
		traversePreOrder(root.right,list);
	}
	
	private static void traversePostOrder(Node root,List<Integer> list) {
		// L R N
		if(root==null) return;
		traversePostOrder(root.left,list);
		traversePostOrder(root.right,list);
		System.out.println(root.data);
		list.add(root.data);
	}
}
