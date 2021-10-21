package bio.codec

// Taken from this gist:
// jhnynek (Oscar Boykin) typed_record.scala
object RecordMap:
  opaque type Record[ A <: Tuple] = Map[String, Any]

  object Record:
    type HasKey[A <: Tuple, K] = A match
      case (K,t)*: _ => t
      case _ *: t => HasKey[t, K]

    type UpdateKey[A <: Tuple, K, V ] <: Tuple =
      A match
        case EmptyTuple => (K,V) *: EmptyTuple
        case (K, _) *: t => (K,V) *: t
        case head *: tail => head *: UpdateKey[tail, K, V]

    val empty: Record[EmptyTuple] = Map.empty

    extension [A <: Tuple](toMap: Record[A])
      def apply[K <: String & Singleton](key: K): HasKey[A, K] =
        toMap(key).asInstanceOf[HasKey[A,K]]

      def +[K <: String & Singleton, V] (kv: (K,V)): Record[UpdateKey[A,K,V]] =
        toMap + kv

object RecordExample:
  import RecordMap.*
  case class Person(name: String, age: Int)
  val rec = Record.empty + ("str" -> "This is a string") + ("int" -> 42) + ("person" -> Person("Arne", 78))

  val strRes: String = rec("str")
  val intRes: Int = rec("int")
  val arne: Person = rec("person")
