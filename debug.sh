#!/bin/bash

export LOG_LEVEL=DEBUG
export MACHINERY_SERIAL=machinery01
export MACHINERY_NAME=machinery01
export FACILITY_MANAGER_SERVICE_URL=https://facility-manager-service-factory.apps.hackfest.vop.viada.de
export TS_PATH=classpath:/certs/bootstrap/truststore.p12
export BROKER_SSL=TRUE
export BROKER_HOST=broker-service-edge-0-svc-rte-factory.apps.hackfest.vop.viada.de
export BROKER_PORT=443

mvn quarkus:dev
