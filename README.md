# datalab

* WIP: Ideas on how to build a data engineering platform using nixpkgs and conda.

Emphasis on data science, machine learning and bioinformatics. This is very rough
around the edges. It should be possible to open the `devcontainer` inside vscode
and it should build. Be aware that there is a lot of improvements before it is
usable.

## Basic ideas

To try it out:

1. create a `nix` docker volume

```bash
docker volume create nix
```

This will contain all the binaries installed using `nixpkgs` in
the contaier. It is persisted, no matter how many times you rebuild
the `devontainer`. This saves significant developer time, and when you
understand more how `nixpkgs` works, you will understand why you
never need to rebuild this.

Then to build the container:

```s
CTRL + SHIFT + P
Remote Containers: Rebuild and Reopen in Containers
```

There is a `initializeCommand` in `.devcontainer/devcontainer.json`, that creates a
docker container content, archived as `nix.tar.gz` using the `init.sh` script.

This contains the complete `nix` environment for the `yoda` user. This environment is
then extracted into the docker container directly.

If rebuilding the container, this archive will not be rebuilt, unless you delete it.
The same goes for the `docker volume`.

### Try it out

When the docker image is finished building, and container has started,
you will get a `/bin/sh` prompt,
alternatively, you can click on the `+` sign at the `terminal`

(CTLR + \`) and select ^ drop down menu, then you will get a bash terminal.

#### basic nix operations

Install a something from the nix package manager:

```bash
nix-env -iA nixpkgs.vim
```

Search for a package:

```bash
nix search vim
```

You can also use the nixos webservice:

* [nixpkgs search](https://search.nixos.org/packages)


#### nix shell for scientific work

you can test out a simple
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

#### conda and installation of bioinformatic packages

To add `ensembl-vep` a very much used bioinformatics library, using
conda:

```bash
conda config --add channels bioconda
```

### nixpgks and conda

`nixpkgs` ([NixOs](https://nixos.org/)) has a plethora of scientific libraries
available. It distinguishes itself from `conda` by:

* package installations are reprodusible until the end of time (unless things get removed from Internet)
* every package is installed in isolation, thus library conflicts between
  versions of a package or library dependencies are never a problem.
* You can install packages from any channel version of nixpkgs
* most of the times the resolution and installation is much faster than for conda.

`conda` however still has more science packages available, so therefor this `devcontainer`
is based upon `vscode`'s `miniconda` container as well.

`nixpkgs` is one of the biggest linux package repositories out there though (it also
supports OSX / mac)

### /nix/store contained in a docker volume

Whenever you rebuild your container, it should be superfast. You can utilize
docker caching / registry etc. for that, but in the end the cache will be invalidated
when you do changes to your Dockerfile.

Since every package in /nix/store is a standalone package
under the path of `/nix/store` (there are massive symlinks under the hood,
so we don't duplicate anything), we can keep our installed packages
isolated.

Therefor you can install new packages, delete old packages, update
packages without invalidating anything with respect to the running
docker container.

You could even maintain your docker container's packages from a secondary
container.

### TODO

* add all `nix-channels` available to support older versions of
  packages.

## credits

`bootstrap.sh` is heavily borrowed from `datakurre`'s `gist`, and is
adapted to change from `/bin/sh` to `/bin/bash` when creating the
docker environment for standalone / single-user `nix`:

* [nix in docker, best of both worlds](https://datakurre.pandala.org/2015/11/nix-in-docker-best-of-both-worlds.html/)

`nix-shell` dev environment is heavily borrowed from:

* [Building a reprodusible data science environment with Nix](https://josephsdavid.github.io/nix.html)

