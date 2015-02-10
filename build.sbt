name := """play-slick-quickstart"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  ws,
  "org.webjars" %% "webjars-play" % "2.3.0",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc41"
)

fork in Test := false

seq(bintrayResolverSettings: _*)

libraryDependencies += "com.google.maps" % "google-maps-services" % "0.1.5"

libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "1.0.2"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)
