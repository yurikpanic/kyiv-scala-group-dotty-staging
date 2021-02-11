
object Interpreter:
  import Exp._

  def eval(exp: Exp, args: Map[String, Boolean]): Boolean = exp match
    case Bool(x) => x

    case Not(smth) => !eval(smth, args)
    case And(x, y) => eval(x, args) && eval(y, args)
    case Or(x, y) => eval(x, args) || eval(y, args)

    case Let(name, value, in) =>
      eval(in, args + (name -> eval(value, args)))
    case Val(name) =>
      args(name)
