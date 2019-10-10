// Import Expr and some extension methods
import scala.quoted._
import scala.quoted.staging.{run, withQuoteContext, Toolbox}

object Main 

  // Needed to run or show quotes
  given Toolbox = Toolbox.make(getClass.getClassLoader)

  def main(args: Array[String]): Unit = 
    import Exp._
    import Compiler._

    val exp = 
      Let("y",
        And(Bool(true), Val("z")),
        Let("x", 
          And(Or(Bool(true), Bool(false)), And(Bool(true), And(Bool(false), Bool(true)))),
          And(Val("y"), Not(Val("x")))))

    def compiled(exp: Exp)(given QuoteContext): Expr[Map[String, Boolean] => Boolean] =
      '{ (args: Map[String, Boolean]) => ${ compile(exp, 'args) } }

    println("=== compiling")
    val cc = withQuoteContext(compiled(exp))
    println("=== code")
    println(withQuoteContext(cc.show))

    println("=== result")
    val result = run(cc)
    println(result(Map("z" -> false)))
    println(result(Map("z" -> true)))


