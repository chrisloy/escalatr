package net.escalatr

import net.escalatr.interpreter.Interpreter
import scala.util.{Failure, Success, Try}
import scala.xml.{XML, Elem, NodeBuffer, Node}
import org.fusesource.scalate.mustache.MustacheParser

class EscalatrServlet extends EscalatrStack {

  val interpret = new Interpreter

  get("/") {
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/layouts/test.mustache", "body" -> Seq(scratchPad(""), form("eval")))
  }

  get("/problem/:id") {
    val id = params("id").toInt
    val problem = Problem(id)
    template(
      <h5>Problem {id}</h5>,
      XML loadString s"""<pre id="problem" class="prettyprint lang-scala">
        |${problem replaceAll ("\n", "<br/>") replace ("?", """<span class="wildcard">?</span>""")}
      |</pre>""".stripMargin,
      scratchPad(""),
      form(s"$id", ("id", id.toString))
    )
  }

  post("/problem/:id") {
    val code = params("code")
    val id = params("id").toInt
    val problem = Problem(id)
    val exec =  problem replace ("?", s"{$code}")
    val result = Try(interpret(exec).asInstanceOf[Boolean])
    val div = result match {
      case Success(true) => <div id="success">Success! Next: <a href={s"/problem/${id+1}"}>{id+1}</a></div>
      case Success(false) => <div id="failure">Nope! Try again :-)</div>
      case Failure(exception) => <div id="output"><p>{exception.getMessage}</p></div>
    }
    template(
      <h2>problem {id}</h2>,
      XML loadString s"""<pre id="problem" class="prettyprint lang-scala">
        |${problem replaceAll ("\n", "<br/>") replace ("?", """<span class="wildcard">?</span>""")}
      |</pre>""".stripMargin,
      div,
      scratchPad(code),
      form(s"$id", ("id", id.toString))
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
      scratchPad(code),
      form("eval"),
      <p>{to}</p>,
      <div id="output"><p>{message}</p></div>
    )
  }

  private val head = {
    <head>
      <link rel="stylesheet" type="text/css" href="/css/codemirror.css"/>
      <link rel="stylesheet" type="text/css" href="/css/styles.css"/>
      <script src="/js/google-code-prettify/run_prettify.js"></script>
      <script src="/js/codemirror.js"></script>
      <script src="/js/clike.js"></script>
      <title>escalatr</title>
    </head>
  }

  private def scratchPad(text: String) = {
    <textarea rows="10" cols="50" id="scratchpad" name="code" form="form">{text}</textarea>
  }

  private def form(action: String, hiddenParams: (String, String)*) = {
    <form action={action} id="form" method="post">
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
          <script>
            var editor = CodeMirror.fromTextArea(document.getElementById('scratchpad'), {{
              mode: 'text/x-scala'
            }});
          </script>
          <div class="footer">
            <a href="/problem/0">First problem</a>
          </div>
        </div>
      </body>
    </html>
  }
}
