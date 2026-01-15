val scala3Version = "3.6.4"


lazy val root = project
  .in(file("."))
  .enablePlugins(CoverallsPlugin)
  .settings(
    name := "HeartsV",
    version := "0.1.0-SNAPSHOT",


    scalaVersion := scala3Version,
    coverageEnabled := true,
    fork in Test := true,
    Test / javaOptions += "-Djava.awt.headless=true",

    Compile / mainClass := Some("de.htwg.se.Hearts.Main"),

    scalacOptions ++= Seq("-deprecation", "-feature"),

    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.19",
      //"org.scala-lang.modules" %% "scala-swing" % "3.6.4",
      "org.scalafx" %% "scalafx" % "21.0.0-R32",
      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
      "net.codingwell" %% "scala-guice" % "7.0.0",
      "com.google.inject" % "guice" % "7.0.0"
    )
  )
