package leetCode;

public class Btree {
	
	static class Node  {
		
		int data;
		Node left;
		Node right; 
		public Node(int data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}
		
	}
	

	static class BinaryTree {
	
		Node root;
		
		public BinaryTree(int data) {
			root = new Node(data);
		}
	
	}

	
	public static void main(String[] args) {
		BinaryTree bt = new BinaryTree(10);
		
		bt.root.left = new Node(5);
		bt.root.right = new Node(15);
		
		bt.root.left.left = new Node(3);
		bt.root.left.right = new Node(7);
		
		bt.root.right.left =  new Node(13);
		bt.root.right.right =  new Node(18);
		
		bt.root.left.left.left = new Node(1);
		bt.root.left.right.left = new Node(6);
		findSum(bt.root,6,10);
		System.out.println("->"+sum);
	}
	static int sum=0;
	private static void findSum(Node root, int low, int high) {
		if(root == null) return;
        
        if(root.data >= low && root.data <= high) {
        	System.out.println("adding "+root.data);
        	sum += root.data;
        }
        findSum(root.left,low,high);
        findSum(root.right,low,high);
		
	}
}
