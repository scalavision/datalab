package bio

import bio.vcf.DataLine
import bio.codec.BioCodec
import bio.ZBHOPosition.{StartPos, OpenEndPos}

// https://www.ebi.ac.uk/training/online/courses/human-genetic-variation-introduction/what-is-genetic-variation/what-effect-do-variants-in-coding-regions-have/

// Start position of the feature in standard
// chromosomal coordinates (i.e. first base is 0).

/*
object g:
  def start(chrom: String, pos: Int): StartPos =
    StartPos(Chr(chrom), Start(pos))
  def end(chrom: String, pos: Int): OpenEndPos =
    OpenEndPos(Chr(chrom), End(pos))
 */
opaque type Start = Int
object Start:
  def apply(value: Int): Start = value
  given Ordering[Start] with
    override def compare(s1: Start, s2: Start): Int =
      s1.value.compare(s2.value)

// End position of the feature in standard chromosomal coordinates
opaque type End = Int
object End:
  def apply(value: Int): End = value

  given Ordering[End] with
    override def compare(s1: End, s2: End): Int =
      s1.value.compare(s2.value)

extension (s: Start | End)
  def value: Int = s.self
  def compare(s2: Start | End): Int =
    s.value.compare(s2.value)

extension (dl: DataLine)
  def startPos = StartPos(Chr(dl.chrom.value), Start(dl.pos.value - 1))
  def openEndPos = ???

extension (s: String) def chr = bio.codec.BioCodec.derived[Chr].decode(s)
