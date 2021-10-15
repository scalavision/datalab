#!/bin/sh

export NIX_PATH="nixpkgs=/dev/.nix-defexpr/channels/nixpkgs"

pushd tools
  nix-env -f "." -iA bwa-mem2
  nix-env -f "." -iA gatk
popd

while read pkgs
do
  nix-env -f '<nixpkgs>' -iA "$pkgs"
done < ./tools-collections/bioinf-defaults.txt
