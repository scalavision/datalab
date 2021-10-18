package bio.codec

import bio.*
import bio.bed.*
import bio.codec.*

import Strand.{Plus, Minus}

object BedCodec:

  import ColumnCodec.to

  given ColumnCodec[Plus] = to(
    { case "+" =>
      Strand.Plus()
    },
    { case Strand.Plus() =>
      "+"
    }
  )

  given ColumnCodec[Minus] = to(
    { case "-" =>
      Strand.Minus()
    },
    { case Strand.Minus() =>
      "-"
    }
  )

  given ColumnCodec[Strand] = to(
    {
      case "+" => Strand.Plus()
      case "-" => Strand.Minus()
    },
    {
      case Strand.Plus()  => "+"
      case Strand.Minus() => "-"
    }
  )

  given ColumnCodec[RGB] = to(
    s =>
      val values = s.split(',').map(_.toInt)
      RGB(values(0), values(1), values(2))
    ,
    { case RGB(r, g, b) =>
      s"$r,$g,$b"
    }
  )

  given ColumnCodec[Start] = to(s => Start(s.toInt), _.toString)

  given ColumnCodec[End] = to(s => End(s.toInt), _.toString)

  given ColumnCodec[ThickStart] = to(s => ThickStart(s.toInt), _.value.toString)

  given ColumnCodec[ThickEnd] = to(s => ThickEnd(s.toInt), _.value.toString)

  import Bed.*
  import scala.deriving.Mirror

  given rowBed3(using
      rowCodec: RowCodec[(String, Start, End), SplitChar.Tab]
  ): RowCodec[Bed3, SplitChar.Tab] = new RowCodec {
    override def decode(s: String): Bed3 =
      val tuples = rowCodec.decode(s)
      summon[Mirror.Of[Bed3]].fromProduct(tuples)

    override def encode(b: Bed3): String =
      rowCodec.encode((b.chrom.toString, b.start, b.end))

  }
