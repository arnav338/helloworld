package main;

public class CreateLinkedList {
	
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
	
	static // to keep track of start and end
	Node head;
	Node tail;
	
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
		CreateLinkedList list = new CreateLinkedList();
		list.addNode(3);
		list.addNode(20);
		list.addNode(5);
		list.addNode(7);
		list.addNode(30);
		list.display();
		
		// assigning node of 30 to 20
		// list.head is 3
		// list.head.node is 20
		// list.head.node.node is 5 and so on...
		list.head.node.node.node.node = list.head.node;
		
		detectIfCircle(list);
		
	}

	private static void detectIfCircle(CreateLinkedList list) {
		Node current = head;
		
		System.out.println("--"+head.data);
		
		while(current!=null) {
			System.out.println("Found "+current.data);
			if( current.visited == true ) { System.out.println("Loop detected "+current.data); break; }
			current.visited = true;
			current = current.node;
		}
		
	}
}
