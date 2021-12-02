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


## Changes made

Added annotation in AMQConnectionFactoryProducer

https://github.com/de-winter/qiot-manufacturing-edge-machinery/commit/476867bc5b6ebc8c1e4ed383cb12c69002791863#diff-a6e7c07197fd7bc7da4db228c9d02de597f0f60a845b2373fedf752e3d047471
