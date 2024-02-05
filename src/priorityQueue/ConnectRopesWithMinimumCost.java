package priorityQueue;

import java.util.PriorityQueue;

public class ConnectRopesWithMinimumCost {
	/*
	 * There are given N ropes of different lengths, we need to connect these ropes
	 * into one rope. The cost to connect two ropes is equal to sum of their
	 * lengths. The task is to connect the ropes with minimum cost.
	 * 
	 * Example 1:
	 * 
	 * Input: 
	 * n = 4 
	 * arr[] = {4, 3, 2, 6} 
	 * Output: 29 
	 * Explanation: For example if we are given 4 ropes of lengths 4, 3, 2 and 6. We can connect the ropes in
	 * following ways. 
	 * 1) First connect ropes of lengths 2 and 3. Now we have three ropes of lengths 4, 6 and 5. 
	 * 2) Now connect ropes of lengths 4 and 5. Now we have two ropes of lengths 6 and 9. 
	 * 3) Finally connect the two ropes and all ropes have connected. 
	 * Total cost for connecting all ropes is 5 + 9 + 15 = 29.
	 * This is the optimized cost for connecting ropes. Other ways of connecting
	 * ropes would always have same or more cost. For example, if we connect 4 and 6
	 * first (we get three strings of 3, 2 and 10), then connect 10 and 3 (we get
	 * two strings of 13 and 2). Finally we connect 13 and 2. Total cost in this way
	 * is 10 + 13 + 15 = 38.
	 */
	public static void main(String[] args) {
		//int[] a = {2,5,4,8,6,9};
		int[] a = {4, 3, 2, 6};
		System.out.println("->->"+minCost(a));
	}
	
	private static int minCost(int[] arr) {
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		int cost = 0;
		int rope1 = 0, rope2 = 0, newRope = 0;
		for (int i = 0; i < arr.length; i++) {
			pq.add(arr[i]);
		}
		while(pq.size() > 1) {
			rope1 = pq.poll();
			rope2 = pq.poll();
			System.out.println("adding "+rope1+"::"+rope2);
			newRope = rope1 + rope2; // 5
			cost += newRope; // 5
			System.out.println("adding : "+newRope);
			pq.add(newRope);
		}
		return cost;
	}
}
