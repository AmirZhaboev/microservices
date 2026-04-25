#!/usr/bin/env bash
set -euo pipefail

CLUSTER_NAME="microservices"
NS="microservices"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

IMAGES=(
  "config-server:latest"
  "license-service:latest"
  "organization-service:latest"
  "api-gateway:latest"
)

if kind get clusters | grep -qx "$CLUSTER_NAME"; then
  echo "Kind cluster '$CLUSTER_NAME' уже существует"
else
  echo "Создание kind cluster: $CLUSTER_NAME"
  kind create cluster --name "$CLUSTER_NAME"
fi

echo "Загрузка Docker images в kind"
for image in "${IMAGES[@]}"; do
  echo "Loading $image"
  kind load docker-image "$image" --name "$CLUSTER_NAME"
done

echo "Применение Kubernetes manifests"

kubectl apply -f "$ROOT_DIR/k8s/namespace.yaml"

kubectl apply -f "$ROOT_DIR/k8s/postgres.yaml"
kubectl apply -f "$ROOT_DIR/k8s/config-server.yaml"
kubectl apply -f "$ROOT_DIR/k8s/organization-service.yaml"
kubectl apply -f "$ROOT_DIR/k8s/license-service.yaml"
kubectl apply -f "$ROOT_DIR/k8s/api-gateway.yaml"

echo
echo "Ожидание запуска pod'ов"
kubectl get pods -n "$NS"

echo
echo "Bootstrap завершён"