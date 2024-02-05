package stack;

import java.util.Stack;

public class BalancedParenthesis {

	public static void main(String[] args) {
		/*
		 * a = "{{}}()("
		 * we have to check if there exists closing bracket for all opening brackets or not
		 * */
		String a = "{{}}()(";
		//String a = "{{}}()";
		System.out.println("->->"+isValid(a));
	}
	
	private static boolean isValid(String a) {
		Stack<Character> s = new Stack<Character>();
		
		for (int i = 0; i < a.length(); i++) {
			char c = a.charAt(i);
			if(c == '(' || c == '{' || c == '[') {
				s.push(c);
			}
			else {
				if(s.isEmpty()) return false; // means there is no corresponding opening bracket
				else if(c=='}') {
					if(s.peek() != '{') return false;
					else s.pop();
				}
				else if(c==')') {
					if(s.peek() != '(') return false;
					else s.pop();
				}
				else if(c==']') {
					if(s.peek() != '[') return false;
					else s.pop();
				}
			}
		}
		return s.isEmpty();
	}

}
