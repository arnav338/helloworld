package arrays;

public class MajorityElement_BoyerMoore {
	public static void main(String[] args) {
		/* * array of size N given
		 * 
		 * we have to find the element which is present more than N/2 times
		 * 
		 * a = {1,4,5,1,1}
		 * 
		 * answer = 1
		 * 
		 * if we try to sort and then find freq using single for loop -> time complexity is O(n*log n)
		 * 
		 * if we use hashmap and store key value pair -> time - O(n) , space - O(n)
		 * 
		 * So to use constant time we will use Boyer Moore
		 * */
		int[] a = {4,3,2,1,4,4,4};
		System.out.println(findMajorityElement(a)+" occurs maximum times");
	}
	
	public static int findMajorityElement(int[] a){
		int curr = a[0];
		int freq = 1;
		for (int i = 1; i < a.length; i++) {
			if(a[i]==curr) {
				freq++;
			}
			else { freq--;
			}
			if(freq==0) { // replace the element
				curr = a[i];
				freq++;
			}
		}
		int temp=0;
		for (int i = 0; i < a.length; i++) {
			if(a[i]==curr) {
				temp++;
			}
		}
		return temp>a.length/2 ? curr : 0;
	}
}