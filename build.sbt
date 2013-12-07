name := "escalatr"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % "2.2.1",
  "org.eclipse.jetty" % "jetty-webapp" % "9.0.4.v20130625",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016",
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "org.scalatra" %% "scalatra-specs2" % "2.2.1" % "test"
)
