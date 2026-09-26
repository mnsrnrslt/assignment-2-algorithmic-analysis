public class DynamicArray<T> {

    private Object[] data;
    private int size;

    private static final int DEFAULT_CAPACITY = 10;

    // --- benchmarking counters ---
    private long accessCount = 0;
    private long comparisonCount = 0;
    private long movementCount = 0;

    public DynamicArray() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public int size() {
        return size;
    }

    public void resetCounters() {
        accessCount = 0;
        comparisonCount = 0;
        movementCount = 0;
    }

    public long getAccessCount() { return accessCount; }
    public long getComparisonCount() { return comparisonCount; }
    public long getMovementCount() { return movementCount; }

    public void add(T x) {
        ensureCapacity(size + 1);
        data[size] = x;
        movementCount++;
        size++;
    }

    public void add(int index, T x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            movementCount++;
        }
        data[index] = x;
        movementCount++;
        size++;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        T removed = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            movementCount++;
        }
        data[size - 1] = null;
        size--;
        return removed;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        accessCount++;
        @SuppressWarnings("unchecked")
        T value = (T) data[index];
        return value;
    }

    public boolean contains(T x) {
        for (int i = 0; i < size; i++) {
            comparisonCount++;
            if (data[i] == null ? x == null : data[i].equals(x)) {
                return true;
            }
        }
        return false;
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