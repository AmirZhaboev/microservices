#!/usr/bin/env bash
set -euo pipefail

NS="microservices"

kubectl scale deployment api-gateway --replicas=0 -n "$NS" || true
kubectl scale deployment license-service --replicas=0 -n "$NS" || true
kubectl scale deployment organization-service --replicas=0 -n "$NS" || true
kubectl scale deployment config-server --replicas=0 -n "$NS" || true
kubectl scale deployment postgres --replicas=0 -n "$NS" || true

echo "Все основные сервисы остановлены. Кластер и PVC сохранены."