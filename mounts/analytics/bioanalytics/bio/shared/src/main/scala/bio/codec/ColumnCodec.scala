package bio.codec

import bio.*

trait ColumnCodec[A] extends Codec[A]

object ColumnCodec:

  def apply[A](using instance: ColumnCodec[A]): ColumnCodec[A] = instance

  def to[A](
      decodeN: String => A,
      encodeN: A => String
  ): ColumnCodec[A] = new ColumnCodec[A] {
    def decode(a: String): A = decodeN(a)
    def encode(a: A): String = encodeN(a)
  }

  given ColumnCodec[Chr] = to(s => Chr(s),c => bio.codec.BioCodec.derived[Chr].encode(c))

  given ColumnCodec[String] = to(
    identity,
    identity
  )

  given ColumnCodec[Short] = to(
    _.toShort,
    _.toString
  )

  given ColumnCodec[Int] = to(
    _.toInt,
    _.toString
  )

  given ColumnCodec[Long] = to(
    _.toLong,
    _.toString
  )

  given ColumnCodec[Double] = to(
    _.toDouble,
    _.toString
  )

  given ColumnCodec[Float] = to(
    _.toFloat,
    _.toString
  )

  //TODO: This is probably a bit too forgiving.
  //      One could handle this with an Option/Either, but
  //      crashing on invalid input data is often times just as good
  //      In future release track all invalid lines of data, and not crash
  given ColumnCodec[Boolean] = to(
    s =>
      if s == "true" || s == "True" || s == "yes" || s == "Yes" then true
      else if s == "false" || s == "False" || s == "no" || s == "No" then false
      else throw new Exception(s" unable to parse boolean value: $s, Exiting"),
    _.toString
  )

  given Option[A](using
      codec: ColumnCodec[A]
  ): ColumnCodec[Option[A]] = to(
    s =>
      if s.nonEmpty then Some(codec.decode(s))
      else None,
    _.fold("")(codec.encode(_))
  )

  given Either[A, B](using
      codecA: ColumnCodec[A],
      codecB: ColumnCodec[B]
  ): ColumnCodec[Either[A, B]] = to(
    s =>
      try {
        Left(codecA.decode(s))
      } catch {
        case _: Throwable =>
          Right(codecB.decode(s))
      },
    {
      case Left(v)  => codecA.encode(v)
      case Right(v) => codecB.encode(v)
    }
  )
