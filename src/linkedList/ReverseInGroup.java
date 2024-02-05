package linkedList;

import linkedList.ReverseLinkedList2.Node;

public class ReverseInGroup {
	/*
	 * Given the head of a linked list, reverse the nodes of the list k at a time,
	 * and return the modified list.
	 * 
	 * k is a positive integer and is less than or equal to the length of the linked
	 * list. If the number of nodes is not a multiple of k then left-out nodes, in
	 * the end, should remain as it is.
	 * 
	 * in a list of 5 nodes, if k = 2, first reverse 1st 2 nodes then next 2 and so on
	 * 
	 * You may not alter the values in the list's nodes, only nodes themselves may
	 * be changed.
	 */
	static Node head;
	private static void addNode(int data) {
		Node temp = new Node(data);
		temp.next = head;
		head = temp;
	}
	public static void main(String[] args) {
		/*
		 * Time Complexity: O(n). 
Traversal of list is done only once and it has ‘n’ elements.
Auxiliary Space: O(n/k). 
For each Linked List of size n, n/k or (n/k)+1 calls will be made during the recursion.
		 * */
		head = new Node(5);
		addNode(4);
		addNode(3);
		addNode(2);
		addNode(1);
		int k = 2;
		reverseInGroup(head,k);
		System.out.println();
	}
	
	private static Node reverseInGroup(Node head, int k) {
		if(head == null)
	          return null;
	        Node current = head;
	        Node next = null;
	        Node prev = null;
	 
	        int count = 0;
	 
	        /* Reverse first k nodes of linked list */
	        while (count < k && current != null) {
	            next = current.next;
	            current.next = prev;
	            prev = current;
	            current = next;
	            count++;
	        }
	 
	        /* next is now a pointer to (k+1)th node
	           Recursively call for the list starting from
	           current. And make rest of the list as next of
	           first node */
	        if (next != null)
	            head.next = reverseInGroup(next, k);
	 
	        // prev is now head of input list
	        return prev;
		
	}
	private static int findLength(Node head2) {
		int k = 0;
		while(head2 != null) {
			k++;
			head2 = head2.next;
		}
		return k;
	}

	static class Node{
		int data;
		Node next;
		public Node(int data) {
			this.data = data;
			this.next = null;
		}
	}
}
