package main;

public class ReverseLinkedList {
	
	public ReverseLinkedList() {
		super();
		System.out.println("Clearing head and tail");
		this.head = null;
		this.tail = null;
	}
	public ReverseLinkedList(Node tail) {
		super();
		System.out.println("Clearing head and tail");
		this.head = null;
		this.tail = null;
	}

	class Node{
			public int data;
			public Node node;
			boolean visited;
			
			public Node(int data) {
				super();
				this.data = data;
				this.node = null;
				this.visited = false;
			}
			
	}
	// to keep track of start and end
	static Node head;
	static Node tail;
	
	void addNode(int data){
		
		Node newnode = new Node(data);
		
		if(head==null) {
			// list is empty
			head = newnode;
			tail = newnode;
		}
		else{
			// assigning node of tail to new node
			tail.node = newnode;
			// assigning new node as the new tail
			tail = newnode;
		}
	}
	
	void display() {
		// traversing a linked list
		Node current = head;
		
		if(head==null) { System.out.println("Empty list");}
		while(current!=null) {
			System.out.println(current.data);
			current = current.node;
		}
	}
	
	public static void main(String args[]) {
		ReverseLinkedList list = new ReverseLinkedList();
		list.addNode(3);
		list.addNode(6);
		list.addNode(9);
		list.addNode(15);
		list.addNode(30);
		list.display();
		System.out.println("-------");
		
		reverse(list.head);
		
		list.display();
		
	}
	private static Node reverse(Node head2) {
		
		if(head == null || head.node == null) { return head; } 
		
		Node rest = reverse(head.node);
        head.node.node = head;
 
        /* tricky step -- see the diagram */
        head.node = null;
 
        /* fix the head pointer */
        return rest;
		
		
		
	}

}
