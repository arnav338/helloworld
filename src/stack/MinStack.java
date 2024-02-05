package stack;

import java.util.Stack;

public class MinStack {
	/*
	 * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.

Implement the MinStack class:

MinStack() initializes the stack object.
void push(int val) pushes the element val onto the stack.
void pop() removes the element on the top of the stack.
int top() gets the top element of the stack.
int getMin() retrieves the minimum element in the stack.
 

Example 1:

Input
["MinStack","push","push","push","getMin","pop","top","getMin"]
[[],[-2],[0],[-3],[],[],[],[]]

Output
[null,null,null,null,-3,null,0,-2]

Explanation
MinStack minStack = new MinStack();
minStack.push(-2);
minStack.push(0);
minStack.push(-3);
minStack.getMin(); // return -3
minStack.pop();
minStack.top();    // return 0
minStack.getMin(); // return -2
	 * */
	public static void main(String[] args) {

	}
	/*
	 *  The idea is to store min element found till current insertion) along with all the elements as a reminder of a DUMMY_VALUE, and the actual element as a multiple of the DUMMY_VALUE.
For example, while pushing an element ‘e’ into the stack, store it as (e * DUMMY_VALUE + minFoundSoFar), this way we know what was the minimum value present in the stack at the time ‘e’ was being inserted.

To pop the actual value just return e/DUMMY_VALUE and set the new minimum as (minFoundSoFar % DUMMY_VALUE)
	 * */
	int min = -1; // sentinel value for min
    static int demoVal = 9999; // DEMO_VALUE
    Stack<Integer> st = new Stack<Integer>();
 
    void getMin() { System.out.println("min is: " + min); }
 
    void push(int val)
    {
        // if stack is empty OR current element is less than
        // min, update min..
        if (st.isEmpty() || val < min) {
            min = val;
        }
 
        st.push(val * demoVal + min); // encode the current value with
                        // demoVal, combine with min and
                        // insert into stack
        System.out.println("pushed: " + val);
    }
 
    int pop()
    {   
        // if stack is empty return -1;
        if (st.isEmpty() ) {
             System.out.println("stack underflow");
               return -1;
          }
       
        int val = st.pop();
 
        if (!st.isEmpty()) // if stack is empty, there would be no min value present, so make min as -1
            min = st.peek() % demoVal;
        else
            min = -1;
        System.out.println("popped: " + val / demoVal);
        return val / demoVal; // decode actual value from encoded value
    }
 
    int peek()
    {
        return st.peek() / demoVal; // decode actual value
                                    // from encoded value
    }
}
