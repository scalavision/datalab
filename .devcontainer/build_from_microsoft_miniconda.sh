#!/usr/bin/env bash

set -euo pipefail

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Attempt to support also Apple M1 architecture
export DOCKER_DEFAULT_PLATFORM=linux/amd64

if [ -f "$DIR"/nix.tar.gz ]; then
 echo "nix.tar.gz exists, remove it if you want to build from scratch"
else
  echo "building nix within docker, outputs (nix.tar.gz)"
  echo "may take 20 sek. to approx 5 minutes, depending on network connection"
  echo "cpu speed, and if there exists a docker cache or not"
  cat "$DIR"/bootstrap.sh | \
    docker run -v $(pwd):/out -e TERM=xterm --rm -i mcr.microsoft.com/vscode/devcontainers/miniconda bash > "$DIR"/nix.tar.gz && \
    sleep 1
fi

