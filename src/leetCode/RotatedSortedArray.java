package leetCode;

public class RotatedSortedArray {
	
	public static void main(String[] args) {
		/*
		 * Sorted array {1,2,3,4,5,6,7,8}
		 * Rotated sorted array {4,5,6,7,8,1,2,3}
		 * 
		 * Task is to find a key in the rotated array in O(log n)
		 * 
		 * Approach: 

			The idea is to find the pivot point, divide the array in two sub-arrays and perform binary search.
			The main idea for finding pivot is – for a sorted (in increasing order) and pivoted array, pivot element is the only element for which next element to it is smaller than it.
			Using the above statement and binary search pivot can be found.
			After the pivot is found out divide the array in two sub-arrays.
			Now the individual sub – arrays are sorted so the element can be searched using Binary Search.
		 * 
		 * */
		int[] arr = new int[] {4,5,6,7,8,1,2,3};
		int k=3;
		System.out.println("Result : "+findIndexOfElement(arr,k,0,arr.length-1));
		/*
		 * Time Complexity: O(log n). 
		Binary Search requires log n comparisons to find the element. So time complexity is O(log n).
		Space Complexity: O(1). 
		As no extra space is required.
		 * */
	}

	private static int findIndexOfElement(int[] arr, int k, int i, int j) {
		int pivot = findPivot(arr,k,i,j);
		System.out.println("Pivot is : "+pivot);
		if(k > arr[pivot]) {
			return BinarySearch.binarySearch(arr, k, 0, pivot);
		}
		return BinarySearch.binarySearch(arr, k, pivot+1, j);
	}

	private static int findPivot(int[] arr, int k, int i, int j) {
		int pivot=0;
		for(int l=0; l<j-1;l++) {
			if(arr[l]>arr[l+1]) {
				pivot= l;
				break;
			}
		}
		return pivot;
	}
}
