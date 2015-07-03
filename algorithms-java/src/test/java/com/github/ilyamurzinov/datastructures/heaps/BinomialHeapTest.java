package com.github.ilyamurzinov.datastructures.heaps;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ilya Murzinov
 */
public class BinomialHeapTest {

    private BinomialHeap<Integer> integerBinomialHeap;
    private BinomialHeap<Integer> integerBinomialHeapWithReverseComparator;
    private BinomialHeap<String> stringBinomialHeap;

    @Before
    public void setUp() throws Exception {
        integerBinomialHeap = new BinomialHeap<>();
        integerBinomialHeapWithReverseComparator = new BinomialHeap<>((i1, i2) -> i2 - i1);
        stringBinomialHeap = new BinomialHeap<>(String.CASE_INSENSITIVE_ORDER);
    }

    @Test
    public void testAddInteger() throws Exception {
        integerBinomialHeap.add(2);
        integerBinomialHeap.add(3);
        integerBinomialHeap.add(1);
        integerBinomialHeap.add(0);
        Assert.assertEquals(0, integerBinomialHeap.findMin(), 0);
    }

    @Test
    public void testAddIntegers() throws Exception {
        for (int i = 0; i < 1000; i++) {
            integerBinomialHeap.add(1000 - i - 1);
        }
        Assert.assertEquals(0, integerBinomialHeap.findMin(), 0);
    }

    @Test
    public void testAddIntegerWithReverseComparator() throws Exception {
        for (int i = 0; i < 1000; i++) {
            integerBinomialHeapWithReverseComparator.add(1000 - i - 1);
        }
        Assert.assertEquals(999, integerBinomialHeapWithReverseComparator.findMin(), 0);
    }

    @Test
    public void testAddString() throws Exception {
        stringBinomialHeap.add("aaaab");
        stringBinomialHeap.add("aaabb");
        stringBinomialHeap.add("aasab");
        stringBinomialHeap.add("aabab");
        stringBinomialHeap.add("aaaab");
        stringBinomialHeap.add("aaaaa");
        Assert.assertEquals("aaaaa", stringBinomialHeap.findMin());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNull() throws Exception {
        integerBinomialHeap.add(null);
    }

    @Test
    public void testDeleteInteger() throws Exception {
        for (int i = 0; i < 1000; i++) {
            integerBinomialHeap.add(1000 - i - 1);
        }

        for (int i = 0; i < 500; i++) {
            integerBinomialHeap.deleteMin();
        }

        Assert.assertEquals(500, integerBinomialHeap.findMin(), 0);
    }

    @Test
    public void testDeleteInteger2() throws Exception {
        for (int i = 0; i < 1000; i++) {
            integerBinomialHeap.add(1000 - i - 1);
        }

        for (int i = 0; i < 999; i++) {
            integerBinomialHeap.deleteMin();
        }

        Assert.assertEquals(999, integerBinomialHeap.findMin(), 0);
    }

    @Test
    public void testDeleteString() throws Exception {
        stringBinomialHeap.add("aaaab");
        stringBinomialHeap.add("aaabb");
        stringBinomialHeap.add("aasab");
        stringBinomialHeap.add("aabab");
        stringBinomialHeap.add("aaaab");
        stringBinomialHeap.add("aaaaa");
        stringBinomialHeap.add("azzzz");

        stringBinomialHeap.deleteMin();
        stringBinomialHeap.deleteMin();
        stringBinomialHeap.deleteMin();
        stringBinomialHeap.deleteMin();

        Assert.assertEquals("aabab", stringBinomialHeap.findMin());
    }

    @Test
    public void testFindMinIntegerFromEmptyHeap() throws Exception {
        Integer integer = integerBinomialHeap.findMin();
        Assert.assertTrue(integerBinomialHeap.isEmpty());
        Assert.assertNull(integer);
    }

    @Test
    public void testDeleteIntegerFromEmptyHeap() throws Exception {
        Integer integer = integerBinomialHeap.deleteMin();
        Assert.assertTrue(integerBinomialHeap.isEmpty());
        Assert.assertNull(integer);
    }

    @Test
    public void testDeleteStringFromEmptyHeap() throws Exception {
        stringBinomialHeap.deleteMin();
        Assert.assertTrue(stringBinomialHeap.isEmpty());
    }

    @Test
    public void testIsEmpty() throws Exception {
        integerBinomialHeap.add(2);
        integerBinomialHeap.add(4);
        integerBinomialHeap.add(7);
        integerBinomialHeap.add(42);
        integerBinomialHeap.add(1);
        integerBinomialHeap.add(-100);

        integerBinomialHeap.deleteMin();
        integerBinomialHeap.deleteMin();
        integerBinomialHeap.deleteMin();
        integerBinomialHeap.deleteMin();
        integerBinomialHeap.deleteMin();
        integerBinomialHeap.deleteMin();

        Assert.assertTrue(integerBinomialHeap.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonComparable() throws Exception {
        BinomialHeap<Object> heap = new BinomialHeap<>();
        heap.add(new Object());
        heap.add(new Object());
    }

    @Test
    public void testMerge() throws Exception {
        BinomialHeap<Integer> heap1 = new BinomialHeap<>();
        heap1.add(2);
        heap1.add(7);
        heap1.add(5);
        heap1.add(0);

        BinomialHeap<Integer> heap2 = new BinomialHeap<>();
        heap1.add(1);
        heap1.add(3);
        heap1.add(6);
        heap1.add(4);

        Assert.assertEquals(0, heap1.merge(heap2).findMin(), 0);
    }
}
