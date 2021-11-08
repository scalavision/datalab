package bio.api

import zio.*
import zio.stream.*
import bio.codec.BioCodec
import bio.vcf.parser.DataLine
import bio.vcf.processor.VcfFilter

object vcf:

  val dataLineParser = summon[BioCodec[DataLine]]

  val rt = zio.Runtime.default

  val isMeta: String => Boolean = _.startsWith("##")
  val isHeader: String => Boolean = _.startsWith("#")
  val isData: String => Boolean = !_.startsWith("#")

  def parseDataLine(data: String) =
    dataLineParser.decode(data)

  def readFile(path: String) =
    ZStream
      .fromFile(java.nio.file.Paths.get(path))
      .transduce(
        stream.ZTransducer.utf8Decode >>> stream.ZTransducer.splitLines
      )

  def dataLines(
      path: String
  ): ZStream[zio.blocking.Blocking, Throwable, DataLine] =
    readFile(path).filter(isData).map(parseDataLine)

  def metaInfo(path: String) =
    readFile(path).filter(_.startsWith("##"))

  def header(path: String) =
    readFile(path).dropWhile(_.startsWith("##")).take(1)

  trait KeyStrategy:
    def init(dataLine: DataLine): String

  enum DuplicationStrategy:
    case KeepFirstEvent
    case KeepLastEvent
    case MergeEvents
    case Custom(mergeFunction: (List[DataLine], DataLine) => List[DataLine])

  def makeDB[A](
      path: String,
      keyStrategy: KeyStrategy,
      duplicationStrategy: DuplicationStrategy
  ): Map[String, List[DataLine]] =
    val dbZIO = dataLines(path).fold(Map.empty[String, List[DataLine]]) {
      case (tmpMap, dataLine) =>
        val idValue = keyStrategy.init(dataLine)
        import DuplicationStrategy.*
        duplicationStrategy match
          case KeepFirstEvent => tmpMap
          case KeepLastEvent => (tmpMap - idValue) + (idValue -> List(dataLine))
          case MergeEvents =>
            tmpMap.updated(idValue, tmpMap(idValue) ++ List(dataLine))
          case Custom(mergeFunc) =>
            tmpMap.updated(idValue, mergeFunc(tmpMap(idValue), dataLine))
    }

    rt.unsafeRun(dbZIO)

  def removeDuplicatedLines(
      db: Map[String, List[DataLine]],
      removeDuplicatedLines: List[DataLine] => DataLine
  ) = ???

  def filter(path: String, vcfFilter: VcfFilter)(using
      codec: BioCodec[DataLine]
  ): ZStream[zio.blocking.Blocking, Throwable, String] =
    metaInfo(path) ++ header(path) ++ dataLines(path)
      .filter(dataLine => VcfFilter.matches(vcfFilter, dataLine))
      .map(dataLine => codec.encode(dataLine))

  enum OutputStrategy:
    case Overwrite
    case To(path: String)

  def filterOut(path: String, vcfFilter: VcfFilter, output: OutputStrategy) =
    val stream = filter(path, vcfFilter)
    val rt = zio.Runtime.default
    val vcfChunk: Chunk[String] = rt.unsafeRun(stream.runCollect)
    val vcf = vcfChunk.mkString("\n") + "\n"

    output match
      case OutputStrategy.Overwrite =>
        os.write.over(os.Path(path), vcf)
      case OutputStrategy.To(path) => os.write.over(os.Path(path), vcf)

  enum AnnotationEngine:
    case Vep(command: String)
    case Genepanel(command: String)

  def annotate(
      path: String,
      out: OutputStrategy,
      annotationEngine: AnnotationEngine
  ) = ???
