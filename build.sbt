name := """play-slick-quickstart"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
ws,
  "org.webjars" %% "webjars-play" % "2.2.2",
  "com.typesafe.slick" %% "slick" % "2.1.0",
"com.typesafe.play" %% "play-slick" % "0.8.0"
)

fork in Test := false

addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.6")

lazy val root = (project in file(".")).enablePlugins(PlayScala)