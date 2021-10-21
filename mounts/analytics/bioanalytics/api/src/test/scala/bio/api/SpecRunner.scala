package bio.api

import zio.test.*
import zio.test.Assertion.*
object SpecRunner extends zio.test.DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    TestDataSpec.suite1
//    vcf.MetaInfoSpecRunner.suite1