package sort;

public class SelectionSort {
	public static void main(String[] args) {
		int a[] = {11,5,7,2,1,9};
		selection(a);
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+"||");
		}
	}
	
	private static void selection(int[] a) {
		for (int i = 0; i < a.length; i++) {
			int min = i; // we will find a min in every iteration and put it at front
			// it differs from insertion sort as in insertion we were not selecting elements
			for (int j = i+1; j < a.length; j++) {
				if(a[j] < a[min]) {
					min = j;
				}
			}
			if(min!=i) {
				swap(a,min,i);
			}
		}
	}

	private static void swap(int[] a, int min, int i) {
		int temp = a[min];
		a[min] = a[i];
		a[i] = temp;
	}
}
