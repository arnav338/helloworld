# Problem 1: DriveCar.java (Progressive Problem-Solving Notes)

## 1) What we infer the problem is
Given an array `a[]` and threshold `k`, infer the required value as:
- return `-1` if no value meets threshold condition,
- otherwise return the maximum excess over `k` among relevant elements.

Note: The class implementation has indexing/sorting issues, so this note explains the intended interview-style approach cleanly.

## 2) Formal interview framing
- **Input**: `n` numbers, threshold `k`
- **Output**: largest `(value - k)` for values above/equal to threshold (as per chosen condition)
- **Goal**: compute fast, avoid unnecessary full sorting when possible

## 3) Brute force thought process
### Idea A: Check every element and track max excess
- For each `x` in array:
  - if `x > k`, candidate excess = `x - k`
  - track max candidate
- If no candidate found, return `-1`

### Why this is actually strong
- Single pass
- No heap needed

### Complexity
- Time: `O(n)`
- Space: `O(1)`

## 4) Why people still think of sorting/heap
In formal settings, candidates often jump to sorting to “organize” values first.

### Idea B: Sort descending, then scan till values drop below `k`
- Sort array (descending)
- Start from largest and compute max excess
- Stop when values no longer exceed `k`

### Problems in this approach
- Sorting cost dominates
- More moving parts, higher bug risk (indexing, boundaries)

### Complexity
- Time: `O(n log n)`
- Space: `O(1)` if in-place sort, else `O(n)` depending on method

## 5) Heap-based variation
### Idea C: Build max-heap and repeatedly inspect root
- Useful only if we need repeated max queries over time
- For one-shot answer, this is overengineering

### Complexity
- Build heap: `O(n)`
- If extracting repeatedly: up to `O(n log n)`
- Space: `O(1)` in-place heap array

## 6) Best-fit approach for this exact objective
Use **single pass max excess tracking** (`O(n)` time, `O(1)` space).

## 7) Common pitfalls (seen in this class type)
- Mixing 0-based and 1-based heap indexing
- Accessing wrong root index (`0` vs `1`)
- Sorting when only max-related statistic is needed
- Stopping conditions that accidentally ignore valid elements

## 8) Mental model
- If output is one aggregate (max/min/count), start with one-pass scan.
- Use heap only when you need **dynamic top element operations**.

## 9) Tiny diagram
```mermaid
graph TD
A[Need max excess over k] --> B{One-shot query?}
B -->|Yes| C[Single pass scan O(n)]
B -->|No, repeated top queries| D[Use max-heap]
```
