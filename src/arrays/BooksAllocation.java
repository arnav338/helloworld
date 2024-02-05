package arrays;

public class BooksAllocation {
	public static void main(String[] args) {
		/*
		 * Aim is to minimize the number of pages of books read by children
		 * {10, 20, 5, 15, 5} k = no of children = 2
		 * all books have to be divided consecutively in such a way that max number of pages allocated
		 * to a student is minimum possible
		 * case 1 : 
		 * 		child 1 - 10 + 20 = 30
		 * 		child 2 - 5 + 15 + 5 = 25
		 * 
		 * case 2 : 
		 * 		child 1 - 10 
		 * 		child 2 - 20 + 5 + 15 + 5 = 45
		 * 
		 * so case 1 is the answer 
		 * min pages read -> 30
		 * 
		 * explanation :->  https://www.youtube.com/watch?v=2JSQIhPcHQg
		 * 
		 * */
		int a[] = {10,20,30,40};
		int m = 2; // number of students
		System.out.println("->"+allocateBooks(a,a.length,m));
	}
	
	private static int allocateBooks(int[] a, int length, int m) {
		int sumOfPagesOfAllBooks = 0;
		int result = Integer.MAX_VALUE;
		int start = 0; 
		// we can also start the binary search with start value as the book with max pages
		// as our answer can't be less than this
		// otherwise we can also start from 0
		if(length < m) {
			// If number of students is less than the number of books  then allocation is not possible
			// as in this condition some students wont be getting any books and that  violates our constraint
			return -1;
		}
		for(int b :a) sumOfPagesOfAllBooks += b;
		start = 0;
		int end = sumOfPagesOfAllBooks;
		while(start <= end) {
			int mid = (start + end) /2;
			System.out.println("Mid : "+mid);
			if(isPossible(a,length,m,mid)) { 
				// this method tells whether the current mid value is feasible or not
				// based on this boolean value, we will move left or right as we do in binary search
				result = Math.min(result, mid);
				end = mid - 1; // if this current mid value is possible we move to its left to find if a better solution exists
			}
			else {
				// if this current mid value is not possible we move to its right to find if a better solution exists
				start = mid + 1;
			}
		}
		return result;
	}
	
	private static boolean isPossible(int[] a, int length, int m, int mid) {
		int student = 1, sum = 0; // sum represents the number of pages being allocated to the current student
		for (int i = 0; i < length; i++) {
			if(a[i] > mid) {
				// if the current number of pages being added is even greater than the expected mid passed in the function, 
				// then allocation is not possible
				return false;
			}
			if(sum + a[i] > mid) {
				// we try to allocate the current book to the current student but it exceeds the expected limit of mid
				// so we allocate it to next student
				student ++;
				sum = a[i];
				if(student > m) return false;
			}
			else {
				// we can continue adding more pages to our current student
				sum += a[i];
			}
		}
		return true;
	}
}