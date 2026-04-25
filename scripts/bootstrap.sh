#!/usr/bin/env bash
set -euo pipefail

kind create cluster --name microservices

kind load docker-image config-server:latest --name microservices
kind load docker-image license-service:latest --name microservices
kind load docker-image organization-service:latest --name microservices

kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/postgres.yaml
kubectl apply -f k8s/organization-service.yaml
kubectl apply -f k8s/config-server.yaml
kubectl apply -f k8s/license-service.yaml