package heap;

public class DriveCar {
	public static void main(String[] args) {
		long[] a = {7, 4, 7, 2, 2};
		System.out.println("++++++"+required(a, a.length, 1));
	}
	
	public static long required(long a[], long n, long k) {
        long[] temp = new long[(int) n+1];
        temp[0] = 0;
        long res = -1;
        for(int i=1; i< temp.length; i++){
            temp[i] = a[i-1];
        }
        a = temp;
        createMaxHeap(a);
        for (int i = a.length - 1; i > 0; i--) {
            long h = a[0];
            a[0] = a[i];
            a[i] = h;
            heapifyMax(a, i, a.length - 1);
        }
        for (long i = 0; i < a.length; i++) {
			System.out.print(a[(int) i]+"||");
		}
        if(a[0] < k){
            return res;
        }
        res = 0;
        for(long b : a){
            if(b > k) {
            	System.out.println("->"+b);
            	res = Math.max(res, (b-k));
            }
            else break;
        }
        System.out.println();
        return res;
    }
    	private static void heapifyMax(long[] a, long i, int length) {
		long largest = i;
		if(2*i<=length) {
			if( a[(int) 2*(int) i] >  a[(int) largest]) {
				largest = 2*i;
			}
		}
		if((2*i)+1<=length) {
			if(a[(2*(int) i)+1] > a[(int) largest]) {
				largest = (2*i)+1;
			}
		}
		if(largest != i) {
			long temp = a[(int) i];
			a[(int) i] = a[(int) largest];
		 	a[(int) largest] = temp;
			
			heapifyMax(a,largest,length);
		}
	}

	private static void createMaxHeap(long[] a) {
		for(long i = (a.length-1)/2 ;i>0 ;i--) {
			heapifyMax(a,i,a.length-1);
		}
		
	}
}
