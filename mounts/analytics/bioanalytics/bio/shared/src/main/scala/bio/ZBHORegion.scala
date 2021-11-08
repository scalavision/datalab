package bio

import bio.ZBHOPosition.{StartPos, OpenEndPos}

final case class ZBHORegion(start: StartPos, end: OpenEndPos):
  self =>

  import OffsetStrategy.*

  def offset(strategy: OffsetStrategy): ZBHORegion =
    strategy match
      case Off                => self
      case WidenStart(value)  => copy(start = start - value)
      case WidenEnd(value)    => copy(end = end + value)
      case ReduceStart(value) => copy(start = start + value)
      case ReduceEnd(value)   => copy(end = end - value)
      case And(s1, s2) =>
        val offset1 = offset(s1)
        offset1.offset(s2)
