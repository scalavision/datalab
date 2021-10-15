{ lib, stdenv, fetchFromGitHub, zlib }:
stdenv.mkDerivation rec {

  pname = "bwa-mem2";
  version = "2.2.1";

  src = fetchFromGitHub {
    owner = pname;
    repo = pname;
    fetchSubmodules = true;
    rev = "v${version}";
    sha256 = "sha256:01ff9v6604g2wlwdz7crs47nmdf14rlyz68gjm0c48ki76sy206v";
  };

  buildInputs = [ zlib ];

  # Avoid hardcoding gcc to allow environments with a different
  # C compiler to build
  preConfigure = ''
    sed -i '/^CC/d' Makefile
  '';

  # it's unclear which headers are intended to be part of the public interface
  # so we may find ourselves having to add more here over time
  installPhase = ''
    install -vD -t $out/bin bwa-mem2
    install -vD -t $out/bin bwa-mem2.avx
    install -vD -t $out/bin bwa-mem2.avx2
    install -vD -t $out/bin bwa-mem2.avx512bw
    install -vD -t $out/bin bwa-mem2.sse41
    install -vD -t $out/bin bwa-mem2.sse42
    install -vD -t $out/lib libbwa.a
    install -vD -t $out/include src/bntseq.h
    install -vD -t $out/include src/bwa.h
    install -vD -t $out/include src/bwamem.h
    install -vD -t $out/include src/bwt.h
  '';

  meta = with lib; {
    description = "Bwa-mem2 is the next version of the bwa-mem algorithm";
    license     = licenses.mit;
    homepage    = "https://github.com/bwa-mem2/bwa-mem2";
    maintainers = with maintainers; [ scalavision ];
    platforms = platforms.x86_64;
  };

}
