package datastructures.heaps

/**
 * @author Ilya Murzinov
 */
abstract class PriorityQueue[T] {
  def add(e: T)
  def isEmpty: Boolean
  def findMin(): T
  def deleteMin(): T
}

class BinaryHeap[T] extends PriorityQueue[T] {
  override def add(e: T): Unit = ???

  override def isEmpty: T = ???

  override def findMin(): T = ???

  override def deleteMin(): Boolean = ???
}
