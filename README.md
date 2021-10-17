# datalab

* WIP: Ideas on how to build a data engineering platform based upon nixpkgs and conda support

Emphasis on data science, machine learning and bioinformatics.

## Basic ideas

To try it out:

```s
CTRL + SHIFT + P
Remote Containers: Rebuild and Reopen in Containers
```

There is a `initializeCommand` in `.devcontainer/devcontainer.json`, that creates a
docker container content, archived down to `nix.tar.gz`. This contains the
complete `nix` environment for the `yoda` user. This environment is
then extracted into the docker container.

When you have built the container for the first time, you don't have to
create that archive anymore. You can there switch the commented code
here:

```json
// comment out this
"initializeCommand": "rm -rf ${localWorkspaceFolder}/.devcontainer/nix.tar.gz && cat ${localWorkspaceFolder}/.devcontainer/bootstrap.sh | docker run -v $(pwd):/out -e TERM=xterm --rm -i mcr.microsoft.com/vscode/devcontainers/miniconda bash > ${localWorkspaceFolder}/.devcontainer/nix.tar.gz && sleep 1",
// uncomment
// "initializeCommand": "echo starting ..",
```

### Try it out

When the container is finished working, you will get a `/bin/sh` prompt,
alternatively, you can click on the `+` sign at the `terminal`

(CTLR + `\``) and select `^` drop down menu, then bash a terminal.

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

#### conda and install bioinformatic packages

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

`conda` however still has more science packages available, so therefor this `devcontainer`
is based upon `vscode`'s `miniconda` container as well.

`nixpkgs` is one of the biggest linux package repositories out there though (it also
supports OSX / mac)

### /nix/store contained in a docker volume [TODO]

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

This feature has not yet been implemented, but has been tested out and
it works.

### TODO

* add docker volume support for `/nix/store`
* add all `nix-channels` available to support older versions of
  packages.

## credits

`bootstrap.sh` is heavily borrowed from `datakurre`'s `gist`, and is
adapted to change from `/bin/sh` to `/bin/bash` when creating the
docker environment for standalone / single-user `nix`:

* [nix in docker, best of both worlds](https://datakurre.pandala.org/2015/11/nix-in-docker-best-of-both-worlds.html/)

`nix-shell` dev environment is heavily borrowed from:

* [Building a reprodusible data science environment with Nix](https://josephsdavid.github.io/nix.html)

