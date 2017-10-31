import sbt._
import BuildConfig.Dependencies

lazy val commonSettings = BuildConfig.commonSettings(currentVersion = "1.0")

commonSettings

name := "markov"

libraryDependencies ++= Seq(
  // your dependencies
) ++ Dependencies.testDeps

lazy val showVersion = taskKey[Unit]("Show version")

showVersion := {
  println(version.value)
}

// custom alias to hook in any other custom commands
addCommandAlias("build", "; compile")
