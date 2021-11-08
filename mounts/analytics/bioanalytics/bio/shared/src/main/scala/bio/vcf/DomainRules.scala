package bio.vcf

import VcfColumn.*

enum SvType:
  case Del
  case Dup
  case DupTandem
  case Ins
  case Inv

enum ChangeType:
  case Snp
  case InDel
  case Del
  case Ins
  case SvType

case class Allele(ref: Ref, alt: Alt):
  def commonSuffix: String = ???
  def length: Int = commonSuffix.length
  def changeType: ChangeType = ???

object Allele:
  def apply(ref: Ref, alt: Alt): Allele = Allele(ref, alt)
