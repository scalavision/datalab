package pipeline

import zio.test.*
import zio.test.Assertion.*

object SpecRunner extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    //learning.NumSpec.suite1
    // pipeline.UserAdminPipelineSpec.suite1
    bash.BashSpec.suite1
