package bio

import zio.test.*
import zio.test.Assertion.*

object SpecRunner extends zio.test.DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    ChrSpec.suite1
//    vcf.MetaInfoSpecRunner.suite1
