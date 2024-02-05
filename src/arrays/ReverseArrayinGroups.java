package arrays;

public class ReverseArrayinGroups {
	public static void main(String[] args) {
		/*
		 * Given an array arr[] of positive integers of size N. Reverse every sub-array
		 * group of size K.
		 * 
		 * 
		 * 
		 * Example 1:
		 * 
		 * Input: 
		 * N = 5, 
		 * K = 3 
		 * arr[] = {1,2,3,4,5} 
		 * Output: 3 2 1 5 4 
		 * Explanation: First group consists of elements 1, 2, 3. Second group consists of 4,5.
		 * 
		 */
		int[] arr = {1,2,3,4,5};
		int k = 3;
		reverse(arr,arr.length,k);
		for(int b : arr) {
			System.out.print("||"+b);
		}
	}

	private static void reverse(int[] arr, int length, int k) {
		for (int i = 0; i < length; i += k)
        {
            int left = i;
     
            // to handle case when k is not multiple of n
            int right = Math.min(i + k - 1, length - 1);
            int temp;
             
            // reverse the sub-array [left, right]
            while (left < right)
            {
                temp=arr[left];
                arr[left]=arr[right];
                arr[right]=temp;
                left+=1;
                right-=1;
            }
        }
	}
}
