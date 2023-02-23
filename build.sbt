ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "ru.misis"
ThisBuild / organizationName := "misis"

val akkaVersion = "2.6.18"
val akkaHttpVersion = "10.2.7"
val akkaHttpJsonVersion = "1.39.2"
val circeVersion = "0.14.1"
val slickVersion = "3.4.1"
val postgresVersion = "42.5.0"
val logbackVersion = "1.2.3"

val commonDependenciesSeq = Seq(
    // JSON
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    // HTTP / REST API
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "de.heikoseeberger" %% "akka-http-circe" % akkaHttpJsonVersion,
    // Работа с БД
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "org.postgresql" % "postgresql" % postgresVersion,
    // Логирование
    "ch.qos.logback" % "logback-classic" % logbackVersion
)

// lazy val root = (project in file("."))
//     .aggregate(service1, service2)
//     .settings(
//         name := "scala-money"
//     )

lazy val service1 = (project in file("service1"))
    .settings(
        name := "scala-money-1",
        Compile / run / mainClass := Some("money.MoneyDbApp"),
        libraryDependencies ++= commonDependenciesSeq
    )

lazy val service2 = (project in file("service2"))
    .settings(
        name := "scala-money-2",
        Compile / run / mainClass := Some("money.MoneyDbApp"),
        libraryDependencies ++= commonDependenciesSeq
    )
