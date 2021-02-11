val scala3Version = "3.0.0-M3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "kyiv-scala-group-dotty-staging",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-staging" % scala3Version,
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "org.scala-lang.modules" % "scala-parser-combinators_2.13" % "1.1.2"
    )
  )
