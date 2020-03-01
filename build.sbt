val PluginOrg = "jkugiya"
val PluginName = "sbt-scalajs-serverless-framework"
val PluginVersion = "0.1"

lazy val root =
  project.in(file("."))
      .settings(
        name := PluginName,
        organization := PluginOrg,
        version := PluginVersion,
        sbtPlugin := true,
        initialCommands in console := """import jkugiya.sbt._""",
        libraryDependencies ++=Seq(
          Defaults.sbtPluginExtra(
            "org.scala-js" %% "sbt-scalajs" % "1.0.0",
            "1.0",
            "2.12"
          )
        ),
        scriptedLaunchOpts ++=
          Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
      )
    .enablePlugins(ScriptedPlugin)
