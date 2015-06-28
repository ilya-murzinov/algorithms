package datastructures.heaps

/**
 * @author Ilya Murzinov
 */
sealed trait Heap[+A] {
  def min: A

  def left: Heap[A]
  def right: Heap[A]
  def isEmpty: Boolean

  val size: Int
  val height: Int
}

case object Leaf extends Heap[Nothing] {
  val size: Int = 0
  val height: Int = 0
  override def isEmpty: Boolean = true

  override def min: Nothing = throw new NoSuchElementException("min of leaf")
  override def left: Heap[Nothing] = throw new NoSuchElementException("left of leaf")
  override def right: Heap[Nothing] = throw new NoSuchElementException("right of leaf")
}

case class Branch[+A](min: A, left: Heap[A] = Leaf, right: Heap[A] = Leaf) extends Heap[A] {
  override def isEmpty: Boolean = false
  val size: Int = left.size + right.size + 1
  val height: Int = math.max(left.height, right.height) + 1
}

trait PriorityQueue[T] {
  def add(e: T): BinaryHeap[T]
  def isEmpty: Boolean
  def findMin(): T
  def deleteMin(): T
}

class BinaryHeap[T] extends PriorityQueue[T] {
  override def add(e: T): BinaryHeap[T] = ???

  override def isEmpty: Boolean = ???

  override def findMin(): T = ???

  override def deleteMin(): T = ???
}
