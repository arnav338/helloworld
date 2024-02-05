package heap;

public class InsertAndDelete {

	public static void main(String[] args) {
		// Array representation of Max-Heap
	    //    10
	    //    /  \
	    //   5     3
	    //  / \   /
	    // 2   4  54
	    int arr[] = { 0, 10, 5, 3, 2, 4 };
	    int res[] = insert(arr,54);
		for(int a : res) {
			System.out.println("-"+a);
		}
		System.out.println("============");
			//	   54
		    //    /  \
		    //   5    10
		    //  / \   /
		    // 2   4  3
		res = delete(res);
		for(int a : res) {
			System.out.println("-"+a);
		}
	}
	
	private static int[] delete(int[] arr) {
		int temp[] = new int[arr.length-1];
		temp[0] = 0;
		temp[1] = arr[arr.length-1]; // setting last element as first
		for (int i = 2; i < temp.length; i++) {
			temp[i] = arr[i]; // copying initial array in temp
		}
		int i = 1;
		while(i < temp.length && ((i*2)+1) < temp.length) {
			if(temp[i] < temp[i*2] || temp[i] < temp[(i*2)+1]) {
				if(temp[i] < temp[(i*2)+1]) {
					int d = temp[i];
					temp[i] =  temp[(i*2)+1];
					temp[(i*2)+1] = d;
					i = (i*2)+1;
				}
				else if(temp[i] < temp[i*2]) {
					int d = temp[i];
					temp[i] =  temp[i*2];
					temp[i*2] = d;
					i = i*2;
				}
			}
		}
		return temp;
	}
	
	private static int[] insert(int[] arr, int k) {
		if(arr.length == 0) return null;
		int temp[] = new int[arr.length+1];
		for (int i = 0; i < arr.length; i++) {
			temp[i] = arr[i]; // copying intial array in temp
		}
		temp[temp.length-1] = k;
		int curr = (temp.length-1);// setting current element
		while(curr > 1 ) {
			int cur = temp[curr];
			int parent = temp[curr/2];
			if(cur > parent ) {
				int d = temp[curr];
				temp[curr] =  temp[curr/2];
				temp[curr/2] = d;
			}
			curr /= 2;
		}
		arr = temp;
		return temp;
	}

}
