package net.escalatr

import net.escalatr.interpreter.Interpreter
import scala.util.{Failure, Success, Try}
import scala.xml.{XML, Elem, NodeBuffer, Node}
import org.fusesource.scalate.mustache.MustacheParser

class EscalatrServlet extends EscalatrStack {

  val interpret = new Interpreter

  get("/") {
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/layouts/main.mustache",
      "action" -> "/",
      "scratchpadText" -> "")
  }

  post("/") {
    val code = params("code")
    val result = Try(interpret(code).toString)
    val (message, to) = result match {
      case Success(output) => (output, "... compiled to ...")
      case Failure(exception) => (exception.getMessage, "... didn't compile due to ...")
    }
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/layouts/main.mustache",
      "scratchpadText" -> code,
      "action" -> "/",
      "compilerMessage" -> to,
      "compilerOutput" -> message
    )
  }

  get("/problem/:id") {
    val id = params("id").toInt
    val problem = Problem(id)
    val problemHtml = problem replaceAll ("\n", "<br/>") replace ("?", "<span class=\"wildcard\">?</span>")
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/layouts/problem.mustache",
      "id" -> id,
      "problem" -> problemHtml,
      "scratchpadText" -> "",
      "action" -> id)
  }

  post("/problem/:id") {
    val code = params("code")
    val id = params("id").toInt
    val problem = Problem(id)
    val problemHtml = problem replaceAll ("\n", "<br/>") replace ("?", "<span class=\"wildcard\">?</span>")
    val exec =  problem replace ("?", s"{$code}")
    val result = Try(interpret(exec).asInstanceOf[Boolean])
    val div = result match {
      case Success(true) => <div id="success">Success! Next: <a href={s"/problem/${id+1}"}>{id+1}</a></div>
      case Success(false) => <div id="failure">Nope! Try again :-)</div>
      case Failure(exception) => <div id="output"><p>{exception.getMessage}</p></div>
    }
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/layouts/problem.mustache",
      "id" -> id,
      "problem" -> problemHtml,
      "scratchpad" -> code,
      "result" -> div,
      "action" -> id)
  }

}
