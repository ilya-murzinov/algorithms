package com.github.ilyamurzinov.datastructures.heaps

/**
 * @author Ilya Murzinov
 */
abstract sealed class BinaryHeap[+T] extends PriorityQueue[T] {
  def min: T
  def left: BinaryHeap[T]
  def right: BinaryHeap[T]

  val size: Int
  val height: Int

  override def add[U >: T](e: U)(implicit ord: U => Ordered[U]): BinaryHeap[U] =
    if (isEmpty) BinaryHeap.make(e)
    else if (left.size < 2 * left.height * left.height - 1)
      BinaryHeap.bubbleUp(min, left.add(e), right)
    else if (left.size < 2 * right.height * right.height - 1)
      BinaryHeap.bubbleUp(min, left, right.add(e))
    else if (right.height < left.height)
      BinaryHeap.bubbleUp(min, left, right.add(e))
    else
      BinaryHeap.bubbleUp(min, left.add(e), right)

  override def isEmpty: Boolean = false

  override def findMin(): T = min

  override def deleteMin(): BinaryHeap[T] = ???
}

case object Leaf extends BinaryHeap[Nothing] {
  val size: Int = 0
  val height: Int = 0

  override def isEmpty: Boolean = true
  override def min: Nothing = throw new NoSuchElementException("min of leaf")
  override def left: BinaryHeap[Nothing] = throw new NoSuchElementException("left of leaf")
  override def right: BinaryHeap[Nothing] = throw new NoSuchElementException("right of leaf")
}

case class Branch[+T](min: T, left: BinaryHeap[T], right: BinaryHeap[T]) extends BinaryHeap[T] {
  override def isEmpty: Boolean = false
  val size: Int = left.size + right.size + 1
  val height: Int = math.max(left.height, right.height) + 1
}

object BinaryHeap {

  def empty[T]: BinaryHeap[T] = Leaf

  def make[T](min: T, left: BinaryHeap[T] = Leaf, right: BinaryHeap[T] = Leaf): BinaryHeap[T] =
    Branch[T](min, left, right)

  private[BinaryHeap] def bubbleUp[T](min: T, left: BinaryHeap[T], right: BinaryHeap[T])
                                     (implicit ord: T => Ordered[T]): BinaryHeap[T] =
    (left, right) match {
      case (Branch(y, lt, rt), _) if min > y =>
        Branch(y, Branch(min, lt, rt), right)
      case (_, Branch(z, lt, rt)) if min > z =>
        Branch(z, left, Branch(min, lt, rt))
      case (_, _) =>
        Branch(min, left, right)
    }
}