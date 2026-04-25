# Microservices Project

Учебный проект микросервисной архитектуры на Java с использованием Spring ecosystem и Kubernetes.

## Архитектура

Проект состоит из следующих компонентов:

- **api-gateway** — единая точка входа для клиентских запросов
- **config-server** — централизованное хранение конфигурации
- **license-service** — сервис лицензий
- **organization-service** — сервис организаций
- **PostgreSQL** — база данных
- **Vault** — хранение секретов
- **Kubernetes (kind)** — оркестрация контейнеров

---

## Используемый стек

- Java 17
- Spring Boot
- Spring Cloud Config
- Spring Cloud Gateway
- Spring Data JPA
- PostgreSQL
- HashiCorp Vault
- Docker
- Kubernetes
- kind
- Maven

---

## Структура проекта

```text
microservices/
├── api_gateway/
├── config-server/
├── licenseservice/
├── organizationservice/
├── k8s/
├── scripts/
└── README.md
```

---

## Предварительные требования

Установите:

- :contentReference[oaicite:5]{index=5}
- :contentReference[oaicite:6]{index=6}
- :contentReference[oaicite:7]{index=7}
- Java 17
- Maven

Проверка:

```bash
docker --version
kubectl version --client
kind version
java -version
mvn -version
```

---

# Запуск проекта

## 1. Создание кластера

```bash
kind create cluster --name microservices
```

---

## 2. Сборка Docker image

```bash
chmod +x scripts/*.sh
./scripts/build-images.sh
```

Скрипт собирает образы для:

- config-server
- license-service
- organization-service
- api-gateway

---

## 3. Загрузка образов в kind

```bash
./scripts/bootstrap.sh
```

Скрипт загружает собранные образы в кластер.

---

## 4. Деплой инфраструктуры

```bash
./scripts/up.sh
```

Скрипт применяет Kubernetes manifests:

- namespace
- postgres
- vault
- config-server
- organization-service
- license-service
- api-gateway

---

## Проверка состояния

```bash
kubectl get pods -n microservices
kubectl get svc -n microservices
```

---

## Просмотр логов

### Config Server

```bash
kubectl logs -n microservices deployment/config-server
```

### License Service

```bash
kubectl logs -n microservices deployment/license-service
```

### Organization Service

```bash
kubectl logs -n microservices deployment/organization-service
```

### API Gateway

```bash
kubectl logs -n microservices deployment/api-gateway
```

---

# Доступ к API

Пробрасываем порт gateway:

```bash
kubectl port-forward -n microservices service/api-gateway 8072:8072
```

После этого API доступно по адресу:

```text
http://localhost:8072
```

---

## Примеры запросов

Получить лицензии:

```bash
curl http://localhost:8072/api/licenses
```

Получить организации:

```bash
curl http://localhost:8072/api/organizations
```

---

# Остановка проекта

Удаление ресурсов:

```bash
./scripts/down.sh
```

Удаление кластера:

```bash
kind delete cluster --name microservices
```

---

# Основные реализованные возможности

- централизованная конфигурация через Config Server
- API Gateway routing
- correlation-id filter
- взаимодействие микросервисов
- fault tolerance через Resilience4j
- PostgreSQL persistence
- Vault secrets management
- deployment в Kubernetes

---

# Безопасность

В репозитории отсутствуют:

- реальные Vault tokens
- root tokens
- unseal keys
- `.env` файлы
- реальные пароли

Секреты передаются через Kubernetes Secrets и environment variables.

---

# Автор

Учебный pet-project для изучения микросервисной архитектуры.
