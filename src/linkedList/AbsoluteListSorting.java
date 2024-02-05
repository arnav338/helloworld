package linkedList;

public class AbsoluteListSorting {
	/*
	 * Given a linked list L of N nodes, sorted in ascending order based on the
	 * absolute values of its data,i.e. negative values are considered as positive
	 * ones. Sort the linked list according to the actual values, consider negative
	 * numbers as negative and positive number as positive.
	 * 
	 * 
	 * Example 1:
	 * 
	 * Input: 
	 * List: 1, -2, -3, 4, -5
	 * -5, 4, -3, -2, 1
	 * Output: 
	 * List: -5, -3, -2, 1, 4
	 * 
	 * An efficient solution can work in O(n) time. 
	 * An important observation is, all negative elements are present in reverse order.
	 * So we traverse the list, whenever we find an element that is out of order, 
	 * we move it to the front of the linked list. 
	 * https://www.youtube.com/watch?v=hH4XI0INmNg
	 */
	
	static Node head;  // head of list
	   
    /* Linked list Node*/
    static class Node
    {
        int data;
        Node next;
        Node(int d) {data = d; next = null; }
    }
    
    Node sortedList(Node head)
    {
        // Initialize previous and current nodes
        Node prev = head;
        Node curr = head.next;
         
        // Traverse list
        while(curr != null)
        {
            // If curr is smaller than prev, then
                        // it must be moved to head
            if(curr.data < prev.data)
            {
                // Detach curr from linked list
                prev.next = curr.next;
                 
                // Move current node to beginning
                curr.next = head;
                head = curr;
                 
                // Update current
                curr = prev;
            }
             
            // Nothing to do if current element
                        // is at right place
            else
            prev = curr;
         
            // Move current
            curr = curr.next;
        }
        return head;
    }
	
	public static void main(String[] args) {

	}
}
