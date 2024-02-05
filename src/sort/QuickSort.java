package sort;

public class QuickSort {
// worst time complexity - O(n^2) : when pivot is always the smallest element
// worst case occurs when all elements of array are already sorted either increasing or decreasing
// average : O(n*log n)
// adv pf quick sort : no extra space
	public static void main(String[] args) {
		int a[] = {11,5,7,2,1,9};
		quickSort(a,0,a.length-1);
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+"||");
		}
	}
	
	// A utility function to swap two elements
	static void swap(int[] arr, int i, int j)
	{
	    int temp = arr[i];
	    arr[i] = arr[j];
	    arr[j] = temp;
	}
	 
	/* This function takes last element as pivot, places
	   the pivot element at its correct position in sorted
	   array, and places all smaller (smaller than pivot)
	   to left of pivot and all greater elements to right
	   of pivot */
	static int partition(int[] arr, int low, int high)
	{
	     
	    // pivot
	    int pivot = arr[high];
	    // Index of smaller element and indicates the right position
	    // of pivot found so far
	    int i = (low - 1); // this will be -1 initially, as this represents which index element has to be swapped with the current element
	    // so initially we havent found any element that can be replaced with current element so its -1
	 
	    for(int j = low; j <= high - 1; j++)
	    {
	        // If current element is smaller than the pivot
	        if (arr[j] < pivot)
	        {
	             
	            // Increment index of smaller element
	            i++;// we found an element that can be repalced with the current one
	            swap(arr, i, j);
	        }
	    }
	    // we have moved all the elemets that were lesser than pivot to the left of pivot
	    // but now we also need to place the actual pivot for that we are doing this swap
	    swap(arr, i + 1, high);
	    return (i + 1);
	}
	 
	/* The main function that implements QuickSort
	          arr[] --> Array to be sorted,
	          low --> Starting index,
	          high --> Ending index
	 */
	static void quickSort(int[] arr, int low, int high)
	{
	    if (low < high)
	    {
	        // pi is partitioning index, arr[partition] is now at right place
	        int partition = partition(arr, low, high);
	        // Separately sort elements before partition and after partition
	        quickSort(arr, low, partition - 1);
	        quickSort(arr, partition + 1, high);
	    }
	}
	
}
