package priorityQueue;

import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueSample {
	public static void main(String[] args) {
		/*
		 * By default, priority queue is a min heap
		 * minimum element will be at top
		 * */
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		pq.add(10);
		pq.add(5);
		pq.add(20);
		pq.add(7);
		while(!pq.isEmpty()) {
			System.out.println("->"+pq.peek());
			pq.poll();
		}
		System.out.println("==========");
		PriorityQueue<Integer> pq1 = new PriorityQueue<>(Collections.reverseOrder());
		pq1.add(10); 
		pq1.add(5);
		pq1.add(20);
		pq1.add(7);
		while(!pq1.isEmpty()) {
			System.out.println("->"+pq1.peek());
			pq1.poll();
		}
	}
}
