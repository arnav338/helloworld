package linkedList;

public class RemoveDuplicate {
	static class Node{
		int data;
		Node next;
		public Node(int data) {
			this.data = data;
			this.next = null;
		}
		
	}
	/*
	 * Given a singly linked list consisting of N nodes. The task is to remove
	 * duplicates (nodes with duplicate values) from the given list (if exists).
	 * Note: Try not to use extra space. Expected time complexity is O(N). The nodes
	 * are arranged in a sorted way.
	 * 
	 * Example 1:
	 * 
	 * Input: 
	 * LinkedList: 2->2->4->5 
	 * Output: 2 4 5 
	 * Explanation: 
	 * In the given linked list 2 ->2 -> 4-> 5, 
	 * only 2 occurs more than 1 time.
	 */
	public static void main(String[] args) {
		Node head = new Node(2);
		head.next = new Node(2);
		head.next.next = new Node(4);
		head.next.next.next = new Node(5);
		removeDuplicate(head);
	}
	private static void removeDuplicate(Node head) {
		Node curr = head;
		Node temp = null;
		while(curr != null) {
			if(temp != null) {
				if(temp.data != curr.data) { // diff nodes/ non repeating nodes
					temp = temp.next;
					curr = curr.next;
				}
				else { // repeating nodes
					temp.next = curr.next; // 
				}
			}
			else {
				temp = curr;
				curr = curr.next;
			}
		}
	}
}
