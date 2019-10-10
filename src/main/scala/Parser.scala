
import scala.util.parsing.combinator._

import scala.language.implicitConversions

object Parser extends RegexParsers
  import Exp._

  def boolLit: Parser[Exp] = "true" ~> success(Bool(true)) | "false" ~> success(Bool(false))

  def valRef: Parser[Exp] = "[a-z]+".r ^^ Val.apply

  def exp: Parser[Exp] = 
    "let" ~ "[a-z]+".r ~ "=" ~ exp ~ "in" ~ exp ^^ { case _ ~ name ~ _ ~ e ~ _ ~ body => Let(name, e, body) } |
    ("(" ~> exp <~ ")" |
      "!" ~ exp ^^ { case _ ~ e => Not(e) } |
      boolLit | 
      valRef
    ).flatMap { e =>
      binaryOp(e)
    }
  
  def binaryOp(e: Exp): Parser[Exp] = 
    "&&" ~> exp ^^ { case x => And(e, x) } |
    "||" ~> exp ^^ { case x => Or(e, x) } |
    success(e)
