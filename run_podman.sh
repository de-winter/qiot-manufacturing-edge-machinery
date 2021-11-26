podman run -it --rm \
-v machinery-service-volume:/var/data/qiot/machinery:Z \
-v /home/edge:/bootstrap:Z \
-p 8080:8080/tcp \
-e  LOG_LEVEL=DEBUG \
-e  MACHINERY_SERIAL=machinery01 \
-e  MACHINERY_NAME=machinery01 \
-e  FACILITY_MANAGER_SERVICE_URL=https://facility-manager-service-factory.apps.hackfest.vop.viada.de \
-e  TS_PATH=/bootstrap/truststore.p12 \
-e  BROKER_SSL=TRUE \
-e  BROKER_HOST=broker-service-edge-0-svc-rte-factory.apps.hackfest.vop.viada.de \
-e  BROKER_PORT=443 \
 quay.io/dewinter/edge-machinery:1.0.0-SNAPSHOT
