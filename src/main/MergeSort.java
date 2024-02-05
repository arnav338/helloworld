package main;

public abstract class MergeSort {
	// time complexity - O(n*log n)
	// space complexity - O(n)
	public static void main(String args[]) {
		
		
	//  1 7 2 15 17 || 21 13 11 8 5
	//  1 7 2 || 15 17 || 21 13 11 || 8 5
	//  1 7 || 2 || 15 17 || 21 13 || 11 || 8 5
	//  1 || 7 || 2 || 15 || 17 || 21 || 13 || 11 || 8 || 5		
	//  0    1    2    3     4     5     6     7     8    9
		mergeSort(list,0,list.length-1);
		for(int a : list) {
			System.out.print(a+" || ");
		}
	}
	static int[] list = {1,7,2,15,17,21,13,11,8,5};
	private static int[] result = new int[list.length];
	private static void mergeSort(int[] list, int left, int right) {
		int mid = 0;
		if(left<right) {
			mid = (left+right)/2; // Finding middle part
			mergeSort(list, left, mid); // sort 1st half
			mergeSort(list, mid+1, right); // sort 2nd half
			merge(list,left,mid,right); // merge sorted halves
		}
	}
	
	private static void merge(int[] arr, int left, int mid, int right) {
		// Find sizes of two subarrays to be merged
        int n1 = mid - left + 1;
        int n2 = right - mid;
        // Create temp arrays 
        int L[] = new int[n1];
        int R[] = new int[n2];
        //Copy data to temp arrays
        for (int i = 0; i < n1; ++i)
            L[i] = arr[left + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[mid + 1 + j];
        // Merge the temp arrays 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
        // Initial index of merged subarray array
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        // Copy remaining elements of L[] if any 
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        // Copy remaining elements of R[] if any 
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
	}
}