package compiler

import scala.quoted._

import ast.Exp._

def compile(exp: ast.Exp, args: Expr[Map[String, Boolean]], symtab: Map[String, Expr[Boolean]] = Map.empty)(using Quotes): Expr[Boolean] = 
  exp match {
    case Bool(value) => Expr(value)

    case Not(exp)  => '{ !(${compile(exp, args, symtab)}) }
    case And(x, y) => '{ (${compile(x, args, symtab)}) && (${compile(y, args, symtab)}) }
    case Or(x, y)  => '{ (${compile(x, args, symtab)}) || (${compile(y, args, symtab)}) }

    case Let(name, value, in) =>
      '{ val x = ${compile(value, args, symtab)}; ${compile(in, args, symtab + (name -> 'x))} }
    case Val(name) =>
      symtab.getOrElse(name, '{ ${args}.apply(${Expr(name)}) })
  }

