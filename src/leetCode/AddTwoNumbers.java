package leetCode;

public class AddTwoNumbers {
	/*
	 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.

		You may assume the two numbers do not contain any leading zero, except the number 0 itself.
		
		Input: l1 = [2,4,3], l2 = [5,6,4]
		Output: [7,0,8]
		Explanation: 342 + 465 = 807.
		
		Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
		Output: [8,9,9,9,0,0,0,1]
		
	 * */
	
	public static class ListNode {
		      int val;
		      ListNode next;
		      ListNode() {}
		      ListNode(int val) { this.val = val; }
		      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
		  }
	
	public static void main(String[] args) {
		ListNode l1 = new ListNode(2);
		l1.next = new ListNode(4);
		l1.next.next = new ListNode(3);
		
		ListNode l2 = new ListNode(5);
		l2.next = new ListNode(6);
		l2.next.next = new ListNode(4);
		addTwoNumbers(l1, l2);
	}
	
	public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int carry = 0;
        ListNode previous = new ListNode();
        ListNode head = previous;
        while(l1!=null || l2!=null ||carry!=0){
        	ListNode current = new ListNode(0);
            int d1 = l1 == null ? 0 : l1.val;
            int d2 = l2 == null ? 0 : l2.val;
            current.val = (d1+d2+carry) % 10;
            carry = (d1+d2+carry) / 10;
            previous.next = current;
            previous = current;
            l1 = (l1==null || l1.next==null) ? null : l1.next;
            l2 = (l2==null || l2.next==null ) ? null : l2.next;
        }
        return head.next;
    }
	
	
}
