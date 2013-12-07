package net.escalatr

import org.scalatra._
import scalate.ScalateSupport

class EscalatrServlet extends EscalatrStack {

  get("/") {
    <html>
      <body>
        <h1>escalatr</h1>
        <ul>
          { 'a' to 'z' map (x => <li>{x}</li>)}
        </ul>
      </body>
    </html>
  }
}
