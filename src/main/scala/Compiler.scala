import scala.quoted._

object Compiler 
  import Exp._

  def compile(exp: Exp)(given QuoteContext): Expr[Boolean] = exp match 
    case Bool(value) => Expr(value)

    case Not(exp) => '{ !(${compile(exp)}) }
    case And(x, y) => '{ (${compile(x)}) && (${compile(y)}) }
    case Or(x, y) => '{ (${compile(x)}) || (${compile(y)}) }
  