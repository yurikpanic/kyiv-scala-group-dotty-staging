package ast

enum Exp:
  case Not(x: Exp)
  case And(x: Exp, y: Exp)
  case Or(x: Exp, y: Exp)

  case Bool(x: Boolean)

  case Let(name: String, x: Exp, in: Exp)
  case Val(name: String)
