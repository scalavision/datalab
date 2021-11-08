# machine learning in a nix environment

This is WIP, taken from this excellent blog:

* [basic introduction to nix](https://josephsdavid.github.io/nix.html)
* [practical tutorial](https://josephsdavid.github.io/nix2.html)

To run it (beware it will download tensorflow with all its dependencies):

```bash
# at /machine_learning/demo, run the following
export NIXPKGS_ALLOW_UNFREE=1
nix-build ./default.nix
```
