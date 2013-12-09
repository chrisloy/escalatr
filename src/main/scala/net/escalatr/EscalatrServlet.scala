package net.escalatr

import org.scalatra._
import scalate.ScalateSupport
import net.escalatr.interpreter.Interpreter
import scala.util.{Failure, Success, Try}
import scala.xml.{XML, Elem, NodeBuffer, Node}

class EscalatrServlet extends EscalatrStack {

  val interpret = new Interpreter

  get("/") {
    template(scratchPad(""))
  }

  get("/:id") {
    val id = params("id").toInt
    val problem = Problem(id)
    template(
      XML loadString s"""<div id="problem">
        ${problem replaceAll ("\n", "<br/>") replace ("?", """<span class="wildcard">?</span>""")}
      </div>""",
      scratchPad(""),
      form(("id", id.toString))
    )
  }

  post("/eval") {
    val code = params("code")
    val exec = params.get("id") match {
      case Some(x) => Problem(x.toInt) replace ("?", s"{\n$code\n}")
      case None => code
    }
    val result = Try(interpret(exec).toString)
    val (message, to) = result match {
      case Success(output) => (output, "... compiled to ...")
      case Failure(exception) => (exception.getMessage, "... didn't compile due to ...")
    }
    template(
      scratchPad(exec),
      form(),
      <p>{to}</p>,
      <div id="output"><p>{message}</p></div>
    )
  }

  private val head = {
    <head>
      <link rel="stylesheet" type="text/css" href="/css/styles.css"/>
      <script src="/js/google-code-prettify/run_prettify.js"></script>
      <title>escalatr</title>
    </head>
  }

  private def scratchPad(text: String) = {
    <textarea rows="10" cols="50" name="code" form="usrform">{text}</textarea>
  }

  private def form(hiddenParams: (String, String)*) = {
    <form action="eval" id="usrform" method="post">
      <div id="submit"><input type="submit" value="Do it!"/></div>
      {
        for ((name, value) <- hiddenParams ) yield {
          XML loadString s"""<input type="hidden" name="$name" value="$value"/>"""
        }
      }
    </form>
  }

  private def template(body: xml.Node*): Node = {
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
}
