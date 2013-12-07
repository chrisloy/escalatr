package net.escalatr

import org.scalatra._
import scalate.ScalateSupport
import net.escalatr.interpreter.Interpreter
import scala.util.{Failure, Success, Try}

class EscalatrServlet extends EscalatrStack {

  val interpret = new Interpreter

  get("/") {
    template(
      <textarea rows="10" cols="50" name="code" form="usrform"></textarea>
      <form action="eval" id="usrform" method="post">
        <div id="submit"><input type="submit" value="Do it!"/></div>
      </form>
    )
  }

  post("/eval") {
    val code = params("code")
    val result = Try(interpret(code).toString)
    val (message, to) = result match {
      case Success(output) => (output, "... compiled to ...")
      case Failure(exception) => (exception.getMessage, "... didn't compile due to ...")
    }
    template(
      <textarea rows="10" cols="50" name="code" form="usrform">{code}</textarea>
      <form action="eval" id="usrform" method="post">
        <div id="submit"><input type="submit" value="Do it!"/></div>
      </form>
      <p>{to}</p>
      <div id="output"><p>{message}</p></div>
    )
  }

  private val head =
    <head>
      <link rel="stylesheet" type="text/css" href="/css/styles.css"/>
      <title>escalatr</title>
    </head>

  private def template(body: xml.NodeBuffer) =
    <html>
      {head}
      <body>
        <h1><i>escalatr</i></h1>
        <p id="strap">A fun place where learning about Scala can happen through your eyes and fingers</p>
        <div id="content">
          {body}
          <div class="footer">About etc</div>
        </div>
      </body>
    </html>
}
