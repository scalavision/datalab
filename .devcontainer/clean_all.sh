#!/usr/bin/env bash

ARG=$1

echo "$ARG"

set -euo pipefail

if [[  $ARG != "-f" ]]; then
  echo "CAVEAT! This is a dangerous script, do not run it unless you know what you are doing"
  echo "This script will remove all containers and empty images, including datalab"
  echo "Add -f parameter, if this is what you want"
  echo "If not, do not run this script!"
  exit 1
fi

rm nix.tar.gz || true

# Remove all non running containers
docker rm $(docker ps -a | grep -v CO | awk '{print $1}') || echo "no docker containers listed"

# Remove empty images
docker rmi $(docker images | grep '<none>' | awk '{print $3}') || echo "no dangling docker containers"

docker volume rm nix
docker volume rm conda

# Remove datalab images
# docker rmi "vsc-datalab-*"

