package net.escalatr.interpreter

import com.twitter.util.Eval

class Interpreter {

  val eval = new Eval

  def apply(code: String): Any = {
    eval.apply(code)
  }
}
