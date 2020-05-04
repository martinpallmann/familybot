ThisBuild / scalaVersion := "2.13.2"
ThisBuild / organization := "de.martinpallmann.familybot"
ThisBuild / organizationName := "Martin Pallmann"

lazy val root = (project in file("."))
  .settings(
    name := "FamilyBot",
    libraryDependencies ++= Seq(
      "de.martinpallmann.gchat" %% "gchat-bot" % "0.0.17",
      "com.google.api-client" % "google-api-client" % "1.30.9",
      "com.google.http-client" % "google-http-client-jackson" % "1.29.2",
    ),
    sbt.Keys.mainClass in Compile := Some("familybot.Main"),
    exportJars := true
  )
  .enablePlugins(JavaAppPackaging)

def http4sVersion = "0.21.2"
