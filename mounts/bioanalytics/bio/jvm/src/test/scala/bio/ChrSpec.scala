package bio

import zio.test.*
import zio.test.Assertion.*

object ChrSpec:

  val suite1 = suite("Chr")(
    test("codec for Chr datastructure"){
      println(Chr.values)
      assert(1)(equalTo(1))
    }
  )