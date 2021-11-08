package bio.api

import zio.*
import scala.util.Random
import bio.vcf.parser.DataLine
import bio.vcf.processor.VcfFilter
import scala.annotation.tailrec
import bio.Chr

object TestDataGenerator:

  final case class VcfRaw(
      meta: Chunk[String],
      header: String,
      data: Chunk[String]
  ):
    def append(line: String) =
      if vcf.isData(line) then copy(data = data :+ line)
      else if vcf.isMeta(line) then copy(meta = meta :+ line)
      else copy(header = line)

    @tailrec
    def extractRandomValuesFromChr(
        nrOfValues: Int,
        dataLines: Chunk[DataLine],
        selected: Chunk[DataLine]
    ): Chunk[DataLine] =
      if nrOfValues == 0 then selected
      else if dataLines.size < 2 then dataLines
      else
        val index = Random.nextInt(dataLines.size - 1)
        extractRandomValuesFromChr(
          nrOfValues = nrOfValues - 1,
          dataLines,
          selected :+ dataLines(index)
        )

    lazy val dataLinesByChr
        : Map[bio.vcf.parser.VcfColumn.Chrom, Chunk[DataLine]] =
      data
        .map(vcf.parseDataLine)
        .filter(line => VcfFilter.isChr.run(line))
        .groupBy(_.chrom)
        .map { case (chrom, dataLines) =>
          if dataLines.isEmpty then chrom -> Chunk.empty
          else
            chrom -> extractRandomValuesFromChr(
              Random.between(3, 7),
              dataLines,
              Chunk.empty
            )
        }
        .toMap

    def extractRandomLinesByChr: Chunk[String] =
      val sortedKeys = dataLinesByChr.keys.toList.sorted
      val initLines: Chunk[DataLine] = Chunk.empty
      sortedKeys
        .foldLeft(initLines) { (chunks, key) =>
          chunks ++ dataLinesByChr(key)
        }
        .map { dl =>
          bio.codec.BioCodec.derived[DataLine].encode(dl)
        }

    def toVcf: String =
      meta.mkString("\n") + "\n" + header + "\n" + extractRandomLinesByChr
        .mkString("\n") + "\n"

  object VcfRaw:
    def apply(): VcfRaw = VcfRaw(Chunk.empty, "", Chunk.empty)

  def generateNewTestData(inputVcf: String, outputVcf: String) =
    val rawZ = vcf.readFile(inputVcf).fold(VcfRaw()) { (raw, value) =>
      raw.append(value)
    }

    val vcfRaw = zio.Runtime.default.unsafeRun(rawZ)
    os.write.over(os.Path(outputVcf), vcfRaw.toVcf)
