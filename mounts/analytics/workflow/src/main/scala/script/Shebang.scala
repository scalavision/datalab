package script

enum Shebang:
  self =>
  case Bash
  case Python
  case Perl
  case Nix
  case Other(value: String)

  private def ^(value: String) =
    s"#!/usr/bin/env $value"

  override def toString() = self match
    case Bash         => ^("bash")
    case Python       => ^("python")
    case Perl         => ^("perl")
    case Nix          => ^("nix")
    case Other(value) => ^(value)
