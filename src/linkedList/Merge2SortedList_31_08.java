package linkedList;

public class Merge2SortedList {
    /*
    You are given the heads of two sorted linked lists list1 and list2.

Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.

Return the head of the merged linked list.



Example 1:


Input: list1 = [1,2,4], list2 = [1,3,4]
Output: [1,1,2,3,4,4]
Example 2:

Input: list1 = [], list2 = []
Output: []
Example 3:

Input: list1 = [], list2 = [0]
Output: [0]
    * */

      public class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }


    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode head = new ListNode(-1);
        merge(head,list1,list2);
        return head.next;
    }

    public void merge(ListNode currentHead, ListNode list1, ListNode list2){
        if(list1 == null && list2 == null) return;
        if(list1 != null && list2 == null){
            System.out.println("l2 null");
            currentHead.next = list1;
            merge(currentHead.next,list1.next,null);
        }
        else if(list1 == null && list2 != null){
            System.out.println("l1 null");
            currentHead.next = list2;
            System.out.println(currentHead.next+" || "+list2.next);
            merge(currentHead.next,null,list2.next);
        }
        else if(list1.val <= list2.val){
            currentHead.next = list1;
            merge(currentHead.next,list1.next,list2);
        }
        else{
            currentHead.next = list2;
            merge(currentHead.next,list1,list2.next);
        }
    }

    public ListNode mergeTwoLists_copied(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode();
        ListNode cur = dummy;

        while (list1 != null && list2 != null) {
            if (list1.val > list2.val) {
                cur.next = list2;
                list2 = list2.next;
            } else {
                cur.next = list1;
                list1 = list1.next;
            }
            cur = cur.next;
        }

        cur.next = (list1 != null) ? list1 : list2;

        return dummy.next;
    }
}
