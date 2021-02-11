package parse

import scala.util.chaining._
import scala.util.Try

import cats.implicits._
import cats.parse.{Parser0 => P0, Parser => P, Numbers}

val expr: P[ast.Exp] = P.recursive[ast.Exp] { recur =>

  import ast.Exp._

  val whitespace = P.charIn(" \t\r\n").void
  val sp = whitespace.rep(1).void

  val symbol = P.charIn('a' to 'z').rep(1).string

  val bool = (P.string("true").as(Bool(true)) | P.string("false").as(Bool(false))) <* sp.?

  val valRef = symbol.map(Val(_)) <* sp.?

  val let = for {
    letName <- P.string("let").void *> sp *> symbol <* sp.? <* P.string("=") <* sp.?
    letValue <- recur <* P.string("in") <* sp.?
    in <- recur
  } yield Let(letName, letValue, in)

  val paren = P.char('(') *> sp.? *> recur <* P.char(')') <* sp.?

  val notExp = (P.char('!') *> recur <* sp.?).map(Not(_))

  val exp = (let | paren | notExp | bool | valRef).flatMap { left =>
    (P.string("&&") *> sp *> recur).map(And(left, _)) |
      (P.string("||") *> sp *> recur).map(Or(left, _)) |
      P.pure(left)
  }

  sp *> exp | exp
}

