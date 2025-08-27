import sbt.Keys.libraryDependencies

ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "SentimentAnalysis",
    idePackagePrefix := Some("net.furitsch.sentiment"),
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql"   % "4.0.0",
      "org.apache.spark" %% "spark-mllib" % "4.0.0"
    )
  )