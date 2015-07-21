package com.github.ilyamurzinov.datastructures.trees

/**
 * @author ilya-murzinov
 */
case class BinomialTree[T] private (rootNode: Node[T]) {
  def order: Int = rootNode.degree
  def root: T = rootNode.value
  def merge(that: BinomialTree[T]): BinomialTree[T] = {
    if (order != that.order) throw new IllegalArgumentException
    else {
      val newChildrenRoot: Branch[T] =
        new Branch[T](that.root, that.order, that.rootNode.childrenRoot, rootNode.childrenRoot)

      new BinomialTree(new Branch[T](root, order + 1, newChildrenRoot, Leaf))
    }
  }
  def deleteRoot(): List[BinomialTree[T]] = {
    def get(node: Node[T], acc: List[BinomialTree[T]]): List[BinomialTree[T]] = node match {
      case Leaf => acc
      case Branch(v, d, c, s) =>
        get(node.siblingRoot, acc) :+ new BinomialTree[T](new Branch(v, d, c, Leaf))
    }

    get(rootNode.childrenRoot, Nil)
  }
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

case class Branch[T](value: T, degree: Int, childrenRoot: Node[T], siblingRoot: Node[T]) extends Node[T]

object BinomialTree {
  def apply[T](value: T): BinomialTree[T] = new BinomialTree[T](new Branch[T](value, 0, Leaf, Leaf))
}