package bio

import zio.test.*
import zio.test.Assertion.*
import bio.bed.Bed
import bio.bed.Bed.*

import bio.ToEventSize.{given, _}

object ToEventSizeSpec:
  val suite1 = suite("ToEventSize")(
    test("test dsl") {

      // safe constructor example
      val bed = Bed3(Chr._1, Start(10), End(100))

      // unsafe constructor example
      val bed2 = Bed("1", 10, 100)

      assert(bed.sizeOf)(equalTo(EventSize(90))) &&
      assert(bed2.sizeOf)(equalTo(EventSize(90)))
    }
  )
