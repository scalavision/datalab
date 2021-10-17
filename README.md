# datalab

* WIP: Ideas on how to build a data engineering platform based upon nixpkgs

Emphasis on data science, machine learning and bioinformatics.

## Basic ideas

To try it out:

```s
CTRL + SHIFT + P
Remote Containers: Rebuild and Reopen in Containers
```

You will get a `/bin/sh` prompt, you can test out a simple
`nix-shell` that will include a lot of data science tools:

```bash
nix-shell
python
```

Type in:

```python
>>> import numpy as np
>>> a = np.arange(15).reshape(3,5)
>>> a
array([[ 0,  1,  2,  3,  4],
       [ 5,  6,  7,  8,  9],
       [10, 11, 12, 13, 14]])
>>>
```

There is a `wip` in progress `nix-shell` environment for more optimized
`tensorflow` library.

To add `ensembl-vep` a very much used bioinformatics library, using
conda:

```bash
conda config --add channels bioconda
```

### nixpgks and conda

`nixpkgs` ([NixOs](https://nixos.org/)) has a plethora of scientific libraries
available. It distinguishes itself from `conda` by:

* package installations are reprodusible
* every package is installed in isolation, thus library conflicts between
  versions of a package or library dependencies are never a problem.
* You can install packages from any channel versio of nixpkgs
* most of the times the resolution and installation is much faster.

`conda` however still have more packages available, so therefor this `devcontainer`
is based upon `vscode`'s `miniconda` container.

### /nix/store contained in a docker volume [TODO]

Whenever you rebuild your container, it should be superfast. You can utilize
docker caching / registry etc. for that, but in the end the cache will be invalidated
when you do changes to your Dockerfile.

Since every package in /nix/store is a standalone package
under the path of `/nix/store` (there are massive symlinks under the hood,
so we don't duplicate anything), we can keep our installed packages
isolated from our Docker container.

Therefor you can install new packages, delete old packages, update
packages without invalidating anything with respect to the running
docker container.

You could even maintain your docker container's packages from a secondary
container.

This feature has not yet been implemented, but has been tested out and
works.

### TODO



## credits

`bootstrap.sh` is heavily borrowed from `datakurre`'s `gist`, and is
adapted to change from `/bin/sh` to `/bin/bash` when creating the
docker environment for standalone / single-user `nix`:

* [nix in docker, best of both worlds](https://datakurre.pandala.org/2015/11/nix-in-docker-best-of-both-worlds.html/)

`nix-shell` dev environment is heavily borrowed from:

* [Building a reprodusible data science environment with Nix](https://josephsdavid.github.io/nix.html)
