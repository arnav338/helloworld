package arrays;

public class MissingNumberInArray {
	public static void main(String[] args) {
		/*
		 * Given an array of size N-1 such that it only contains distinct integers in
		 * the range of 1 to N. Find the missing element.
		 * 
		 * Example 1:
		 * 
		 * Input: N = 5 A[] = {1,2,3,5} Output: 4 
		 * 
		 * Example 2:
		 * 
		 * Input: N = 10 A[] = {6,1,2,8,3,4,7,10,5} Output: 9
		 */
		int a[] = {1,2,3,5};
		System.out.println(printMissingElement(a,5)+" missing");
		
	}

	private static int printMissingElement(int[] a, int i) {
		int expectedSum = ((i+1)*i)/2;
		for (int j = 0; j < a.length; j++) {
			expectedSum -= a[j];
		}
		return expectedSum;
	}
}
