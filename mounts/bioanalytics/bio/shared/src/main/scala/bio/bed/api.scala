package bio.bed

import zio.Chunk
import bio.codec.*
import Bed.*
import bio.Chr.*

object api:

  val bed3Parser = summon[BioCodec[Bed3]]
  val bed4Parser = summon[BioCodec[Bed4]]
  val bed6Parser = summon[BioCodec[Bed6]]
  val bed9Parser = summon[BioCodec[Bed9]]
  val bed12Parser = summon[BioCodec[Bed12]]

  inline def parseBed3Row(s: String): Bed3 = bed3Parser.decode(s)
  inline def parseBed4Row(s: String): Bed4 = bed4Parser.decode(s)
  inline def parseBed6Row(s: String): Bed6 = bed6Parser.decode(s)
  inline def parseBed9Row(s: String): Bed9 = bed9Parser.decode(s)
  inline def parseBed12Row(s: String): Bed12 = bed12Parser.decode(s)

  inline def toMultiLine(s: String): Chunk[String] =
    Chunk.fromArray(s.split('\n').filter(_.nonEmpty))

  inline def parseMultilineContentBed3(s: String): Chunk[Bed3] =
    toMultiLine(s).map(bed3Parser.decode)

  inline def parseMultilineContentBed4(s: String): Chunk[Bed4] =
    toMultiLine(s).map(bed4Parser.decode)

  inline def parseMultilineContentBed6(s: String): Chunk[Bed6] =
    toMultiLine(s).map(bed6Parser.decode)

  inline def parseMultilineContentBed9(s: String): Chunk[Bed9] =
    toMultiLine(s).map(bed9Parser.decode)

  inline def parseMultilineContentBed12(s: String): Chunk[Bed12] =
    toMultiLine(s).map(bed12Parser.decode)
