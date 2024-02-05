package dynamic;

import java.util.HashMap;
import java.util.Map;

public class UglyNumbers {
	public static HashMap<Integer,Boolean> map;
	public static void main(String[] args) {
		map = new HashMap<>();
		long start = System.nanoTime();
		System.out.println("->"+findNthUglyNumber(150));
		long end = System.nanoTime();
		System.out.println("algo took : "+(end-start)*Math.pow(10, -9)+" sec");
		Map<Character,Integer> c = new HashMap<>();
		c.put('d', 1);
		System.out.println("->"+c.get('e'));
	}
	private static int findNthUglyNumber(int n) {
		if(n==1) return 1;
		int k = 1;
		int res=0;
		for(int i = 2;; i++) {
			if(isUgly(i)) {
				System.out.println(i+" is ugly");
				map.put(i, true);
				k++;
			}
			else {
				System.out.println(i+" is not ugly");
			}
			if(k==n) {
				res=i;
				break;
			}
		}
		return res;
	}

	private static boolean isUgly(int i) {
		if(i==1) { 
			return true; 
		}
		else if(i%2 == 0) { 
			return map.get(i/2)!=null ? true : isUgly(i/2);
			//return isUgly(i/2);
		}
		else if(i%3 == 0) {
			return map.get(i/3)!=null ? true : isUgly(i/3);
			//return isUgly(i/3);
		}
		else if(i%5 == 0) {
			return map.get(i/5)!=null ? true : isUgly(i/5);
			//return isUgly(i/5);
		}
		else {
			return false;
		}
	}
	
}
