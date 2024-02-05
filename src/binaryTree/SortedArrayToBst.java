package binaryTree;

public class SortedArrayToBst {
	/*
	 * Given a sorted array. Write a function that creates a Balanced Binary Search Tree using array elements.
		Examples: 
 

		Input:  Array {1, 2, 3}
		Output: A Balanced BST
     			 2
   				/  \
  				1    3 
  				
  		1) Get the Middle of the array and make it root.
		2) Recursively do same for left half and right half.
      		a) Get the middle of left half and make it left child of the root
          		created in step 1.
      		b) Get the middle of right half and make it right child of the
          		root created in step 1.		
	 * */
	public static void main(String[] args) {
		
	}
	
	static Node sortedArrayToBST(int arr[], int start, int end) {
		 
        /* Base Case */
        if (start > end) {
            return null;
        }
 
        /* Get the middle element and make it root */
        int mid = (start + end) / 2;
        Node node = new Node(arr[mid]);
 
        /* Recursively construct the left subtree and make it
         left child of root */
        node.left = sortedArrayToBST(arr, start, mid - 1);
 
        /* Recursively construct the right subtree and make it
         right child of root */
        node.right = sortedArrayToBST(arr, mid + 1, end);
         
        return node;
    }
}
