package leetCode;

public class RotateArray {

	public static void main(String[] args) {
		// output: [6, 7, 8, 1, 2, 4, 5]
		int[] a = new int[] {1,2,4,5,6,7,8};
		int k=3;
		rotateArray(a,k);

	}

	private static void rotateArray(int[] a, int k) {
		int i=0;
		int c = a.length-k-1;
		while(true) {
			if(i < c) {
				swap(a,i,k+1);
				i++;
				k++;
			}
			else {
				for(int b : a) {
					System.out.println("->"+b);
				}
				System.out.println("++++");
				if(i!=k+1) {
					for(int j = i;j<a.length-1;j++) {
						swap(a,j,j+1);
					}
				}
				for(int b : a) {
					System.out.println("->"+b);
				}
				break;
			}
		}
		
	}

	private static void swap(int[] a, int j, int i) {
		int temp = a[j];
		a[j] = a[i];
		a[i] = temp;
	}

}
