package bio

final case class Contig(value: String)

enum Chr:
  self =>
  case _1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16,
  _17, _18, _19, _20, _21, _22, X, Y, MT

  def str =
    Chr.codec.encode(self)

  def isNumeric: Boolean = Chr.isNumeric(str)

  def intValue: Int =
    if isNumeric then str.toInt
    else if str == "X" then 23
    else if str == "Y" then 24
    else 25

object Chr:

  val codec = summon[bio.codec.BioCodec[Chr]]

  def apply(s: String): Chr =
    codec.decode(s)

  lazy val numerical: List[Chr] = Chr.values.filter(_.isNumeric).toList
  lazy val isChr = Chr.values.map(chr => codec.encode(chr)).toSet
  lazy val isNumeric = (1 to 22).map(_.toString).toSet
  lazy val isX: String => Boolean = _ == "X"
  lazy val isY: String => Boolean = _ == "Y"
  lazy val isMT: String => Boolean = _ == "MT"

  given Ordering[Chr] with
    override def compare(c1: Chr, c2: Chr): Int =
      c1.intValue.compareTo(c2.intValue)
