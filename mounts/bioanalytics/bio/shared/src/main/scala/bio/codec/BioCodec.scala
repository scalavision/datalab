package bio.codec

import scala.deriving.*
import scala.compiletime.{erasedValue, summonInline}

import bio.bed.*
import bio.vcf.*

import zio.Chunk
import bio.*
import Strand.*

trait BioCodec[A]:
  def decode(s: String): A
  def encode(a: A): String

object BioCodec:

  def apply[A](using instance: BioCodec[A]): BioCodec[A] = instance

  inline def to[A](
      encodeN: String => A,
      decodeN: A => String
  ): BioCodec[A] = new BioCodec[A] {
    def decode(s: String): A = encodeN(s)
    def encode(a: A): String = decodeN(a)
  }

  given BioCodec[Int] = to(
    _.toInt,
    _.toString()
  )

  given BioCodec[Chr] = to(
    {
      case "1"  => Chr._1
      case "2"  => Chr._2
      case "3"  => Chr._3
      case "4"  => Chr._4
      case "5"  => Chr._5
      case "6"  => Chr._6
      case "7"  => Chr._7
      case "8"  => Chr._8
      case "9"  => Chr._9
      case "10" => Chr._10
      case "11" => Chr._11
      case "12" => Chr._12
      case "13" => Chr._13
      case "14" => Chr._14
      case "15" => Chr._15
      case "16" => Chr._16
      case "17" => Chr._17
      case "18" => Chr._18
      case "19" => Chr._19
      case "20" => Chr._20
      case "21" => Chr._21
      case "22" => Chr._22
      case "X"  => Chr.X
      case "Y"  => Chr.Y
      case "MT" => Chr.MT
    },
    {
      case Chr._1  => "1"
      case Chr._2  => "2"
      case Chr._3  => "3"
      case Chr._4  => "4"
      case Chr._5  => "5"
      case Chr._6  => "6"
      case Chr._7  => "7"
      case Chr._8  => "8"
      case Chr._9  => "9"
      case Chr._10 => "10"
      case Chr._11 => "11"
      case Chr._12 => "12"
      case Chr._13 => "13"
      case Chr._14 => "14"
      case Chr._15 => "15"
      case Chr._16 => "16"
      case Chr._17 => "17"
      case Chr._18 => "18"
      case Chr._19 => "19"
      case Chr._20 => "20"
      case Chr._21 => "21"
      case Chr._22 => "22"
      case Chr.X   => "X"
      case Chr.Y   => "Y"
      case Chr.MT  => "MT"
    }
  )

  given BioCodec[Start] = to(
    s => Start.apply(s.toInt),
    _.toString()
  )

  given BioCodec[End] = to(s => End(s.toInt), _.toString)

  given BioCodec[RGB] = to(
    s =>
      val values = s.split(',').map(_.toInt)
      RGB(values(0), values(1), values(2)),
    s => s"${s.red},${s.blue},${s.red}"
  )

  given BioCodec[Boolean] = to(
    s => if s == "true" then true else false,
    b => if b then "true" else "false"
  )

  given BioCodec[Char] = to(_.head, _.toString)

  given BioCodec[String] = to(identity, identity)

  private inline def strToA[A](f: Chunk[String] => A): String => A = s =>
    val values = Chunk.fromArray(s.split(':'))
    f(values)

  private inline def toColonStr: Chunk[String] => String = _.mkString(":")

  given BioCodec[VcfColumn.Format] = to(
    s => strToA[VcfColumn.Format](VcfColumn.Format.apply)(s),
    f => toColonStr(f.values)
  )

  given BioCodec[VcfColumn.Filter] = to(
    s => strToA[VcfColumn.Filter](VcfColumn.Filter.apply)(s),
    f => toColonStr(f.values)
  )

  given BioCodec[VcfColumn.Genotype] = to(
    s => strToA[VcfColumn.Genotype](VcfColumn.Genotype.apply)(s),
    f => toColonStr(f.values)
  )

  given genotypeCodec: BioCodec[bio.vcf.Genotype] = to(
    s => bio.vcf.Genotype.Missing,
    g => ""
  )

  /*
  given chunkCodec: Chunk[bio.vcf.VcfColumn.VariantId] = to(
  chunk => ???,
  data => ???
  )
  given chunkCodec2: Chunk[bio.vcf.VcfColumn.Genotype] = to(???, ???)
   */
  //given BioCodec[Chunk[bio.vcf.VcfColumn.Genotype]] = to(???, ???)

  given BioCodec[VcfColumn.Info] = to(
    s =>
      val values = Chunk.fromArray(s.split(';'))
      val keyValues = values.map { v =>
        val keyValues = v.split('=')
        val key = keyValues.head
        val values = Chunk.fromArray(keyValues.last.split(','))
        key -> values
      }.toMap

      VcfColumn.Info(keyValues),
    f =>
      f.values
        .map { case (key, value) =>
          s"${key}=${value.mkString(",")}"
        }
        .mkString(";")
  )

  given BioCodec[Plus] = to(
    { case "+" =>
      Strand.Plus()
    },
    { case Strand.Plus() =>
      "+"
    }
  )

  given BioCodec[Minus] = to(
    { case "-" =>
      Strand.Minus()
    },
    { case Strand.Minus() =>
      "-"
    }
  )

  given BioCodec[Strand] = to(
    {
      case "+" => Strand.Plus()
      case "-" => Strand.Minus()
      case s =>
        throw new Exception(
          s"Bed:Strand: expected either - or +, input was : $s"
        )
    },
    {
      case Strand.Plus()  => "+"
      case Strand.Minus() => "-"
    }
  )

  def decodeElem(elem: BioCodec[_])(x: Any): String =
    elem.asInstanceOf[BioCodec[Any]].encode(x)

  def encodeElem[T](elem: BioCodec[T])(s: String): T =
    elem.asInstanceOf[BioCodec[T]].decode(s)

  def iterator[T](p: T) = p.asInstanceOf[Product].productIterator

  def tupleEncoder[T](s: Chunk[String], elems: Chunk[BioCodec[_]]): Tuple =
    if s.isEmpty then EmptyTuple
    else encodeElem(elems.head)(s.head) *: tupleEncoder(s.tail, elems.tail)

  def bioCodecProduct[T](
      p: Mirror.ProductOf[T],
      elems: Chunk[BioCodec[_]]
  ): BioCodec[T] =
    new BioCodec[T]:

      def decode(s: String): T =
        val columns = Chunk.fromArray(s.split('\t'))

        val tuples = tupleEncoder(columns, elems)
        p.fromProduct(tuples)

      def encode(x: T): String =
        iterator(x)
          .zip(elems.iterator)
          .map { case (x, elem) =>
            decodeElem(elem)(x)
          }
          .mkString("\t")

  def bioCodecSum[T](
      s: Mirror.SumOf[T],
      elems: List[BioCodec[_]]
  ): BioCodec[T] =
    new BioCodec[T]:
      def decode(txt: String): T =
        println("#################")
        println(
          "CONTIGS ARE UNSUPPORTED!, unable to convert: " + s + " to a chromosome"
        )
        println("################")
        ???
      /*
        elems.map { e =>
          encodeElem(e)(txt)
        }
       */

      def encode(x: T): String =
        val ordx = s.ordinal(x) // (3)
        decodeElem(elems(ordx))(x)
  // (4)

  inline def summonAll[T <: Tuple]: List[BioCodec[_]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts)  => summonInline[BioCodec[t]] :: summonAll[ts]

  inline given derived[T](using m: Mirror.Of[T]): BioCodec[T] =
    val elemInstances = summonAll[m.MirroredElemTypes]
    inline m match
      case s: Mirror.SumOf[T] => bioCodecSum(s, elemInstances)
      case p: Mirror.ProductOf[T] =>
        bioCodecProduct(p, Chunk.fromIterable(elemInstances.toIterable))
