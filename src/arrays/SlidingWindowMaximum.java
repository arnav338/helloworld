package arrays;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.PriorityQueue;

public class SlidingWindowMaximum {
	/*
	 * Due to the rise of covid-19 cases in India, this year BCCI decided to
	 * organize knock-out matches in IPL rather than a league.
	 * 
	 * Today is matchday 2 and it is between the most loved team Chennai Super Kings
	 * and the most underrated team - Punjab Kings. Stephen Fleming, the head coach
	 * of CSK, analyzing the batting stats of Punjab. He has stats of runs scored by
	 * all N players in the previous season and he wants to find the maximum score
	 * for each and every contiguous sub-list of size K to strategize for the game.
	 * 
	 * Example 1:
	 * 
	 * Input: 
	 * N = 9, 
	 * K = 3 
	 * arr[] = 1 2 3 1 4 5 2 3 6 
	 * Output: 3 3 4 5 5 5 6
	 * Explanation: 
	 * 1st contiguous subarray = {1 2 3} Max = 3 
	 * 2nd contiguous subarray = {2 3 1} Max = 3 
	 * 3rd contiguous subarray = {3 1 4} Max = 4 
	 * 4th contiguous subarray = {1 4 5} Max = 5 
	 * 5th contiguous subarray = {4 5 2} Max = 5 
	 * 6th contiguous subarray = {5 2 3} Max = 5 
	 * 7th contiguous subarray = {2 3 6} Max = 6
	 */
	
	public static void main(String[] args) {
		int[] a = {1, 2, 3, 1, 4, 5, 2, 3, 6};
		int k = 3;
		//ArrayList<Integer> res = findMax(a,k);
		ArrayList<Integer> res = findMaxDeque(a,k);
		res.stream().forEach(b -> System.out.print(" || "+b));
	}
	
	private static ArrayList<Integer> findMax(int[] arr, int k) {
		// time complexity - O((n-k+1)*log k) -> n is no of elements, k is window size
		// space is O(n)
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(Collections.reverseOrder());
        ArrayList<Integer> result = new ArrayList<Integer>();
        int start = 0, end = k-1;
        for(int i = 0; i<k; i++){
            pq.add(arr[i]);
        }
        while(end < arr.length){
        	pq.add(arr[end]); // adding new element after shifting window
        	System.out.println("window start : "+start+" end : "+end+" max : "+pq.peek());
            result.add(pq.peek()); // add biggest element to result
            pq.remove(arr[start]); // removing the leftmost element of window
            start++;
            end++;            
        }
		return result;
	}
	
	private static ArrayList<Integer> findMaxDeque(int[] arr, int k) {
		// we are further optimizing space and time using deque
		// Our aim is that at any given point of time the first element should always be max
		// Before adding new element, if there is already an element that is smaller than 
		// the newly added element, then first remove that small element and then add new one
		// time complexity - O(n)
		// space is O(k)  - only k elements being stored
		Deque<Integer> dq = new ArrayDeque<>();
        ArrayList<Integer> result = new ArrayList<Integer>();
        int start = 0, end = k-1;
        for(int i = 0; i<k; i++){
        	if(dq.isEmpty()) {
        		dq.addFirst(arr[i]);
        		continue;
        	}
        	while(!dq.isEmpty()) {
        		if(dq.peekLast() < arr[i]) { // removing elements lesser than the one being added
        				dq.pollLast();
        		}
        	}
        	dq.add(arr[i]);
        }
        while(end < arr.length){
        	System.out.println("window start : "+start+" end : "+end+" max : "+dq.peekFirst());
        	result.add(dq.peekFirst());
        	if(dq.contains(arr[start])) {
        		dq.remove(arr[start]);
        	}
            start++;   
            end ++;
            if(end < arr.length) {
            	add(dq,arr,end); // adding new element after shifting window
            }
        }
		return result;
	}

	private static void add(Deque<Integer> dq, int[] arr, int i) {
		if(dq.isEmpty()) {
    		dq.addFirst(arr[i]);
    		return;
    	}
		if(dq.peekLast() < arr[i]) {
			while(!dq.isEmpty()) {
        		if(dq.peekLast() < arr[i]) { // removing elements lesser than the one being added
        				dq.pollLast();
        		}
        		else {
        			break;
        		}
        	}
		}
		dq.offerLast(arr[i]);
	}
}
