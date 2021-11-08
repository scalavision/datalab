with import <nixpkgs> { };

rec {
  # Model using default values (cuda true, 10 epochs)
  model = pkgs.callPackage ./model.nix { };

  #/* Model using tensorflow backend with 1 epoch
  #*/
  #simple-model = pkgs.callPackage ./model.nix {
  #  epochs = 1;
  #};

  #/* Model using no CUDA backend with 3 epochs
  #*/
  slow-model = pkgs.callPackage ./model.nix {
    cuda = false;
    epochs = 20;
  };
}

/* If we wanted fully reproducible pipeline, we
    could add this:

   with import (
   builtins.fetchGit {
       name = "nixos-tensorflow-2";
       url = https://github.com/nixos/nixpkgs;
       ref = "d59b4d07045418bae85a9bdbfdb86d60bc1640bc";}
   ) {};

   instead of:
   import <nixpkgs> { };
*/
