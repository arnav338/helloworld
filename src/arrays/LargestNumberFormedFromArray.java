package arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LargestNumberFormedFromArray {
	public static void main(String[] args) {
		/*
		 * Given a list of non negative integers, arrange them in such a manner that
		 * they form the largest number possible.The result is going to be very large,
		 * hence return the result in the form of a string.
		 * 
		 * 
		 * Example 1:
		 * 
		 * Input: 
		 * N = 5 
		 * Arr[] = {3, 30, 34, 5, 9} 
		 * Output: 9534330 
		 * Explanation: 
		 * Given numbers are {3, 30, 34, 5, 9}, the arrangement 9534330 gives the largest
		 * value.
		 * comparison based sorting can be used to solve this 
		 * The idea is to use any comparison based sorting algorithm. In the used sorting algorithm,
		 * instead of using the default comparison, write a comparison function
		 * myCompare() and use it to sort numbers.
		 * 
		 * Given two numbers X and Y, how should myCompare() decide which number to put
		 * first – we compare two numbers XY (Y appended at the end of X) and YX (X
		 * appended at the end of Y). If XY is larger, then X should come before Y in
		 * output, else Y should come before. For example, let X and Y be 542 and 60. To
		 * compare X and Y, we compare 54260 and 60542. Since 60542 is greater than
		 * 54260, we put Y first.
		 */
		//int[] a = { 3, 30, 34, 5, 9 };
		int[] a = { 3, 2, 1 };
		String result = compare(a);
		System.out.println(result);
	}

	private static String compare(int[] a) {
		List<String> arr = new ArrayList<String>();
		for (int b : a) {
			arr.add(String.valueOf(b));
		}
		Collections.sort(arr, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String xy = o1 + o2; 
				// if xy.compareTo(yx) returns negative number this means that o1 needs to be appended in front or in other words, 
				// we want that o2 be placed lower than o1. hence we are returning -1 in xy.compareTo(yx) > 0 ? -1 : 1;
				String yx = o2 + o1; // less
				return xy.compareTo(yx) > 0 ? -1 : 1;
			}
		});
		String res = "";
		for (String c : arr) {
			res += c;
		}
		return res;
	}
}
