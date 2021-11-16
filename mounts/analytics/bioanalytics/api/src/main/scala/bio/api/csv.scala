package bio.api

import bio.codec.{RowCodec, ColumnCodec, Splitter, SplitChar}
import zio.Chunk

object csv:

  def parse[A, B, C](a: String)(using
      rowEnc: RowCodec[A, C],
      colCodec: ColumnCodec[B]
  ): A =
    rowEnc.decode(a)

  def parseLine[A, B](a: String)(using
      rowCodec: RowCodec[A, B],
      splitter: Splitter[B]
  ): A =
    rowCodec.decode(a)

  def parseMultiLine[A, B](a: String)(using
      codec: RowCodec[A, B],
      splitter: Splitter[B]
  ): Chunk[A] =
    val lines =
      parse[Chunk[String], String, SplitChar.NewLine](a).filter(_.nonEmpty)
    lines.map(parseLine)

  def toCsvMultiLine[A, B](data: Chunk[A])(using
      //columnCodec: ColumnCodec[A],
      rowCodec: RowCodec[A, B],
      splitter: Splitter[B]
  ): String =
    data
      .map { d =>
        rowCodec.encode(d)
      }
      .mkString("\n")
