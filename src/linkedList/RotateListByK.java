package linkedList;

public class RotateListByK {

	static class Node {
		int data;
		Node next;

		public Node(int data) {
			this.data = data;
			this.next = null;
		}
	}

	public static void main(String[] args) {
		/*
		 * Given a singly linked list and an integer K, the task is to rotate the linked
		 * list clockwise to the right by K places. Examples:
		 * 
		 * 
		 * Input: 1 -> 2 -> 3 -> 4 -> 5 -> NULL, 
		 * K = 2 
		 * Output: 4 -> 5 -> 1 -> 2 -> 3 -> NULL
		 */
		Node head = new Node(1);
		head.next = new Node(2);
		head.next.next = new Node(3);
		head.next.next.next = new Node(4);
		head.next.next.next.next = new Node(5);
		int k = 2;
		rotate(head, k);
	}

	private static void rotate(Node head, int k) {
		int length = 0;
		Node curr = head;
		Node temp = head;
		int c = 0;
		while (curr.next != null) {
			curr = curr.next;
			length++;
		}
		length++;
		System.out.println("->" + length);
		if (length < k)
			return;
		while(c < k) {
			temp = temp.next;
			c++;
		}
		// temp points to node which has to be disconnected
		// curr points to last node of list
		curr.next = head; // pointing last to head
		head = temp.next; // pointing the head to the node next to the node to be disconnected
		temp.next = null; // disconnecting the node
		System.out.println();
	}
}
