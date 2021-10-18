package bash

implicit class CommandSyntax(sc: StringContext):
  def grep(args: Any*): CommandList = bash.cmd(s"grep ${sc.s(args: _*)}")
  def find(args: Any*): CommandList = bash.cmd(s"grep ${sc.s(args: _*)}")
  def ls(args: Any*): CommandList = bash.cmd(s"grep ${sc.s(args: _*)}")
  def cat(args: Any*): CommandList = bash.cmd(s"grep ${sc.s(args: _*)}")
  def rsync(args: Any*): CommandList = bash.cmd(s"grep ${sc.s(args: _*)}")
  def sed(args: Any*): CommandList = bash.cmd(s"grep ${sc.s(args: _*)}")
  def awk(args: Any*): CommandList = bash.cmd(s"grep ${sc.s(args: _*)}")
  def exec(args: Any*): CommandList = bash.cmd(s"grep ${sc.s(args: _*)}")
