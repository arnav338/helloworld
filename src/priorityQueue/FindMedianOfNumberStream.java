package priorityQueue;

import java.util.Collections;
import java.util.PriorityQueue;

public class FindMedianOfNumberStream {
	/*
	 * Given a running stream of integers find median at any given time
	 * 
	 * 3, 1, 5, 4....
	 * 
	 * idea is to place the incoming number at the calculated middle position
	 * and then median will be 
	 * the median of middle 2 numbers (if number of integers is even)
	 * or
	 * middle number (if number of integers is odd)
	 * 
	 * so we will create 2 heaps max and min
	 * at any given time numbers to the left of median will be smaller than median 
	 * and the elements to the right will be greater than the median
	 * hence we create 2 heaps
	 * while inserting a new number we will check 
	 * if num <= maxHeap.poll() then put in maxHeap
	 * else put in minHeap
	 * 
	 * Also after putting we will keep the size maintained
	 * if there are even numbers in total each heap will contain equal number of elements
	 * if there are odd numbers in total max heap will contain the 1 extra element
	 * 
	 * */
	public static void main(String[] args) {
	}
	static PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>();
	static PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(Collections.reverseOrder());
	// time complexity for inserting - O(log N) 
	// time complexity for finding median - O(1) 
	// for N operations - O(N log N)
	// space complexity - O(N) as we are storing all elements at any given time
	public static void insertNum(int a) {
		if(maxHeap.isEmpty() || a <= maxHeap.peek()) {
			maxHeap.add(a);
		}
		else {
			minHeap.add(a);
		}
		// after adding we also check size
		if(maxHeap.size() > minHeap.size()+1) {
			minHeap.add(maxHeap.poll());
		}
		else if(maxHeap.size() < minHeap.size()) {
			maxHeap.add(minHeap.poll());
		}
	}
	
	public static int findMedian() {
		if(maxHeap.size() == minHeap.size()) {
			return (maxHeap.peek()/2) + (minHeap.peek()/2);
		}
		return maxHeap.peek();
	}
	
}
