package binaryTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class ViewOfTree {

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
		System.out.println("Left view : ");
		printLeftView(bt.root);
		System.out.println("+++++++++++++");
		System.out.println("Right view : ");
		printRightView(bt.root);
		System.out.println("+++++++++++++");
		System.out.println("Top view : ");
		printTopView(bt.root);
		System.out.println("+++++++++++++");
		System.out.println("Bottom view : ");
		printBottomView(bt.root);
	}

	private static void printBottomView(Node root) {
		int level=0;
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		bottom(root,result,level);
		result.entrySet().stream().forEach(System.out::println);
	}

	private static void bottom(Node root, Map<Integer, Integer> result, int level) {
		if(root==null) {
			return;
		}
		result.put(level, root.data);
		bottom(root.left, result, level-1);
		bottom(root.right, result, level+1);
	}

	private static void printTopView(Node root) {
		int level=0;
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		top(root,result,level);
		result.entrySet().stream().forEach(System.out::println);
	}
	public static void vertical(Node root,TreeMap<Integer,ArrayList<Integer>> map,int level){
        if(root==null) return;
        ArrayList<Integer> list = new ArrayList<>();
        vertical(root.left,map,level-1);
        vertical(root.right,map,level+1);
    }
	private static void top(Node root, Map<Integer, Integer> result, int level) {
		if(root==null) {
			return;
		}
		if(result.get(level)==null) {
			result.put(level, root.data);
		}
		top(root.left, result, level-1);
		top(root.right, result, level+1);
	}

	private static void printRightView(Node root) {
		int level=0;
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		right(root,result,level);
		result.entrySet().stream().forEach(System.out::println);
	}

	private static void right(Node root, Map<Integer, Integer> result, int level) {
		if(root==null) {
			return;
		}
		if(result.get(level)==null) {
			result.put(level, root.data);
		}
		right(root.right, result, level+1);
		right(root.left, result, level+1);
	}

	private static void printLeftView(Node root) {
		int level=0;
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		left(root,result,level);
		result.entrySet().stream().forEach(System.out::println);
	}

	private static void left(Node root, Map<Integer, Integer> result, int level) {
		if(root==null) {
			return;
		}
		if(result.get(level)==null) {
			result.put(level, root.data);
		}
		left(root.left, result, level+1);
		left(root.right, result, level+1);
	}

}
