package bio.vcf

import bio.*

/*
enum VcfProcessor:
  case Delete(row: String)
 */

enum VcfMerge:
  case Combine(v1: VcfMerge, v2: VcfMerge)
  case InfoField(key: String, value: String)

def mergeFunction(vcfMerge: VcfMerge, dataLine: DataLine): DataLine =
  vcfMerge match
    case VcfMerge.Combine(v1, v2) => 
      val dl = mergeFunction(v1, dataLine)
      mergeFunction(v2, dl)
    case VcfMerge.InfoField(key, value) => ???


/*
def mergeFunction2(vcfMerge: VcfMerge, dataLine: VcfMetaHeader): VcfMeta  = vcfMerge match
  case VcfMerge.Combine(v1, v2) => ???
 */

enum MergeSvs:
  case Rule()

enum VcfFilter:
  self =>
  def &&(that: VcfFilter): VcfFilter = And(self, that)
  def ||(that: VcfFilter): VcfFilter = Or(self, that)
  def unary_! : VcfFilter = Not(self)

  case IsChr(selected: List[Chr])

  case ForAll(filters: List[VcfFilter])
  case FindOne(filters: List[VcfFilter])

  case And(left: VcfFilter, right: VcfFilter)
  case Or(left: VcfFilter, right: VcfFilter)
  case Not(value: VcfFilter)

  case InfoKey(value: String)
  case InfoValue(key: String, values: List[String])

  case AtPos(pos: ZBHOPosition)
  case Before(pos: ZBHOPosition)
  case After(pos: ZBHOPosition)

  case Size(value: Int)
  case LessThan(value: Size)
  case LargerThan(value: Size)

  case Format(key: String, value: String)

  def run(dataLine: DataLine): Boolean =
    VcfFilter.matches(self, dataLine)

case class Validation(nonPassedEvents: List[Any], passedEvents: List[Any])

object VcfFilter:

  case object Info:
    def hasKey(value: String): VcfFilter = VcfFilter.InfoKey(value)
    def hasValue(key: String, value: String*): VcfFilter =
      VcfFilter.InfoValue(key, value.toList)

  /*
  Need to use These
  def split(filter: VcfFilter, dataLine: DataLine: These[List[DataLine], List[DataLine]] = filter match
    case AtPos(pos) =>
      if dataLine.pos == pos then
        Right(pos)
      else
        Left(pos)
    case And(left, right) =>
      (selector(left), selector(right)) match
        case (Right(right), Right(left)) =>
          Right(List(right, left))
        case (Left(left), Right(right)) =>

      val ourLeftDataResult: Validation = selector(left, dataLine)
      val ourRightDataResult: Validation = selector(right, dataLine)
      val allIsTrue = ourLeftDataResult.nonPassed
   */

  def matches(filter: VcfFilter, dataLine: DataLine): Boolean =
    filter match

      case And(l, r) =>
        val left = matches(l, dataLine)
        val right = matches(r, dataLine)
        left && right

      case Not(innerFilter) => !matches(innerFilter, dataLine)
      case Or(l, r)         => matches(l, dataLine) || matches(r, dataLine)

      case InfoKey(key) => dataLine.info.values.isDefinedAt(key)
      case InfoValue(key: String, values: List[String]) =>
        dataLine.info.values.isDefinedAt(key) && dataLine.info.values(
          key
        ) == values

      case AtPos(pos)  => dataLine.startPos == pos
      case Before(pos) => pos < dataLine.startPos
      case After(pos)  => pos > dataLine.startPos

      //TODO: make this stack safe
      case ForAll(filters) =>
        filters.forall(filter => matches(filter, dataLine))

      case FindOne(filters) =>
        val result: Option[VcfFilter] =
          filters.find(filter => matches(filter, dataLine))
        result.fold(false)(_ => true)

      case IsChr(selected) =>
        if selected.isEmpty then Chr.isChr(dataLine.chrom.value)
        else selected.contains(Chr(dataLine.chrom.value))

      case Size(value) => ???
      case LessThan(value) => ???
      case LargerThan(value) => ???
      case Format(key, value) => ???


  /** Chromosome * */
  def isChr = IsChr(List.empty)
  def chr1_to_22 = IsChr(Chr.numerical)
  def chrMT = IsChr(List(Chr.MT))
  def chrX = IsChr(List(Chr.X))
  def chrY = IsChr(List(Chr.Y))
  def chrOf(chr: Chr, chrs: Chr*) = IsChr(chr :: chrs.toList)
  def chr1_to_22_or_X_or_Y =
    chr1_to_22 || chrX || chrY

  /** Position based * */
  def pos(p: ZBHOPosition) = AtPos(p)
  def before(p: ZBHOPosition) = Before(p)
  def beforeInclusive(p: ZBHOPosition) = Before(p) || AtPos(p)
  def after(p: ZBHOPosition) = After(p)
  def afterInclusive(p: ZBHOPosition) = AtPos(p) || After(p)

  /** Region based * */
  def insideRegion(
      start: ZBHOPosition,
      end: ZBHOPosition
  ) =
    after(start) && before(end)
  def insideRegionInclusive(
      start: ZBHOPosition,
      end: ZBHOPosition
  ) =
    (pos(start) || after(start)) && (pos(end) || before(end))

  def overlapLeftBreakpoint(
      start: ZBHOPosition,
      end: ZBHOPosition
  ) =
    before(start) && before(end)
  def overlapRightBreakpoint(
      start: ZBHOPosition,
      end: ZBHOPosition
  ) =
    after(start) && (pos(end) || after(end))

  /** Transcripts and Genepanels
    */
  def filterByGenePanel(genepanel: ZBHORegion) = ???
  def filterByTranscript(
      transcripts: List[ZBHORegion],
      offsetStrategy: OffsetStrategy = OffsetStrategy.Off
  ) =
    offsetStrategy match
      case OffsetStrategy.Off =>
        FindOne(transcripts.map(cr => insideRegion(cr.start, cr.end)))
      case _ =>
        FindOne(
          transcripts
            .map(offsetStrategy.offset)
            .map(cr => insideRegion(cr.start, cr.end))
        )

  /** CNV based * */
  private def svType(svType: String) = Info.hasValue("SVTYPE", svType)

  def isBnd = svType("BND")
  def isDup = svType("DUP")
  def isDupTandem = svType("DUP:TANDEM")
  def isDel = svType("DEL")
  def isIns = svType("INS")
  def isCnv =
    svType("CNV") || svType("DUP") || svType("DUP:TANDEM") || svType("DEL")
  def hasSvType = Info.hasKey("SVTYPE")

  //TODO: make this stack safe
  def forAll(filters: List[VcfFilter]) = ForAll(filters)
  def findOne(filters: List[VcfFilter]) = FindOne(filters)
