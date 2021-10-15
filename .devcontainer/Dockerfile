# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.194.3/containers/ubuntu/.devcontainer/base.Dockerfile

# [Choice] Ubuntu version: hirsute, bionic, focal
FROM mcr.microsoft.com/vscode/devcontainers/base:0-focal
# FROM ubuntu:bionic-20200219
# FROM scratch
# [Optional] Uncomment this section to install additional OS packages.
# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
#     && apt-get -y install --no-install-recommends <your-package-list-here>

ADD nix.tar.gz /

# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive && \
#  apt-get -y install --no-install-recommends vim

# RUN cat /etc/passwd
# RUN cat /etc/group

# RUN adduser --force-badname --system --gid 1000 --no-create-home _apt
# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive
# && apt-get -y install --no-install-recommends

ENV HOME="/yoda" TERM="xterm" \
  PATH="/usr/local/bin:/usr/bin:/bin" \
  SSL_CERT_FILE="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  SYSTEM_CERTIFICATE_PATH="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  NIX_PATH="nixpkgs=/yoda/.nix-defexpr/channels/nixpkgs" \
  LANGUAGE=C.UTF-8 \
  LANG=C.UTF-8 \
  LC_ALL=C.UTF-8

RUN chown 0:0 -R /bin /etc /usr /var /tmp

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

RUN nix-env -f "/yoda/.nix-defexpr/channels/nixpkgs" -iA busybox

RUN mkdir -p /yoda
RUN adduser yoda -D -h /yoda
USER yoda
CMD ["/bin/bash"]
