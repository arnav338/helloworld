package leetCode;

public class StackUsingLinkedList {
	
	@Override
	public String toString() {
		return " [head=" + head.data + "]";
	}

	StackUsingLinkedList(){
		this.head = new Node();
	}
	
	public Node head;
	public Node top;
	
	public void add(int data) {
		this.head.data = data;
		this.head.next = top;
		top = this.head;
	}
	
	public void pop() {
		top = top.next;
	}
	
	public int peek() {
		return top.data;
	}
	
	static class Node {
		int data;
		Node next;
	}
	
	public static void main(String[] args) {
		StackUsingLinkedList stack = new StackUsingLinkedList();
		stack.add(1);
		stack.add(2);
		stack.add(3);
		System.out.println(stack);
		
	}

}
