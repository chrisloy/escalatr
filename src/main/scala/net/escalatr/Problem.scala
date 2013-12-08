package net.escalatr

object Problem {

  def apply(i: Int) = problems(i)

  private val problems = Vector(
    "? == 1 + 2 + 3",

    """val x = 1 + 2
      |6 == x + ?
    """.stripMargin
  )

}
