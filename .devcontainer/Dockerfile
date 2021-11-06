# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.194.3/containers/ubuntu/.devcontainer/base.Dockerfile

# [Choice] Ubuntu version: hirsute, bionic, focal
# FROM mcr.microsoft.com/vscode/devcontainers/base:0-focal
# FROM mcr.microsoft.com/vscode/devcontainers/base:0-focal
FROM mcr.microsoft.com/vscode/devcontainers/miniconda

# Adjust to your preferences (Linux only) so that files mounted into
# the container has the same file / folder permissions as outside
# of the container. You may also need to update these in the `bootstrap.sh`
ARG NIX_USERNAME=yoda
ARG NIX_GROUPNAME=users
ARG NIX_USER_UID=1000
ARG NIX_USER_GID=1000

# [Optional] Uncomment this section to install additional OS packages.
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive
#     && apt-get -y install --no-install-recommends <your-package-list-here>

ADD nix.tar.gz /

ENV HOME="/yoda" TERM="xterm" \
  PATH="/usr/local/bin:/usr/sbin:/usr/bin:/bin" \
  SSL_CERT_FILE="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  SYSTEM_CERTIFICATE_PATH="/usr/local/etc/ssl/certs/ca-bundle.crt" \
  NIX_PATH="nixpkgs=/yoda/.nix-defexpr/channels/nixpkgs" \
  LANGUAGE=C.UTF-8 \
  LANG=C.UTF-8 \
  LC_ALL=C.UTF-8

# RUN groupadd --gid $NIX_USER_GID $NIX_GROUPNAME \
#  && useradd --uid $NIX_USER_UID --gid $NIX_USER_GID -m $NIX_USERNAME
# [Optional] Add sudo support. Omit if you don't need to install software after connecting.

# Uncomment this, if you want sudo
# RUN apt-get install -y sudo \
#  && echo $NIX_USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$NIX_USERNAME \
#  && chmod 0440 /etc/sudoers.d/$NIX_USERNAME

RUN mkdir -p /src /config /data /tools /nixpkgs /bio/ref /bio/genepanels /bio/transcripts /opt/conda/pkgs/
RUN touch /opt/conda/pkgs/urls.txt
RUN ls -halt /etc
RUN cat /etc/passwd
RUN chown $NIX_USERNAME:$NIX_USER_GID /opt/conda/pkgs/urls.txt

# You should be able to run singularity with sudo
# also the container must be running in privileged mode for this to work
RUN echo "$NIX_USERNAME ALL=(ALL) NOPASSWD:/usr/local/bin/singularity" >> /etc/sudoers

# caching folders used by the scala tools and home
RUN mkdir -p /.cache /.sbt /.ivy2 /$NIX_USERNAME
RUN chown -R $NIX_USERNAME:$NIX_USER_GID /.cache /src /.sbt /.ivy2


USER $NIX_USERNAME

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


