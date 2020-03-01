
lazy val root =
  project.in(file("."))
    .settings(
      name := "example",
      organization := "jkugiya",
      version := "1.0",
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    )
    .enablePlugins(ScalaJSPlugin, ServerlessFrameworkPlugin)
