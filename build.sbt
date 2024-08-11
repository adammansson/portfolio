val scala3Version = "3.4.2"
val http4sVersion = "0.23.27"

lazy val root = project
  .in(file("."))
  .settings(
    name := "portfolio",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-client" % http4sVersion,
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-scalatags" % "0.25.2",
      "com.lihaoyi" %% "scalatags" % "0.12.0",
      "io.circe" %% "circe-core" % "0.14.8",
      "io.circe" %% "circe-generic" % "0.14.8",
      "org.typelevel" %% "log4cats-slf4j" % "2.7.0",
      "ch.qos.logback" % "logback-classic" % "1.5.6" % Runtime,
      "org.tpolecat" %% "doobie-core" % "1.0.0-RC4",
      "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC4",
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test,
    )
  )
