package net.escalatr.interpreter

import net.escalatr.BaseSpec
import com.twitter.util.Eval.CompilerException

class InterpreterSpec extends BaseSpec {

  val interpret = new Interpreter

  "An interpreter" should "execute 1 + 1" in {
    interpret("1 + 1") should be (2)
  }

  it should "throw a compiler exception" in intercept[CompilerException]{
    interpret("1 + ")
    fail()
  }

  it should "execute a string" in {
    interpret(""" "hello there" """) should be ("hello there")
  }
}
