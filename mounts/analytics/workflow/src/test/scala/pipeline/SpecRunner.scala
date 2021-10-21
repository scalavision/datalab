package pipeline

import zio.test.*
import zio.test.Assertion.*

object SpecRunner extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    bash.BashSpec.suite1
