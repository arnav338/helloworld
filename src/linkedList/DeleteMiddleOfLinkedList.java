package linkedList;

public class DeleteMiddleOfLinkedList {
	/* Link list Node */
	static class Node {
		int data;
		Node next;
	}

	// Utility function to create a new node.
	static Node newNode(int data) {
		Node temp = new Node();
		temp.data = data;
		temp.next = null;
		return temp;
	}

	public static void main(String[] args) {
		/*
		 * Given a singly linked list, delete the middle of the linked list. For
		 * example, if the given linked list is 1->2->3->4->5 then the linked list
		 * should be modified to 1->2->4->5
		 * 
		 * If there are even nodes, then there would be two middle nodes, we need to
		 * delete the second middle element. For example, if given linked list is
		 * 1->2->3->4->5->6 then it should be modified to 1->2->3->5->6. If the input
		 * linked list is NULL, then it should remain NULL.
		 * 
		 * If the input linked list has 1 node, then this node should be deleted and a
		 * new head should be returned.
		 * 
		 * Simple solution: The idea is to first count the number of nodes in a linked
		 * list, then delete n/2�th node using the simple deletion process.
		 * 
		 * Efficient solution: Approach: The above solution requires two traversals of
		 * the linked list. The middle node can be deleted using one traversal. The idea
		 * is to use two pointers, slow_ptr, and fast_ptr. Both pointers start from the
		 * head of list. When fast_ptr reaches the end, slow_ptr reaches middle. The
		 * additional thing in this post is to keep track of the previous middle so the
		 * middle node can be deleted.
		 * 
		 * Time Complexity: O(n). Only one traversal of the linked list is needed
		 * Auxiliary Space: O(1). As no extra space is needed.
		 * 
		 */
		/* Start with the empty list */
		Node head = newNode(1);
		head.next = newNode(2);
		head.next.next = newNode(3);
		head.next.next.next = newNode(4);

		System.out.println("Given Linked List");
		printList(head);

		head = deleteMid(head);

		System.out.println("Linked List after deletion of middle");
		printList(head);
	}

	static void printList(Node ptr) {
		while (ptr != null) {
			System.out.print(ptr.data + "->");
			ptr = ptr.next;
		}
		System.out.println("NULL");
	}

	// Deletes middle node and returns
	// head of the modified list
	static Node deleteMid(Node head) {
		// Base cases
		if (head == null)
			return null;
		if (head.next == null) {
			return null;
		}

		// Initialize slow and fast pointers
		// to reach middle of linked list
		Node slow_ptr = head;
		Node fast_ptr = head;

		// Find the middle and previous of middle.
		Node prev = null;

		// To store previous of slow_ptr
		while (fast_ptr != null && fast_ptr.next != null) {
			fast_ptr = fast_ptr.next.next;
			prev = slow_ptr;// before moving to next, we save the current
			slow_ptr = slow_ptr.next;
		}

		// Delete the middle node
		prev.next = slow_ptr.next;

		return head;
	}
}
