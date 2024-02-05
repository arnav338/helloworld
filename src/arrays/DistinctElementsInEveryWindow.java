package arrays;

import java.util.HashMap;

public class DistinctElementsInEveryWindow {

	public static void main(String[] args) {
		/*
		 * counting distinct elements in every window of size k
		 * 
		 * a = {1,2,2,1,3,1,1,3}
		 * k = 4
		 * 
		 * brute force - in every possible window apply a nested for loop and check for distinct elements takes O(n^2)
		 * 
		 * optimal approach - use sliding window
		 * at first window add all elements in a hashmap in the window, with key as the element and value as its freq
		 * for 2 nd window, move the left and right pointer one step ahead and reduce the freq of the 1st element by 1 which is going out of the widow
		 * and increase the freq of the last element by 1 which is coming in the window
		 * */
		
		int[] a = {1,2,2,1,3,1,1,3};
		int k = 4; // window size
		distinctElementsInWindow(a,k);
	}

	private static void distinctElementsInWindow(int[] a, int k) {
		// time complexity - O(n)
		int left = 0;
		int right = k-1;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i <= right; i++) { // fill the map with values of first window
			if(map.containsKey(a[i])) {
				map.put(a[i], map.get(a[i])+1);
			}
			else {
				map.put(a[i], 1);
			}
		}
		System.out.println("left : "+left+" || right :"+right+" || no of distinct elements : "+map.size());
		while(right < a.length-1) {
			if(map.get(a[left])>1) {// before moving left pointer to next position we are reducing the freq of 1st position element by 1
				map.put(a[left], map.get(a[left])-1 );
			}
			else {
				map.remove(a[left]);
			}
			left++;
			right++;
			if(map.containsKey(a[right])) { // after moving right pointer to next position we are increasing freq of newly added element by 1
				map.put(a[right], map.get(a[right])+1);
			}
			else {
				map.put(a[right], 1);
			}
			System.out.println("left : "+left+" || right :"+right+" || no of distinct elements : "+map.size());
		}
		
	}
}
