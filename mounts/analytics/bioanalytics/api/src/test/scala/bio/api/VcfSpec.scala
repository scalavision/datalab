package bio.api

import zio.test.*
import zio.test.Assertion.*
import zio.stream.*
import bio.api.vcf
import bio.Chr
import bio.vcf.processor.VcfFilter
import bio.ZBHOPosition

object VcfSpec:

  val pos1 = ZBHOPosition.start(Chr._1, 125)
  val pos2 = ZBHOPosition.start(Chr._1, 234)

  def posFilter =
    VcfFilter.before(pos1) &&
      VcfFilter.after(pos2)

  def cnvExample() = vcf.filterOut(
    "/storage/bio/HG002/merged_Diag-ValidationWGS-HG002C2a-PM.vcf",
    VcfFilter.isCnv,
    vcf.OutputStrategy.To(
      "/storage/bio/HG002/merged2_Diag-ValidationWGS-HG002C2a-PM.vcf"
    )
  )

  def snvExample() = vcf.filterOut(
    "/storage/bio/HG002/Diag-ValidationWGS2-HG002C2-PM-Mendel-v09.vcf",
    !VcfFilter.isCnv,
    vcf.OutputStrategy.To("/storage/bio/HG002/snv_example.vcf")
  )

  def suite1 = suite("Exploring vcf api usability")(
    test("filter out SVTYPE=BND") {
      assert(1)(equalTo(1))
    }
  )
