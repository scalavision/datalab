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

RUN mkdir -p /src /config /data /tools /nixpkgs /bio/ref /bio/genepanels /bio/transcripts /opt/conda/pkgs/
RUN touch /opt/conda/pkgs/urls.txt
RUN chown yoda:100 /opt/conda/pkgs/urls.txt
# You should be able to run singularity with sudo
# also the container must be running in privileged mode for this to work
RUN echo "yoda ALL=(ALL) NOPASSWD:/usr/local/bin/singularity" >> /etc/sudoers

RUN mkdir /.cache
RUN chown -R yoda:100 /.cache
RUN chown -R yoda:100 /src
RUN mkdir /.sbt
RUN chown -R yoda:100 /.sbt

RUN mkdir /.ivy2/
RUN chown -R yoda:100 /.ivy2

USER yoda

RUN /nix/store/*-nix-*/bin/nix-store --init && \
  /nix/store/*-nix-*/bin/nix-store --load-db < /nix/store/.reginfo && \
  /nix/store/*-nix-*/bin/nix-env -i \
  /nix/store/*-nix-* /nix/store/*-coreutils-* \
  /nix/store/*-curl-* /nix/store/*-cacert-* /nix/store/*-gnutar-* \
  /nix/store/*-xz-* /nix/store/*-gzip-* /nix/store/*-bzip2-* && \
  nix-channel --add https://nixos.org/channels/nixos-unstable nixpkgs && \
  nix-channel --update && \
  nix-collect-garbage -d


# RUN nix-env -iA nixpkgs.busybox nixpkgs.singularity

# There is a bug in Docker that makes recursive ownership of folders extremely slow in some cases
# Give this issue a thumbs up
# https://github.com/docker/for-linux/issues/388
# RUN chown 0:0 -R /bin /etc /usr /var /tmp


