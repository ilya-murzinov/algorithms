package com.github.ilyamurzinov.datastructures.trees;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ilya Murzinov
 */
public class BinomialTree<T> {
    private final Node<T> root;

    public BinomialTree(T value) {
        this.root = new Node<>(value, 0, null, null);
    }

    public BinomialTree(Node<T> root) {
        this.root = new Node<>(root.value, root.degree, root.childrenRoot, root.siblingsRoot);
    }

    public T getRootValue() {
        return root.value;
    }

    public int getDegree() {
        return root.getDegree();
    }

    public BinomialTree<T> merge(BinomialTree<T> that) {
        if (that.root.getDegree() != this.root.getDegree()) {
            throw new IllegalArgumentException("Can not merge two heaps of different degrees!");
        }

        Node<T> newChildrenRoot = new Node<>(that.root.value, that.root.degree, that.root.childrenRoot,
                this.root.childrenRoot);

        return new BinomialTree<>(new Node<>(this.root.value, this.root.degree + 1, newChildrenRoot, null));
    }

    public List<BinomialTree<T>> deleteRoot() {
        ArrayList<BinomialTree<T>> result = new ArrayList<>();

        Node<T> subtree = root.childrenRoot.siblingsRoot;
        while (subtree != null) {
            result.add(new BinomialTree<>(subtree));
            subtree = subtree.siblingsRoot;
        }

        return result;
    }

    private static class Node<T> {
        private final T value;
        private final int degree;
        private final Node<T> childrenRoot;
        private final Node<T> siblingsRoot;

        private Node(T value, int degree, Node<T> childrenRoot, Node<T> siblingsRoot) {
            this.value = value;
            this.degree = degree;
            this.childrenRoot = childrenRoot;
            this.siblingsRoot = siblingsRoot;
        }

        private int getDegree() {
            return degree;
        }
    }
}
