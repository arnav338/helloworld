# Quick Refresher (3-4 Line Problem -> Identify -> Solve)

## DriveCar-style threshold problem
- **Problem**: Find required excess over threshold `k`.
- **Identify**: Only one aggregate output is required.
- **Solve**: Single-pass max tracking (`if x > k, ans=max(ans,x-k)`).
- **Tip**: Avoid sorting/heap unless repeated top queries are needed.

## Heapify / Heap Sort
- **Problem**: Organize array as heap and optionally sort.
- **Identify**: Parent-child property + repeated root placement.
- **Solve**: Bottom-up build-heap, then swap root to end and heapify reduced heap.
- **Tip**: Build-heap is `O(n)`, total sort is `O(n log n)`.

## Insert and Delete in Max-Heap
- **Problem**: Maintain heap after updates.
- **Identify**: Dynamic operations with priority access.
- **Solve**: Insert -> append + bubble-up; Delete root -> move last + bubble-down.
- **Tip**: Both operations are usually `O(log n)`.

## Min-Heap to Max-Heap conversion
- **Problem**: Convert property, not just move one max element.
- **Identify**: Need full global max-heap validity.
- **Solve**: Heapify all internal nodes bottom-up.
- **Tip**: One root swap is not enough; bottom-up gives `O(n)` conversion.

## Fast mental triggers
- One value needed -> scan.
- Repeated highest/lowest -> heap.
- Convert array to heap -> bottom-up heapify.
- Kth only -> heap size `k` often better than full sort.
