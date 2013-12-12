package net.escalatr.interpreter

import com.twitter.util.Eval

class Interpreter {

  val eval = new Eval

  def apply(code: String): Any = {
    eval.apply(validate(code))
  }

  private def validate(code: String): String = {
    invalidTerms foreach { term =>
      if (code contains term) throw new IllegalAccessException(sarcasm(term))
    }
    code
  }

  private val invalidTerms = List(
    "System", "Runtime", "println", "sys", "Thread", "Compiler"
  )

  private def sarcasm(term: String) = s"I'd prefer if you wrote code which didn't mention $term"
}
