package heap;

public class MinToMaxHeap {
	public static void main(String[] args) {
		int[] arr = {0, 3, 5, 9, 6, 8, 20, 10, 12, 18, 9};
		/*[0 , 20 18 10 12 9 9 3 5 6 8]*/
		int maxIndex = 0; 
		int max = 0;
		for (int i = 0; i < arr.length; i++) {
			if(arr[i] > max) {
				maxIndex = i;
			}
		}
		int temp = arr[maxIndex];
		arr[maxIndex] = arr[1];
		arr[1] = temp;
		/*{0, 20, 5, 9, 6, 8, 3, 10, 12, 18, 9}*/
		
	}
	// To heapify a subtree with root at given index
    static void MaxHeapify(int arr[], int i, int n)
    {
        int l = 2*i + 1;
        int r = 2*i + 2;
        int largest = i;
        if (l < n && arr[l] > arr[i])
            largest = l;
        if (r < n && arr[r] > arr[largest])
            largest = r;
        if (largest != i)
        {
            // swap arr[i] and arr[largest]
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            MaxHeapify(arr, largest, n);
        }
    }
  
    // This function basically builds max heap
    static void convertMaxHeap(int arr[], int n)
    {
        // Start from bottommost and rightmost
        // internal mode and heapify all internal
        // modes in bottom up way
        for (int i = (n-2)/2; i >= 0; --i)
            MaxHeapify(arr, i, n);
    }
}
