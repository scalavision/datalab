package bio

enum OffsetStrategy:
  self =>
  case And(s1: OffsetStrategy, s2: OffsetStrategy)
  case WidenStart(value: Int)
  case WidenEnd(value: Int)
  case ReduceStart(value: Int)
  case ReduceEnd(value: Int)
  case Off

  def &&(that: OffsetStrategy): OffsetStrategy =
    And(self, that)

  def widen(value: Int) = And(self, And(WidenStart(value), WidenEnd(value)))
  def widenStart(value: Int) = And(self, WidenStart(value))
  def widenEnd(value: Int) = And(self, WidenEnd(value))
  def reduce(value: Int) = And(self, And(ReduceStart(value), ReduceEnd(value)))
  def reduceEnd(value: Int) = And(self, ReduceEnd(value))
  def reduceStart(value: Int) = And(self, ReduceStart(value))

  def offset(region: ZBHORegion): ZBHORegion = self match
    case Off                => region
    case WidenStart(value)  => region.copy(start = region.start - value)
    case WidenEnd(value)    => region.copy(end = region.end + value)
    case ReduceStart(value) => region.copy(start = region.start + value)
    case ReduceEnd(value)   => region.copy(end = region.end - value)
    case And(s1, s2) =>
      val region1 = s1.offset(region)
      s2.offset(region1)

object OffsetStrategy:
  import OffsetStrategy.*
  def widen(value: Int) = And(WidenStart(value), WidenEnd(value))
  def widenStart(value: Int) = WidenStart(value)
  def widenEnd(value: Int) = WidenEnd(value)
  def reduce(value: Int) = And(ReduceStart(value), ReduceEnd(value))
  def reduceEnd(value: Int) = ReduceEnd(value)
  def reduceStart(value: Int) = ReduceStart(value)
