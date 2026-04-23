import json
import pandas as pd
import matplotlib.pyplot as plt

# --- Загрузка ---
with open('results/raw.json') as f:
    lines = [json.loads(l) for l in f]

rows = []
for item in lines:
    if item.get('type') == 'Point':
        rows.append({
            'time':     item['data']['time'],
            'metric':   item['metric'],
            'value':    item['data']['value'],
            'endpoint': item['data'].get('tags', {}).get('endpoint', ''),
        })

df = pd.DataFrame(rows)
df['time'] = pd.to_datetime(df['time'])
df['elapsed'] = (df['time'] - df['time'].min()).dt.total_seconds()

# --- Этапы: подбери длительности под свой k6-скрипт ---
stage_windows = {
    10: (20,  80),
    20: (100, 160),
    40: (180, 240),
    80: (260, 320),
}

endpoints = [
    ('student', 'steelblue', 'POST /students/'),
    ('average', 'tomato',    'GET /stats/average'),
]

print("=== Длительность теста ===")
print(f"elapsed max: {df['elapsed'].max():.1f}s")

print("\n=== Данные http_req_duration по временным окнам ===")
stage_windows = {10: (20, 80), 20: (100, 160), 40: (180, 240), 80: (260, 320)}
for vus, (t0, t1) in stage_windows.items():
    mask = (df['elapsed'] >= t0) & (df['elapsed'] < t1) & (df['metric'] == 'http_req_duration')
    print(f"  VUS {vus:2d} ({t0}–{t1}s): {mask.sum()} записей")

print("\n=== Уникальные значения endpoint ===")
print(df[df['metric'] == 'http_req_duration']['endpoint'].value_counts())

print("\n=== Последняя временная метка ===")
print(df['time'].max())

# --- Сбор результатов ---
results = {ep: {'vus': [], 'avg_ms': []} for ep, _, _ in endpoints}

for vus_level, (t_start, t_end) in stage_windows.items():
    time_mask = (df['elapsed'] >= t_start) & (df['elapsed'] < t_end)

    for endpoint, _, _ in endpoints:
        mask = time_mask & (df['metric'] == 'http_req_duration') & (df['endpoint'] == endpoint)
        avg = df[mask]['value'].mean()
        results[endpoint]['vus'].append(vus_level)
        results[endpoint]['avg_ms'].append(round(avg, 2))

# --- Вывод таблицы ---
for endpoint, _, label in endpoints:
    print(f"\n{label}:")
    print(pd.DataFrame(results[endpoint]))

# --- График ---
targets = list(stage_windows.keys())

plt.figure(figsize=(10, 6))

for endpoint, color, label in endpoints:
    vus_vals = results[endpoint]['vus']
    avg_vals = results[endpoint]['avg_ms']

    plt.plot(vus_vals, avg_vals, marker='o', linewidth=2, color=color, label=label)

    for vus, ms in zip(vus_vals, avg_vals):
        plt.annotate(f"{ms}ms", (vus, ms),
                     textcoords="offset points", xytext=(0, 10),
                     ha='center', fontsize=9)

plt.title('Зависимость времени отклика от нагрузки (Тест удвоения)', fontsize=14)
plt.xlabel('Количество VUs', fontsize=12)
plt.ylabel('Среднее время отклика (мс)', fontsize=12)
plt.xticks(targets)
plt.legend()
plt.grid(True, linestyle='--', alpha=0.6)
plt.tight_layout()
plt.savefig('results/scalability_graph.png', dpi=150)
print("\nГрафик сохранён: results/scalability_graph.png")