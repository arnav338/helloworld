package arrays;

import java.util.ArrayList;

public class SubArrayWithGivenSum {
	public static void main(String[] args) {
		//int[] a = new int[] {1,2,3,7,5};
		//int k = 12;
		int[] a = new int[] {1,2,3,4,5,6,7,8,9,10};
		int k = 15;
		ArrayList<Integer> result = calculate(a,a.length,k);
		result.stream().forEach(System.out::println);
	}
	private static ArrayList<Integer> calculate(int[] arr, int n, int s) {
		   ArrayList<Integer> result = new ArrayList<>();
	       int start=0,end=0,currentsum=0;
	       
	       while(end < n){
	           currentsum+=arr[end];
	           
	           if(currentsum == s){
	               result.add(start+1);
	               result.add(end+1);
	               return result;
	           }
	           
	           while(currentsum>s){
	               currentsum -= arr[start];
	               start++;
	               
	               if(currentsum==s){
	                    result.add(start+1);
	                    result.add(end+1);
	                    return result;
	               }
	           }
	           
	           if(currentsum<s){
	               end++;
	           }
	       }
	       
	       result.add(-1);
	       return result;
	}
/*
	private static ArrayList<Integer> calculate(int[] arr, int n, int s) {
		LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
		ArrayList<Integer> output = new ArrayList<Integer>();
		int sum=0;
		for(int i=0;i<n;i++) {
			sum += arr[i];
			map.put(i, arr[i]);
			if(sum==s) {
				System.out.println(map.keySet().stream().findFirst().get()+" || "+i);
				output.add(map.keySet().stream().findFirst().get()+1);
				output.add(i+1);
				break;
			}
			else if(sum>s) {
				System.out.println("--"+sum);
				while(true) {
					int key = map.keySet().stream().findFirst().get();
					sum -= map.get(key);
					map.remove(key);
					if(sum<=s) break;
				}
				System.out.println("-->"+sum);
				if(sum==s) {
					output.add(map.keySet().stream().findFirst().get()+1);
					output.add(i+1);
					break;
				}
			}
		}
		return output;
	}*/
}
