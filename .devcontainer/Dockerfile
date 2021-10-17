# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.194.3/containers/ubuntu/.devcontainer/base.Dockerfile

# [Choice] Ubuntu version: hirsute, bionic, focal
# FROM mcr.microsoft.com/vscode/devcontainers/base:0-focal
# FROM mcr.microsoft.com/vscode/devcontainers/base:0-focal
FROM mcr.microsoft.com/vscode/devcontainers/miniconda
# FROM ubuntu:bionic-20200219
# FROM scratch
# [Optional] Uncomment this section to install additional OS packages.
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive
#     && apt-get -y install --no-install-recommends <your-package-list-here>

ADD nix.tar.gz /

ENV HOME="/yoda" TERM="xterm" \
  PATH="/usr/local/bin:/usr/bin:/bin" \
  SSL_CERT_FILE="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  SYSTEM_CERTIFICATE_PATH="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  NIX_PATH="nixpkgs=/yoda/.nix-defexpr/channels/nixpkgs" \
  LANGUAGE=C.UTF-8 \
  LANG=C.UTF-8 \
  LC_ALL=C.UTF-8

RUN /nix/store/*-nix-*/bin/nix-store --init && \
  /nix/store/*-nix-*/bin/nix-store --load-db < /nix/store/.reginfo && \
  /nix/store/*-nix-*/bin/nix-env -i \
  /nix/store/*-nix-* /nix/store/*-coreutils-* \
  /nix/store/*-curl-* /nix/store/*-cacert-* /nix/store/*-gnutar-* \
  /nix/store/*-xz-* /nix/store/*-gzip-* /nix/store/*-bzip2-* && \
  nix-channel --add https://nixos.org/channels/nixos-unstable nixpkgs && \
  nix-channel --update && \
  nix-env -i bash-interactive iana-etc which && \
  ln -sf /usr/local/bin/env /usr/bin && \
  ln -sf /usr/local/bin/bash /bin && \
  ln -sf /usr/local/bin/sh /bin && \
  nix-collect-garbage -d

# RUN chown -R 1000:1000 /opt/conda*

RUN mkdir -p /root /yoda
# RUN nix-env -f "/root/.nix-defexpr/channels/nixpkgs" -iA busybox singularity
RUN cat /etc/passwd

RUN mkdir -p /src /config /data /tools /conda /nixpkgs /bio/ref /bio/genepanels /bio/transcripts /opt/conda/pkgs/
RUN ln -sf /config /src/config && \
  ln -sf /data /src/config && \
  ln -sf /tools /src/tools && \
  ln -sf /bio /src/bio && \
  ln -sf /nixpkgs /src/nixpkgs

RUN sudo chown -R 1000:1000 /yoda
RUN chmod 1777 /nix/var/nix/profiles/per-user
RUN mkdir -p /nix/var/nix/daemon-socket
# RUN chown 1000:1000 /nix/var/nix/daemon-socket
run chgrp 1000 /nix/var/nix/daemon-socket
RUN chmod ug=rwx,o= /nix/var/nix/daemon-socket
RUN ls -halt /

# There is a bug in Docker that makes recursive ownership of folders extremely slow in some cases
# Give this issue a thumbs up
# https://github.com/docker/for-linux/issues/388
# RUN chown 0:0 -R /bin /etc /usr /var /tmp
# RUN addgroup nix-users
# -G nix-users 
# RUN adduser --disabled-password -D -s /bin/bash -h /yoda --gecos '' yoda
# RUN apt-get install -y sudo
# RUN adduser yoda sudo

# RUN echo "yoda ALL=(ALL) SETENV:HOME=/root, NOPASSWD:/usr/local/bin/nix-env" >> /etc/sudoers
# RUN adduser docker sudo
# RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
# RUN nix-daemon &
# RUN mkdir -m 0755 -p /nix/var/nix/{profiles,gcroots}/per-user/yoda
# RUN chmod -R 775 /nix/var/nix/profiles/per-user
USER yoda

# RUN sudo nix-env -iA nixpkgs.vim

