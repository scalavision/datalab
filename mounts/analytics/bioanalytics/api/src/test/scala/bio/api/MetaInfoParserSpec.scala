package bio.api


import zio.test.*
import zio.test.Assertion.*
import bio.vcf.*

object MetaInfoSpecRunner:

  val suite1 = suite("header test")(
    test("parse header example"){
      val wd = os.pwd
      val header = wd / "bio" / "jvm" / "src" / "test" / "resources" / "header.vcf"
      val lines = os.read.lines(header).toList
      val result = lines.map(MetaInfo.apply)
      assert(result.size)(equalTo(101))
    }
  )