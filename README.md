# Microservices Project

Учебный проект микросервисной архитектуры на Java с использованием Spring ecosystem, Kubernetes, Config Server и Vault.

---

# Архитектура

Проект состоит из следующих компонентов:

- **api-gateway** — единая точка входа
- **config-server** — централизованная конфигурация
- **license-service** — сервис лицензий
- **organization-service** — сервис организаций
- **PostgreSQL** — база данных
- **Vault** — хранение секретов
- **Kubernetes (kind)** — оркестрация контейнеров

---

# Стек

- Java 17
- Spring Boot
- Spring Cloud Config
- Spring Cloud Gateway
- Spring Data JPA
- Resilience4j
- PostgreSQL
- HashiCorp Vault
- Docker
- Kubernetes
- kind
- Maven

---

# Как работает конфигурация

В проекте используется Spring Cloud Config.

## Config Server

Config Server запускается на:

```text
http://config-server:8071
```

Он получает конфигурации из отдельного Git-репозитория:

```text
https://github.com/AmirZhaboev/microservices-config-repo.git
```

Настройка находится в:

```text
config-server/config-server/src/main/resources/application.yml
```

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/AmirZhaboev/microservices-config-repo.git
```

---

## Config repository

Config repository содержит конфиги микросервисов:

```text
microservices-config-repo/
├── license-service.yml
├── organization-service.yml
├── api-gateway.yml
└── application.yml
```

Имя файла должно совпадать с:

```yaml
spring.application.name
```

Например:

```yaml
spring:
  application:
    name: license-service
```

Config Server будет искать:

```text
license-service.yml
```

---

## Vault

Vault используется для хранения чувствительных данных:

- пароли БД
- токены
- секретные параметры

Config Server подключается к Vault:

```yaml
spring:
  config:
    import: vault://
```

```yaml
spring:
  cloud:
    vault:
      authentication: TOKEN
      token: ${VAULT_CONFIG_SERVER_TOKEN}
```

---

## Общий поток конфигурации

```text
Git config repo + Vault
        ↓
 Config Server
        ↓
Microservices
```

---

# Структура проекта

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

# Требования

Перед запуском установить:

- docker
- kubectl
- kind
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

## 2. Сборка Docker images

```bash
chmod +x scripts/*.sh
./scripts/build-images.sh
```

Будут собраны:

- config-server
- license-service
- organization-service
- api-gateway

---

## 3. Загрузка image в kind

```bash
kind load docker-image config-server:latest --name microservices
kind load docker-image license-service:latest --name microservices
kind load docker-image organization-service:latest --name microservices
kind load docker-image api-gateway:latest --name microservices
```

---

## 4. Деплой PostgreSQL + Vault

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/postgres.yaml
kubectl apply -f k8s/vault-values.yaml
```

Проверка:

```bash
kubectl get pods -n microservices
```

---

# Настройка Vault

## Зайти в pod

```bash
kubectl exec -it -n microservices vault-0 -- sh
```

---

## Инициализация

```bash
vault operator init
```

Сохраните:

- Unseal keys
- Root token

---

## Unseal Vault

```bash
vault operator unseal
```

Повторить несколько раз разными ключами.

Проверка:

```bash
vault status
```

Должно быть:

```text
Sealed: false
```

---

## Login

```bash
vault login
```

Вставить root token.

---

## Включить KV engine

```bash
vault secrets enable -path=secret kv-v2
```

---

## Добавить секреты

### License Service

```bash
vault kv put secret/license-service \
spring.datasource.username="postgres" \
spring.datasource.password="postgres"
```

### Organization Service

```bash
vault kv put secret/organization-service \
spring.datasource.username="postgres" \
spring.datasource.password="postgres"
```

---

# Kubernetes Secret для Config Server

Config Server получает Vault token через Kubernetes Secret:

```bash
kubectl create secret generic vault-config-server-token \
--from-literal=token=YOUR_VAULT_TOKEN \
-n microservices
```

---

# Запуск микросервисов

```bash
kubectl apply -f k8s/config-server.yaml
kubectl apply -f k8s/organization-service.yaml
kubectl apply -f k8s/license-service.yaml
kubectl apply -f k8s/api-gateway.yaml
```

---

# Проверка Config Server

```bash
kubectl port-forward -n microservices service/config-server 8071:8071
```

```bash
curl http://localhost:8071/license-service/default
```

```bash
curl http://localhost:8071/organization-service/default
```

---

# Проверка состояния

```bash
kubectl get pods -n microservices
kubectl get svc -n microservices
```

---

# Логи

Config Server:

```bash
kubectl logs -n microservices deployment/config-server
```

License Service:

```bash
kubectl logs -n microservices deployment/license-service
```

Organization Service:

```bash
kubectl logs -n microservices deployment/organization-service
```

API Gateway:

```bash
kubectl logs -n microservices deployment/api-gateway
```

---

# Доступ к API

```bash
kubectl port-forward -n microservices service/api-gateway 8072:8072
```

После этого:

```text
http://localhost:8072
```

Примеры:

```bash
curl http://localhost:8072/api/licenses
curl http://localhost:8072/api/organizations
```

---

# Автоматизация через скрипты

Полный запуск:

```bash
./scripts/build-images.sh
./scripts/bootstrap.sh
```

Повторный запуск:

```bash
./scripts/up.sh
```

Остановка:

```bash
./scripts/down.sh
```

---

# Удаление

Удалить namespace:

```bash
kubectl delete namespace microservices
```

Удалить cluster:

```bash
kind delete cluster --name microservices
```

---

# Основные возможности

- centralized config
- Vault secrets management
- API Gateway routing
- correlation id filter
- interservice communication
- Resilience4j fault tolerance
- PostgreSQL persistence
- Kubernetes deployment
- bash deployment automation

---

# Безопасность

В репозитории отсутствуют:

- root tokens
- unseal keys
- реальные пароли
- `.env`
- GitHub tokens

---

# Автор

Учебный pet-project для изучения микросервисной архитектуры.
