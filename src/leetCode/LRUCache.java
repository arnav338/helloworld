package leetCode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class LRUCache {
	class Node{
		int key;
		int value;
		Node pre,next;
		public Node(int key, int value) {
			this.key = key;
			this.value = value;
		}
	}
	private HashMap<Integer, Node> map; // hashmap ensures O(1) searching
    private int capacity, count;
    private Node head, tail; // doubly linked list ensures O(1) addition/deletion
	
	public LRUCache(int dq_capacity) {
		this.map = new HashMap<Integer, Node>();
		this.capacity = dq_capacity;
		head = new Node(0, 0);
		tail = new Node(0, 0);
		
		head.pre = null; // head is the 1st element
		head.next = tail;
		
		tail.next = null; // last element
		tail.pre = head;
	}
	
	public int get(int key) {
		if(map.containsKey(key)) { // key is present
			Node node = map.get(key); // extract the node
			int res = node.value;
			deleteNode(node);
			addToHead(node);
			return res;
		}
		return -1; // base case
	}
	
	// works in O(n)
	private void addToHead(Node node) {
		// we never change value of head and tail, they remain 0, just the pointers are exchanged
		node.next = head.next; // connecting node with next of head
		node.next.pre = node; // setting pre of node's next to current node
		node.pre = head; // setting pre of node to head
		head.next = node; // setting next of head to current
	}

	void deleteNode(Node node) {
		node.pre.next = node.next;
		node.next.pre = node.pre;
	}
	
	public void set(int key, int value)
    {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value; // setting new value for already existing node
            deleteNode(node);
            addToHead(node); // adding to head
        }
        else {
            Node node = new Node(key, value);
            map.put(key, node);
            if (count < capacity) {
                count++; // safe to add as capacity is still there
                addToHead(node);
            }
            else {
                map.remove(tail.pre.key);
                deleteNode(tail.pre); // tail is 0, the least recently used element will be pre of tail
                addToHead(node);
            }
        }
    }
	/*
	Deque<Integer> dq;
	Map<Integer, Integer> map;
	int size;
	
	public LRUCache(int dq_capacity) {
		this.dq = new ArrayDeque<Integer>();
		this.map = new HashMap<Integer, Integer>();
		this.size = dq_capacity;
	}
	
	public int get(int key) {
		if(!map.containsKey(key)) {
			return -1;
		}
		if(dq.contains(key)) {
			dq.remove(key);
			dq.offerLast(key);
		}
		return map.get(key);
	}
	
	public void put(int key, int value) {
		if(dq.size() < size) { // element can be added as size is not exceeding
			dq.offerLast(key);
			map.put(key, value);
		}
		else {
			if(map.containsKey(key)) {
				map.put(key, value);
				dq.remove(key); // this will take O(n) time to delete hence we use doubly linked list approach to remove
				dq.offerLast(key);
			}
			else {
				map.remove(dq.peekFirst()); // remove the leftmost (least recently used) element from map
				dq.pollFirst(); // remove the leftmost (least recently used) element from queue
				dq.offerLast(key); // add the current element at last of queue
				map.put(key, value); // add the current element at last of map
			}
		}
	}
	*/
	public static void main(String[] args) {
		String a = "aacdzyzhabhjik";
		char[] c = a.toCharArray();
		Arrays.sort(c);
		for(char d : c) {
			System.out.println(d);
		}
	}

}
