package bio.vcf.model

import bio.codec.BioCodec

enum Genotype derives BioCodec:
  self =>
  case Phased(allele1: Genotype, allele2: Genotype)
  case Unphased(allele1: Genotype, allele2: Genotype)
  case _0
  case _1
  case _2
  case AlternateAllele(allele: Int)
  case Hemizygous(allele: Int)
  case `.`
  case Nullizygous

  def |(g: Genotype) = Unphased(self, g)
  def /(g: Genotype) = Phased(self, g)

object Genotype:
  import Genotype.*
  def HomozygousReference = _0 / _0
  def HomozygousAlternate = _1 / _1
  def Heterozygous = _0 / _1
  def Missing = `.` / `.`
