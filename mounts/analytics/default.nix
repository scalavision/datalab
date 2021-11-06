let pkgs = import <nixpkgs> { };
in pkgs.mkShell {
  name = "minimal scala env";
  buildInputs = with pkgs; [
    openjdk17
    sbt
    ammonite
    bash-completion
    # Lets assume we also want to use R, maybe to compare sklearn and R models
  ];
  shellHook = ''
    export JAVA_HOME=${pkgs.openjdk11}/lib/openjdk
    export COURSIER_CACHE=/yoda/.cache
    export COURSIER_JVM_CACHE=/yoda/.cache

    # simple bash enhancement
    PS1='\[\033[01;32m\]\u@\h\[\033[00m\]:\[\033[01;34m\]\w\[\033[00m\]\n\$ '
    eval "$(dircolors)"

    # using the function ls below instead
    alias ls='ls -F -h --color=always -v --author --time-style=long-iso'
    alias ll='ls -l'
    alias l='ls -l -a'
  '';
}
