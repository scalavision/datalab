import sbt.Keys._
import sbt._
//import org.scalafmt.config.SortSettings

object LibraryDeps {

  lazy val OSLibVersion = "0.7.7"
  lazy val PPrintVersion = "0.6.6"
  lazy val SourcecodeVersion = "0.2.7"
  lazy val mUnitVersion = "0.7.26"
  lazy val uPickleVersion = "1.3.15"

  val lihaoyiStack = Seq(
    "com.lihaoyi" %% "sourcecode" % SourcecodeVersion,
    "com.lihaoyi" %% "pprint" % PPrintVersion,
    "com.lihaoyi" %% "os-lib" % OSLibVersion,
    "com.lihaoyi" %% "upickle" % uPickleVersion
  )

  lazy val ZioVersion = "1.0.11"
  lazy val ZioProcessVersion = "0.5.0"
  lazy val ZioConfigVersion = "1.0.6"
  lazy val ZioOpticsVersion = "0.1.0"
  lazy val ZioPreludeVersion = "1.0.0-RC5"

  val zioStack = Seq(
    "dev.zio" %% "zio" % ZioVersion,
    "dev.zio" %% "zio-streams" % ZioVersion,
    "dev.zio" %% "zio-test" % ZioVersion % Test,
    "dev.zio" %% "zio-test-sbt" % ZioVersion % Test
    /* We work in the 2.0.0 M2, will add these later
    "dev.zio" %% "zio-optics" % ZioOpticsVersion,
    //"dev.zio"       %% "zio-json"     % ZioJsonVersion,
    "dev.zio" %% "zio-process" % ZioProcessVersion,
    "dev.zio" %% "zio-config" % ZioConfigVersion,
    "dev.zio" %% "zio-prelude" % ZioPreludeVersion
    //"dev.zio" %% "zio-cli" %  ZioProcessVersion
     */
  )

  lazy val testLibs = Seq(
    "dev.zio" %% "zio-test" % ZioVersion % Test,
    "dev.zio" %% "zio-test-sbt" % ZioVersion % Test,
    "org.scalameta" %% "munit" % mUnitVersion % Test
  )

  lazy val testFrameworkStack = Seq(
    new TestFramework("munit.Framework"),
    new TestFramework("zio.test.sbt.ZTestFramework")
  )

  lazy val catsCore = "2.6.1"
  lazy val fs2 = "3.1.1"

  val typelevelStack = Seq(
    "org.typelevel" %% "cats-core" % catsCore,
    "co.fs2" %% "fs2-core" % fs2,
    "co.fs2" %% "fs2-io" % fs2
  )

  val scodec = "2.0.0"
  val scodecStream = "3.0.1"
  val scodecBits = "1.1.27"

  val codecStacks = Seq(
    "org.scodec" %% "scodec-core" % scodec,
    "org.scodec" %% "scodec-stream" % scodecStream,
    "org.scodec" %% "scodec-bits" % scodecBits
  )

  val htmlLibs = Seq(
    "io.github.ciaraobrien" %% "dottytags" % "1.1.0"
  )

  lazy val JannovarVersion = "0.36"
  lazy val htslib = "2.24.1"
  lazy val picard = "2.26.0"

  lazy val bioStack = Seq(
    "de.charite.compbio" % "Jannovar" % JannovarVersion,
    "de.charite.compbio" % "jannovar-cli" % JannovarVersion,
    "de.charite.compbio" % "jannovar-stats" % JannovarVersion,
    "de.charite.compbio" % "jannovar-filter" % JannovarVersion,
    "de.charite.compbio" % "jannovar-vardbs" % JannovarVersion,
    "de.charite.compbio" % "jannovar-htsjdk" % JannovarVersion,
    "de.charite.compbio" % "jannovar-inheritance-checker" % "0.20",
    "com.github.samtools" % "htsjdk" % htslib,
    "com.github.broadinstitute" % "picard" % picard
  )
}
