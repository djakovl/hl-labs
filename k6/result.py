import json
import pandas as pd
import matplotlib.pyplot as plt

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

vus_df = df[df['metric'] == 'vus'][['elapsed', 'value']].copy()
vus_df.columns = ['elapsed', 'vus']

targets = [10, 20, 40, 80]

plt.figure(figsize=(10, 6))

for endpoint, color, label in [
    ('student', 'steelblue', 'POST /students/'),
    ('average',   'tomato',    'GET /stats/average'),
]:
    latency = df[
        (df['metric'] == 'http_req_duration') & (df['endpoint'] == endpoint)
    ][['elapsed', 'value']].copy()

    latency = pd.merge_asof(
        latency.sort_values('elapsed'),
        vus_df.sort_values('elapsed'),
        on='elapsed'
    )

    latency['vus_bucket'] = latency['vus'].apply(
        lambda v: min(targets, key=lambda t: abs(t - v))
    )

    result = latency.groupby('vus_bucket')['value'].mean().reset_index()
    result.columns = ['vus', 'avg_ms']
    result['avg_ms'] = result['avg_ms'].round(2)
    print(f"\n{label}:")
    print(result)

    plt.plot(result['vus'], result['avg_ms'], marker='o', linewidth=2, color=color, label=label)

    for _, row in result.iterrows():
        plt.annotate(f"{row['avg_ms']}ms",
                     (row['vus'], row['avg_ms']),
                     textcoords="offset points", xytext=(0, 10), ha='center', fontsize=9)

plt.title('Зависимость времени отклика от нагрузки (Тест удвоения)', fontsize=14)
plt.xlabel('Количество VUs', fontsize=12)
plt.ylabel('Среднее время отклика (мс)', fontsize=12)
plt.xticks(targets)
plt.legend()
plt.grid(True, linestyle='--', alpha=0.6)
plt.tight_layout()
plt.savefig('results/scalability_graph.png', dpi=150)
print("\nГрафик сохранён: results/scalability_graph.png")