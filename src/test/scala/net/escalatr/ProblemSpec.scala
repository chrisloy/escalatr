package net.escalatr

class ProblemSpec extends BaseSpec {

  "Problem" should "find the first problem" in {
    val problem = Problem.get(1)
    problem.isDefined should be (true)
    problem.get.id should be (1)
    problem.get.source should be (List("? == 1 + 2 + 3"))
  }
}
