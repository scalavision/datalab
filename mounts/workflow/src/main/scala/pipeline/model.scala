package pipeline

enum GlobalVariable:
  case Text(value: String)
  case Decimal(value: Float)
  case Integer(value: Int)
  case File(value: String)

/** workflow component is a required top-level component of a WDL script. It
  * contains call statements that invoke task components, as well as
  * workflow-level input definitions. There are various options for chaining
  * tasks together through call and other statements; these are all detailed in
  * the Plumbing Options documentation.
  *
  * @param calls
  */
final case class Workflow(
    input: Option[Workflow.Input],
    calls: Vector[Call],
    output: Option[Workflow.Output]
)

object Workflow:
  final case class Input()
  final case class Output()

/** The task component is a top-level component of WDL scripts. It contains all
  * the information necessary to "do something" centering around a command
  * accompanied by definitions of input files and parameters, as well as the
  * explicit identification of its output(s) in the output component. It can
  * also be given additional (optional) properties using the runtime, meta and
  * parameter_meta components. Tasks are "called" from within the workflow
  * command, which is what causes them to be executed when we run the script.
  * The same task can be run multiple times with different parameters within the
  * same workflow, which makes it very easy to reuse code. How this works in
  * practice is explained in detail in the Plumbing Options section.
  *
  * @param name
  * @param input
  */
final case class Task(
    name: TaskParam.Id,
    input: Option[TaskParam.Input] = None,
    command: TaskParam.Command,
    runtime: TaskParam.Runtime = TaskParam.Runtime(RuntimeType.Local),
    output: Option[TaskParam.Output] = None,
    variables: Vector[GlobalVariable] = Vector.empty,
    optionalVariables: Vector[TaskParam.OptionalVariable] = Vector.empty
) {

  def input(value: TaskParam.Input) =
    copy(
      input = Some(value)
    )

  def runtime(value: TaskParam.Runtime): Task = copy(
    runtime = value
  )

  def variables(value: Vector[GlobalVariable]): Task = copy(
    variables = value
  )

  def optionalVariables(value: Vector[TaskParam.OptionalVariable]): Task = copy(
    optionalVariables = value
  )
}

enum RuntimeType:
  case Local
  case Container(value: String)
  case Slurm(value: RuntimeType)
  case Kubernets(value: RuntimeType)

enum CommandType:
  case ScriptTemplate(text: String)
  case ScriptFile(path: String)
  case Dsl(value: String)

enum TaskParam:
  case Id(value: String)
  case Input()
  case Output()
  case Command(command: CommandType)
  case Runtime(runtime: RuntimeType)
  case OptionalVariable(
      value: Option[GlobalVariable],
      default: Option[GlobalVariable]
  )

object Task:

  def create(
      name: String,
      command: TaskParam.Command
  ): Task =
    Task(
      name = TaskParam.Id(name),
      command = command
    )

enum CallArg:
  case Output(task: TaskParam.Output)
  case Global(value: GlobalVariable)

final case class Call(
    args: Vector[CallArg],
    task: TaskParam.Id
)
