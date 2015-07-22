package com.github.ilyamurzinov.datastructures.trees

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

/**
 * @author ilya-murzinov
 */
@RunWith(classOf[JUnitRunner])
class BinomialTreeSpec extends FlatSpec with Matchers {
  "A binomial tree" should "be created successfully" in {
    val tree: BinomialTree[Int] = BinomialTree(1)
    tree.order shouldBe 0
    tree.root shouldBe 1
  }

  "A binomial tree of order 0" should "merge with another binomial tree of order 0 successfully" in {
    val tree: BinomialTree[Int] = BinomialTree(21) merge BinomialTree(42)
    tree.order shouldBe 1
    tree.root shouldBe 21
  }

  "A binomial tree" should "not merge tree of different order" in {
    intercept[IllegalArgumentException] {
      BinomialTree(21) merge BinomialTree(42) merge BinomialTree(11)
    }
  }

  "A binomial tree of order 1" should "merge with another binomial tree of order 1 successfully" in {
    val tree: BinomialTree[Int] =
      (BinomialTree(42) merge BinomialTree(21)) merge (BinomialTree(11) merge BinomialTree(14))
    tree.order shouldBe 2
    tree.root shouldBe 42
  }

  "A binomial tree" should "delete root in tree of order 0" in {
    val trees: List[BinomialTree[Int]] = BinomialTree(42).deleteRoot()
    trees shouldBe Nil
  }

  "A binomial tree" should "delete root in tree of order 1" in {
    val trees: List[BinomialTree[Int]] = (BinomialTree(42) merge BinomialTree(21)).deleteRoot()
    trees.size shouldBe 1
    trees.head shouldBe BinomialTree(21)
  }

  "A binomial tree" should "delete root in tree of order 2" in {
    val trees: List[BinomialTree[Int]] =
      ((BinomialTree(42) merge BinomialTree(21)) merge (BinomialTree(11) merge BinomialTree(14))).deleteRoot()
    trees.size shouldBe 2
    trees.head shouldBe BinomialTree(21)
    trees.tail.head shouldBe (BinomialTree(11) merge BinomialTree(14))
  }
}
