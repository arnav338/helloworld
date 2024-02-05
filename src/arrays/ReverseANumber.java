package arrays;

public class ReverseANumber {
	public static void main(String[] args) {
		System.out.println(reverse(123456789));
	}
	private static int reverse(int i) {
		// time complexity - O(log(n)) where n is the number of digits in the given integer. 
		// Because there are only log(n) digits in a number n.
		int a = 0;
		int temp=i;
		while(temp>0) {
			int b = (temp%10);
			if(a==0) {
				a += b;
			}
			else {
				a = a*10 + b;
			}
			temp = temp/10;
		}
		return a;
	}
}
