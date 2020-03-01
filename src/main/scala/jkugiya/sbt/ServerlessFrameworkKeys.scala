package jkugiya.sbt

import sbt.settingKey
import sbt._

trait ServerlessFrameworkKeys {

  lazy val lambdaHandler =
    settingKey[String]("The name of lambda handler.")

  lazy val serverless =
    inputKey[Unit]("Run serverless")
}

object ServerlessFrameworkKeys extends ServerlessFrameworkKeys
