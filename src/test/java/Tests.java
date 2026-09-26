import java.util.NoSuchElementException;

public class Tests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testDynamicArray();
        testLinkedList();
        testMinHeap();

        System.out.println();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }

    // ==================== DynamicArray ====================
    private static void testDynamicArray() {
        System.out.println("--- DynamicArray ---");

        DynamicArray<Integer> empty = new DynamicArray<>();
        check("empty.size() == 0", empty.size() == 0);
        check("empty.contains(1) == false", !empty.contains(1));
        checkThrows("empty.get(0) throws", () -> empty.get(0));
        checkThrows("empty.remove(0) throws", () -> empty.remove(0));

        DynamicArray<Integer> single = new DynamicArray<>();
        single.add(42);
        check("single.size() == 1", single.size() == 1);
        check("single.get(0) == 42", single.get(0) == 42);
        check("single.contains(42) == true", single.contains(42));
        single.remove(0);
        check("after remove, size == 0", single.size() == 0);

        DynamicArray<Integer> arr = new DynamicArray<>();
        for (int i = 0; i < 5; i++) arr.add(i);
        check("get(0) == 0 (first boundary)", arr.get(0) == 0);
        check("get(4) == 4 (last boundary)", arr.get(4) == 4);

        arr.add(0, -1);
        check("add at index 0 shifts correctly", arr.get(0) == -1 && arr.get(1) == 0);

        arr.add(arr.size(), 99);
        check("add at index == size appends", arr.get(arr.size() - 1) == 99);

        DynamicArray<Integer> dup = new DynamicArray<>();
        dup.add(7); dup.add(7); dup.add(7);
        check("duplicates: size == 3", dup.size() == 3);
        check("duplicates: contains(7)", dup.contains(7));
        dup.remove(0);
        check("duplicates: after removing one, still contains(7)", dup.contains(7));
        check("duplicates: size == 2 after removal", dup.size() == 2);

        checkThrows("add(-1, x) throws", () -> arr.add(-1, 1));
        checkThrows("add(size+1, x) throws", () -> arr.add(arr.size() + 1, 1));
        checkThrows("get(-1) throws", () -> arr.get(-1));
        checkThrows("get(size) throws", () -> arr.get(arr.size()));
        checkThrows("remove(-1) throws", () -> arr.remove(-1));
        checkThrows("remove(size) throws", () -> arr.remove(arr.size()));

        DynamicArray<Integer> big = new DynamicArray<>();
        for (int i = 0; i < 50_000; i++) big.add(i);
        check("large input: size == 50000", big.size() == 50000);
        check("large input: get(25000) == 25000", big.get(25_000) == 25_000);
    }

    // ==================== LinkedList ====================
    private static void testLinkedList() {
        System.out.println("--- LinkedList ---");

        LinkedList<Integer> empty = new LinkedList<>();
        check("empty.size() == 0", empty.size() == 0);
        check("empty.contains(1) == false", !empty.contains(1));
        checkThrows("empty.get(0) throws", () -> empty.get(0));
        checkThrows("empty.remove(0) throws", () -> empty.remove(0));

        LinkedList<Integer> single = new LinkedList<>();
        single.add(42);
        check("single.size() == 1", single.size() == 1);
        check("single.get(0) == 42", single.get(0) == 42);
        single.remove(0);
        check("after remove, size == 0 (head/tail reset correctly)", single.size() == 0);
        single.add(100);
        check("re-add after empty works", single.get(0) == 100 && single.size() == 1);

        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 5; i++) list.add(i);
        check("get(0) == 0 (first boundary)", list.get(0) == 0);
        check("get(4) == 4 (last boundary)", list.get(4) == 4);

        list.remove(0);
        check("remove head works", list.get(0) == 1);
        list.remove(list.size() - 1);
        check("remove tail works", list.get(list.size() - 1) == 3);

        list.add(0, -1);
        check("add at head works", list.get(0) == -1);
        list.add(list.size(), 999);
        check("add at index == size appends", list.get(list.size() - 1) == 999);

        LinkedList<Integer> dup = new LinkedList<>();
        dup.add(7); dup.add(7); dup.add(7);
        check("duplicates: size == 3", dup.size() == 3);
        dup.remove(1);
        check("duplicates: still contains(7) after removing middle one", dup.contains(7));
        check("duplicates: size == 2", dup.size() == 2);

        checkThrows("add(-1, x) throws", () -> list.add(-1, 1));
        checkThrows("get(-1) throws", () -> list.get(-1));
        checkThrows("get(size) throws", () -> list.get(list.size()));
        checkThrows("remove(-1) throws", () -> list.remove(-1));
        checkThrows("remove(size) throws", () -> list.remove(list.size()));

        LinkedList<Integer> big = new LinkedList<>();
        for (int i = 0; i < 50_000; i++) big.add(i);
        check("large input: size == 50000", big.size() == 50_000);
        check("large input: get(25000) == 25000", big.get(25_000) == 25_000);
    }

    // ==================== MinHeap ====================
    private static void testMinHeap() {
        System.out.println("--- MinHeap ---");

        MinHeap<Integer> empty = new MinHeap<>();
        check("empty.isEmpty() == true", empty.isEmpty());
        checkThrows("empty.peekMin() throws", empty::peekMin);
        checkThrows("empty.extractMin() throws", empty::extractMin);

        MinHeap<Integer> single = new MinHeap<>();
        single.insert(5);
        check("single.peekMin() == 5", single.peekMin() == 5);
        check("single.extractMin() == 5", single.extractMin() == 5);
        check("after extract, isEmpty() == true", single.isEmpty());

        MinHeap<Integer> dup = new MinHeap<>();
        dup.insert(3); dup.insert(3); dup.insert(3);
        check("duplicates: peekMin() == 3", dup.peekMin() == 3);
        check("duplicates: 3 extractions all == 3",
                dup.extractMin() == 3 && dup.extractMin() == 3 && dup.extractMin() == 3);
        check("duplicates: isEmpty() after", dup.isEmpty());

        MinHeap<Integer> heap = new MinHeap<>();
        int[] values = {5, 3, 8, 1, 9, 2, 7, 4, 6, 0};
        for (int v : values) heap.insert(v);

        int previous = Integer.MIN_VALUE;
        boolean nonDecreasing = true;
        int count = 0;
        while (!heap.isEmpty()) {
            int extracted = heap.extractMin();
            if (extracted < previous) nonDecreasing = false;
            previous = extracted;
            count++;
        }
        check("extractMin() returns non-decreasing sequence", nonDecreasing);
        check("all 10 elements were extracted", count == 10);

        MinHeap<Integer> big = new MinHeap<>();
        java.util.Random random = new java.util.Random(42);
        for (int i = 0; i < 50_000; i++) big.insert(random.nextInt());
        int prev = Integer.MIN_VALUE;
        boolean ok = true;
        while (!big.isEmpty()) {
            int v = big.extractMin();
            if (v < prev) { ok = false; break; }
            prev = v;
        }
        check("large input (50000): heap property holds through full drain", ok);
    }

    // ==================== helpers ====================
    private interface ThrowingAction {
        void run();
    }

    private static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + description);
        } else {
            failed++;
            System.out.println("  [FAIL] " + description);
        }
    }

    private static void checkThrows(String description, ThrowingAction action) {
        try {
            action.run();
            failed++;
            System.out.println("  [FAIL] " + description + " (no exception thrown)");
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            passed++;
            System.out.println("  [PASS] " + description);
        }
    }
}