package com.github.ilyamurzinov.datastructures.trees

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FunSuite}

/**
 * @author ilya-murzinov
 */
@RunWith(classOf[JUnitRunner])
class BinomialTreeTest extends FunSuite with Matchers {
  test("A binary tree") {
    val tree1: BinomialTree[Int] = BinomialTree(1)
    val tree2: BinomialTree[Int] = BinomialTree(42)
    val merged: BinomialTree[Int] = (tree1 :+: tree2) :+: (tree1 :+: tree2)
    merged.order shouldBe 3
  }
}
