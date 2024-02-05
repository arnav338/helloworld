package arrays;

import java.util.List;

public class JosephusProblem {
	public static void main(String[] args) {
		/*
		 * 
		 * n friends standing in a circle. they all have 1 gun in total
		 * they start killing the person at a distance of k from each other
		 * Find which friend is alive at the end
		 * 
		 * After the first person (kth from beginning) is killed, n-1 persons are left. 
		 * So we call josephus(n – 1, k) to get the position with n-1 persons. 
		 * But the position returned by josephus(n – 1, k) will consider the position 
		 * starting from k%n + 1. So, we must make adjustments to the position returned by josephus(n – 1, k). 
		 * 
		 * */
		
		System.out.println(jos(5,3));
	}

	private static int jos(int n, int k) {
		if(n==1) return 0;
		// we are doing n-1 as the index of person changes after a person is killed
		return (jos(n-1, k)+k)%n;
	}
	
	
	// in this case we will have to pass k-1, as 0 based indexing is to be used
	// so if 2nd person has to be killed we will pass 1
	// as then only 0,1(this person will be killed) 
	static void Josh(List<Integer> person, int k, int index)
	  {
	     
	    // Base case , when only one person is left
	    if (person.size() == 1) {
	      System.out.println(person.get(0));
	      return;
	    }
	 
	    // find the index of first person which will die
	    index = ((index + k) % person.size());
	 
	    // remove the first person which is going to be killed
	    person.remove(index);
	 
	    // recursive call for n-1 persons
	    Josh(person, k, index);
	  }
}
