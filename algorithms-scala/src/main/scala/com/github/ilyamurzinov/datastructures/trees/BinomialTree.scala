package com.github.ilyamurzinov.datastructures.trees

/**
 * @author ilya-murzinov
 */
abstract sealed class BinomialTree[T] {
  def rootNode: Node[T]
  def order: Int = rootNode.degree
  def root: T = rootNode.value
  def merge(that: BinomialTree[T]): BinomialTree[T] = {
    if (order != that.order) throw new IllegalArgumentException
    else {
      val newChildrenRoot: Branch[T] =
        new Branch[T](that.root, that.order, rootNode, that.rootNode.childrenRoot)

      BinomialTree.make(root, order + 1, newChildrenRoot, Leaf)
    }
  }
  def :+:(that: BinomialTree[T]): BinomialTree[T] = merge(that)
}

trait Node[+T] {
  def value: T
  def degree: Int
  def childrenRoot: Node[T]
  def siblingRoot: Node[T]
}

case object Leaf extends Node[Nothing] {
  def value: Nothing = throw new NoSuchElementException
  def degree: Int = throw new NoSuchElementException
  def childrenRoot: Node[Nothing] = throw new NoSuchElementException
  def siblingRoot: Node[Nothing] = throw new NoSuchElementException
}

case class Branch[T] (value: T, degree: Int, childrenRoot: Node[T], siblingRoot: Node[T]) extends Node[T]

object BinomialTree {
  private[BinomialTree] def make[T](value: T, degree: Int,
                                    childrenRoot: Node[T], siblingRoot: Node[T]): BinomialTree[T] = {
    new BinomialTree[T] {
      val rootNode: Node[T] = new Branch[T](value, degree, childrenRoot, siblingRoot)
    }
  }

  def apply[T](value: T): BinomialTree[T] = make(value, 1, Leaf, Leaf)
}