name := """sbt-scalajs-serverlessframework"""
organization := "jkugiya"
version := "0.1-SNAPSHOT"

sbtPlugin := true

// choose a test framework

// utest
//libraryDependencies += "com.lihaoyi" %% "utest" % "0.4.8" % "test"
//testFrameworks += new TestFramework("utest.runner.Framework")

// ScalaTest
//libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1" % "test"
//libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies ++= Seq(
)


initialCommands in console := """import jkugiya.sbt._"""

enablePlugins(ScriptedPlugin)
libraryDependencies ++=Seq(
  Defaults.sbtPluginExtra(
    "org.scala-js" %% "sbt-scalajs" % "1.0.0",
    "1.0",
    "2.12"
  )
)
// set up 'scripted; sbt plugin for testing sbt plugins
scriptedLaunchOpts ++=
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
