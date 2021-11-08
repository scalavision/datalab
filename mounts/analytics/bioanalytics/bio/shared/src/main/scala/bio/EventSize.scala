package bio

import bio.bed.Bed
import bio.vcf.DataLine

final case class EventSize(value: Int)

trait ToEventSize[A]:
  extension (a: A) def sizeOf: EventSize

object ToEventSize:

  given ToEventSize[Bed] with
    extension (bed: Bed)
      def sizeOf: EventSize =
        EventSize(bed.end.value - bed.start.value)

  given ToEventSize[DataLine] with
    extension (dataLine: DataLine)
      def sizeOf: EventSize =
        val end: Int = ???
        EventSize(end - dataLine.pos.value)
