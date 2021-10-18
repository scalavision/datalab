package bio.api

import zio.test.*
import zio.test.Assertion.*

object TestDataSpec:
  val suite1 = suite("Helper to generate testdata")(
    test("generate data:") {
      val base = "/storage/bio/HG002"
      val sample1 = s"$base/merged2_Diag-ValidationWGS-HG002C2a-PM.vcf"
      val sample2 = s"$base/Diag-ValidationWGS2-HG002C2-PM-Mendel-v09.cnv.vcf"
      val outSample2 = "HG002_sv_cnv.Mendeliome_v01.vcf"
      val wd = os.pwd
      val outFile =
        wd / "api" / "src" / "test" / "resources" / "vcf" / "hg002" / outSample2
      TestDataGenerator.generateNewTestData(sample2, outFile.toString)
      assert(1)(equalTo(1))
    }
  )
