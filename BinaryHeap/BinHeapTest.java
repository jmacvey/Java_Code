    /**
     * Tests to see if the heap passed is a minimum binary heap.
     * @param bheap the binary heap to be tested
     * @author jmacvey
     */
    private boolean isMinBinaryHeap(BinaryHeap<Integer> bheap)
    {
        return isMinBinHeapHelper(bheap, 1, 2, 3, bheap.size());
    }

    /**
     * Recursive utility method to help isMinBinaryHeap method detect if
     * the passed heap is a minimum binary heap.
     * @param bHeap the binary heap being tested.
     * @param root the root of the heap (or subheap) to be tested.
     * @param lChild the left child index of the parameter root.
     * @param rChild the right child index of the parameter root.
     * @param heapSize the total heap size of the binary heap.  This is invariant
     * for all recursive calls.
     * @return True if the heap provided is a minimum binary heap. False otherwise.
     * @author jmacvey
     */
    private boolean isMinBinHeapHelper(BinaryHeap<Integer> bHeap,
            int root, int lChild, int rChild,
            int heapSize)
    {
        // Scenario 1: Children don't exist.  Simplest scenario.
        
        // base case 1: lChild and rChild don't exist.  The heap is then a binary heap.
        // In array implementation, it is an axiom that the rChild cannot exist
        // unless an lChild does since rChild = lChild + 1.  If the rChild did exist
        // without the lChild we would be in type violation.  This implies
        // that the heap will always be nearly full which means we don't have
        // to test for near-fullness. We can test if the children don't exist
        // by comparing the 
        // maximum heap size to the indices of the right and left children.
        if (lChild >= heapSize && rChild >= heapSize)
        {
            return true;
        } // Scenario 2: leftChildExists, follows from the first conditional.
        else if (rChild >= heapSize)
        {
           // if the leftChildExists and the root node is greater, then this is
            // not a binary heap.  otherwise it is a binary heap.
            return (comp.compare(bHeap.get(root), bHeap.get(lChild)) <= 0);
        } // Scenario 3: both children exist.
        else
        {
            // can safely access root and children without fear of nullexception
            Integer rootE = bHeap.get(root);
            Integer lChildE = bHeap.get(lChild);
            Integer rChildE = bHeap.get(rChild);

            // base case for Scenario 3: root is greater than either of the
            if (rootE > lChildE || rootE > rChildE)
            {
                return false;
            } // else we can apply recursion.
            else
            {
                // test the right heap
                boolean rightHeapIsBinHeap = isMinBinHeapHelper(bHeap,
                        rChild, rChild * 2, rChild * 2 + 1, heapSize);
                // test the left heap
                boolean leftHeapIsBinHeap = isMinBinHeapHelper(bHeap,
                        lChild, lChild * 2, lChild * 2 + 1, heapSize);
                // if both the left AND right subHeaps are binheaps, and root is 
                // less than or equal to both, then we have a binHeap.
                return leftHeapIsBinHeap && rightHeapIsBinHeap;
            }
        }
    }