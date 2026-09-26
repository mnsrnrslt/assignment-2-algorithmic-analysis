public class LinkedList<T> {

    private static class Node<T> {
        T value;
        Node<T> prev;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    // --- benchmarking counters ---
    private long accessCount = 0;
    private long comparisonCount = 0;

    public int size() {
        return size;
    }

    public void resetCounters() {
        accessCount = 0;
        comparisonCount = 0;
    }

    public long getAccessCount() { return accessCount; }
    public long getComparisonCount() { return comparisonCount; }

    public void add(T x) {
        Node<T> node = new Node<>(x);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    public void add(int index, T x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node<T> target = getNode(index);
        Node<T> node = new Node<>(x);
        Node<T> before = target.prev;

        node.next = target;
        node.prev = before;
        target.prev = node;

        if (before == null) {
            head = node;
        } else {
            before.next = node;
        }
        size++;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> target = getNode(index);
        Node<T> before = target.prev;
        Node<T> after = target.next;

        if (before == null) {
            head = after;
        } else {
            before.next = after;
        }

        if (after == null) {
            tail = before;
        } else {
            after.prev = before;
        }

        size--;
        return target.value;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNode(index).value;
    }

    public boolean contains(T x) {
        Node<T> current = head;
        while (current != null) {
            comparisonCount++;
            if (current.value == null ? x == null : current.value.equals(x)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private Node<T> getNode(int index) {
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
                accessCount++;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
                accessCount++;
            }
        }
        accessCount++; // counting the final landing step too
        return current;
    }
}