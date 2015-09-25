name := "chirper"

version := "1.0"

scalaVersion := "2.11.6"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.mongodb" % "mongodb-driver" % "3.0.2"
)

libraryDependencies ++= Seq(javaWs)