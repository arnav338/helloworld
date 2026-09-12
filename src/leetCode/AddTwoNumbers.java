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

	/*

	╔════════════════════════════════════════════════════════════╗
║                    ADD TWO NUMBERS                         ║
╚════════════════════════════════════════════════════════════╝

PATTERN
─────────────────────────────────────────────────────────────
Linked List + Simulation + State

PROBLEM
─────────────────────────────────────────────────────────────
Two numbers are represented using linked lists.

Digits are stored in REVERSE order.

Example:
    2 → 4 → 3 = 342
    5 → 6 → 4 = 465

Therefore:
    342 + 465 = 807
    result = 7 → 0 → 8


FIRST THOUGHT
─────────────────────────────────────────────────────────────
Don't think:
    "How do I manipulate these linked lists?"

Think:
    "How do I perform normal addition?"

Because the digits are reversed, the linked list already
gives us digits from least significant → most significant.

So:
    Traverse left → right
    and simulate elementary-school addition.


BRUTE FORCE / NAIVE IDEA
─────────────────────────────────────────────────────────────
Convert l1 → integer
Convert l2 → integer
Add them
Convert result → linked list

Problems:
    - unnecessary conversions
    - 100-node lists may represent 100-digit numbers
    - normal integer types can overflow
    - misses the intended digit-by-digit simulation

Better:
    Perform addition directly on the lists.


CORE CONCEPT
─────────────────────────────────────────────────────────────
SIMULATION WITH STATE

At each position:

    sum = digit1 + digit2 + carry

Then:

    current digit = sum % 10
    next carry    = sum / 10

MEMORY TRIGGER:

    % 10 → current digit
    / 10 → carry forward


STATE
─────────────────────────────────────────────────────────────
Ask:

    "What information from the previous iteration
     affects the current iteration?"

Answer:

    carry

We DON'T need:
    - complete previous number
    - previous digits
    - previous sums
    - complete history

The entire relevant history is compressed into:

    carry


TRANSITION
─────────────────────────────────────────────────────────────
At every position:

    digit1 + digit2 + carry
              ↓
             sum
           ↙     ↘
       sum % 10  sum / 10
          ↓         ↓
     result node   carry


UNEQUAL LENGTH LISTS
─────────────────────────────────────────────────────────────
If one list finishes:

    missing digit = 0

Example:

    l1 = 9 → 9 → 9
    l2 = 1

After l2 ends:

    9 + 0 + carry

This lets us use ONE unified loop instead of separate
loops for:
    - both lists
    - only l1
    - only l2


FINAL CARRY
─────────────────────────────────────────────────────────────
Both lists may finish while carry still exists.

Example:

    99 + 99 = 198

Processing:
    9 + 9 = 18 → digit 8, carry 1
    9 + 9 + 1 = 19 → digit 9, carry 1

Both lists are now finished.

But:
    carry = 1

Therefore create:
    8 → 9 → 1

Loop condition should be:

    while (l1 != null || l2 != null || carry != 0)


DUMMY NODE
─────────────────────────────────────────────────────────────
When dynamically constructing a linked list:

    dummy → actual result
              ↑
             curr

For every new digit:

    curr.next = new ListNode(digit)
    curr = curr.next

At the end:

    return dummy.next

WHY?

Without dummy:
    First node requires special handling.

With dummy:
    Every node is created using the exact same logic.

MENTAL TRIGGER:

    "Building a linked list from scratch?"
    → Think DUMMY NODE.


IMPORTANT LINKED-LIST TECHNIQUES
─────────────────────────────────────────────────────────────
1. Two-pointer traversal

    l1 → current node in first list
    l2 → current node in second list


2. Unequal-length handling

    missing node → value 0


3. Dummy node

    dummy → result


4. Current pointer

    curr → last node in result


5. Preserve the head

    Keep dummy separately.
    Don't lose access to the beginning of the result.


CLEAN CODE
─────────────────────────────────────────────────────────────
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        // Dummy node simplifies result-list construction.
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;

        // Carry from the previous digit addition.
        int carry = 0;

        // Process digits while either list or carry remains.
        while (l1 != null || l2 != null || carry != 0) {

            // Missing node is treated as digit 0.
            int digit1 = (l1 != null) ? l1.val : 0;
            int digit2 = (l2 != null) ? l2.val : 0;

            // Normal addition.
            int sum = digit1 + digit2 + carry;

            // Store current digit.
            curr.next = new ListNode(sum % 10);
            curr = curr.next;

            // Carry to next position.
            carry = sum / 10;

            // Move forward if nodes exist.
            if (l1 != null) {
                l1 = l1.next;
            }

            if (l2 != null) {
                l2 = l2.next;
            }
        }

        return dummy.next;
    }
}


INTERVIEW DERIVATION
─────────────────────────────────────────────────────────────
Step 1:
    Notice digits are reversed.

Step 2:
    Therefore traverse head → tail.

Step 3:
    This is normal digit-by-digit addition.

Step 4:
    Previous calculation only matters through carry.

Step 5:
    sum = digit1 + digit2 + carry

Step 6:
    sum % 10 → current digit
    sum / 10 → carry

Step 7:
    If a list ends, treat missing digit as 0.

Step 8:
    Continue if carry remains.

Step 9:
    Use dummy node to construct result.


EASY THINGS TO FORGET
─────────────────────────────────────────────────────────────
☐ Include carry in the sum.

☐ Update carry after every digit.

☐ Use:
      sum % 10
  for current digit.

☐ Use:
      sum / 10
  for carry.

☐ Handle unequal lengths.

☐ Process final carry.

☐ Return dummy.next, NOT dummy.

☐ Don't convert the whole number to int/long.

☐ Don't create separate loops unnecessarily.


WHAT CAN I GENERALIZE?
─────────────────────────────────────────────────────────────
1. STATE COMPRESSION

Many previous operations may be summarized by
a very small state.

Here:

    entire previous calculation
             ↓
           carry


2. NEUTRAL VALUES REMOVE SPECIAL CASES

Instead of:
    "What if l1 ends?"
    "What if l2 ends?"

Use:

    missing digit = 0

Now everything follows one transition.


3. DUMMY NODE REMOVES HEAD SPECIAL CASES

Instead of:
    "Is this the first result node?"

Use:

    dummy → result


4. SIMULATION

Identify:

    current input
         +
    current state
         ↓
    output + new state

Here:

    digit1 + digit2 + carry
              ↓
       digit + carry


5. SEPARATE ALGORITHM FROM DATA STRUCTURE

Algorithm:
    addition + carry

Data structure:
    traverse linked lists
    construct result list

Don't let linked-list mechanics hide
the simple mathematical algorithm.


KEY MENTAL MODELS
─────────────────────────────────────────────────────────────
MODEL 1:
    "What does the next step actually need to know?"

MODEL 2:
    "Can I compress the entire history into a small state?"

MODEL 3:
    "Can a neutral value eliminate a special case?"

MODEL 4:
    "Am I constructing a linked list?"
    → Use a dummy node.

MODEL 5:
    "Is this a simulation?"
    → Find the repeated transition.


COMPLEXITY
─────────────────────────────────────────────────────────────
Let:

    m = length(l1)
    n = length(l2)

Time:
    O(max(m, n))

Output space:
    O(max(m, n))

Auxiliary space:
    O(1)


2–3 MONTH RECALL
─────────────────────────────────────────────────────────────
If I forget the solution, ask myself:

    1. Why can I traverse left → right?
       → Digits are reversed.

    2. What am I simulating?
       → Elementary-school addition.

    3. What is my state?
       → carry.

    4. What is the transition?
       → digit1 + digit2 + carry.

    5. Current digit?
       → sum % 10.

    6. Next carry?
       → sum / 10.

    7. One list ends?
       → Missing digit = 0.

    8. Both lists end but carry remains?
       → Add another node.

    9. How do I construct result cleanly?
       → Dummy node.


ONE-LINE MEMORY HOOK
─────────────────────────────────────────────────────────────
    REVERSED DIGITS → SIMULATE ADDITION

    sum = d1 + d2 + carry
    node = sum % 10
    carry = sum / 10

    missing digit → 0
    building list → dummy node

	* */
	
	
}
