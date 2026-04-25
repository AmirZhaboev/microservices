#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEFAULT_TAG="latest"


if [ "$#" -gt 0 ]; then
  SERVICES=("$@")
else
  mapfile -t SERVICES < <(
    find "$ROOT_DIR" -mindepth 1 -maxdepth 2 -type d | while read -r dir; do
      if [ -f "$dir/pom.xml" ] && [ -f "$dir/Dockerfile" ]; then
        basename "$dir"
      fi
    done | sort
  )
fi

if [ "${#SERVICES[@]}" -eq 0 ]; then
  echo "Не найдено ни одного сервиса с pom.xml и Dockerfile"
  exit 1
fi

echo "Будут собраны сервисы:"
printf ' - %s\n' "${SERVICES[@]}"
echo

build_service() {
  local service="$1"
  local service_dir="$ROOT_DIR/$service/$service"
  local image_tag="${IMAGE_TAG:-$DEFAULT_TAG}"
  local image_name

  case "$service" in
    config-server) image_name="config-server" ;;
    licenseservice) image_name="license-service" ;;
    organizationservice) image_name="organization-service" ;;
    *)
      image_name="$service"
      ;;
  esac
  echo "=================================================="
  echo "Сервис: $service"
  echo "Папка: $service_dir"
  echo "=================================================="

  if [ ! -d "$service_dir" ]; then
    echo "Ошибка: папка $service_dir не существует"
    exit 1
  fi

  if [ ! -f "$service_dir/pom.xml" ]; then
    echo "Ошибка: в $service_dir нет pom.xml"
    exit 1
  fi

  if [ ! -f "$service_dir/Dockerfile" ]; then
    echo "Ошибка: в $service_dir нет Dockerfile"
    exit 1
  fi

  cd "$service_dir"

  echo "==> Maven package: $service"
  if [ -f "./mvnw" ]; then
    chmod +x ./mvnw
    ./mvnw clean package -DskipTests
  else
    mvn clean package -DskipTests
  fi

  echo "==> Поиск jar"
  local jar_file
  jar_file=$(find target -maxdepth 1 -type f -name "*.jar" \
    ! -name "*sources.jar" \
    ! -name "*javadoc.jar" \
    ! -name "original-*.jar" | head -n 1)

  if [ -z "$jar_file" ]; then
    echo "Ошибка: jar файл не найден в $service_dir/target"
    exit 1
  fi

  echo "==> Найден jar: $jar_file"

  echo "==> Docker build: ${image_name}:${image_tag}"
  docker build \
    --build-arg JAR_FILE="$jar_file" \
    -t "${image_name}:${image_tag}" \
    .

  echo "==> Готово: ${image_name}:${image_tag}"
  echo
}

for service in "${SERVICES[@]}"; do
  build_service "$service"
done

echo "======================================"
echo "Сборка завершена успешно"
echo "======================================"