package net.escalatr

import org.scalatra._
import scalate.ScalateSupport
import net.escalatr.interpreter.Interpreter

class EscalatrServlet extends EscalatrStack {

  val interpret = new Interpreter

  get("/") {
    <html>
      <body>
        <h1>escalatr</h1>
        <form action="eval" id="usrform">
          <input type="submit"/>
          </form>
        <textarea rows="4" cols="50" name="code" form="usrform">
        </textarea>
      </body>
    </html>
  }

  get("/eval") {
    val code = params("code")
    val result = interpret(code).toString
    <html>
      <body>
        <h1>{code} => </h1>
        <h2>{result}</h2>
      </body>
    </html>
  }
}
