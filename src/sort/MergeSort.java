package sort;

public class MergeSort {
	
	public static void main(String args[]) {
		// time complexity - O(n)
		int[] list = {1,7,2,15,17,21,13,11,8,5};
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

	private static void mergeSort(int[] list, int left, int right) {
		int mid = 0;
		if(left<right) {
			mid = (left+right)/2; // Finding middle part
			mergeSort(list, left, mid); // sort 1st half
			mergeSort(list, mid+1, right); // sort 2nd half
			System.out.println("left:"+left+"||mid:"+mid+"||right:"+right); 
			merge(list,left,mid,right); // merge sorted halves
		}
	}

	private static void merge(int[] arr, int l, int m, int r) {
		// Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
  
        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];
  
        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];
  
        /* Merge the temp arrays */
  
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
  
        // Initial index of merged subarray array
        int k = l;
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
  
        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
  
        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
		
	}
}