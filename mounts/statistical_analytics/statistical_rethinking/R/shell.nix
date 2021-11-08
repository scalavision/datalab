{ pkgs ? import <nixpkgs> { } }:
with pkgs;
let
  my-r-pkgs = rWrapper.override {
    packages = with rPackages; [
      ggplot2
      knitr
      rstan
      tidyverse
      V8
      dagitty
      coda
      mvtnorm
      shape
      Rcpp
      tidybayes
      (buildRPackage {
        name = "rethinking";
        src = fetchFromGitHub {
          owner = "rmcelreath";
          repo = "rethinking";
          rev = "d0978c7f8b6329b94efa2014658d750ae12b1fa2";
          sha256 = "1qip6x3f6j9lmcmck6sjrj50a5azqfl6rfhp4fdj7ddabpb8n0z0";
        };
        propagatedBuildInputs = [ coda MASS mvtnorm loo shape rstan dagitty ];
      })
    ];
  };
in mkShell {
  buildInputs = with pkgs; [
    git
    glibcLocales
    openssl
    which
    openssh
    curl
    wget
    my-r-pkgs
  ];
  shellHook = ''
    mkdir -p "$(pwd)/_libs"
    export R_LIBS_USER="$(pwd)/_libs"
    echo ${my-r-pkgs}/bin/R
  '';
  GIT_SSL_CAINFO = "${cacert}/etc/ssl/certs/ca-bundle.crt";
  LOCALE_ARCHIVE = lib.optionalString stdenv.isLinux
    "${glibcLocales}/lib/locale/locale-archive";
}
