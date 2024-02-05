package sort;

public class InsertionSort {
	public static void main(String[] args) {
		// we will take one element in the unsorted array and compare with every element in unsorted one
		int a[] = {11,5,7,2,1,9};
		insertion(a);
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+"||");
		}
	}
	
	private static void insertion(int[] a) {
		int temp = 0;
		int j = 0;
		for(int i=1;i<a.length;i++) {
			temp = a[i];
			j = i-1;
			while(j>=0 && a[j]>=temp) {
				a[j+1] = a[j];
				j--;
			}
			a[j+1] = temp;
		}
	}
}