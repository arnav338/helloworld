package priorityQueue;

import java.util.Collections;
import java.util.PriorityQueue;

public class KthLargestElement {
	public static void main(String[] args) {
		/*
		 * kth largest element has to be found
		 * 
		 * create a min heap of size k
		 * and keep on adding elements to it based on a check
		 * that the element being added is less than the top element
		 * 
		 * we use min heap as we know that in a min heap the top element
		 * is the lowest of all of the heap
		 * so if we have to find 3rd largest element and we have a heap of 
		 * size 3 then we can be sure that the 2 elements apart from the one at the 
		 * top will be bigger than the current one
		 * hence the element at the top will be the 3rd largest in size
		 * 
		 * if we create a max heap then the element at the top will be the biggest one
		 * and we will have to traverse the heap to find the element
		 * so it will inc the time complexity
		 * 
		 * By same logic we create a max heap for j th min element
		 * */
		int[] a = {20, 10, 60, 30, 50, 40};
		int k = 3; // kth largest element
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		for (int i = 0; i<k; i++) {
			pq.add(a[i]);
		}
		// start traversing from where elements have not been added to priority queue
		for (int i = k; i < a.length; i++) {
	  		if(pq.peek() !=null && pq.peek() < a[i] ) {
				pq.poll();
				pq.add(a[i]);
			}
		}
		System.out.println("k th largest element "+k+"->"+pq.peek());
		
		System.out.println("==============");
		int[] b = {20, 10, 60, 30, 50, 40};
		int j = 3; // kth largest element
		PriorityQueue<Integer> pq1 = new PriorityQueue<>(Collections.reverseOrder());
		for (int i = 0; i<j; i++) {
			pq1.add(b[i]);
		}
		// start traversing from where elements have not been added to priority queue
		for (int i = j; i < b.length; i++) {
			if(pq1.peek() !=null && pq1.peek() > b[i] ) {
				pq1.poll();
				pq1.add(b[i]);
			}
		}
		System.out.println("j th smallest element "+j+"->"+pq1.peek());
	}
}
