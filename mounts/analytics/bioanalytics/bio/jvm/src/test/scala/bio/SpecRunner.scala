package bio

import zio.test.*
import zio.test.Assertion.*

object SpecRunner extends zio.test.DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    ToEventSizeSpec.suite1
//ChrSpec.suite1
//    vcf.MetaInfoSpecRunner.suite1
