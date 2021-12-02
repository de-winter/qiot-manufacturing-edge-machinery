# qiot-manufacturing-edge-machinery

## Prerequisites

Container Image Repository

Running Factory in OpenShift https://github.com/qiot-project/qiot-manufacturing-factory-installer

OC CLI

Podman

## Setup

Run gen-facility-manager-secret.sh with OpenShift credentials

Place generated truststore on Edge Device

Run build.sh with Image Repository credentials

run_podman.sh on Edge Device with own truststore folder structure, OpenShift URLs, Container Image
