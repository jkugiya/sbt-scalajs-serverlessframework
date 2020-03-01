# sbt-scalajs-serverlessframework

Plugin for deploying Scala.js code as an AWS Lambda function.

## Usage

This plugin requires sbt 1.0.0+.

### Deploy
```shell script
$ sbt
sbt> serverless deploy
```

This plugin is just a thin wrapper of the serverless framework. Therefore, any arguments the serverless command accepts are available. (So, if you perform tasks other than Scala.js deployment, you can do it.)
After the Scala code is compiled into JavaScript, it is copied to the following directory along with the serverless framework configuration file.
This plugin just execute the serverless command in this directory after compiling Scala.js.

```shell script
.
|-- build.sbt
|-- handler.js
|-- src
|   `-- main
|       `-- scala
|           `-- Handler.scala
`-- target
    `-- lambda
        |-- example-fastopt.js
        |-- handler.js
        `-- serverless.yml

# target/lambda is the working directory for serverless command.
```

