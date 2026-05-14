# Combined Notes: Heap Problems (Interview-Style Table)

| Problem | Overview | Intuition Path | Concepts Used | How to Identify | Solve Strategy | Indicators/Patterns | Mental Model | Key Takeaway | Underlying Intent |
|---|---|---|---|---|---|---|---|---|---|
| DriveCar threshold/excess | Compute required excess relative to `k` | Try scan -> consider sort/heap -> realize one-pass is enough | max tracking, threshold checks | Output asks only aggregate max/min/count | Single pass track best candidate | “Need one summary value, not ordered list” | Aggregate query first, structure later | Do not overuse heap/sort for one-pass stats | Test if candidate can avoid unnecessary data structures |
| Heapify + heap sort + kth | Build heap and derive sorted order / kth info | Understand heap property -> build heap -> extract root repeatedly | heapify, build-heap, in-place swaps | Problem mentions heap transform/sort or repeated root ops | Bottom-up heapify then extraction | “Top element repeatedly needed” | Maintain local parent-child invariants | Build heap is `O(n)`, full sort is `O(n log n)` | Evaluate command of heap mechanics and complexity awareness |
| Insert/Delete in max-heap | Maintain heap under updates | Naive rebuild -> bubble up/down standard operations | bubble-up, bubble-down, priority queue behavior | Dynamic insert/remove-max workload | Insert at end then up-heap; delete root then down-heap | “Frequent updates + priority access” | Heap is partially ordered tree, not sorted array | Each operation typically `O(log n)` | Check if candidate knows operational maintenance, not just sorting |
| Min-heap to max-heap conversion | Convert structure property efficiently | Root swap idea fails -> bottom-up heapify works | maxHeapify, internal nodes, tree indexing | “Convert/rebuild heap property” tasks | Heapify from last internal node to root | “Need global validity, not one-node fix” | Local fixes compose bottom-up | Conversion in `O(n)` is expected | Validate understanding of structural correctness vs superficial fixes |

## Cross-problem identification map
1. If question asks for one statistic (max excess, count, min value): start with one-pass scan.
2. If question asks for repeated highest/lowest extraction: use heap operations.
3. If question asks to transform arbitrary array to heap: bottom-up heapify.
4. If question asks kth frequently with streaming/large data: heap of size `k`.

## Cross-problem complexity map
- One-pass scan: `O(n)` time, `O(1)` space
- Build heap: `O(n)` time
- Insert/Delete in heap: `O(log n)` per operation
- Heap sort: `O(n log n)` time, `O(1)` extra space (in-place)
- Kth with size-`k` heap: `O(n log k)` time, `O(k)` space
