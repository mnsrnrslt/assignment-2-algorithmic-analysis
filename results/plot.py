import csv
import os
import matplotlib.pyplot as plt

TABLES_DIR = os.path.join(os.path.dirname(__file__), "tables")
PLOTS_DIR = os.path.join(os.path.dirname(__file__), "plots")
os.makedirs(PLOTS_DIR, exist_ok=True)


def read_csv(name):
    path = os.path.join(TABLES_DIR, name)
    with open(path, newline="") as f:
        return list(csv.DictReader(f))


def series_by_structure(rows, x_key, y_key):
    result = {}
    for row in rows:
        structure = row["structure"]
        result.setdefault(structure, {"x": [], "y": []})
        result[structure]["x"].append(int(row[x_key]))
        result[structure]["y"].append(float(row[y_key]))
    return result


# ---------------- Plot 1: Execution Time vs n ----------------
w1 = read_csv("workload1_random_access.csv")
w2 = read_csv("workload2_search.csv")
w4 = read_csv("workload4_heap.csv")

fig, axes = plt.subplots(1, 3, figsize=(16, 5))

for structure, data in series_by_structure(w1, "n", "avgTimeNanos").items():
    axes[0].plot(data["x"], data["y"], marker="o", label=structure)
axes[0].set_title("Workload 1: Random Access")
axes[0].set_xlabel("n")
axes[0].set_ylabel("Avg time (ns)")
axes[0].set_xscale("log")
axes[0].set_yscale("log")
axes[0].legend()
axes[0].grid(True, which="both", alpha=0.3)

for structure, data in series_by_structure(w2, "n", "avgTimeNanos").items():
    axes[1].plot(data["x"], data["y"], marker="o", label=structure)
axes[1].set_title("Workload 2: Search")
axes[1].set_xlabel("n")
axes[1].set_ylabel("Avg time (ns)")
axes[1].set_xscale("log")
axes[1].set_yscale("log")
axes[1].legend()
axes[1].grid(True, which="both", alpha=0.3)

n_vals = [int(r["n"]) for r in w4]
insert_t = [float(r["avgInsertTimeNanos"]) for r in w4]
extract_t = [float(r["avgExtractTimeNanos"]) for r in w4]
axes[2].plot(n_vals, insert_t, marker="o", label="insert (n ops)")
axes[2].plot(n_vals, extract_t, marker="o", label="extractMin (n ops)")
axes[2].set_title("Workload 4: Min-Heap")
axes[2].set_xlabel("n")
axes[2].set_ylabel("Avg total time (ns)")
axes[2].set_xscale("log")
axes[2].set_yscale("log")
axes[2].legend()
axes[2].grid(True, which="both", alpha=0.3)

fig.suptitle("Plot 1 — Execution Time vs. n")
fig.tight_layout()
fig.savefig(os.path.join(PLOTS_DIR, "plot1_execution_time_vs_n.png"), dpi=150)
plt.close(fig)

# ---------------- Plot 2: Operations/Comparisons/Accesses vs n ----------------
fig, axes = plt.subplots(1, 3, figsize=(16, 5))

for structure, data in series_by_structure(w1, "n", "accesses").items():
    axes[0].plot(data["x"], data["y"], marker="o", label=structure)
axes[0].set_title("Workload 1: Element Accesses")
axes[0].set_xlabel("n")
axes[0].set_ylabel("Accesses")
axes[0].set_xscale("log")
axes[0].set_yscale("log")
axes[0].legend()
axes[0].grid(True, which="both", alpha=0.3)

for structure, data in series_by_structure(w2, "n", "comparisons").items():
    axes[1].plot(data["x"], data["y"], marker="o", label=structure)
axes[1].set_title("Workload 2: Comparisons")
axes[1].set_xlabel("n")
axes[1].set_ylabel("Comparisons")
axes[1].set_xscale("log")
axes[1].set_yscale("log")
axes[1].legend()
axes[1].grid(True, which="both", alpha=0.3)

comparisons = [int(r["comparisons"]) for r in w4]
axes[2].plot(n_vals, comparisons, marker="o", color="green", label="MinHeap comparisons")
axes[2].set_title("Workload 4: Heap Comparisons")
axes[2].set_xlabel("n")
axes[2].set_ylabel("Comparisons")
axes[2].set_xscale("log")
axes[2].set_yscale("log")
axes[2].legend()
axes[2].grid(True, which="both", alpha=0.3)

fig.suptitle("Plot 2 — Operations / Comparisons / Accesses vs. n")
fig.tight_layout()
fig.savefig(os.path.join(PLOTS_DIR, "plot2_operations_vs_n.png"), dpi=150)
plt.close(fig)

print("Saved plot1_execution_time_vs_n.png and plot2_operations_vs_n.png to", PLOTS_DIR)