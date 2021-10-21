package bio.vcf

import VcfColumn.*
import zio.Chunk
import bio.codec.BioCodec

final case class DataLine(
    chrom: Chrom,
    pos: Pos,
    id: VariantId,
    ref: Ref,
    alt: Alt,
    qual: Qual,
    filter: Filter,
    info: Info,
    format: Format,
    genotypes: Genotype
) derives BioCodec

object DataLine:
  given Conversion[Chrom, bio.Chr] with
    def apply(crhom: Chrom) =
      bio.Chr(crhom.value)

enum VcfColumn:
  case Chrom(value: String) extends VcfColumn
  case Pos(value: Int) extends VcfColumn
  case VariantId(value: String) extends VcfColumn
  case Ref(value: String) extends VcfColumn
  case Alt(value: String) extends VcfColumn
  case Qual(value: String) extends VcfColumn
  case Filter(values: Chunk[String]) extends VcfColumn
  case Info(values: Map[String, Chunk[String]]) extends VcfColumn
  case Format(values: Chunk[String]) extends VcfColumn
  case Genotype(values: Chunk[String])

object VcfColumn:
  import bio.Chr
  given Ordering[VcfColumn.Chrom] with
    override def compare(c1: Chrom, c2: Chrom): Int =
      val chr1 = Chr(c1.value)
      val chr2 = Chr(c2.value)
      chr1.intValue.compareTo(chr2.intValue)
