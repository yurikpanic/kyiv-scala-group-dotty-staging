import scala.quoted._

object Compiler 
  import Exp._

  def compile(exp: Exp, symtab: Map[String, Expr[Boolean]] = Map.empty)(given QuoteContext): Expr[Boolean] = exp match 
    case Bool(value) => Expr(value)

    case Not(exp)  => '{ !(${compile(exp, symtab)}) }
    case And(x, y) => '{ (${compile(x, symtab)}) && (${compile(y, symtab)}) }
    case Or(x, y)  => '{ (${compile(x, symtab)}) || (${compile(y, symtab)}) }
  
    case Let(name, value, in) => 
      '{ val x = ${compile(value, symtab)}; ${compile(in, symtab + (name -> 'x))} }
    case Val(name) =>
      symtab(name)
      