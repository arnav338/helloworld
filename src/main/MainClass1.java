package main;

import java.util.HashMap;
import java.util.Map;

public class MainClass {
	
	public static void main(String args[]) {
		
		int n=5;
		fibonacci(n);
	}

	private static int fibonacci(int n) {
		if(n==0 || n==1) {
			//System.out.println("0");
			return n;
		}
		return fibonacci(n-1)+fibonacci(n-2);
		/*else {
			int a = fibonacci(n-1)+fibonacci(n-2);
			System.out.println(a);
			return a;
		}*/
			
			
		
	}
	
	
	
}
