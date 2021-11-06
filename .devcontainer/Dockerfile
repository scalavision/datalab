# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.194.3/containers/ubuntu/.devcontainer/base.Dockerfile

# [Choice] Ubuntu version: hirsute, bionic, focal
# FROM mcr.microsoft.com/vscode/devcontainers/base:0-focal
# FROM mcr.microsoft.com/vscode/devcontainers/base:0-focal
FROM mcr.microsoft.com/vscode/devcontainers/miniconda

# [Optional] Uncomment this section to install additional OS packages.
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive
#     && apt-get -y install --no-install-recommends <your-package-list-here>

ADD nix.tar.gz /

ENV HOME="/yoda" TERM="xterm" \
  PATH="/usr/local/bin:/usr/sbin:/usr/bin:/bin" \
  SSL_CERT_FILE="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  SYSTEM_CERTIFICATE_PATH="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  NIX_PATH="nixpkgs=/yoda/.nix-defexpr/channels/nixpkgs" \
  XDG_DATA_DIRS="/yoda/.nix-profile/share/bash-completion/completions" \
  LANGUAGE=C.UTF-8 \
  LANG=C.UTF-8 \
  LC_ALL=C.UTF-8

ADD ./build_env.sh /tmp
ADD ./build_container.sh /tmp

WORKDIR /tmp
RUN chmod +x ./build_container.sh && ./build_container.sh

USER yoda

# /nix/store/*-gzip-* 
# /nix/store/*-gnutar-* 

RUN /nix/store/*-nix-*/bin/nix-store --init && \
  /nix/store/*-nix-*/bin/nix-store --load-db < /nix/store/.reginfo && \
  /nix/store/*-nix-*/bin/nix-env -i \
  /nix/store/*-nix-* /nix/store/*-coreutils-* \
  /nix/store/*-curl-* /nix/store/*-cacert-* \
  /nix/store/*-xz-* /nix/store/*-bzip2-* && \
  nix-channel --add https://nixos.org/channels/nixos-unstable nixpkgs && \
  nix-channel --update && \
  nix-collect-garbage -d

RUN nix-env -iA nixpkgs.nixfmt
# RUN nix-env -iA nixpkgs.busybox nixpkgs.singularity

# There is a bug in Docker that makes recursive ownership of folders extremely slow in some cases
# Give this issue a thumbs up
# https://github.com/docker/for-linux/issues/388
# RUN chown 0:0 -R /bin /etc /usr /var /tmp


