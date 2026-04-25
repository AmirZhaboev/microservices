#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
DEFAULT_TAG="latest"
IMAGE_TAG="${IMAGE_TAG:-$DEFAULT_TAG}"

if [ "$#" -gt 0 ]; then
  SERVICES=("$@")
else
  SERVICES=(
    "config-server"
    "licenseservice"
    "organizationservice"
    "api_gateway"
  )
fi

image_name_for_service() {
  case "$1" in
    config-server) echo "config-server" ;;
    licenseservice) echo "license-service" ;;
    organizationservice) echo "organization-service" ;;
    api_gateway) echo "api-gateway" ;;
    *) echo "$1" ;;
  esac
}

echo "Будут собраны сервисы:"
printf ' - %s\n' "${SERVICES[@]}"
echo

for service in "${SERVICES[@]}"; do
  SERVICE_DIR="$ROOT_DIR/$service/$service"
  IMAGE_NAME="$(image_name_for_service "$service")"

  echo "=================================================="
  echo "Сервис: $service"
  echo "Папка: $SERVICE_DIR"
  echo "Docker image: $IMAGE_NAME:$IMAGE_TAG"
  echo "=================================================="

  if [ ! -f "$SERVICE_DIR/pom.xml" ]; then
    echo "Ошибка: не найден pom.xml в $SERVICE_DIR"
    exit 1
  fi

  if [ ! -f "$SERVICE_DIR/Dockerfile" ]; then
    echo "Ошибка: не найден Dockerfile в $SERVICE_DIR"
    exit 1
  fi

  cd "$SERVICE_DIR"

  if [ -f "./mvnw" ]; then
    chmod +x ./mvnw
    ./mvnw clean package -DskipTests
  else
    mvn clean package -DskipTests
  fi

  JAR_FILE="$(find target -maxdepth 1 -type f -name "*.jar" \
    ! -name "*sources.jar" \
    ! -name "*javadoc.jar" \
    ! -name "original-*.jar" | head -n 1)"

  if [ -z "$JAR_FILE" ]; then
    echo "Ошибка: jar файл не найден в $SERVICE_DIR/target"
    exit 1
  fi

  docker build \
    --build-arg JAR_FILE="$JAR_FILE" \
    -t "$IMAGE_NAME:$IMAGE_TAG" \
    .

  echo "Готово: $IMAGE_NAME:$IMAGE_TAG"
  echo
done

echo "Сборка завершена успешно"