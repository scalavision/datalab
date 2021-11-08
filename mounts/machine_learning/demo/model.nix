{ lib, stdenv, fetchurl, python38Packages
# Number of epochs
, epochs ? 10
  # Selecting backend
, cuda ? true }:

with lib;

let

  # Model data!
  data = fetchurl {
    url = "https://s3.amazonaws.com/img-datasets/mnist.npz";
    sha256 = "1lbknqbzqs44qhnczv9a5bfdjl5qqgwgrgwgwk4609vm0b35l73k";
  };

in stdenv.mkDerivation rec {
  name = "model-${version}";
  version = "1";

  src = ./py;

  # Model dependencies, adapted to the backed
  nativeBuildInputs = with python38Packages;
    [ python h5py ]
    ++ optionals (cuda == true) [ python38Packages.tensorflowWithCuda ]
    ++ optionals (cuda == false) [ python38Packages.tensorflow ];

  /* requiredSystemFeatures could be used to make
     the model leverage nix distributed builds

       requiredSystemFeatures = [ "big-parallel" "cuda" ];
  */

  # environment variables used in the train script
  EPOCHS = epochs;
  DATA = data;

  unpackPhase = ":";

  # Training the model
  buildPhase = ''
    python $src/training.py
  '';

  installPhase = ''
    mkdir -p $out
    cp model.h5 $out/
  '';
}
