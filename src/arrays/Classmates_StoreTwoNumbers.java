package arrays;

public class Classmates_StoreTwoNumbers {
	public static void main(String[] args) {
		/*
		 * Geek and his classmates are playing a prank on their Computer Science teacher. 
		 * They change places every time the teacher turns to look at the blackboard. 
		
		Each of the N students in the class can be identified by a unique roll number X 
		and each desk has a number i associated with it. Only one student can sit on one desk. 
		Each time the teacher turns her back, 
		a student with roll number X sitting on desk number i gets up and 
		takes the place of the student with roll number i.
		
		If the current position of N students in the class is given to you in an array, 
		such that i is the desk number and a[i] is the roll number of the student sitting on the desk, 
		can you modify a[ ] to represent the next position of all the students ? 
		Example 1:
		
		Input:
		N = 6
		a[] = {0, 5, 1, 2, 4, 3} it is indirectly saying that element a[a[i]] will be stored at a[i]
		Output: 0 3 5 1 4 2
		Explanation: After reshuffling, the modified 
		position of all the students would be 
		{0, 3, 5, 1, 4, 2}.
		
		we can store two numbers in one joint number and extract those two numbers after

		j= a + b * n  ( n can be any number larger than both a and b)
		
		To get initial value of a we do a%n To get the value of b we do a/n
		
		a = a + b * n = 3 + 2*5 = 13
		
		j% n = 13 % 5 = 3 = a
		
		j / n = 13 / 5 = 2 = b
		
		So we will use this technique to store two numbers in one location.
		
		 
		
		for(int i=0;i<n;i++)
		    {
		        nums[i] = nums[i] + n*(nums[nums[i]]);
		    }
		
		 
		
		but there is a problem what if the nums[nums[i]] is already a joint number (a+b*n) but we need the original value before any modification 
		
		so we need to extract the original value using j%n where j in joint number.
		
		so we will use this if number is not modified then there will be no effect since n is larger than every num else we get the original value before modification.
		
		 * */
	}
	void prank(long[] a, int n)  
    {   
        for(int i=0;i<n;i++)
      {
          a[i] = a[i] + n*(a[(int) a[i]]%n);
      }
      
      for(int i=0;i<n;i++)
      {
          a[i] = a[i]/n;
      }
    }
}
