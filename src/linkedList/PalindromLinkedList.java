package linkedList;

public class PalindromLinkedList {
	/*
	 * Reverse the linked list from middle 
	 * then keep one pointer at start of list
	 * other at the end (start of reversed list)
	 * then keep incrementing first one and decrementing the last one
	 * at any point, if different, return false;
	 * 
	 * */
	static class Node{
		String data;
		Node next;
		public Node(String data) {
			this.data = data;
			this.next = null;
		}
	}
	
	public static void main(String[] args) {
		
	}

}
