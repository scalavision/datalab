let
  pkgs = import <nixpkgs> {}; # standard
  # bring in yellowbrick from pypi, building it with a recursive list
    yellowbrick = pkgs.python37.pkgs.buildPythonPackage rec {
      pname = "yellowbrick";
      version = "1.0.1";

      src = pkgs.python37.pkgs.fetchPypi {
        inherit pname version;
        sha256 = "1q659ayr657p786gwrh11lw56jw5bdpnl6hp11qlckvh15haywvk";
      };

# no tests because this is a simple example
      doCheck = false;

# dependencies for yellowbrick
      buildInputs = with pkgs.python37Packages; [
      pytest 
      pytestrunner 
      pytest-flakes  
      numpy 
      matplotlib 
      scipy 
      scikitlearn
    ];
  };
  in with import <nixpkgs> {};
stdenv.mkDerivation rec { # new boilerplate
  name = "simpleEnv";

  # Mandatory boilerplate for buildable env
  # this boilerplate is courtesy of Asko Soukka
  env = buildEnv { name = name; paths = buildInputs; };
  builder = builtins.toFile "builder.sh" ''
    source $stdenv/setup; ln -s $env $out
  '';

  buildInputs = [

      python37
      python37Packages.numpy
      python37Packages.scikitlearn
      python37Packages.scipy
      python37Packages.matplotlib
      yellowbrick

      python37Packages.tensorflowWithCuda
      python37Packages.Keras
      python37Packages.pytorchWithCuda

      R
      rPackages.mlr
      rPackages.data_table # "_" replaces "."
      rPackages.ggplot2
  ];

}
