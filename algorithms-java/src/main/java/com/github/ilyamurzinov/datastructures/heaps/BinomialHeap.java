package com.github.ilyamurzinov.datastructures.heaps;

import com.github.ilyamurzinov.datastructures.trees.BinomialTree;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private BinomialHeap(Comparator<T> comparator, LinkedList<BinomialTree<T>> trees) {
        this.comparator = comparator;
        this.trees = new LinkedList<>(trees);
    }

    @Override
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("element");
        }

        LinkedList<BinomialTree<T>> list = new LinkedList<>();
        list.add(new BinomialTree<>(element));
        trees = merge(trees, list);
    }

    @Override
    public boolean isEmpty() {
        return trees == null || trees.isEmpty();
    }

    @Override
    public T findMin() {
        return findMinTree().<T>map(BinomialTree::getRootValue).orElse(null);
    }

    @Override
    public T deleteMin() {
        Optional<BinomialTree<T>> minTree = deleteMinTree();

        return minTree.map(tree -> {
            trees = merge(trees, tree.deleteRoot());
            return tree.getRootValue();
        }).orElse(null);
    }

    public BinomialHeap<T> merge(BinomialHeap<T> that) {
        return new BinomialHeap<>(comparator, merge(trees, that.trees));
    }

    @SuppressWarnings("unchecked")
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

    private Optional<BinomialTree<T>> findMinTree() {
        return trees.stream().min((o1, o2) -> compare(o1.getRootValue(), o2.getRootValue()));
    }

    private Optional<BinomialTree<T>> deleteMinTree() {
        Optional<BinomialTree<T>> result = findMinTree();

        trees = trees.stream().filter(next -> result.isPresent() && next.getDegree() != result.get().getDegree())
                .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    private LinkedList<BinomialTree<T>> merge(LinkedList<BinomialTree<T>> thisTrees, List<BinomialTree<T>> thatTrees) {
        LinkedList<BinomialTree<T>> trees = new LinkedList<>();

        if (thisTrees.isEmpty()) {
            trees = new LinkedList<>(thatTrees);
        } else if (thatTrees.isEmpty()) {
            trees = new LinkedList<>(thisTrees);
        } else {
            Iterator<BinomialTree<T>> thisIterator = thisTrees.iterator();
            Iterator<BinomialTree<T>> thatIterator = thatTrees.iterator();

            BinomialTree<T> thisCurrentTree = null;
            BinomialTree<T> thatCurrentTree = null;

            while (true) {
                if (thisCurrentTree == null && !thisIterator.hasNext()) {
                    break;
                } else if (thisCurrentTree == null) {
                    thisCurrentTree = thisIterator.next();
                }
                if (thatCurrentTree == null && !thatIterator.hasNext()) {
                    break;
                } else if (thatCurrentTree == null) {
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

        return mergeTreesOfSameDegree(trees);
    }

    @SuppressWarnings("unchecked")
    private LinkedList<BinomialTree<T>> mergeTreesOfSameDegree(LinkedList<BinomialTree<T>> trees) {
        for (int i = 0; i < trees.size() - 1; i++) {
            BinomialTree<T> current = trees.get(i);
            BinomialTree<T> next = trees.get(i + 1);
            if (current.getDegree() == next.getDegree()) {
                if (compare(current.getRootValue(), next.getRootValue()) < 0) {
                    trees.set(i + 1, current.merge(next));
                } else {
                    trees.set(i + 1, next.merge(current));
                }
                trees.set(i, null);
            }
        }

        return trees.stream().filter(tree -> tree != null).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("BinomialHeap[trees (size: ");
        result.append(trees.size()).append("): ");
        for (BinomialTree<T> tree : trees) {
            result.append(tree.toString()).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append("]");
        return result.toString();
    }
}
