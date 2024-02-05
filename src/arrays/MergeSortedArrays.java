package arrays;

public class MergeSortedArrays {
	public static void main(String[] args) {
		/*
		 * Given two sorted arrays arr1[] and arr2[] of sizes n and m in non-decreasing
		 * order. Merge them in sorted order without using any extra space. Modify arr1
		 * so that it contains the first N elements and modify arr2 so that it contains
		 * the last M elements.
		 * 
		 * Example 1:
		 * 
		 * Input: 
		 * n = 4, arr1[] = [1 3 5 7] 
		 * m = 5, arr2[] = [0 2 6 8 9] 
		 * Output: 
		 * arr1[] =[0 1 2 3] 
		 * arr2[] = [5 6 7 8 9] 
		 * Explanation: 
		 * After merging the two 
		 * non-decreasing arrays, 
		 * we get, 0 1 2 3 5 6 7 8 9.
		 */
		
		int arr1[] = {1, 3, 5, 7}; 
		int arr2[] = {0, 2, 6, 8, 9};
		merge(arr1,arr2,arr1.length,arr2.length);
		for (int i = 0; i < arr1.length; i++) {
			System.out.print(arr1[i]+" ");
		}
		System.out.println();
		for (int i = 0; i < arr2.length; i++) {
			System.out.print(arr2[i]+" ");
		}
		
		/* Swap using XOR
		int a=5,b=7;
		a = a ^ b;
		b= a ^ b;
		a = a ^ b;
		System.out.println("a :"+a+" b: "+b);
		*/
	}

	private static void merge(int[] arr1, int[] arr2, int length, int length2) {
		int low = 0;
		int high = 0;
		
		while(low < arr1.length && high < arr2.length) {
			if(arr1[low]>arr2[high]) {
				System.out.println("arr1[low]:"+arr1[low]+"||arr2[high]:"+arr2[high]);
				arr1[low] = arr1[low] ^ arr2[high];
				arr2[high] = arr1[low] ^ arr2[high];
				arr1[low] = arr1[low] ^ arr2[high];
				System.out.println("arr1[low]:"+arr1[low]+"||arr2[high]:"+arr2[high]);
				System.out.println("++++++++++++++++++++++++++");
				low++;
			}
			else if(arr1[low]>arr2[high]) {
				high++;
			}
		}
		
	}
}
