package jkugiya.sbt

import java.nio.file.{ Files, Path, Paths, StandardCopyOption }

import sbt.{ Compile, Def, InputKey, _ }
import Keys._
import Def.Initialize
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import complete.DefaultParsers._
import org.scalajs.sbtplugin.ScalaJSPlugin
import ServerlessFrameworkKeys._

import scala.annotation.tailrec
import scala.sys.process.Process

object ServerlessFramework {


  def runServerless(key: InputKey[Unit]): Def.Initialize[InputTask[Unit]] =
    Def.inputTask {
      val task = new ServerlessTask(
        args = spaceDelimited("<arg>").parsed,
        lambdaHandler = lambdaHandler.value,
        projectBaseDirectory = (baseDirectory in key).value,
        projectName =(name in key).value,
        scalaBinaryVersion = (scalaBinaryVersion in key).value
      )
      task.run()
    }.dependsOn(Compile / fastOptJS)

  private class ServerlessTask(
      args: Seq[String],
      lambdaHandler: String,
      projectName: String,
      projectBaseDirectory: File,
      scalaBinaryVersion: String
  ) {
    private val buildDir = Paths.get(projectBaseDirectory.getAbsolutePath, "target", "lambda")
    private val sourceConfig =
      getConfig(args).map(_.toPath)
        .getOrElse(Paths.get(projectBaseDirectory.getAbsolutePath, "serverless.yml"))
    private val configDestination =
      Paths.get(projectBaseDirectory.getAbsolutePath, "target", "lambda", "serverless.yml")
    private val options = commandOptions(args)
    private val jsName = s"${projectName}-fastopt.js"

    private def copyConfig(): Unit = {
      if (!buildDir.toFile.exists()) Files.createDirectories(buildDir)
      Files.copy(sourceConfig, configDestination, StandardCopyOption.REPLACE_EXISTING)
    }
    private def copyJS(): Unit = {
      Files.copy(
        projectBaseDirectory.toPath
          .resolve("target")
          .resolve( s"scala-${scalaBinaryVersion}/${jsName}"),
        buildDir.resolve(jsName),
        StandardCopyOption.REPLACE_EXISTING)
      Files.copy(
        projectBaseDirectory.toPath
          .resolve(lambdaHandler),
        buildDir.resolve(lambdaHandler),
        StandardCopyOption.REPLACE_EXISTING)
    }

    private def initialize(): Unit = {
      copyConfig()
      copyJS()
    }

    def run(): Unit = {
      initialize()
      runServerless
    }

    @tailrec
    private def commandOptions(args: Seq[String], options: List[String] = Nil): List[String] =
      args match {
        case Nil => options.reverse
        case "-c" +: path +: tail =>
          commandOptions(tail, options)
        case "--config" +: path +: tail =>
          commandOptions(tail, options)
        case head +: tail =>
          commandOptions(tail, head :: options)
      }

   @tailrec
    private def getConfig(args: Seq[String]): Option[File] =
      args match {
        case Nil => None
        case "-c" +: path +: tail =>
          Some(new File(path))
        case "--config" +: path +: tail =>
          Some(new File(path))
        case _ +: tail =>
          getConfig(tail)
      }
    private[this] def runServerless(): Unit = {
      val command =
        sys.props.get("os.name").collect {
          case osName if osName.toLowerCase.contains("win") =>
            "serverless.cmd"
        }.getOrElse("serverless")
      val terminalCommand = command +: options
      val process = Process(terminalCommand, buildDir.toFile)
      val exitCode: Int = process.!
      if (exitCode != 0)  {
        sys.error(s"Failed to execute serverless command.(${terminalCommand.mkString(" ")})")
      }
    }
  }
}
