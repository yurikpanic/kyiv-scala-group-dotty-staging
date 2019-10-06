
enum Exp 
  case Not(x: Exp)
  case And(x: Exp, y: Exp)
  case Or(x: Exp, y: Exp)

  case Bool(x: Boolean)
