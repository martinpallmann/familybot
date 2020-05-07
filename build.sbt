ThisBuild / scalaVersion := "2.13.2"
ThisBuild / organization := "de.martinpallmann.familybot"
ThisBuild / organizationName := "Martin Pallmann"

val doobieVersion = "0.8.8"

lazy val root = (project in file("."))
  .settings(
    name := "FamilyBot",
    libraryDependencies ++= Seq(
      "de.martinpallmann.gchat" %% "gchat-bot" % "0.0.23",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres"  % doobieVersion,
      "org.postgresql" % "postgresql" % "42.2.12",
      "org.flywaydb" % "flyway-core" % "6.4.1",
      "io.monix" %% "minitest" % "2.8.2" % Test,
    ).map(_.withSources),
    testFrameworks += new TestFramework("minitest.runner.Framework"),
    sbt.Keys.mainClass in Compile := Some("familybot.Main"),
    exportJars := true
  )
  .enablePlugins(JavaAppPackaging)
