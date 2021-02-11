package mcompiler

import scala.quoted._

inline def compile(inline exp: ast.Exp, inline symtab: Map[String, Boolean]): Boolean = ${ compileImpl('exp, 'symtab) }

def compileImpl(exp: Expr[ast.Exp], symtab: Expr[Map[String, Boolean]])(using Quotes): Expr[Boolean] = {
  import quotes.reflect._
  import ast.Exp._

  exp match {
    case '{ Bool($x) }      => x

    case '{ Not($x) }       => '{ ! ${ compileImpl(x, symtab) } }
    case '{ And($x, $y) }   => '{ ${ compileImpl(x, symtab) } && ${ compileImpl(y, symtab) } }
    case '{ Or($x, $y) }    => '{ ${ compileImpl(x, symtab) } || ${ compileImpl(y, symtab) } }

    case '{ Let($name, $value, $in) } =>
      '{ val x = ${ compileImpl(value, symtab) }; ${ compileImpl(in, '{ $symtab + ($name -> x) }) } }

    case '{ Val($name) } => '{ ${ symtab }($name) }
  }
}

