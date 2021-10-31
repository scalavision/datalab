let pkgs = import <nixpkgs> { };
in pkgs.mkShell {
  name = "simpleEnv";
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
  ];
  doCheck = false;
  #export JAVA_HOME=${openjdk11}/lib/openjdk
  shellHook = ''
    export COURSIER_CACHE=/yoda/.cache
  '';
}
