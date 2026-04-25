#!/usr/bin/env bash
set -euo pipefail

SERVICE="${1:?usage: ./reload-one.sh <service-name> <path>}"
PATH_TO_SERVICE="${2:?usage: ./reload-one.sh <service-name> <path>}"
NS="microservices"
CLUSTER="microservices"

cd "$PATH_TO_SERVICE"

mvn clean package -DskipTests

JAR_FILE="$(find target -maxdepth 1 -type f -name '*.jar' ! -name 'original-*.jar' | head -n 1)"

if [ -z "$JAR_FILE" ]; then
  echo "Jar file not found in $PATH_TO_SERVICE/target"
  exit 1
fi

echo "Using jar: $JAR_FILE"

docker build \
  -t "${SERVICE}:latest" \
  --build-arg JAR_FILE="$JAR_FILE" \
  .

kind load docker-image "${SERVICE}:latest" --name "$CLUSTER"

kubectl rollout restart deployment/"$SERVICE" -n "$NS"
kubectl rollout status deployment/"$SERVICE" -n "$NS"