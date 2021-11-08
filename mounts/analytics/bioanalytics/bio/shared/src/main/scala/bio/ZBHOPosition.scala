package bio

import bio.vcf.parser.DataLine
import bio.bed.Bed
import scala.math.Ordering

trait ToZBHOPosition[A]:
  def toPos(a: A): ZBHOPosition

/** 0-based, half open position
  *
  * All calculations using a position, are based upon the 0-based, half open
  * principle. The reason is described here:
  *
  *   - https://www.biostars.org/p/6373/#6377
  *
  * See also this blog post:
  *
  *   - https://blog.goldenhelix.com/between-two-bases-coordinate-representations-for-describing-variants/
  */
enum ZBHOPosition:
  self =>

  def chrom: Chr
  def pos: Int = self match
    case StartPos(_, start) => start.value
    case OpenEndPos(_, end) => end.value

  case StartPos(chrom: Chr, start: Start)
  case OpenEndPos(chrom: Chr, end: End)

  def <(p2: ZBHOPosition): Boolean =
    Ordering[ZBHOPosition].lt(self, p2)

  def >(p2: ZBHOPosition): Boolean =
    Ordering[ZBHOPosition].gt(self, p2)

  def <=(p2: ZBHOPosition): Boolean =
    Ordering[ZBHOPosition].lteq(self, p2)

  def >=(p2: ZBHOPosition): Boolean =
    Ordering[ZBHOPosition].gteq(self, p2)

  def ==(p2: ZBHOPosition): Boolean =
    Ordering[ZBHOPosition].eq(p2)

object ZBHOPosition:
  import Chr.*

  def start(chr: Chr, pos: Int): ZBHOPosition =
    StartPos(chr, Start(pos))

  def openEndPos(chr: Chr, pos: Int): ZBHOPosition =
    OpenEndPos(chr, End(pos))

  given Ordering[StartPos] with
    override def compare(pos1: StartPos, pos2: StartPos): Int =
      ZBHOPosition.comparePos(
        pos1.chrom,
        pos1.pos.value,
        pos2.chrom,
        pos2.pos.value
      )

  given Ordering[OpenEndPos] with
    override def compare(pos1: OpenEndPos, pos2: OpenEndPos): Int =
      ZBHOPosition.comparePos(
        pos1.chrom,
        pos1.pos.value,
        pos2.chrom,
        pos2.pos.value
      )

  given Ordering[ZBHOPosition] with
    override def compare(
        pos1: ZBHOPosition,
        pos2: ZBHOPosition
    ): Int =
      comparePos(pos1.chrom, pos1.pos, pos2.chrom, pos2.pos)

  def comparePos(chr1: Chr, pos1: Int, chr2: Chr, pos2: Int) =
    if chr1 == chr2 then pos1.compare(pos2)
    else chr1.intValue.compare(chr2.intValue)

import ZBHOPosition.*

extension (p1: StartPos)

  def -(value: Int): StartPos =
    StartPos(p1.chrom, Start(p1.start.value - value))

  def +(value: Int): StartPos =
    StartPos(p1.chrom, Start(p1.start.value + value))

extension (p1: OpenEndPos)

  def -(value: Int): OpenEndPos =
    OpenEndPos(p1.chrom, End(p1.end.value - value))

  def +(value: Int): OpenEndPos =
    OpenEndPos(p1.chrom, End(p1.end.value + value))

trait ToStartPos[A]:
  def startPos(a: A): StartPos

object ToStartPos:

  def apply[A](using instance: ToStartPos[A]): ToStartPos[A] = instance

  inline def to[A](fn: A => StartPos) = new ToStartPos[A]:
    def startPos(a: A): StartPos = fn(a)

  given ToStartPos[DataLine] =
    to(dl => StartPos(bio.Chr(dl.chrom.value), bio.Start(dl.pos.value - 1)))

  given ToStartPos[Bed] = to(bed => StartPos(bed.chrom, bed.start))

trait ToOpenEndPos[A]:
  def OpenEndPos(a: A): OpenEndPos

object ToOpenEndPos:
  def apply[A](using instance: ToOpenEndPos[A]): ToOpenEndPos[A] = instance

  inline def to[A](fn: A => OpenEndPos) = new ToOpenEndPos[A]:
    def OpenEndPos(a: A): OpenEndPos = fn(a)

  given ToOpenEndPos[Bed] = to(bed => OpenEndPos(bed.chrom, bed.end))
