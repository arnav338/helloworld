package linkedList;

public class IntersectedLinkedList {
	
	public IntersectedLinkedList() {
		super();
		System.out.println("Clearing head and tail");
		this.head = null;
		this.tail = null;
	}
	public IntersectedLinkedList(Node tail) {
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
		IntersectedLinkedList list = new IntersectedLinkedList();
		list.addNode(3);
		list.addNode(6);
		list.addNode(9);
		list.addNode(15);
		list.addNode(30);
		list.display();
		System.out.println("-------");
		IntersectedLinkedList list2 = new IntersectedLinkedList();
		list2.addNode(10);
		list2.display();
		
		// 10 of list2 pointing to 15 of list
		list2.head.node = list.head.node.node.node;
		
		
		
		//list.head.node.node.node.node = list.head.node;
		
		//detectIfCircle(list);
		
	}

	private static void detectIfCircle(IntersectedLinkedList list) {
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
