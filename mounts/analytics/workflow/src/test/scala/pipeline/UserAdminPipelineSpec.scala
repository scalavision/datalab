package pipeline

import zio.test.*
import zio.test.Assertion.*

/** Dummy pipeline to play with workflow dsl
  */

case class PasswordExtractor(
    output: String
)

case class IdExtractor(
    user: String,
    output: String
)

object PasswdParser:
  import zio.Chunk
  import bio.api.csv

  def apply(file: os.Path): Chunk[linux.Passwd] =
    val text = os.read.lines(file).toString

    csv
      .parseMultiLine[
        (
            String,
            String,
            Int,
            Int,
            String,
            String,
            String
        ),
        bio.codec.SplitChar.Colon
      ](text)
      .map { case (un, password, userId, groupId, userInfo, homeDir, shell) =>
        linux.Passwd(
          un,
          password,
          userId,
          groupId,
          userInfo,
          homeDir,
          shell
        )
      }

/** TODO's
  *   - input and output needs to be used when chaining tasks together
  *   - need to add correct args to the scripts, these can also be taken from
  *     previous task's output if relevant etc.
  */
object UserAdminPipelineSpec:

  lazy val wd =
    os.Path(java.nio.file.Paths.get(".").toAbsolutePath)

  def suite1 = suite("workflow example")(
    test("pipeline example") {

      val scriptBase = wd / "test" / "resources" / "scripts"
      val workspace = wd / "test" / "resource" / "workspace"

      val passwd_extractor = scriptBase / "passwd_extractor.sh"
      val id_extractor = scriptBase / "id_extractor.sh"

      val passwdOutput = workspace / "mypassinfo.txt"
      val userInfoOutput = workspace / "workspace.txt"

      val pwdExtractor = Task
        .create(
          "password_extractor",
          scriptFile(passwd_extractor)
        )
        .output(
          fileOutput(passwdOutput)
        )

      // val content = os.proc("cat", "/etc/passwd").call(os.Path("/tmp"))
      //pprint.pprintln(content)

      val parsePasswordFile = Task
        .create(
          "parse_password_file",
          scalaCmd[os.Path, zio.Chunk[linux.Passwd]](PasswdParser.apply)
        )
        .input(
          fileInput(passwdOutput)
        )

      val idExtractor = Task
        .create(
          "id_extractor",
          scriptFile(id_extractor)
        )
        .output(
          fileOutput(userInfoOutput)
        )

      val myPipeline =
        Pipeline
          .init(pwdExtractor)
          .andThen(
            parsePasswordFile
          )
          .andThen(
            idExtractor
          )

      pprint.pprintln(myPipeline)

      assert(1)(equalTo(1))
    },
    test("hello world") {

      import zio.ZIO
      import zio.Schedule

      val effect2 = ZIO.succeed(println("World"))
      val effect1 = ZIO.succeed(println("Hello"))

      val program = effect1 *> effect2

      val rt = zio.Runtime

      rt.default.unsafeRun(program)

      //println("Hello")

      assert(1)(equalTo(1))
    }
  )
