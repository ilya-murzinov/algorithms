package com.github.ilyamurzinov.datastructures.heaps;

import com.github.ilyamurzinov.datastructures.trees.BinomialTree;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ilya Murzinov
 */
public class BinomialHeap<T> implements PriorityQueue<T> {

    private final Comparator<T> comparator;
    private LinkedList<BinomialTree<T>> trees;

    public BinomialHeap() {
        this(null);
    }

    public BinomialHeap(Comparator<T> comparator) {
        this(comparator, new LinkedList<>());
    }

    private BinomialHeap(Comparator<T> comparator, List<BinomialTree<T>> trees) {
        this.comparator = comparator;
        this.trees = new LinkedList<>(trees);
    }

    @Override
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("element");
        }

        trees = merge(new BinomialHeap<>(comparator, Collections.singletonList(new BinomialTree<>(element)))).trees;
    }

    @Override
    public boolean isEmpty() {
        return trees == null || trees.isEmpty();
    }

    @Override
    public T findMin() {
        if (trees.isEmpty()) {
            return null;
        }

        T min = trees.get(0).getRootValue();

        if (trees.size() == 1) {
            return min;
        }

        for (int i = 1; i < trees.size(); i++) {
            BinomialTree<T> tree = trees.get(i);
            if (compare(tree.getRootValue(), min) < 0) {
                min = tree.getRootValue();
            }
        }

        return min;
    }

    @Override
    public T deleteMin() {
        return null;
    }

    public BinomialHeap<T> merge(BinomialHeap<T> that) {
        LinkedList<BinomialTree<T>> trees = new LinkedList<>();

        if (this.trees.isEmpty()) {
            trees = that.trees;
        } else if (that.trees.isEmpty()) {
            trees = this.trees;
        } else {
            Iterator<BinomialTree<T>> thisIterator = this.trees.iterator();
            Iterator<BinomialTree<T>> thatIterator = that.trees.iterator();

            BinomialTree<T> thisCurrentTree = null;
            BinomialTree<T> thatCurrentTree = null;

            while (thisIterator.hasNext() && thatIterator.hasNext()) {
                if (thisCurrentTree == null) {
                    thisCurrentTree = thisIterator.next();
                }
                if (thatCurrentTree == null) {
                    thatCurrentTree = thatIterator.next();
                }

                if (thisCurrentTree.getDegree() > thatCurrentTree.getDegree()) {
                    trees.add(thatCurrentTree);
                    thatCurrentTree = null;
                } else if (thisCurrentTree.getDegree() < thatCurrentTree.getDegree()) {
                    trees.add(thisCurrentTree);
                    thisCurrentTree = null;
                } else {
                    if (compare(thisCurrentTree.getRootValue(), thatCurrentTree.getRootValue()) < 0) {
                        trees.add(thisCurrentTree.merge(thatCurrentTree));
                    } else {
                        trees.add(thatCurrentTree.merge(thisCurrentTree));
                    }
                    thatCurrentTree = null;
                    thisCurrentTree = null;
                }
            }

            if (thatCurrentTree != null) {
                trees.add(thatCurrentTree);
            }
            if (thisCurrentTree != null) {
                trees.add(thisCurrentTree);
            }

            while (thisIterator.hasNext()) {
                trees.add(thisIterator.next());
            }

            while (thatIterator.hasNext()) {
                trees.add(thatIterator.next());
            }
        }

        return new BinomialHeap<>(comparator, trees);
    }

    public BinomialHeap<T> delete(T element) {
        return null;
    }

    private int compare(T element1, T element2) {
        if (comparator != null) {
            return comparator.compare(element1, element2);
        } else {
            try {
                return ((Comparable<T>) element1).compareTo(element2);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(
                        "Cannot compare instances of non-comparable class without comparator");
            }
        }
    }
}
