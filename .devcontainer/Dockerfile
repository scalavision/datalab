# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.194.3/containers/ubuntu/.devcontainer/base.Dockerfile

# [Choice] Ubuntu version: hirsute, bionic, focal
ARG VARIANT="hirsute"
FROM scratch

# [Optional] Uncomment this section to install additional OS packages.
# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
#     && apt-get -y install --no-install-recommends <your-package-list-here>

ADD nix.tar.gz /

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive
# && apt-get -y install --no-install-recommends

ENV HOME="/dev" TERM="xterm" \
  PATH="/usr/local/bin:/usr/bin:/bin" \
  SSL_CERT_FILE="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  SYSTEM_CERTIFICATE_PATH="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  NIX_PATH="nixpkgs=/dev/.nix-defexpr/channels/nixpkgs"

WORKDIR /opt/extras
ADD install-tools.sh /opt/extras
ADD packages.txt /opt/extras
ADD tools /opt/extras/

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
  ln -sf /usr/local/etc/* /etc && \
  nix-collect-garbage -d
RUN chown 0:0 -R /bin /etc /usr /var /tmp

# RUN chmod +x /opt/extras/install-tools.sh && /opt/extras/install-tools.sh && rm -rf /opt/extras
