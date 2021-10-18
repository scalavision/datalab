package bash

import zio.test.*
import zio.test.Assertion.*

object BashSpec:

  def suite1 = suite("bash dsl")(
    test("basic bash syntax") {

      val pipeline = find"""-name "*txt"""" | grep"dog"

      println(pipeline)
      assert(1)(equalTo(1))
    }
  )
