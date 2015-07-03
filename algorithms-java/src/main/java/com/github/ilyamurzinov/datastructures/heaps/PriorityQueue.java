package com.github.ilyamurzinov.datastructures.heaps;

/**
 * @author Ilya Murzinov
 */
public interface PriorityQueue<T> {
    void add(T element);

    boolean isEmpty();

    T findMin();

    T deleteMin();
}
