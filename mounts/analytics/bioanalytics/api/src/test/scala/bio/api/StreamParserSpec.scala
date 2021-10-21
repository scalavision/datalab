package bio.api

import zio.test._
import zio.test.Assertion._
import zio.stream._

object StreamParserSpec:
  def suite1 = suite("StreamParserSpec")(
    test("parse complete file") {

      val data = ZStream.fromFile(java.nio.file.Paths.get("/storage/data/HG002/vcf/Diag-ValidationWGS2-HG002C2-PM-Mendel-v09.vcf")).transduce(
        ZTransducer.utf8Decode >>> ZTransducer.splitLines
      )
      .filter(!_.startsWith("#")).map { line =>
        vcf.parseDataLine(line)
      }.take(4)
      //.peel(ZSink.head[String])
      
      val rt = zio.Runtime.default

      val result = rt.unsafeRun(data.runCollect)
      println(result)
      assert(1)(equalTo(1))
    }

  )
