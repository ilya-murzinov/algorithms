package com.github.ilyamurzinov.datastructures.heaps;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

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

    @Test
    public void testMergeWithDifferentDegrees() throws Exception {
        BinomialHeap<Integer> heap1 = new BinomialHeap<>();
        BinomialHeap<Integer> heap2 = new BinomialHeap<>();

        for (int i = -10; i < 0; i++) {
            heap1.add(i);
        }

        for (int i = 0; i < 1024; i++) {
            heap2.add(i);
        }

        Assert.assertEquals(-10, heap1.merge(heap2).findMin(), 0);
    }

    @Test
    public void testDeleteAddMerge() throws Exception {
        BinomialHeap<Integer> heap1 = new BinomialHeap<>();
        BinomialHeap<Integer> heap2 = new BinomialHeap<>();
        BinomialHeap<Integer> heap3 = new BinomialHeap<>();
        BinomialHeap<Integer> heap4 = new BinomialHeap<>();

        for (int i = 0; i < 10; i++) {
            heap1.add(i);
            heap3.add(i);

            heap2.add(i + 10);
            heap4.add(i + 10);
        }

        heap4.add(heap3.deleteMin());
        Assert.assertTrue(heapEquals(heap1.merge(heap2), heap3.merge(heap4)));
    }

    @Test
    public void testDeleteAddMergeWithRandom() throws Exception {
        for (int j = 0; j < 100; j++) {
            BinomialHeap<Integer> heap1 = new BinomialHeap<>();
            BinomialHeap<Integer> heap2 = new BinomialHeap<>();
            BinomialHeap<Integer> heap3 = new BinomialHeap<>();
            BinomialHeap<Integer> heap4 = new BinomialHeap<>();

            Random random = new Random();

            for (int i = 0; i < 1000; i++) {
                int i1 = random.nextInt(1000);
                int i2 = random.nextInt(1000);
                heap1.add(i1);
                heap3.add(i1);

                heap2.add(i2);
                heap4.add(i2);
            }

            heap4.add(heap3.deleteMin());
            BinomialHeap<Integer> merge1 = heap1.merge(heap2);
            BinomialHeap<Integer> merge2 = heap3.merge(heap4);
            Assert.assertTrue(heapEquals(merge1, merge2));
        }
    }

    @Test
    public void testToString() throws Exception {
        stringBinomialHeap.add("aaaab");
        stringBinomialHeap.add("aaabb");
        stringBinomialHeap.add("aasab");
        stringBinomialHeap.add("aabab");
        stringBinomialHeap.add("aaaab");
        stringBinomialHeap.add("aaaaa");
        stringBinomialHeap.add("azzzz");
        Assert.assertEquals(
                "BinomialHeap[trees (size: 3): BinomialTree[root: Node[degree: 0, value: azzzz, children: null, siblings: null]], BinomialTree[root: Node[degree: 1, value: aaaaa, children: Node[degree: 0, value: aaaab, children: null, siblings: null], siblings: null]], BinomialTree[root: Node[degree: 2, value: aaaab, children: Node[degree: 1, value: aabab, children: Node[degree: 0, value: aasab, children: null, siblings: null], siblings: Node[degree: 0, value: aaabb, children: null, siblings: null]], siblings: null]]]",
                stringBinomialHeap.toString()
        );
    }

    private boolean heapEquals(BinomialHeap<Integer> heap1, BinomialHeap<Integer> heap2) {
        if (heap1.isEmpty() && heap2.isEmpty()) {
            return true;
        }
        int m1 = heap1.deleteMin();
        int m2 = heap2.deleteMin();

        return m1 == m2 && heapEquals(heap1, heap2);
    }
}
