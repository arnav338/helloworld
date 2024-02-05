package arrays;

import java.util.Vector;

public class Complement_kadane {
	/*
	 * You are given a binary string str. In a single operation, you can choose two
	 * indices L and R such that 1 ≤ L ≤ R ≤ N and complement the characters between
	 * L and R i.e strL, strL+1, …, strR. By complement, we mean change character 0
	 * to 1 and vice-versa.
	 * 
	 * You task is to perform ATMOST one operation such that in final string number
	 * of 1s is maximised. If there is no need to completement, i.e., string
	 * contains all 1s, return -1. Else, return the two values denoting L and R. If
	 * there are multiple solutions, return the lexicographically smallest pair of L
	 * and R.
	 * 
	 * Example 1:
	 * 
	 * Input: 
	 * N = 3 
	 * str = "111" 
	 * Output: -1 
	 * Explanation: As all characters are '1', so don't need to complement any.
	 */
	public static void main(String[] args) {
		String s = "1000101011";
		Vector<Integer> v = findRange(s, s.length());
		System.out.println();
	}
	
	static Vector<Integer> findRange(String str, int n) {
	    // using kadane algorithm
		Vector<Integer> v = new Vector<>();
		char[] temp = str.toCharArray();
		int countOfZero = 0;
        for(char c : temp){
            if(c == '0'){ countOfZero++; }
        }
        if(countOfZero == 0){
            v.add(-1);
            return v;
        }
	       //msf - max so far
	       //meh - max ending here
	       //s - start of new sub array
	       //maxl.maxr - start and end index of the maximum sub array 
	      
	       int maxl=0,maxr=0,msf=0,meh=0,s=0;
	       
	       for(int i=0;i<str.length();i++){
	           meh += (temp[i]=='0') ? 1 : -1; //we have to find a sub array with max zeros and min ones
	           
	           if(msf<meh){
	               //found a new max so updating the required variables
	               msf=meh;
	               maxl=s;
	               maxr=i;
	           }
	           
	           if(meh<0){
	               meh=0;
	               s=i+1; //start point of a new subarray
	           }
	       }
	       v.add(maxl+1);
	       v.add(maxr+1);
	       return v; //adding 1 to index to convert it to position
	   }

}
