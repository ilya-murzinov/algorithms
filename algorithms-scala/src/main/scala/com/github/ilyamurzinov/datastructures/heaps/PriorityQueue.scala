package com.github.ilyamurzinov.datastructures.heaps

/**
 * @author Ilya Murzinov
 */
trait PriorityQueue[+T] {
  def add[U >: T](e: U)(implicit ord: U => Ordered[U]): PriorityQueue[U]

  def isEmpty: Boolean

  def findMin(): T

  def deleteMin(): PriorityQueue[T]
}
