#!/usr/bin/env bash
set -euo pipefail

NS="microservices"

kubectl scale deployment postgres --replicas=1 -n "$NS" || true
kubectl scale deployment config-server --replicas=1 -n "$NS" || true
kubectl scale deployment organization-service --replicas=1 -n "$NS" || true
kubectl scale deployment license-service --replicas=1 -n "$NS" || true
kubectl scale deployment api-gateway --replicas=1 -n "$NS" || true

echo "Сервисы запущены"
kubectl get pods -n "$NS"