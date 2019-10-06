// Import Expr and some extension methods
import scala.quoted._
import scala.quoted.staging.{run, withQuoteContext, Toolbox}

object Main 

  // Needed to run or show quotes
  given Toolbox = Toolbox.make(getClass.getClassLoader)

  def main(args: Array[String]): Unit = 
    import Exp._
    import Compiler._

    val exp = And(Or(Bool(true), Bool(false)), And(Bool(true), And(Bool(false), Bool(true))))

    println("=== compiling")
    val cc = withQuoteContext(compile(exp))
    println("=== code")
    println(withQuoteContext(cc.show))

    println("=== result")
    val result = run(cc)
    println(result)


