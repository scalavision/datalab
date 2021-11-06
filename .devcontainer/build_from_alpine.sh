#!/usr/bin/env bash

# Building a nix container based upon alpine

set -euo pipefail
set -x

# setting username, groupname, uid, gid
. "$DIR"/build_env.sh

VERSION=${1:-2.4}
IMAGE_NAME=${2:-nix}
IMAGE_TAG=${3:-$VERSION}

echo "building docker image ${IMAGE_NAME}}:${IMAGE_TAG} with nix version: ${VERSION}"

# setting username, groupname, uid, gid
. "$DIR"/build_env.sh

cat bootstrap.sh "${VERSION}" | docker run --network=host --rm -i alpine sh > nix.tar.gz
docker build --rm --network host -t "${IMAGE_NAME}:${IMAGE_TAG}" .
