ThisBuild / scalaVersion := "2.13.2"
ThisBuild / organization := "de.martinpallmann.familybot"
ThisBuild / organizationName := "Martin Pallmann"

lazy val root = (project in file("."))
  .settings(
    name := "FamilyBot",
    libraryDependencies ++= Seq(
      "de.martinpallmann.gchat" %% "gchat-bot" % "0.0.21",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.tpolecat" %% "doobie-core" % "0.8.8",
      "io.monix" %% "minitest" % "2.8.2" % Test,
    ),
    testFrameworks += new TestFramework("minitest.runner.Framework"),
    sbt.Keys.mainClass in Compile := Some("familybot.Main"),
    exportJars := true
  )
  .enablePlugins(JavaAppPackaging)
