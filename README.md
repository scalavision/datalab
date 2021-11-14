# datalab

## CONDA + NIX = Data Science and Engineering Platform

- WIP: Under heavy development and unstable

Datalab wants to create a simple environment for complex data engineering
and datascience tasks.

## What it is

It contains:

- miniconda: get easy access to the scientific universe of anaconda packages.
- nix: reproducible builds and deployments, more reliable and faster than conda and almost
  the same amount of scientific packages.
- vscode decontainer: built on conda devcontainer for conda.
- scripts to install bioinformatic tools not in conda or nix

You install all nix packages inside a docker volume, it means that it contains
all of your heavy data science tools. You can then maintain those independent of your
docker container lifecycle.

## To try it out

1. Create a docker volume for `nix`:

```bash
docker volume create nix
```

2. Open it in vscode

open the folder in `vscode`, then:

```s
CTRL + SHIFT + P
Remote Containers: Rebuild and Reopen in Containers
```

Grab your favorite beverage, it will build for some time.

### Basic shell interaction

When the docker image is finished building, and container has started,
you will get a `/bin/sh` prompt,
alternatively, you can click on the `+` sign at the `terminal`

(CTLR + \`) and select ^ drop down menu, then you will get a bash terminal.

### Open a scientific shell environment

At the root of the terminal path, there is a `default.nix` file, i.e.
`/src/analytics/default.nix`, you can now type in:

```bash
nix-shell
```

It will download all the packages declared in this scientific environment:

```nix
  buildInputs = with pkgs; [
    # basic python dependencies
    python38
    python38Packages.numpy
    python38Packages.scikitlearn
    python38Packages.scipy
    python38Packages.matplotlib
    # a couple of deep learning libraries
    python38Packages.tensorflow
    python38Packages.Keras
    python38Packages.pytorch
    openjdk11
    bloop
    # Lets assume we also want to use R, maybe to compare sklearn and R models
    R
    rPackages.mlr
    rPackages.data_table # "_" replaces "."
    rPackages.ggplot2
  ]
```

Remember, this will only be needed once, the packages will be saved into the `docker volume` .

After the download, open a python shell, and try it out:

```bash
python
```

Then type in:

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

Feel free to search any scientific package:

- [nxpkgs search](https://search.nixos.org/packages)

and add it to the shell environment, buildInputs list declared above.

### Install packages from nix package manager

You can also manually install a package in the docker container from the nix package manager:

```bash
nix-env -iA nixpkgs.vim
```

Search for a package from the command line:

```bash
nix search vim
```

### conda and installation of bioinformatic packages

To add `ensembl-vep` a very much used bioinformatics library, using
conda:

```bash
conda config --add channels bioconda
# an example installing vep
conda install ensembl-vep
```

## Basic ideas

### How it works

There is a `initializeCommand` in `.devcontainer/devcontainer.json`, that creates a
docker container content, archived as `nix.tar.gz` using the `init.sh` script.

This contains the complete `nix` environment for the `yoda` user. This environment is
then extracted into the docker container directly.

If rebuilding the container, this archive will not be rebuilt, unless you delete it.
The same goes for the `docker volume`.

### nixpgks and conda

`nixpkgs` ([NixOs](https://nixos.org/)) has a plethora of scientific libraries
available. It distinguishes itself from `conda` by:

- package installations are reproducible until the end of time (unless things get removed from Internet)
- every package is installed in isolation, thus library conflicts between
  versions of a package or library dependencies are never a problem.
- You can install packages from any channel version of nixpkgs
- most of the times the resolution and installation is much faster than for conda.

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

### resources

#### vscode

- [main docs](https://code.visualstudio.com/docs/remote/containers)
- [devcontainer.json](https://code.visualstudio.com/docs/remote/devcontainerjson-reference#_variables-in-devcontainerjson)
- [non root user](https://code.visualstudio.com/remote/advancedcontainers/add-nonroot-user)

### troubleshooting

#### /bin/sh or /bin/bash not working

If you have upgraded nix outside the running container, your `docker volume nix`
will have a different version of `nix` than what is provided by the operating system
running in the container.

You can try mount the volume running another container `with the same nix version` as
inside the docker volume, then upgrade `nix` as follows:

```bash
nix nix-upgrade
```

In theory when you start devcontainer again, and the `nix` version align with the version
in the volume, this should work. This has not been tested.

### TODO

- add all `nix-channels` available to support older versions of
  packages.

## credits

`bootstrap.sh` is heavily borrowed from `datakurre`'s `gist`, and is
adapted to change from `/bin/sh` to `/bin/bash` when creating the
docker environment for standalone / single-user `nix`:

- [nix in docker, best of both worlds](https://datakurre.pandala.org/2015/11/nix-in-docker-best-of-both-worlds.html/)

`nix-shell` dev environment is heavily borrowed from:

- [Building a reprodusible data science environment with Nix](https://josephsdavid.github.io/nix.html)
