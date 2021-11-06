#!/usr/bin/env bash

# groupadd --gid $NIX_USER_GID $NIX_GROUPNAME \
#  && useradd --uid $NIX_USER_UID --gid $NIX_USER_GID -m $NIX_USERNAME
# [Optional] Add sudo support. Omit if you don't need to install software after connecting.

# Uncomment this, if you want sudo
# apt-get install -y sudo \
#  && echo $NIX_USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$NIX_USERNAME \
#  && chmod 0440 /etc/sudoers.d/$NIX_USERNAME

. ./build_env.sh

set -euo pipefail

mkdir -p /src /config /data /tools /nixpkgs /bio/ref /bio/genepanels /bio/transcripts /opt/conda/pkgs/
touch /opt/conda/pkgs/urls.txt
chown $NIX_USERNAME:$NIX_USER_GID /opt/conda/pkgs/urls.txt

# You should be able to run singularity with sudo
# also the container must be running in privileged mode for this to work
echo "$NIX_USERNAME ALL=(ALL) NOPASSWD:/usr/local/bin/singularity" >> /etc/sudoers

# caching folders used by the scala tools and home
mkdir -p /.cache /.sbt /.ivy2 /$NIX_USERNAME

# chown -R does not work very well in Docker, this should make it faster,
# but does not seem to work properly
# COPY --chown=$USER_ID:$GROUP_ID /.cache /.cache
chown -R $NIX_USERNAME:$NIX_USER_GID /.cache /src /.sbt /.ivy2
