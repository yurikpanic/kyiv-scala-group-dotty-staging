// Import Expr and some extension methods
import scala.quoted._
import scala.quoted.staging.{run, withQuoteContext, Toolbox}

object Main 

  // Needed to run or show quotes
  given Toolbox = Toolbox.make(getClass.getClassLoader)

  def main(args: Array[String]): Unit = 
    import Exp._
    import Compiler._
    import Interoreter._

    // let y = true && z in let x = (true || false) && (true && false && true) in y && !x
    val expDefault = 
      Let("y",
        And(Bool(true), Val("z")),
        Let("x", 
          And(Or(Bool(true), Bool(false)), And(Bool(true), And(Bool(false), Bool(true)))),
          And(Val("y"), Not(Val("x")))))

    val exp = 
      if (args.length >= 1)
        val res = Parser.parse(Parser.exp, args(0)).get
        println(s"=== parsed: $res")
        res
      else
        expDefault

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

    println("=== interpret")
    println(eval(exp, Map("z" -> false)))
    println(eval(exp, Map("z" -> true)))

    val repCount = 1000000

    val params = Map("z" -> true)
    
    val startCompiled = System.currentTimeMillis
    (1 to repCount).foreach(_ => result(params)) 
    val timeCompiled = System.currentTimeMillis - startCompiled
    println(s"== compiled: ${timeCompiled}")

    val startInterpreted = System.currentTimeMillis
    (1 to repCount).foreach(_ => eval(exp, params)) 
    val timeInterpreted = System.currentTimeMillis - startInterpreted
    println(s"== interpreted: ${timeInterpreted}")

    println(s"== speedup: ${timeInterpreted.toDouble / timeCompiled}")



