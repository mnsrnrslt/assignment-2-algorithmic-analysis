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

    public int size() {
        return size;
    }

    // ---- add(x): append at the end ----
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

    // ---- add(index, x): insert before current element at index ----
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
            head = node; // inserting at index 0
        } else {
            before.next = node;
        }
        size++;
    }

    // ---- remove(index) ----
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> target = getNode(index);
        Node<T> before = target.prev;
        Node<T> after = target.next;

        if (before == null) {
            head = after; // removing head
        } else {
            before.next = after;
        }

        if (after == null) {
            tail = before; // removing tail
        } else {
            after.prev = before;
        }

        size--;
        return target.value;
    }

    // ---- get(index) ----
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNode(index).value;
    }

    // ---- contains(x) ----
    public boolean contains(T x) {
        Node<T> current = head;
        while (current != null) {
            if (current.value == null ? x == null : current.value.equals(x)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Walks from whichever end (head or tail) is closer to index.
    private Node<T> getNode(int index) {
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }
}