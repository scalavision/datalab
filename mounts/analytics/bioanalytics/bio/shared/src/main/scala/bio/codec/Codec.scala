package bio.codec

import zio.Chunk

//TODO: Try to implement full codec derivation
//      https://dotty.epfl.ch/docs/reference/contextual/derivation.html
trait Codec[A]:
  def decode(a: String): A
  def encode(a: A): String

object Codec:

  def apply[A](using instance: Codec[A]): Codec[A] = instance

  def to[A](
      decodeN: String => A,
      encodeN: A => String
  ): Codec[A] = new Codec[A] {
    def decode(a: String): A = decodeN(a)
    def encode(a: A): String = encodeN(a)
  }
