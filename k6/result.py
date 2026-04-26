import json
import os
import re
import glob
import pandas as pd
import matplotlib.pyplot as plt

RESULTS_DIR = "results"
OUT_DIR = os.path.join(RESULTS_DIR, "plots")
os.makedirs(OUT_DIR, exist_ok=True)

FILENAME_RE = re.compile(
    r"(?P<origin>local|server|backend)_cpu(?P<cpu>\d+(?:\.\d+)?)_w(?P<write>\d+)_r(?P<read>\d+)\.json"
)


def parse_file_meta(path):
    m = FILENAME_RE.match(os.path.basename(path))
    if not m:
        return None

    d = m.groupdict()
    d["cpu"] = float(d["cpu"])
    d["write"] = int(d["write"])
    d["read"] = int(d["read"])
    d["profile"] = f"{d['write']}/{d['read']}"

    if d["origin"] == "local":
        d["origin"] = "local->server"
    else:
        d["origin"] = "server->server"

    return d


def load_k6_json(path, meta):
    rows = []

    with open(path, encoding="utf-8") as f:
        for line in f:
            try:
                d = json.loads(line)
            except json.JSONDecodeError:
                continue

            if d.get("type") != "Point":
                continue

            metric = d.get("metric")
            data = d.get("data", {})
            tags = data.get("tags", {})
            value = data.get("value")

            if metric != "http_req_duration" or value is None:
                continue

            rows.append(
                {
                    "time": data.get("time"),
                    "value": value,
                    "scenario": tags.get("scenario"),
                    "operation": tags.get("operation"),
                    "expected_response": tags.get("expected_response"),
                    **meta,
                }
            )

    return pd.DataFrame(rows)


def load_all():
    frames = []

    for path in glob.glob(os.path.join(RESULTS_DIR, "*.json")):
        meta = parse_file_meta(path)
        if not meta:
            continue

        df = load_k6_json(path, meta)
        if not df.empty:
            frames.append(df)

    if not frames:
        return pd.DataFrame()

    return pd.concat(frames, ignore_index=True)


def aggregate(df):
    df = df[df["expected_response"] == "true"].copy()

    agg = (
        df.groupby(["origin", "profile", "cpu", "operation"])["value"]
        .agg(avg="mean", p95=lambda s: s.quantile(0.95), count="count")
        .reset_index()
        .sort_values(["origin", "profile", "operation", "cpu"])
    )

    return agg


def plot_metric(agg, metric="avg"):
    profiles = ["5/95", "50/50", "95/5"]
    origins = ["local->server", "server->server"]
    ops = [("create", "POST /visitors/"), ("read", "GET /exhibits/rating")]

    for origin in origins:
        fig, axes = plt.subplots(1, 3, figsize=(18, 5), sharey=True)
        fig.suptitle(f"{metric.upper()} response time vs CPU cores ({origin})", fontsize=14)

        for ax, profile in zip(axes, profiles):
            sub = agg[(agg["origin"] == origin) & (agg["profile"] == profile)]

            for op, label in ops:
                op_df = sub[sub["operation"] == op]
                if op_df.empty:
                    continue

                ax.plot(op_df["cpu"], op_df[metric], marker="o", linewidth=2, label=label)

                for _, r in op_df.iterrows():
                    ax.annotate(
                        f"{r[metric]:.1f}",
                        (r["cpu"], r[metric]),
                        textcoords="offset points",
                        xytext=(0, 8),
                        ha="center",
                        fontsize=8,
                    )

            ax.set_title(f"write/read = {profile}")
            ax.set_xlabel("CPU cores")
            ax.grid(True, linestyle="--", alpha=0.4)

            unique_cpu = sorted(sub["cpu"].unique())
            if len(unique_cpu) > 0:
                ax.set_xticks(unique_cpu)

        axes[0].set_ylabel(f"{metric.upper()} response time (ms)")
        handles, labels = axes[0].get_legend_handles_labels()
        if handles:
            fig.legend(handles, labels, loc="upper center", ncol=2)

        fig.tight_layout(rect=[0, 0, 1, 0.92])
        fig.savefig(os.path.join(OUT_DIR, f"{origin.replace('->', '_')}_{metric}.png"), dpi=150)
        plt.close(fig)


def main():
    df = load_all()
    if df.empty:
        print("Нет данных")
        return

    agg = aggregate(df)
    agg.to_csv(os.path.join(OUT_DIR, "cpu_scaling_summary.csv"), index=False)

    plot_metric(agg, "avg")
    plot_metric(agg, "p95")

    print("Готово")


if __name__ == "__main__":
    main()