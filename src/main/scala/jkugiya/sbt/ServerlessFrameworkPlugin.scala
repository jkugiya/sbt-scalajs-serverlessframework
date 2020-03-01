package jkugiya.sbt

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin

object ServerlessFrameworkPlugin extends AutoPlugin {

  override def trigger = allRequirements
  override def requires = ScalaJSPlugin

  object autoImport extends ServerlessFrameworkKeys {
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    lambdaHandler := "handler.js",
    serverless := ServerlessFramework.runServerless(serverless).evaluated
  )

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()
}
