let pkgs = import <nixpkgs> { };
in pkgs.mkShell {
  name = "simpleEnv";
  buildInputs = with pkgs; [
    # basic python dependencies
    openjdk11
    sbt
    ammonite
    # Lets assume we also want to use R, maybe to compare sklearn and R models
  ];
  doCheck = false;
  shellHook = ''
    export JAVA_HOME=${pkgs.openjdk11}/lib/openjdk
    export COURSIER_CACHE=/yoda/.cache
  '';
}
