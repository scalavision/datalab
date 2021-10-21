package bio.codec

import zio.Chunk

//TODO: put this at the typelevel
enum SplitChar:
  case NewLine()
  case WhiteSpace()
  case Tab()
  case Semi()
  case Comma()
  case Custom(c: Char)

trait Splitter[A]:
  val splitChar: Char

import SplitChar.*
object Splitter:

  def apply[A](using instance: Splitter[A]): Splitter[A] = instance

  inline def to[A](c: Char): Splitter[A] = new Splitter {
    val splitChar: Char = c
  }

  given Splitter[NewLine] = to('\n')
  given Splitter[Tab] = to('\t')
  given Splitter[Semi] = to(';')
  given Splitter[Comma] = to(',')
  given Splitter[WhiteSpace] = to(' ')
  given Conversion[Custom, Splitter[Custom]] = c =>
    new Splitter {
      val splitChar: Char = c.c
    }

  given [A](using s: Splitter[A]): Splitter[A] = to(s.splitChar)
