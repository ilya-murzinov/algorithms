package com.github.ilyamurzinov.datastructures.heaps

import org.junit.runner.RunWith
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck._
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

/**
 * @author Ilya Murzinov
 */
@RunWith(classOf[JUnitRunner])
class BinaryHeapTest extends FunSuite with Checkers {
  test("BinaryHeap should satisfy all properties") {
    check(new BinaryHeapProperties)
  }
}

class BinaryHeapProperties extends Properties("Heap") {
  property("min") = forAll { a: Int =>
    val h = BinaryHeap.empty.add(a)
    h.min == a
  }

  property("gen") = forAll { (h: BinaryHeap[Int]) =>
    val m = if (h.isEmpty) 0 else h.min
    h.add(m).min == m
  }

//  property("deleteMin") = forAll { (a: Int) =>
//    BinaryHeap.empty == BinaryHeap.empty.add(a).deleteMin()
//  }

  property("findMin") = forAll { (a: Int) =>
    forAll { (b: Int) => {
      BinaryHeap.empty.add(a).add(b).min == (if (a > b) b else a) }
    }
  }

//  property("sort") = forAll { (h: BinaryHeap[Int]) =>
//    def getList(heap: BinaryHeap[Int]): List[Int] = {
//      if (heap.isEmpty) Nil
//      else heap.min :: getList(heap.deleteMin())
//    }
//
//    val list: List[Int] = getList(h)
//    list.sorted == list
//  }

  lazy val genHeap: Gen[BinaryHeap[Int]] = for {
    x <- arbitrary[Int]
    h <- oneOf(const(BinaryHeap.empty[Int]), genHeap)
  } yield h.add(x)

  implicit lazy val arbHeap: Arbitrary[BinaryHeap[Int]] = Arbitrary(genHeap)
}
