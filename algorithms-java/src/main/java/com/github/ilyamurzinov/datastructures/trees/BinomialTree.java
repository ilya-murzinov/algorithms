package com.github.ilyamurzinov.datastructures.trees;

import java.util.LinkedList;

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

    public LinkedList<BinomialTree<T>> deleteRoot() {
        LinkedList<BinomialTree<T>> result = new LinkedList<>();

        if (root.childrenRoot == null) {
            return result;
        }

        result.add(new BinomialTree<>(
                new Node<>(root.childrenRoot.value, root.childrenRoot.degree, null, root.childrenRoot.childrenRoot, null)
        ));

        Node<T> subtree = root.childrenRoot.siblingsRoot;
        while (subtree != null) {
            result.addFirst(new BinomialTree<>(
                    new Node<>(subtree.value, subtree.degree, null, subtree.childrenRoot, null)
            ));
            subtree = subtree.siblingsRoot;
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

        @Override
        public String toString() {
            return "Node[degree: " + degree + ", "
                    + "value: " + value + ", "
                    + "children: " + (childrenRoot == null ? null : childrenRoot.toString())
                    + ", " + "siblings: " + (siblingsRoot == null ? null : siblingsRoot.toString())
                    + "]";
        }
    }

    @Override
    public String toString() {
        return String.format("BinomialTree[root: %s]", root.toString());
    }
}
