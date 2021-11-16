package learning

import zio.test.*
import zio.test.Assertion.*

object NumSpec:
  def suite1 = suite("NumSpec")(
    test("simple calculation") {

      val expr =
        num(10) + -num(11) - -num(12)

      val result = calc(expr)
      pprint.pprintln(expr)

      val debugInfo = introspect(expr)
      pprint.pprintln(debugInfo)

      assert(result)(equalTo(11))
    }
  )
