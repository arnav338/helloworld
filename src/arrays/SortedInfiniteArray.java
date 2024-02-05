package arrays;

import leetCode.BinarySearch;

public class SortedInfiniteArray {
	public static void main(String[] args) {
		/*
		 * Aim is to find a number in a sorted infinite array
		 * By infinite it is meant that the size of array is not known
		 * So we cant directly apply binary search as binary search requires start and end pointer s
		 * 
		 * So the trick is to set a search window explicitly
		 * and expand it exponentially in each required iteration
		 * 
		 * Once we find an element which is greater than the key, we apply binary search in that window
		 * 
		 * Finding the window complexity - O(log N) 
		 * eg : if the key to be found is at index 1600 
		 * 		it would take 11 iterations as 2^10 < 1600 < 2^11
		 * 		and log(1600) = ~10, so its being done in log (N) time 
		 * Binary search - O(log N)
		 * this way the combined time complexity will be O(log N)
		 * 
		 * */
		int a[] = new int[1000];
		for(int i=0; i < a.length;i++) {
			a[i] = i+1;
		}
		System.out.println("->->"+infiniteArray(a,348));
	}

	private static int infiniteArray(int[] a, int key) {
		double start = 0;
		int result = -1;
		//int k = 1;
		//int step = 5;
		//double end = Math.pow(step, k);
		double end = 5;
		while(end < a.length) {
			int curr = a[(int) end];
			if(curr > key) {
				System.out.println("Found, start :"+start+"||end:"+end);
				break;
			}
			start = end;
			//k++;
			//end = Math.pow(step, k);
			end *= 2;
			System.out.println("end:"+end);
		}
		result = BinarySearch.binarySearch(a, key,(int) start,(int) end);
		return result;
	}
}
