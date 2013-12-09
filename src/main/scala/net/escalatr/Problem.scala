package net.escalatr

object Problem {

  def apply(i: Int) = problems(i)

  private val problems = Vector(
    "? == 1 + 2 + 3",

    """val x = 1 + 2
      |6 == x + ?
    """.stripMargin,

    "List(1, 2, ?) == 1 :: 2 :: 3 :: Nil",

    "List(2, 4, 6) == (List(1, 2, 3) map (_ * ?))",

    """def sq(x: Int) = x * x
      |(List(1, 2, 3) map sq) == ?""".stripMargin,

    """def countDown(x: Int): List[Int] = ?
      |List(5, 4, 3, 2, 1) == countDown(5)""".stripMargin
  )

}
