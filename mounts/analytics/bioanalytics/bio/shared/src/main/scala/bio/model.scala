package bio

import bio.*
import bio.vcf.DataLine
import bio.bed.Bed

import scala.math.Ordering

/** Experimental modelling based upon intro course to human genetic variation
  *
  * Multiple Variants in the genome may increase the likelihood of certain
  * Phenotypes along with environmental factors.
  */
enum Phenotype:
  case Genotype()
  case Environment()
  case Phenotype(
      variants: List[Variant],
      env: Environment
  )

final case class Variant(
    region: ZBHORegion
)

enum AlleleType:
  case Major
  case Minor

enum Allele:
  case Reference(
      region: ZBHORegion,
      alleleType: AlleleType,
      phenotype: Option[Phenotype]
  )
  case Alternate(
      region: ZBHORegion,
      alleleType: AlleleType,
      phenotype: Option[Phenotype]
  )
  case Alleles(
      variant: Variant,
      reference: Reference,
      alternate: List[Alternate]
  )

case class Haplotype(
    alleles: Vector[Allele],
    minDistance: Int,
    maxDistance: Int
)

def linkageDesiquilibrium(haplotypes: List[Haplotype]): Double = ???

sealed trait TransitionType

enum Purine extends TransitionType:
  case Adenine
  case Guanine

enum Pyrimidine extends TransitionType:
  case Cytosine
  case Thymine

enum SingleBasePairSubstitution:
  case PurineTransition(from: Purine, to: Purine)
  case PyrimidineTransition(from: Pyrimidine, to: Pyrimidine)
  case Transversion(from: TransitionType, to: TransitionType)

enum InsertionOrDeletion():
  case Insertion()
  case Deletion()

enum StructuralVariation():
  case Deletion()
  case Insertion()
  case Inversion()
  case CNV()
  case Duplication()

enum GeneticVariation:
  // single nucleotide polymorphism, can be any nucleic acid
  // substitution
  case SNP(value: SingleBasePairSubstitution)
  case InDel(value: InsertionOrDeletion)
  case SV(value: StructuralVariation)

/** Indels with a length divisible by three (i.e. whole codon indels) in coding
  * regions will cause insertions or deletions of whole amino acids into the
  * protein, and are known as in-frame deletions or insertions.
  *
  * Note that indels divisible by three may also cause a missense or nonsense
  * variant if the the variant falls across two codons. However, if the length
  * is not divisible by three, this will cause a frameshift where all codons
  * downstream of the indel are shifted, often resulting in a malformed protein
  * or nonsense-mediated decay.
  */
enum InFrame:
  case Deletion()
  case Insertion()
  case Framshift()

/** Transcription Factor Binding Motifs
  *
  * Genomic sequences that specifically bind to transcription factors. The
  * consensus sequence of a TFBM is variable, and there are a number of possible
  * bases at certain positions in the motif, whereas other positions have a
  * fixed base.
  *
  * These are usually illustrated in sequence logo diagrams (Figure 7), where
  * the height of the letter represents how frequently that nucleotide is
  * observed in that position.
  *
  * Known TFBMs are curated in the JASPAR database.
  *   - http://jaspar.genereg.net/
  *
  * Due to the variable nature of TFBMs, all motifs found in genomes are given a
  * score out of one, indicating how strong the TFBM is. The score represents
  * the probability of each base occurring at each location in the motif. If a
  * variant hits a TFBM, it will alter the motif score, making it more or less
  * likely to bind the transcription factor. These variants are called TF
  * binding site variants, and the change in the motif score can be calculated.
  */
trait TFBM

/** Algorithms such as SIFT and PolyPhen estimate how likely this amino acid
  * change is to affect protein function. These estimates are based on how well
  * conserved the protein is, the chemical difference between the amino acids,
  * and the 3D structure of the protein (PolyPhen only).
  *
  * Both provide a score out of one (0 is the most severe for SIFT, whereas 1 is
  * the most severe for PolyPhen) along with a qualitative prediction. These are
  * predictions only, not experimental validations of the effect.
  *
  *   - https://sift.bii.a-star.edu.sg/
  *   - http://genetics.bwh.harvard.edu/pph2/
  */

enum CodonVariant:
  // Due to redundancies in the genetic code, many nucleotide changes
  // will not change the amino acid sequence, for example a GCT to
  // GCC change would still encode an alanine.
  case Synonymous()

  // These turn a coding codon, such as GGA glycine, to a stop codon,
  // e.g. TGA. This will result in a truncated protein, which may or
  // may not be subject to nonsense-mediated decay depending on where
  // in the peptide it occurs.
  case Nonsense()

  // This change results in a change in amino acid, for example
  // ACC threonine to AAC asparagine.
  case Missense()

enum VariantType:
  case InZBHORegion(effect: CodonVariant)
  case TranscriptionFactorBindingSite()

case class Locus(basePos1: Int, basePos2: Int)

/** Germline Variant Calling In germline variant calling, the reference genome
  * is the standard for the species of interest. This allows us to identify
  * genotypes.
  *
  * As most genomes are diploid, we expect to see that at any given locus,
  * either all reads have the same base, indicating homozygosity, or
  * approximately half of all reads have one base and half have another,
  * indicating heterozygosity.
  *
  * An exception to this would be the sex chromosomes in male mammals.
  */

case class Genotype()

trait Transcription

/** Somatic Variant Calling In somatic variant calling, the reference is a
  * related tissue from the same individual. Here, we expect to see mosaicism
  * between cells.
  */
