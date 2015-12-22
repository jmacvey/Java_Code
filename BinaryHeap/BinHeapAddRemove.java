/**
     * Adds a new element to this binary heap. At the end of the add, the heap
     * has one more element and the heap property is maintained.
     *
     * @param element The element to add
     * @return true. In accordance with the Collection interface, returns true
     * since duplicate elements are allowed.
     */
    public boolean add(E element)
    {
        // iterative solution.
        return iterativeAdd(element);
    }

    /**
     * Removes an element from the root of this binary heap. After removal, the
     * heap has one less element and the heap property is restored. This method
     * does not override any method in the ArrayList class (note absence of an
     * index parameter).
     *
     * @return The removed element
     */
    public E remove()
    {
        return recursiveRemove(1, super.size());
    }

    /**
     * A Comparator object used to compare binary heap elements during its add
     * and remove operations.
     */
    private Comparator<E> comp;

    /**
     * Private iterative utility method to help the add operation.
     *
     * @param element the element to be added.
     */
    private boolean iterativeAdd(E element)
    {
        // i = index of root
        // 2i = index of left child
        // 2i + 1 = index of right child
        // for node a in A, a's parent is i/2
        // comparator object returns negative integer if param1 < param2 
        // case 1: first item added to list
        if (super.size() == 1)
        {
            // add initial item to the array
            super.add(element);
        } // case 2: another item added
        else
        {
            // add the item
            super.add(element);
            // now iterate up the list
            int elementIndex = super.size() - 1;

            while ((elementIndex >= 1) && (elementIndex / 2 != 0)
                    && comp.compare(super.get(elementIndex / 2), element) > 0)
            {
                swap(elementIndex, elementIndex / 2);
                elementIndex /= 2;
            }
        }
        return true;
    } // end iterative add

    /**
     * Private recursive utility method to help the remove method.
     *
     * @param index the index of the element to remove
     * @param arraySize the number of elements in the heap
     */
    private E recursiveRemove(int root, int heapSize)
    {
        E removedItem = super.get(1);
        // swap the lowest level with the top
        super.set(1, super.get(heapSize - 1));
        // remove the bottom
        super.remove(heapSize - 1);
        // let heapSwap do the brunt of the work recursively
        heapSwap(root, root * 2, root * 2 + 1, heapSize - 1);

        return removedItem;
    }

    /**
     * Private recursive utility method to heapSwap down the tree
     */
    private void heapSwap(int root, int lChild, int rChild, int heapSize)
    {
        // base case 0: heapSize > lChild -> no children exist -> we're done.

        // only act if the left child is less than the heapsize.
        if (lChild < heapSize)
        {
            // Scenario 1: right child doesn't exist:
            // if the rChild doesn't exist and the root is greater than left, we
            // swap the root and we are done. rChild doesn't exist and left is > root,
            // we're still done.
            if (rChild >= heapSize)
            {
                if (comp.compare(super.get(root), super.get(lChild)) > 0)
                {
                    swap(root, lChild);
                }
            } else // scenario 2: both children exist 
            {
                E lChildE = super.get(lChild);
                E rootE = super.get(root);
                E rChildE = super.get(rChild);
                // case 1: left >= right, root > left -> swap root/right, recurse
                if (comp.compare(lChildE, rChildE) >= 0
                        && comp.compare(rootE, lChildE) > 0)
                {
                    swap(root, rChild);
                    heapSwap(rChild, rChild * 2, rChild * 2 + 1, heapSize);
                } // case 2: left >= right, left > root, root > right -> swap root/right
                else if (comp.compare(lChildE, rChildE) >= 0
                        && comp.compare(lChildE, rootE) > 0
                        && comp.compare(rootE, rChildE) > 0)
                {
                    swap(root, rChild);
                    heapSwap(rChild, rChild * 2, rChild * 2 + 1, heapSize);
                } // case 3: left >= right, left > root, root < right -> base case 2
                // case 4: right > left, root > right -> swap root/left and recurse
                else if (comp.compare(rChildE, lChildE) > 0
                        && comp.compare(rootE, rChildE) > 0)
                {
                    swap(root, lChild);
                    heapSwap(lChild, lChild * 2, lChild * 2 + 1, heapSize);
                } // case 5: right > left, right > root, root > left -> swap root/left
                else if (comp.compare(rChildE, lChildE) > 0
                        && comp.compare(rChildE, rootE) > 0
                        && comp.compare(rootE, lChildE) > 0)
                {
                    swap(root, lChild);
                    heapSwap(lChild, lChild * 2, lChild * 2 + 1, heapSize);
                }
                // case 6: right > left, right > root, left > root -> base case 3
            } // end scenario 2
        }
    }

    /**
     * Private utility method to swap two elements in the array.
     *
     * @param elemOne first element to swap.
     * @param elemTwo second element to swap.
     */
    private void swap(int elemOne, int elemTwo)
    {
        E lhsElement = super.get(elemOne);
        super.set(elemOne, super.get(elemTwo));
        super.set(elemTwo, lhsElement);
    }
