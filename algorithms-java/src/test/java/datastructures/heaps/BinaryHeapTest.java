package datastructures.heaps;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Ilya Murzinov
 */
public class BinaryHeapTest {

    private BinaryHeap<Integer> integerBinaryHeap;
    private BinaryHeap<Integer> integerBinaryHeapWithReverseComparator;
    private BinaryHeap<String> stringBinaryHeap;

    @Before
    public void setUp() throws Exception {
        integerBinaryHeap = new BinaryHeap<>();
        integerBinaryHeapWithReverseComparator = new BinaryHeap<>((i1, i2) -> i2 - i1);
        stringBinaryHeap = new BinaryHeap<>(String.CASE_INSENSITIVE_ORDER);
    }

    @Test
    public void testAddInteger() throws Exception {
        for (int i = 0; i < 1000; i++) {
            integerBinaryHeap.add(1000 - i - 1);
        }
        Assert.assertEquals(0, integerBinaryHeap.findMin(), 0);
    }

    @Test
    public void testAddIntegerWithReverseComparator() throws Exception {
        for (int i = 0; i < 1000; i++) {
            integerBinaryHeapWithReverseComparator.add(1000 - i - 1);
        }
        Assert.assertEquals(999, integerBinaryHeapWithReverseComparator.findMin(), 0);
    }

    @Test
    public void testAddString() throws Exception {
        stringBinaryHeap.add("aaaab");
        stringBinaryHeap.add("aaabb");
        stringBinaryHeap.add("aasab");
        stringBinaryHeap.add("aabab");
        stringBinaryHeap.add("aaaab");
        stringBinaryHeap.add("aaaaa");
        Assert.assertEquals("aaaaa", stringBinaryHeap.findMin());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNull() throws Exception {
        integerBinaryHeap.add(null);
    }

    @Test
    public void testDeleteInteger() throws Exception {
        for (int i = 0; i < 1000; i++) {
            integerBinaryHeap.add(1000 - i - 1);
        }

        for (int i = 0; i < 500; i++) {
            integerBinaryHeap.deleteMin();
        }

        Assert.assertEquals(500, integerBinaryHeap.findMin(), 0);
    }

    @Test
    public void testDeleteString() throws Exception {
        stringBinaryHeap.add("aaaab");
        stringBinaryHeap.add("aaabb");
        stringBinaryHeap.add("aasab");
        stringBinaryHeap.add("aabab");
        stringBinaryHeap.add("aaaab");
        stringBinaryHeap.add("aaaaa");

        stringBinaryHeap.deleteMin();
        stringBinaryHeap.deleteMin();
        stringBinaryHeap.deleteMin();
        stringBinaryHeap.deleteMin();

        Assert.assertEquals("aabab", stringBinaryHeap.findMin());
    }

    @Test
    public void testFindMinIntegerFromEmptyHeap() throws Exception {
        Integer integer = integerBinaryHeap.findMin();
        Assert.assertTrue(integerBinaryHeap.isEmpty());
        Assert.assertNull(integer);
    }

    @Test
    public void testDeleteIntegerFromEmptyHeap() throws Exception {
        Integer integer = integerBinaryHeap.deleteMin();
        Assert.assertTrue(integerBinaryHeap.isEmpty());
        Assert.assertNull(integer);
    }

    @Test
    public void testDeleteStringFromEmptyHeap() throws Exception {
        stringBinaryHeap.deleteMin();
        Assert.assertTrue(stringBinaryHeap.isEmpty());
    }

    @Test
    public void testIsEmpty() throws Exception {
        integerBinaryHeap.add(2);
        integerBinaryHeap.add(4);
        integerBinaryHeap.add(7);
        integerBinaryHeap.add(42);
        integerBinaryHeap.add(1);
        integerBinaryHeap.add(-100);

        integerBinaryHeap.deleteMin();
        integerBinaryHeap.deleteMin();
        integerBinaryHeap.deleteMin();
        integerBinaryHeap.deleteMin();
        integerBinaryHeap.deleteMin();
        integerBinaryHeap.deleteMin();

        Assert.assertTrue(integerBinaryHeap.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonComparable() throws Exception {
        BinaryHeap heap = new BinaryHeap();
        heap.add(new Object());
        heap.add(new Object());
    }

    @Test
    public void testPerformance() throws Exception {
        long[] times = new long[5];
        times[0] = addElements(1000);
        times[1] = addElements(10000);
        times[2] = addElements(100000);
        times[3] = addElements(1000000);
        times[4] = addElements(10000000);
        System.out.println(Arrays.toString(times));
    }

    private long addElements(long n) {
        BinaryHeap<Long> heap = new BinaryHeap<>();
        long millisecondsStart = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            heap.add(n - i - 1);
        }
        return System.currentTimeMillis() - millisecondsStart;
    }
}