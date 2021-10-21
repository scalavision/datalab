package bio.bed

/** Bed format
  *
  * The bed format is really messy if you have to deal with Optional fields.
  * Normally, you know how your bed format works, other times, you will have to
  * check if a field is empty or not.
  *
  * To make it more convenient to work with these formats, There are several
  * different versions of the Bed class:
  *
  * * Those handling a fixed number of non-empty fields have a prefix number
  * indicatingthe number of fields available * BedGeneric, follows the
  * specification, using Option for optional fields. It helps the user of the
  * library, never to forget to check for empty fields / non values. To handle
  * Option efficiently in scala, I recommend spending 30 minutes learning these
  * functions: * fold( fuction for emptyValue / nonExisting, function for valid
  * data) * getOrElse "default value" // returning a default value if value is
  * missing * map (only process the happy path) * filter (_.isEmpty) /
  * filter(_.nonEmpty) // simple filtering These functions are fundamental to
  * everything you do in Scala, from web servers to Spark, and also exist in
  * most other modern programming languages so it will be good investment in
  * time either way.
  *
  * resources:
  *   - https://genome.ucsc.edu/FAQ/FAQformat.html
  *   - https://m.ensembl.org/info/website/upload/bed.html
  */

import zio.Chunk
import bio.codec.BioCodec
import bio.*

final case class ThickStart(value: Int)
final case class ThickEnd(value: Int)
final case class RGB(red: Int, green: Int, blue: Int)

final case class TrackLine(track: Map[String, String])
final case class BedFile(lines: Chunk[BedLine], path: Option[String] = None)

enum Strand:
  case Plus()
  case Minus()

enum BedLine:
  case Track(track: Map[String, String])
  case Data(row: Bed)

enum Bed derives BioCodec:

  case Bed3(
      chrom: Chr,
      start: Start,
      end: End
  )

  case Bed4(
      chrom: Chr,
      start: Start,
      end: End,
      strand: Strand
  )

  case Bed6(
      chrom: Chr,
      start: Start,
      end: End,
      name: String,
      score: Int,
      strand: Strand
  )

  case Bed9(
      chrom: Chr,
      start: Start,
      end: End,
      name: String,
      score: Int,
      strand: Strand,
      thickStart: ThickStart,
      thickEnd: ThickEnd,
      rgb: RGB
  )

  case Bed12(
      chrom: Chr,
      start: Start,
      end: End,
      name: String,
      score: Int,
      strand: Strand,
      thickStart: ThickStart,
      thickEnd: ThickEnd,
      rgb: RGB,
      blockCount: Int,
      blockSizes: Int,
      blockStarts: Int
  )

  def chrom: Chr
  def start: Start
  def end: End

final case class Name(value: String) extends AnyVal
final case class BedGraph[A](
    chrom: Chr,
    start: Start,
    stop: End,
    score: A
)
