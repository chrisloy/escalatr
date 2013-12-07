package net.escalatr

import org.scalatra._

class Main extends ScalatraFilter {

  get("/") {
    <h1>Hello, world!</h1>
  }
}
