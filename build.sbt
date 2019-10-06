val dottyVersion = "0.19.0-RC1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "kyiv-scala-group-dotty-staging",
    version := "0.1.0",

    scalaVersion := dottyVersion,

    libraryDependencies ++= Seq(
      "ch.epfl.lamp" %% "dotty-staging" % dottyVersion,
      "com.novocode" % "junit-interface" % "0.11" % "test"
    )
  )
