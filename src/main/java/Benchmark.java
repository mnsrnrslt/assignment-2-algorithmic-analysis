import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int REPEATS = 5;
    private static final long SEED = 42;

    public static void main(String[] args) throws IOException {
        new java.io.File("results/tables").mkdirs();

        runRandomAccessWorkload();
        runSearchWorkload();
        runInsertRemoveWorkload();
        runHeapWorkload();

        System.out.println("Done. CSV files written to results/tables/");
    }

    private static void runRandomAccessWorkload() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter("results/tables/workload1_random_access.csv"))) {
            out.println("structure,n,avgTimeNanos,accesses");

            for (int n : SIZES) {
                double totalArr = 0, totalList = 0;
                long accessesArr = 0, accessesList = 0;

                for (int rep = 0; rep < REPEATS; rep++) {
                    Random random = new Random(SEED);

                    DynamicArray<Integer> array = new DynamicArray<>();
                    for (int i = 0; i < n; i++) array.add(random.nextInt());

                    int[] indices = new int[10_000];
                    for (int i = 0; i < indices.length; i++) indices[i] = random.nextInt(n);

                    array.resetCounters();
                    long start = System.nanoTime();
                    for (int idx : indices) array.get(idx);
                    totalArr += (System.nanoTime() - start);
                    accessesArr = array.getAccessCount();
                }

                for (int rep = 0; rep < REPEATS; rep++) {
                    Random random = new Random(SEED);

                    LinkedList<Integer> list = new LinkedList<>();
                    for (int i = 0; i < n; i++) list.add(random.nextInt());

                    int[] indices = new int[10_000];
                    for (int i = 0; i < indices.length; i++) indices[i] = random.nextInt(n);

                    list.resetCounters();
                    long start = System.nanoTime();
                    for (int idx : indices) list.get(idx);
                    totalList += (System.nanoTime() - start);
                    accessesList = list.getAccessCount();
                }

                out.println("DynamicArray," + n + "," + (totalArr / REPEATS) + "," + accessesArr);
                out.println("LinkedList," + n + "," + (totalList / REPEATS) + "," + accessesList);
            }
        }
    }

    private static void runSearchWorkload() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter("results/tables/workload2_search.csv"))) {
            out.println("structure,n,avgTimeNanos,comparisons");

            for (int n : SIZES) {
                double totalArr = 0, totalList = 0;
                long compArr = 0, compList = 0;

                for (int rep = 0; rep < REPEATS; rep++) {
                    Random random = new Random(SEED);

                    DynamicArray<Integer> array = new DynamicArray<>();
                    for (int i = 0; i < n; i++) array.add(random.nextInt());

                    int[] searchValues = new int[1_000];
                    for (int i = 0; i < searchValues.length; i++) {
                        searchValues[i] = (random.nextBoolean())
                                ? array.get(random.nextInt(n))
                                : random.nextInt();
                    }

                    array.resetCounters();
                    long start = System.nanoTime();
                    for (int v : searchValues) array.contains(v);
                    totalArr += (System.nanoTime() - start);
                    compArr = array.getComparisonCount();
                }

                for (int rep = 0; rep < REPEATS; rep++) {
                    Random random = new Random(SEED);

                    LinkedList<Integer> list = new LinkedList<>();
                    for (int i = 0; i < n; i++) list.add(random.nextInt());

                    int[] searchValues = new int[1_000];
                    for (int i = 0; i < searchValues.length; i++) {
                        searchValues[i] = (random.nextBoolean())
                                ? list.get(random.nextInt(n))
                                : random.nextInt();
                    }

                    list.resetCounters();
                    long start = System.nanoTime();
                    for (int v : searchValues) list.contains(v);
                    totalList += (System.nanoTime() - start);
                    compList = list.getComparisonCount();
                }

                out.println("DynamicArray," + n + "," + (totalArr / REPEATS) + "," + compArr);
                out.println("LinkedList," + n + "," + (totalList / REPEATS) + "," + compList);
            }
        }
    }

    private static void runInsertRemoveWorkload() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter("results/tables/workload3_insert_remove.csv"))) {
            out.println("structure,n,operation,position,avgTimeNanos,movements");

            for (int n : SIZES) {
                benchInsert(out, "DynamicArray", n, 0, true);
                benchInsert(out, "DynamicArray", n, n / 2, false);
                benchRemove(out, "DynamicArray", n, 0, true);
                benchRemove(out, "DynamicArray", n, n / 2, false);

                benchInsert(out, "LinkedList", n, 0, true);
                benchInsert(out, "LinkedList", n, n / 2, false);
                benchRemove(out, "LinkedList", n, 0, true);
                benchRemove(out, "LinkedList", n, n / 2, false);
            }
        }
    }

    private static void benchInsert(PrintWriter out, String structureName, int n, int index, boolean beginning) {
        double totalTime = 0;
        long movements = 0;

        for (int rep = 0; rep < REPEATS; rep++) {
            Random random = new Random(SEED);

            if (structureName.equals("DynamicArray")) {
                DynamicArray<Integer> array = new DynamicArray<>();
                for (int i = 0; i < n; i++) array.add(random.nextInt());

                array.resetCounters();
                long start = System.nanoTime();
                for (int i = 0; i < 1_000; i++) array.add(index, random.nextInt());
                totalTime += (System.nanoTime() - start);
                movements = array.getMovementCount();
            } else {
                LinkedList<Integer> list = new LinkedList<>();
                for (int i = 0; i < n; i++) list.add(random.nextInt());

                list.resetCounters();
                long start = System.nanoTime();
                for (int i = 0; i < 1_000; i++) list.add(index, random.nextInt());
                totalTime += (System.nanoTime() - start);
                movements = list.getAccessCount();
            }
        }

        out.println(structureName + "," + n + ",insert," + (beginning ? "start" : "middle")
                + "," + (totalTime / REPEATS) + "," + movements);
    }

    private static void benchRemove(PrintWriter out, String structureName, int n, int index, boolean beginning) {
        double totalTime = 0;
        long movements = 0;

        for (int rep = 0; rep < REPEATS; rep++) {
            Random random = new Random(SEED);

            if (structureName.equals("DynamicArray")) {
                DynamicArray<Integer> array = new DynamicArray<>();
                for (int i = 0; i < n; i++) array.add(random.nextInt());

                array.resetCounters();
                long start = System.nanoTime();
                for (int i = 0; i < 1_000; i++) {
                    int safeIndex = Math.min(index, array.size() - 1);
                    array.remove(safeIndex);
                }
                totalTime += (System.nanoTime() - start);
                movements = array.getMovementCount();
            } else {
                LinkedList<Integer> list = new LinkedList<>();
                for (int i = 0; i < n; i++) list.add(random.nextInt());

                list.resetCounters();
                long start = System.nanoTime();
                for (int i = 0; i < 1_000; i++) {
                    int safeIndex = Math.min(index, list.size() - 1);
                    list.remove(safeIndex);
                }
                totalTime += (System.nanoTime() - start);
                movements = list.getAccessCount();
            }
        }

        out.println(structureName + "," + n + ",remove," + (beginning ? "start" : "middle")
                + "," + (totalTime / REPEATS) + "," + movements);
    }

    private static void runHeapWorkload() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter("results/tables/workload4_heap.csv"))) {
            out.println("n,avgInsertTimeNanos,avgExtractTimeNanos,comparisons,orderCorrect");

            for (int n : SIZES) {
                double totalInsertTime = 0, totalExtractTime = 0;
                long comparisons = 0;
                boolean orderCorrect = true;

                for (int rep = 0; rep < REPEATS; rep++) {
                    Random random = new Random(SEED);
                    MinHeap<Integer> heap = new MinHeap<>();

                    int[] values = new int[n];
                    for (int i = 0; i < n; i++) values[i] = random.nextInt();

                    heap.resetCounters();
                    long startInsert = System.nanoTime();
                    for (int v : values) heap.insert(v);
                    totalInsertTime += (System.nanoTime() - startInsert);

                    long startExtract = System.nanoTime();
                    int previous = Integer.MIN_VALUE;
                    for (int i = 0; i < n; i++) {
                        int extracted = heap.extractMin();
                        if (extracted < previous) orderCorrect = false;
                        previous = extracted;
                    }
                    totalExtractTime += (System.nanoTime() - startExtract);

                    comparisons = heap.getComparisonCount();
                }

                out.println(n + "," + (totalInsertTime / REPEATS) + "," + (totalExtractTime / REPEATS)
                        + "," + comparisons + "," + orderCorrect);
            }
        }
    }
}