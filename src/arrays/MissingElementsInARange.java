package arrays;

import java.util.ArrayList;
import java.util.Arrays;

public class MissingElementsInARange {

	public static void main(String[] args) {
		/*
		 * {1, 14, 11, 51, 15}
		 * 
		 * low = 50, high = 55
		 * 
		 * output : 50, 52, 53, 54
		 * */
		int[] a = {1, 14, 11, 51, 15};
		int low = 50;
		int high = 55;
		ArrayList<Integer> res = findMissingNumbers(a,low ,high);
		res.stream().forEach(b -> System.out.print(" || "+b));
	}
	
	private static ArrayList<Integer> findMissingNumbers(int[] a, int low, int high) {
		// time complexity is O(n + (high-low+1)). 
		ArrayList<Integer> res = new ArrayList<Integer>();
		int min = low;
		int max = high;
		for (int i = 0; i < a.length; i++) {
			if(a[i] >= min) {
				min = Math.min(a[i], min);
				max = Math.max(a[i], max);
				res.add(a[i]);
			}
		}
		for(int i=low; i <= high; i++) {
			if(!res.contains(i)) {
				res.add(i);
			}
			else {
				res.remove((Integer) i);
			}
		}
		return res;
	}

}
