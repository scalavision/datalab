package wdl

import bash.CommandList
import java.io.File

/*
case class File()

enum Type[T]:
  self =>
  case Boolean(value: scala.Boolean) extends Type[scala.Boolean]
  case Int(value: scala.Int) extends Type[scala.Int]
  case Float(value: scala.Float) extends Type[scala.Float]
  case String(value: java.lang.String) extends Type[java.lang.String]
  case File(value: wdl.File) extends Type[wdl.File]
  case Array(values: zio.Chunk[T]) extends Type[T]
  case NonEmptyArray(a: Array[T]) extends Type[T]

extension [T](t: Type.Array[T])
  def + = Type.NonEmptyArray(t)
*/

enum CommandType:
  case ScriptTemplate(text: String)
  case ScriptFile(path: String)
  case Dsl(value: String)
  case BashScript(cmd: bash.CommandList)
  case Scala[A, B](fn: A => B)

// enum Input[T]:
//   self =>
//   case Value(t: T) extends Input[T]
//   case Optional(t: Input[T]) extends Input[T]
//   case Append(t1: T, t2: T) extends Input[T]
//   def ::[T2 >: T](input: Input[T2]) = Append(self, input)
//   def ? = Optional(self)

// enum Output[+T]:
//   self =>
//   case Value(t: T) extends Output[T]
//   case Optional(t: Output[T]) extends Output[T]
//   case Append(t1: T, t2: T) extends Output[T]
//   def ::[T2 >: T](input: Output[T2]) = Append(self, input)

enum Task[+In, +Out]:
  self =>
  case Name(value: String) extends Task[Nothing, String]
  case Input(t: In) extends Task[In, Nothing]
  case Command(command: CommandType) extends Task[In, CommandType]
  case Output(t: Out) extends Task[Nothing, Out]
  case Append(input: Task[In, Out], t2: Task[In, Out]) extends Task[In,Out]

  def input[T2 >: In]: T2 => Input[T2, Nothing] = t => Input(t)
  def output[T2 >: Out]: T2 => Output[Nothing, T2] = t => Output(t)


  def run[In2 >: In, Out2 >: Out](in: In2, out: Out2) = ???

  //def output[T2 >: Out]: T2 => TaskOutput[Nothing, T2] = t => TaskOutput(Output.Value(t))
  // def command[In1 <: In](cmd: CommandType)(implicit ev: self.type =:= TaskInput[In1, Nothing]) =
  //   Append(self.asInstanceOf[TaskInput[In, Nothing]], Command(cmd))
  // def command[In1 <: In](cmd: CommandType)(implicit ev: self.type =:= TaskInput[In1, Nothing]) =
  //   Append(self.asInstanceOf[TaskInput[In, Nothing]], Command(cmd))

  def bash(cmd: CommandList) = Command(CommandType.BashScript(cmd))

extension [Arg](in: Arg => Task.Input[Arg, Nothing])
  def output[Out]: Task[Arg, Out] = ???

def task(name: String) = Task.Name(name)
// def str(value: String) = Input.Value(value)
// def int(value: Int) = Input.Value(value)
// def float(value: Int) = Input.Value(value)
// def file(value: String) = Input.Value(value)
// def array[T](values: List[T]) = Input.Value(values)
// def map[X,Y](values: Map[X,Y]) = Input.Value(values)
// def tuple[X,Y](x: X, y: Y) = Input.Value((x,y))

// val myTask = 
//       task("hello")
//         .input(
//           str(pattern),
//           file(bamFile)    
//         ).bash(
//           ls"-halt" | grep"something"
//         )


object DslTest:
  import bash.*

  final case class Data(
    bamFile: File,
    pattern: String
  )

  final case class OutData(
    outFile: File
  )

  val myTask =
    task("hello world")
      .input[Data]
      .output[OutData]
      .run(
        Data(new java.io.File("/path/to/file.txt"), "myPattern"),
        OutData(new java.io.File("/path/to/output"))
      )




  
