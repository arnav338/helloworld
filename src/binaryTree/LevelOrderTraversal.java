package binaryTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import binaryTree.StandardTree.BinaryTree;
import binaryTree.StandardTree.Node;

public class LevelOrderTraversal {
	
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
		 * Level order traversal : 
		 * 		10
		 * 		12 2 
		 * 		24 15 18 
		 * 		17 11
		 * 		19
		 * */
		Map<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>();
		int level=0;
		levelOrderTraversal(bt.root,result,level);
		result.entrySet().stream().forEach(System.out::println);
		System.out.println("++++++++++");
		//levelOrderTraversalUsingQueue(bt.root);
		
	}

	private static void levelOrderTraversalUsingQueue(Node root) {
		// less space complexity, but data is not printed line by line
		Queue<Node> q = new PriorityQueue<>();
		q.add(root);
		while(!q.isEmpty()) {
			Node current = q.poll();
			System.out.println(current.data);
			if(current.left!=null) {
				q.add(current.left);
			}
			if(current.right!=null) {
				q.add(current.right);
			}
		}
	}

	private static int levelOrderTraversal(Node root, Map<Integer, List<Integer>> result, int level) {
		// space complexity is O(n) more as for each level a separate list is initialized
		if(root!=null) {
			if(result.get(level)==null) {
				System.out.println("Initialized empty list for level : "+level);
				List<Integer> list = new ArrayList<Integer>();
				result.put(level, list);
			}
			result.get(level).add(root.data);
		}
		if(root.left!=null) {
			levelOrderTraversal(root.left, result, level+1);
		}
		if(root.right!=null) {
			levelOrderTraversal(root.right, result, level+1);
		}
		return 0;
	}
	
}
