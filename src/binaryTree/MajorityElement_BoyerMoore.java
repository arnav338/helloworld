package binaryTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		 * 
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
	
	public static List<Integer> findMajorityThree(int[] nums){
		/* element occuring more than n/3 
		 * there cant be more than 2 elements that satisfy the above condition
		 * */
		List<Integer> result = new ArrayList<>();
        Map<Integer,Integer> map = new HashMap<>();
        for(int a : nums){
            if(map.containsKey(a)){
                map.put(a,map.get(a)+1);
            }
            else{
                map.put(a,1);
            }
        }
        map.keySet().stream().filter( (b)->
        	map.get(b) > nums.length
        ).forEach(e -> result.add(e));
        return result;
	}
	
	// A structure to store an element and its current count
	static class eleCount {
	    int e; // Element
	    int c; // Count
	};
	 
	// Prints elements with more
	// than n/k occurrences in arr[]
	// of size n. If there are no
	// such elements, then it prints
	// nothing.
	static void moreThanNdK(int arr[], int n, int k)
	{
	    // k must be greater than
	    // 1 to get some output
	    if (k < 2)
	        return;
	 
	    /* Step 1: Create a temporary
	       array (contains element
	       and count) of size k-1.
	       Initialize count of all
	       elements as 0 */
	    eleCount []temp = new eleCount[k - 1];
	    for (int i = 0; i < k - 1; i++)
	        temp[i] = new eleCount();
	    for (int i = 0; i < k - 1; i++) {
	        temp[i].c = 0;
	    }
	   
	    /* Step 2: Process all
	      elements of input array */
	    for (int i = 0; i < n; i++)
	    {
	        int j;
	 
	        /* If arr[i] is already present in
	           the element count array,
	           then increment its count
	         */
	        for (j = 0; j < k - 1; j++)
	        {
	            if (temp[j].e == arr[i])
	            {
	                temp[j].c += 1;
	                break;
	            }
	        }
	 
	        /* If arr[i] is not present in temp[] */
	        if (j == k - 1) {
	            int l;
	 
	            /* If there is position available
	              in temp[], then place arr[i] in
	              the first available position and
	              set count as 1*/
	            for (l = 0; l < k - 1; l++)
	            {
	                if (temp[l].c == 0)
	                {
	                    temp[l].e = arr[i];
	                    temp[l].c = 1;
	                    break;
	                }
	            }
	 
	            /* If all the position in the
	               temp[] are filled, then decrease
	               count of every element by 1 */
	            if (l == k - 1)
	                for (l = 0; l < k-1; l++)
	                    temp[l].c -= 1;
	        }
	    }
	 
	    /*Step 3: Check actual counts of
	     * potential candidates in temp[]*/
	    for (int i = 0; i < k - 1; i++)
	    {
	       
	        // Calculate actual count of elements
	        int ac = 0; // actual count
	        for (int j = 0; j < n; j++)
	            if (arr[j] == temp[i].e)
	                ac++;
	 
	        // If actual count is more than n/k,
	       // then print it
	        if (ac > n / k)
	            System.out.print("Number:" +  temp[i].e
	                + " Count:" +  ac +"\n");
	    }
	}
	
	public static List<Integer> findMajorityThreeConstantSpace(int[] nums){
		/* element occuring more than n/3 
		 * there cant be more than 2 elements that satisfy the above condition
		 * */
		List<Integer> result = new ArrayList<>();
		int candidate1 = 0;
		int candidate2 = 0;
		int freq1 = 0;
		int freq2 = 0;
        for(int a : nums){
            if(a == candidate1) {
            	freq1++;
            }
            else if(a == candidate2) {
            	freq2++;
            }
            else if(freq1 ==0) {
            	candidate1 = a;
            	freq1 = 1;
            }
            else if(freq2 ==0) {
            	candidate2 = a;
            	freq2 = 1;
            }
            else {
            	freq1--;
            	freq2--;
            }
        }
        freq1 = 0;
        freq2 = 0;
        for(int b : nums){
            if(b == candidate1) freq1++;
            if(b == candidate2) freq2++;
        }
        if(freq1 > nums.length/3) result.add(candidate1);
        if(freq2 > nums.length/3 && !result.contains(candidate2)) result.add(candidate2); // to ensure same element doesnt go in result
        return result;
	}
}