package heap;

public class Heapify {
	/*
	 * converts an array into a heap in O(n)
	 * */
	public static void main(String[] args) {
		//int[] a = {0,20,10,30,5,50,40};
		//int[] a = {0,1, 23, 12, 9, 30, 2, 50};
		int[] a = {0, 1, 6, 3, 5, 2};
		createMaxHeap(a); 
		//createMinHeap(a); 
		System.out.println();
		int k = 2;
		kthLargestElement(a,k);
		// logic for sorting after creating max heap
        for (int i = a.length - 1; i > 0; i--) {
            // Move current root to end
            int temp = a[0];
            a[0] = a[i];
            a[i] = temp;
            // call max heapify on the reduced heap
            heapify(a, i, 0);
        }
        for(int b : a) {
			System.out.print(" // "+b);
		}
		//int z = 2;
		//kthSmallestElement(a,z);
	}

	private static void createMinHeap(int[] a) {
		for(int i = (a.length-1)/2 ;i>0 ;i--) {
			heapify(a,i,a.length-1);
		}
		for(int b : a) {
			System.out.print(" || "+b);
		}
	}
	
	private static void heapify(int[] a, int i, int length) {
		int smallest = i;
		if(2*i<=length) {
			if(a[2*i] < a[smallest]) {
				smallest = 2*i;
			}
		}
		if((2*i)+1<=length) {
			if(a[(2*i)+1] < a[smallest]) {
				smallest = (2*i)+1;
			}
		}
		if(smallest != i) {
			int temp = a[i];
			a[i] = a[smallest];
			a[smallest] = temp;
			
			heapify(a,smallest,length);
		}
	}
	
	private static void heapifyMax(int[] a, int i, int length) {
		int largest = i;
		if(2*i<=length) {
			if(a[2*i] > a[largest]) {
				largest = 2*i;
			}
		}
		if((2*i)+1<=length) {
			if(a[(2*i)+1] > a[largest]) {
				largest = (2*i)+1;
			}
		}
		if(largest != i) {
			int temp = a[i];
			a[i] = a[largest];
		 	a[largest] = temp;
			
			heapifyMax(a,largest,length);
		}
	}

	private static void createMaxHeap(int[] a) {
		for(int i = (a.length-1)/2 ;i>0 ;i--) {
			heapifyMax(a,i,a.length-1);
		}
		for(int b : a) {
			System.out.print(" || "+b);
		}
	}

	private static void kthSmallestElement(int[] a, int z) {
		System.out.println(z+" th smallest element"+a[z]);
	}

	private static void kthLargestElement(int[] a, int k) {
		System.out.println(k+" th largest element : "+a[k]);
	}
}
