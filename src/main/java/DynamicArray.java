public class DynamicArray<T> {

    private Object[] data;
    private int size;

    private static final int DEFAULT_CAPACITY = 10;

    public DynamicArray() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public int size() {
        return size;
    }

    // ---- add(x): append at the end ----
    public void add(T x) {
        ensureCapacity(size + 1);
        data[size] = x;
        size++;
    }

    // ---- add(index, x): insert at arbitrary position ----
    public void add(int index, T x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        // shift everything from index..size-1 one step to the right
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = x;
        size++;
    }

    // ---- remove(index) ----
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        T removed = (T) data[index];
        // shift everything after index one step to the left
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null; // help GC
        size--;
        return removed;
    }

    // ---- get(index) ----
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        T value = (T) data[index];
        return value;
    }

    // ---- contains(x) ----
    public boolean contains(T x) {
        for (int i = 0; i < size; i++) {
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