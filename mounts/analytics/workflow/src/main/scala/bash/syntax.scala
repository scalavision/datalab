package bash

implicit class CommandSyntax(sc: StringContext):
  def grep(args: Any*): CommandList = bash.cmd(s"grep ${sc.s(args: _*)}")
  def find(args: Any*): CommandList = bash.cmd(s"find ${sc.s(args: _*)}")
  def ls(args: Any*): CommandList = bash.cmd(s"ls ${sc.s(args: _*)}")
  def cat(args: Any*): CommandList = bash.cmd(s"cat ${sc.s(args: _*)}")
  def rsync(args: Any*): CommandList = bash.cmd(s"rsync ${sc.s(args: _*)}")
  def sed(args: Any*): CommandList = bash.cmd(s"sed ${sc.s(args: _*)}")
  def awk(args: Any*): CommandList = bash.cmd(s"awk ${sc.s(args: _*)}")
  def exec(args: Any*): CommandList = bash.cmd(s"exec ${sc.s(args: _*)}")
