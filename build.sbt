ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "calendar"
  )
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.10"
libraryDependencies += "org.scalafx" %% "scalafx" % "18.0.1-R28"