#!/bin/sh

./mvnw clean package -U -Pnative -Dquarkus.native.container-build=true
docker rmi quay.io/qiotmanufacturing/edge-machinery:1.0.0-alpha7 --force
docker build -f src/main/docker/Dockerfile.native -t quay.io/qiotmanufacturing/edge-machinery:1.0.0-alpha7 .
docker push quay.io/qiotmanufacturing/edge-machinery:1.0.0-alpha7