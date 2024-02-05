package arrays;

public class ZigZag {
	public static void main(String[] args) {
		/*
		 * Given an array Arr (distinct elements) of size N. Rearrange the elements of
		 * array in zig-zag fashion. The converted array should be in form a < b > c < d
		 * > e < f. The relative order of elements is same in the output i.e you have to
		 * iterate on the original array only.
		 * 
		 * Example 1:
		 * Input: 
		 * N = 7 
		 * Arr[] = {4, 3, 7, 8, 6, 2, 1} 
		 * Output: 3 7 4 8 2 6 1 
		 * Explanation: 3 < 7 > 4 < 8 > 2 < 6 > 1
		 * 
		 * The concept is that in the resultant array, the elements at even position(not index) 
		 * are greater than the nieghbouring elements
		 * So at every element we will be checking that if the index is even and if a[i] > a[i+1]
		 */
		int arr[] = {4, 3, 7, 8, 6, 2, 1};
		for(int i = 1 ; i<arr.length; i++){
            if(i%2 == 0){
                if(arr[i-1] < arr[i]){
                    arr[i] = arr[i] ^ arr[i-1];
                    arr[i-1] = arr[i] ^ arr[i-1];
                    arr[i] = arr[i] ^ arr[i-1];
                }
            }
            else if(i%2 == 1){
                if(arr[i-1] > arr[i]){
                    arr[i] = arr[i] ^ arr[i-1];
                    arr[i-1] = arr[i] ^ arr[i-1];
                    arr[i] = arr[i] ^ arr[i-1];
                }
            }
        }
		for (int i = 0; i < arr.length; i++) {
			System.out.print("||"+arr[i]);
		}
	}
}
