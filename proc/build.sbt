import com.typesafe.sbt.packager.Keys._
import sbtdocker.Plugin.DockerKeys._
import sbtdocker.{Dockerfile, ImageName}

name := "processor"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.0.1"

libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.0.1"

libraryDependencies += "org.apache.spark" % "spark-catalyst_2.11" % "2.0.1"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.0.1"

libraryDependencies += "org.apache.kafka" % "kafka_2.11" % "0.8"

libraryDependencies += "com.spotify" % "docker-client" % "3.5.13"

libraryDependencies += "com.datastax.spark" % "spark-cassandra-connector_2.11" % "2.0.0-M3"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"

packageArchetype.java_application

sbtdocker.Plugin.dockerSettings

docker <<= docker.dependsOn(com.typesafe.sbt.packager.universal.Keys.stage.in(Compile))

dockerfile in docker <<= (name, stagingDirectory in Universal) map {
  case(appName, stageDir) =>
    val workingDir = s"/app/${appName}"
    new Dockerfile {
      //use java8 base box
      from("relateiq/oracle-java8")

      //upload native-packager staging directory files
      add(stageDir, "/app")
      //make files executable
      run("chmod", "+x", s"/app/bin/${appName}")
      run("chmod", "+x", s"/app/bin/${appName}.bat")
      //set working directory
      workDir("/app/")
      //entrypoint into our start script
      entryPointShell(s"bin/$appName", "$@")
    }
}

imageName in docker := {
  ImageName(
    namespace = Some("kovavol.com"),
    repository = name.value
  )
}

