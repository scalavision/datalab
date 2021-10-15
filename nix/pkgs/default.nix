let p = import <nixpkgs> { };
in rec {
  gatk = p.callPackage ./gatk4 { };
  bwa-mem2 = p.callPackage ./bwa-mem2 { };
  nextflow = p.callPackage ./nextflow { };
}
