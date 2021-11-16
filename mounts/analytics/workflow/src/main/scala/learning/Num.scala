package learning

/*

Scala Highlights

Guest programming language, all focus is on creating
the best programming language possible. Compiled to
specified runtime.

- Great support for Functional programming
- 3 runtimes: jvm, javascript, llvm / C / native
- integrates with Python via scala.py similarily as with javascript

Scala is a compiled programming language, compared to Python
that is interpreted.

- Based around Records (named tuples) and Categories (enums of Records describing a category)
- Pattern Matching
- map, flatMap, filter (basic functions)
- traverse, reduce / foldLeft, fold (more advanced functions)

Advanced features:

- Very advanced typesystem
  - You get a long way using the simple stuff
  - Typeclass deriviation
- Macro programming
  - Happens at compile time, thus safe

 */

// Record
// CSV
/*

----------
|name|age|
----------
|John|42 |
----------
|Alice|42 |
----------
 */

// Record
// Product type
// name AND age
case class Person(name: String, age: Int)

val name = "John"
val age = 42

val person = Person(name, age)

// Enum
enum Color:
  case Blue
  case Red
  case Green

/** Enum is the same an OR It is a concept, (College), will be one of
  */
enum College:
  case Student(name: String, age: Int, learning: Set[Subject])
  case Subject(name: String)
  case Teacher(name: String, students: List[Student], teaches: Set[Subject])

//case class Add(v1: Cell, v2: Cell)
enum Cell:
  // base case
  case Value(value: Int)
  case Add(v1: Cell, v2: Cell)
  case Minus(c1: Cell, v2: Cell)

object Cell:
  import Cell.*

  def calcCell(cell: Cell): Int = cell match
    case Value(v) => v
    case Add(v1: Cell, v2: Cell) =>
      calcCell(v1) + calcCell(v2)
    case Minus(v1, v2) =>
      calcCell(v1) - calcCell(v2)

enum Num:
  self =>
  case Value(x: Int)
  case Add(x1: Num, x2: Num)
  case Subtract(x1: Num, x2: Num)
  case Negate(x: Num)

  def -(x: Num) = Subtract(self, x)
  def +(x: Num) = Add(self, x)
  def unary_- = Negate(self)

object Num:
  def lift(x: Int) = Num.Value(x)

def num(x: Int) = Num.Value(x)

import Num.*

def calc(n: Num): Int = n match
  case Value(x)                   => x
  case Add(x1: Num, x2: Num)      => calc(x1) + calc(x2)
  case Subtract(x1: Num, x2: Num) => calc(x1) - calc(x2)
  case Negate(x)                  => -calc(x)

def introspect(n: Num): String = n match {
  case Value(x)         => s"${x.toString()}"
  case Add(x1, x2)      => s"${introspect(x1)} + ${introspect(x2)}"
  case Subtract(x1, x2) => s"${introspect(x1)} - ${introspect(x2)}"
  case Negate(x)        => s"(-${introspect(x)})"
}
