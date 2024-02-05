 package linkedList;

public class ReverseLinkedList2 {
	
	static class Node{
		int data;
		Node next;
		public Node(int data) {
			this.data = data;
			this.next = null;
		}
	}
	
	static Node head;
	
	public static void main(String args[]) {
		head = new Node(3);
		head.next = new Node(11);
		head.next.next = new Node(7);
		head.next.next.next = new Node(9);
		reverseIteratively(head);
		//reverseRecursion(head);
	}
	
	
	private static Node reverseRecursion(Node root) {
		if(root == null || root.next == null) {
			return root;
		}
		Node temp = reverseRecursion(root.next);
		
		//Node curr = root.next;
		//curr.next = root;
		root.next.next = root;
		root.next = null;
		
		return temp;
	}

	private static void reverseIteratively(Node root) {
		Node curr = root;
		Node prev = null;
		while(curr != null) {
			Node temp = curr.next;
			curr.next = prev;
			prev = curr;
			curr = temp;
		}
		return;
	}

}
