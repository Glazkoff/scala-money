import Dependencies._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "ru.misis"
ThisBuild / organizationName := "misis"

val akkaVersion = "2.6.18"
val akkaHttpVersion = "10.2.7"
val akkaHttpJsonVersion = "1.39.2"
val circeVersion = "0.14.1"

lazy val root = (project in file("."))
  .settings(
    name := "scala-money",
    Compile / run / mainClass := Some("money.MoneyHttpApp"),
    libraryDependencies ++= Seq(
      // 
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      // 
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "de.heikoseeberger" %% "akka-http-circe" % akkaHttpJsonVersion,
      // 
      "com.typesafe.slick" %% "slick" % "3.4.1",
      
      scalaTest % Test
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
