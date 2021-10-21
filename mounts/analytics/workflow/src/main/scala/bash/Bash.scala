package bash

enum CommandList:
  self =>
  case Pipe(left: CommandList, right: CommandList)
  case And(left: CommandList, right: CommandList)
  case Or(left: CommandList, right: CommandList)
  case Semi(cl: CommandList)
  case Amper(cl: CommandList)
  case NewLine(cl: CommandList)
  case Command(value: String)

  def |(that: CommandList): CommandList = Pipe(self, that)
  def &&(that: CommandList): CommandList = And(self, that)
  def ||(that: CommandList): CommandList = Or(self, that)
  def `;` = Semi(self)
  def :: = NewLine(self)
  def & = Amper(self)

object CommandList:
  import CommandList.*

import CommandList.*
def cmd(value: String): Command = Command(value)

def grep = cmd("grep")
def find = cmd("find")
def ls = cmd("ls")
def cat = cmd("cat")
def rsync = cmd("rsync")
def sed = cmd("sed")
def awk = cmd("awk")
def exec = cmd("exec")
