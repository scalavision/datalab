#!/usr/bin/env bash

set -euo pipefail

export NIX_PATH="nixpkgs=/yoda/.nix-defexpr/channels/nixpkgs"

HOMEGROWN=(bwa-mem2 gatk nextflow)

pushd pkgs
for tool in "${HOMEGROWN}"; do
  nix-env -f "." -iA "$tool"
done
popd

while read pkgs
do
  nix-env -f '<nixpkgs>' -iA "$pkgs"
done < ./tools-collections/bioinf-defaults.txt

conda config --add channels bioconda
conda install vep
