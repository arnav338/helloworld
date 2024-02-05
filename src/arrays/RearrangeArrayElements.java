package arrays;

public class RearrangeArrayElements {
	public static void main(String[] args) {
		/*
		 * Given a sorted array of positive integers. Your task is to rearrange the
		 * array elements alternatively i.e first element should be max value, second
		 * should be min value, third should be second max, fourth should be second min
		 * and so on.
		 * 
		 * Example 1:
		 * 
		 * Input: N = 6 arr[] = {1,2,3,4,5,6} Output: 6 1 5 2 4 3 Explanation: Max
		 * element = 6, min = 1, second max = 5, second min = 2, array is : 6 1 5 2 4 3
		 * 
		 * this is to be achieved in O(n)
		 * 
		 * In this post a solution that requires O(n) time and O(1) extra space is
		 * discussed. The idea is to use multiplication and modular trick to store two
		 * elements at an index.
		 * 
		 * even index : remaining maximum element.
		 * 
		 * odd index : remaining minimum element.
		 * 
		 * 
		 * 
		 * max_index : Index of remaining maximum element
		 * 
		 * (Moves from right to left)
		 * 
		 * min_index : Index of remaining minimum element
		 * 
		 * (Moves from left to right)
		 * 
		 * 
		 * 
		 * Initialize: max_index = 'n-1'
		 * 
		 * min_index = 0
		 * 
		 * max_element = arr[max_index] + 1 //can be any element which is more than the maximum value in array
		 * For i = 0 to n-1
		 * 
		 * If 'i' is even
		 * 
		 * arr[i] += arr[max_index] % max_element * max_element
		 * 
		 * max_index--
		 * 
		 * ELSE // if 'i' is odd
		 * 
		 * arr[i] += arr[min_index] % max_element * max_element
		 * 
		 * min_index++
		 * 
		 * 
		 * 
		 */

	}

	/*
	 * 
	 * How does expression "arr[i] += arr[max_index] % max_element * max_element" work ? 
	 * The purpose of this expression is to store two elements at index arr[i]. 
	 * arr[max_index] is stored as multiplier and "arr[i]" is stored as remainder. 
	 * For example in {1 2 3 4 5 6 7 8 9}, max_element is 10 
	 * and we store 91 at index 0. 
	 * With 91, we can get original element as 91%10 
	 * and new element as 91/10. Below implementation of above idea:
	 */
	public static void rearrange(int arr[], int n) {
		// initialize index of first minimum and first
		// maximum element
		int max_idx = n - 1, min_idx = 0;

		// store maximum element of array
		int max_elem = arr[n - 1] + 1;

		// traverse array elements
		for (int i = 0; i < n; i++) {
			// at even index : we have to put
			// maximum element
			if (i % 2 == 0) {
				arr[i] += (arr[max_idx] % max_elem) * max_elem;
				max_idx--;
			}

			// at odd index : we have to put minimum element
			else {
				arr[i] += (arr[min_idx] % max_elem) * max_elem;
				min_idx++;
			}
		}

		// array elements back to it's original form
		for (int i = 0; i < n; i++)
			arr[i] = arr[i] / max_elem;
	}
}
