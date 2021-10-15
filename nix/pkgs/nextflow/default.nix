{ stdenv, fetchurl, makeWrapper, bash, jre8 }:
stdenv.mkDerivation rec {

  version = "21.04.3";
  pname = "nextflow";

  src = fetchurl {
    url =
      "https://github.com/nextflow-io/nextflow/releases/download/v21.04.3/nextflow-21.04.3-all";
    sha256 = "sha256-bu0jaZBVr2CoXdAXsDUbcVGYLGRAsexcJzwdTQdWdXI=";
  };

  buildInputs = [ jre8 makeWrapper ];
  phases = [ "installPhase" ];

  installPhase = ''
    BINARY=${pname}-${version}-all
    mkdir -p $out/bin
    cp $src $out/bin/$BINARY
    chmod +x $out/bin/$BINARY
    wrapProgram \
      $out/bin/$BINARY \
      --set JAVA_HOME ${jre8}
  '';
}
