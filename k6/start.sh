#!/usr/bin/env bash
set -euo pipefail

CPUS=(0.5 1.0 1.5 2.0)
PROFILES=(5 50 95)
VUS=40
DURATION=1m

# Путь к compose-файлу (локальный, используется только для local-режима)
COMPOSE_FILE="${COMPOSE_FILE:-../docker-compose.yml}"
COMPOSE_DIR="$(cd "$(dirname "$COMPOSE_FILE")" && pwd)"
ENV_FILE="${COMPOSE_DIR}/.env"

TARGET="${1:-local}"

case "$TARGET" in
  local)
    BASE_URL="http://localhost:8080"
    REMOTE=""
    ;;
  backend)
    # IP или hostname удалённого сервера
    REMOTE_HOST="backend"
    # Путь к docker-compose.yml на удалённом сервере
    REMOTE_COMPOSE_DIR="~/hl-labs"   # ← замени на реальный путь
    BASE_URL="http://localhost:8080"
    REMOTE="ssh ${REMOTE_HOST}"
    ;;
  *)
    echo "Неизвестный таргет: $TARGET (доступно: local, backend)" >&2
    exit 1
    ;;
esac

mkdir -p results

# Функция: обновить LIMITS_CPU и перезапустить контейнер
restart_app() {
  local cpu="$1"

  if [ -z "$REMOTE" ]; then
    # --- локальный режим ---
    sed -i "s/^LIMITS_CPU=.*/LIMITS_CPU=${cpu}/" "$ENV_FILE"
    (cd "$COMPOSE_DIR" && docker compose up -d --force-recreate)
  else
    # --- удалённый режим: меняем .env и перезапускаем на сервере по SSH ---
    $REMOTE "sed -i 's/^LIMITS_CPU=.*/LIMITS_CPU=${cpu}/' ${REMOTE_COMPOSE_DIR}/.env"
    $REMOTE "cd ${REMOTE_COMPOSE_DIR} && docker compose up -d --force-recreate"
  fi
}

for cpu in "${CPUS[@]}"; do
  echo ""
  echo "============================================="
  echo " CPU = ${cpu} | TARGET = ${TARGET}"
  echo "============================================="

  restart_app "$cpu"

  echo "Ждём 40с пока приложение поднимется..."
  sleep 40

  for write in "${PROFILES[@]}"; do
    read=$((100 - write))
    out="results/${TARGET}_cpu${cpu}_w${write}_r${read}.json"

    echo ""
    echo "--- CPU=${cpu} | ${write}/${read} | VUS=${VUS} | DURATION=${DURATION} ---"

    k6 run \
      -e BASE_URL="$BASE_URL" \
      -e VUS="$VUS" \
      -e DURATION="$DURATION" \
      -e WRITE_SHARE="$write" \
      --out json="$out" \
      test.js

    echo "Сохранено: $out"
  done
done

echo ""
echo "Все тесты завершены. Результаты в: results/"
echo "Запусти: python3 result.py"