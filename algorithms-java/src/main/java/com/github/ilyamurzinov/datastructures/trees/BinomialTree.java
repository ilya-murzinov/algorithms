package com.github.ilyamurzinov.datastructures.trees;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ilya Murzinov
 */
public class BinomialTree<T> {
    private final Node<T> root;

    public BinomialTree(T value) {
        this.root = new Node<>(value, 0, null, null, null);
    }

    public BinomialTree(Node<T> root) {
        this.root = new Node<>(root.value, root.degree, root.parent, root.childrenRoot, root.siblingsRoot);
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

        Node<T> newChildrenRoot = new Node<>(that.root.value, that.root.degree, this.root, that.root.childrenRoot,
                this.root.childrenRoot);

        return new BinomialTree<>(new Node<>(this.root.value, this.root.degree + 1, null, newChildrenRoot, null));
    }

    public List<BinomialTree<T>> deleteRoot() {
        LinkedList<BinomialTree<T>> tmp = new LinkedList<>();

        if (root.childrenRoot == null) {
            return tmp;
        }

        tmp.add(new BinomialTree<>(
                new Node<>(root.childrenRoot.value, root.childrenRoot.degree, null, root.childrenRoot.childrenRoot, null)
        ));

        Node<T> subtree = root.childrenRoot.siblingsRoot;
        while (subtree != null) {
            tmp.add(new BinomialTree<>(
                    new Node<>(subtree.value, subtree.degree, null, subtree.childrenRoot, null)
            ));
            subtree = subtree.siblingsRoot;
        }

        LinkedList<BinomialTree<T>> result = new LinkedList<>();
        while (!tmp.isEmpty()) {
            result.addLast(tmp.pollLast());
        }

        return result;
    }

    private static class Node<T> {
        private final T value;
        private final int degree;
        private final Node<T> parent;
        private final Node<T> childrenRoot;
        private final Node<T> siblingsRoot;

        private Node(T value, int degree, Node<T> parent, Node<T> childrenRoot, Node<T> siblingsRoot) {
            this.value = value;
            this.degree = degree;
            this.parent = parent;
            this.childrenRoot = childrenRoot;
            this.siblingsRoot = siblingsRoot;
        }

        private int getDegree() {
            return degree;
        }
    }
}
