# Problem 2: Heapify.java (Build Heap + Heap Sort + Kth Thinking)

## 1) Problem intention
This class demonstrates:
- building a heap from array,
- heapify operation,
- heap sort,
- kth element idea.

## 2) Formal approach sequence
1. Understand heap property
2. Build heap bottom-up
3. Repeatedly place root at end (heap sort)
4. For kth queries, avoid full sorting when unnecessary

## 3) Brute force thinking
### Idea A: For sorting, use simple comparison sort
- Works, but not heap-specific and can be slower in some implementations.

### Idea B: For kth largest, sort whole array then index
- Easy to reason about
- But full sort may be unnecessary for one kth answer

### Complexity
- Full sort route: `O(n log n)` time, space depends on sort

## 4) Better heap-centric reasoning
### Step 1: Build heap in `O(n)`
- Start from last non-leaf node down to root
- Call heapify at each internal node

### Step 2: Heap sort
- Swap root with end
- Reduce heap size
- Heapify root again

### Complexity
- Build heap: `O(n)`
- Sorting phase: `O(n log n)`
- Total: `O(n log n)`
- Space: `O(1)` in-place

## 5) For kth largest/smallest (better than full sort in some scenarios)
### Idea C: Maintain a heap of size `k`
- kth largest: min-heap of size `k`
- kth smallest: max-heap of size `k`

### Complexity
- Time: `O(n log k)`
- Space: `O(k)`

## 6) Shortcomings checklist
- Off-by-one index bugs (especially if 1-indexed heap with dummy at index 0)
- Mixing min-heap and max-heap logic in same helper
- Printing kth as `a[k]` without proving sorted/index convention

## 7) Mental model
- Need fully sorted output -> heap sort acceptable.
- Need only kth -> bounded-size heap usually cleaner.

## 8) Diagram
```mermaid
graph LR
A[Raw array] --> B[Build max-heap O(n)]
B --> C[Swap root with end]
C --> D[Heapify reduced heap]
D --> E[Repeat until sorted]
```
