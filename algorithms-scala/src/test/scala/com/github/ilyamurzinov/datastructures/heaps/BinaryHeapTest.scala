package com.github.ilyamurzinov.datastructures.heaps

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

/**
 * @author Ilya Murzinov
 */
@RunWith(classOf[JUnitRunner])
class BinaryHeapTest extends FlatSpec with Matchers {
  "BinaryHeap" should "add elements" in {
    val heap = new BinaryHeap[Int]
    heap.add(1)
    heap.findMin() should be(1)
  }
}
