package arrays;

import java.util.ArrayList;

public class SubsetOfString {
	
	public static void main(String[] args) {
		/*
		 *aim is to print all possible subsequences of a string
		 *
		 * abc .\-> { "", a, b, c, ac, ab, bc, abc }
		 * 
		 */
		
		String a = "abc";
		ArrayList<String> s = new ArrayList<String>();
		addSubSequences(a,s,"",0);
		s.stream().forEach(System.out::println);
		System.out.println("=="+s.size());
		
	}
	
	private static void addSubSequences(String input, ArrayList<String> output,String current,int i) {
		if(i == input.length()) {
			output.add(current);
			return;
		}
		// in every iteration we are either 
		// adding the current character - current+input.charAt(i)
		// or ignoring the current character - current
		addSubSequences(input, output, current+input.charAt(i),i+1);
		addSubSequences(input, output, current,i+1);
	}
}