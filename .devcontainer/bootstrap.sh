#!/bin/bash

set -eu

VERSION=${1:-2.3.11}
BASEURL=https://releases.nixos.org/nix
SYSTEM=x86_64-linux
BASENAME=nix-$VERSION
ARCHNAME=$BASENAME-$SYSTEM
FILENAME=$ARCHNAME.tar.xz

DEBIAN_FRONTEND=noninteractive apt-get -qq -o=Dpkg::Use-Pty=0 update && \
  apt-get -qq -o=Dpkg::Use-Pty=0 install -y --no-install-recommends \
  apt-utils \
  ca-certificates \
  wget \
  xz-utils &> /dev/null

wget -q $BASEURL/$BASENAME/$FILENAME

DEBIAN_FRONTEND=noninteractive tar xf $FILENAME

# Prepare build directory
mkdir -p build/etc/nix build/nix/var/nix/profiles \
         build/tmp build/bin build/usr/bin build/var build/yoda \
         build/opt/conda

cat > build/etc/passwd << EOF
root:*:0:0:::
yoda:*:1000:1000:::
nixbld1:*:30001:30000:::
nixbld2:*:30002:30000:::
nixbld3:*:30003:30000:::
nixbld4:*:30004:30000:::
nixbld5:*:30005:30000:::
nixbld6:*:30006:30000:::
nixbld7:*:30007:30000:::
nixbld8:*:30008:30000:::
nixbld9:*:30009:30000:::
nixbld10:*:30010:30000:::
EOF

cat > build/etc/group<< EOF
nixbld:x:30000:nixbld1,nixbld2,nixbld3,nixbld4,nixbld5,nixbld6,nixbld7,nixbld8,nixbld9,nixbld10
EOF

# Symlink the default profile to /usr/local
ln -s /nix/var/nix/profiles/default build/usr/local
ln -s /nix/var/nix/profiles/default build/yoda/.nix-profile

# Move Nix store into build directory
mv $ARCHNAME/store build/nix
mv $ARCHNAME/.reginfo build/nix/store

# Symlink bash binaries into /bin
BASH=$(find build -name "*-bash-*"|grep -o "/.*")
ln -s "$BASH"/bin/bash build/bin
ln -s "$BASH"/bin/sh build/bin

# Symlink channel to be
mkdir -p build/var/nixpkgs build/yoda/.nix-defexpr

# Configure nixpkgs
mkdir -p build/yoda/.nixpkgs
echo "{ allowBroken = true; }" > build/yoda/.nixpkgs/config.nix

# Fix permissions
find build -type d -print0 |xargs -0 chmod 0555
find build -type f -print0 |xargs -0 chmod ugo-ws
find build/nix/var -type d -print0 |xargs -0 chmod 0755
chmod 0755 build/nix/store
chmod 0777 build/tmp
chmod -R 0700 build/yoda
chown -R 1000:1000 /opt/conda*
chown -R 1000:1000 /build/*

# Create tarball
cd ./build && tar cz .
