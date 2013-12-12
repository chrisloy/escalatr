package net.escalatr

import spray.json._

case class Problem(id: Int, source: Seq[String], title: String, description: String, disallowedTerms: Seq[String]) {

  def replaceAll(target: String, replacement: String) = source.head replaceAll (target, replacement)
  def replace(target: String, replacement: String) = source.head replace (target, replacement)
}

object ProblemJsonProtocol extends DefaultJsonProtocol {

  implicit val problemFormat = jsonFormat5(Problem.apply)
}

object Problem {

  import ProblemJsonProtocol._

  val fileName = "/net/escalatr/problems.json"

  val problems: List[Problem] = {
    val source = io.Source.fromInputStream(getClass.getResourceAsStream(fileName)).mkString.asJson
    source.convertTo[List[Problem]]
  }

  def apply(i: Int) = get(i).get

  def get(i: Int) = problems find (_.id == i)
}
