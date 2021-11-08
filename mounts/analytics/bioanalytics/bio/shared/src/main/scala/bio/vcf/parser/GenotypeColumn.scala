package bio.vcf.parser

import bio.vcf.model.Genotype

final case class GenotypeColumn(
    genotype: Genotype,
    otherFields: Map[GenotypeKey, GenotypeField]
)

trait GenotypeKey:
  def key[A <: GenotypeField](a: A): String

enum GenotypeField:
  case AD(readDepthEachAllele: List[Int])
  case ADF(readDepthForwardStrand: List[Int])
  case ADR(readDepthReverseStrand: List[Int])
  case DP(readDepth: Int)
  case EC(expectedAlternateAlleleCount: List[Int])
  case FT(called: String)
  case GL(genotypeLikelihood: List[Float])
  case GP(posteriorProbability: List[Float])
  case HQ(haplotypeQuality1: Int, haplotypeQuality2: Int)
  case MQ(rmsMappingQuality: Int)
  case PL(phredScaledGenotypeLikelihood: List[Int])
  case PP(phredScaledGenotypeProbability: List[Int])
  case PQ(phsaeingQuality: Int)
  case PS(phasedSet: Int)

enum GenotypeFilter:
  case Or(left: GenotypeFilter, right: GenotypeFilter)
  case And(left: GenotypeFilter, right: GenotypeFilter)
  case LessThan(value: GenotypeField)
  case LargerThan(value: GenotypeField)
  case EqualTo(value: GenotypeField)

//object GenotypeFilter:
