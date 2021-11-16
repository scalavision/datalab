package script

case class ScriptPath(value: String)
case class ScriptArgs(args: Vector[String])
case class Script(path: ScriptPath, args: ScriptArgs)
