package datastructures.heaps;

import java.util.Comparator;

/**
 * @author Ilya Murzinov
 */
public class BinaryHeap<T> implements PriorityQueue<T> {

    public static final int INITIAL_CAPACITY = 16;

    private Comparator<T> comparator;

    private T[] elements;
    private int size;
    private int capacity;

    public BinaryHeap() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public BinaryHeap(Comparator<T> comparator) {
        elements = (T[]) new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.capacity = INITIAL_CAPACITY;
        this.comparator = comparator;
    }

    @Override
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("element");
        }

        size++;
        ensureCapacity();

        elements[size - 1] = element;
        heapifyUp(size - 1);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T findMin() {
        if (size == 0) {
            return null;
        }

        return elements[0];
    }

    @Override
    public T deleteMin() {
        if (size == 0) {
            return null;
        }

        T result = elements[0];

        swap(0, size - 1);
        elements[size - 1] = null;

        size--;
        shrink();

        heapifyDown(0);

        return result;
    }

    private void heapifyUp(int index) {
        if (index <= 0) {
            return;
        }

        int parentIndex = getParentIndex(index);

        if (compare(elements[parentIndex], elements[index]) > 0) {
            swap(parentIndex, index);
            heapifyUp(parentIndex);
        }
    }

    private void heapifyDown(int index) {
        int min = index;
        int leftChildIndex = getLeftChildIndex(index);
        int rightChildIndex = getRightChildIndex(index);

        if (leftChildIndex < size && compare(elements[index], elements[leftChildIndex]) > 0) {
            min = leftChildIndex;
        }

        if (rightChildIndex < size && compare(elements[index], elements[rightChildIndex]) > 0
                && compare(elements[leftChildIndex], elements[rightChildIndex]) > 0) {
            min = rightChildIndex;
        }

        if (min != index) {
            swap(min, index);
            heapifyDown(min);
        }
    }

    private int compare(T element1, T element2) {
        assert element1 != null;
        assert element2 != null;

        if (comparator != null) {
            return comparator.compare(element1, element2);
        } else {
            try {
                return ((Comparable<T>) element1).compareTo(element2);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(
                        "Cannot compare instances of non-comparable class without comparator");
            }
        }
    }

    private void swap(int index1, int index2) {
        assert index1 >= 0;
        assert index1 < size;
        assert index2 >= 0;
        assert index2 < size;

        T tmp = elements[index1];
        elements[index1] = elements[index2];
        elements[index2] = tmp;
    }

    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    private int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == capacity) {
            T[] tmp = elements;
            capacity = size * 2;
            elements = (T[]) new Object[capacity];
            if (tmp.length > 0) {
                System.arraycopy(tmp, 0, elements, 0, size);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void shrink() {
        if (capacity > INITIAL_CAPACITY && ((double) size) / capacity < 0.25) {
            T[] tmp = elements;
            capacity = capacity / 2;
            elements = (T[]) new Object[capacity];
            System.arraycopy(tmp, 0, elements, 0, capacity);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BinaryHeap: [");
        for (T element : elements) {
            if (element == null) {
                break;
            }

            stringBuilder.append(element).append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
