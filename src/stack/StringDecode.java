package stack;

import java.util.Stack;

public class StringDecode {
	/*
	 * An encoded string (s) is given, the task is to decode it. The pattern in
	 * which the strings are encoded is as follows.
	 * 
	 * <count>[sub_str] ==> The substring 'sub_str' appears count times. 
	 * Examples:
	 * 
	 * Input : str[] = "1[b]" Output : b
	 * 
	 * Input : str[] = "2[ab]" Output : abab
	 * 
	 * Input : str[] = "2[a2[b]]" Output : abbabb
	 * 
	 * Input : str[] = "3[b2[ca]]" Output : bcacabcacabcaca
	 * 
	 * Input : str[] = "3[b2[ca]3[da]]" Output : bcacadadadabcacadadadabcacadadada
	 */
	public static void main(String[] args) {
		String a = "3[b2[ca]]";
		String code = decode(a);
		System.out.println("->->" + code);
	}

	private static String decode(String s) {
		String result = "";
		boolean flag = false;
		String temp = "";
		Stack<Character> stack = new Stack<Character>();
		for(int i=0; i < s.length() ;i++ ) {
			if(s.charAt(i)!=']') {
				stack.push(s.charAt(i));
			}
		}
		while(!stack.isEmpty()) {
			char curr = stack.pop();
			if(curr == '[' && flag) flag = false;
			if(!Character.isDigit(curr) && curr!='[' && !flag) {
				temp = curr + temp;
			}
			else if(curr == '[' && Character.isDigit(stack.peek()) ) {
				int d = Integer.valueOf(""+stack.peek());
				if(temp.isEmpty()) {
					System.out.println("empty");
					temp = result;
					result = "";
					for(int j = 1; j<=d; j++) {
						result += temp;
					}
				}
				else {
					for(int j = 1; j<=d; j++) {
						result += temp;
					}
				}
				temp="";
			}
			else if(flag && !Character.isDigit(curr) && curr!='[') {
				result = curr + result;
			}
			else {
				flag = true;
			}
		}
		return result;
	}
}
