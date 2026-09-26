import java.util.NoSuchElementException;

public class MinHeap<T extends Comparable<T>> {

    private Object[] data;
    private int size;

    private static final int DEFAULT_CAPACITY = 10;

    // --- benchmarking counter ---
    private long comparisonCount = 0;

    public MinHeap() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void resetCounters() {
        comparisonCount = 0;
    }

    public long getComparisonCount() { return comparisonCount; }

    public void insert(T x) {
        ensureCapacity(size + 1);
        data[size] = x;
        siftUp(size);
        size++;
    }

    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException("Heap is empty");
        }
        return get(0);
    }

    public T extractMin() {
        if (size == 0) {
            throw new NoSuchElementException("Heap is empty");
        }
        T min = get(0);
        size--;
        data[0] = data[size];
        data[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return min;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(index, parent) < 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && compare(left, smallest) < 0) {
                smallest = left;
            }
            if (right < size && compare(right, smallest) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(int i, int j) {
        comparisonCount++;
        return ((T) data[i]).compareTo((T) data[j]);
    }

    private void swap(int i, int j) {
        Object tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    @SuppressWarnings("unchecked")
    private T get(int index) {
        return (T) data[index];
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCapacity = data.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newData = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
    }
}