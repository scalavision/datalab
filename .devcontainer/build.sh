#!/usr/bin/env bash

set -euo pipefail
set -x

VERSION=${1:-2.4.0}
IMAGE_NAME=${2:-nix}
IMAGE_TAG=${3:-$VERSION}

echo "building docker image ${IMAGE_NAME}}:${IMAGE_TAG} with nix version: ${VERSION}"

cat bootstrap.sh "${VERSION}" | docker run --network=host --rm -i alpine sh > nix.tar.gz
docker build --rm --network host -t "${IMAGE_NAME}:${IMAGE_TAG}" .
