import sbt.Keys._
import sbt._

object BuildConfig {
  object Dependencies {
    val testDeps = Seq(
      "org.scalatest" %% "scalatest" % versions.scalatest,
      "org.mockito" % "mockito-all" % versions.mockito
    ).map(_ % "test")
  }

  object Revision {
    lazy val revision = System.getProperty("revision", "SNAPSHOT")
  }

  object versions {
    val mockito = "1.10.19"
    val scalatest = "3.0.1"
  }

  def commonSettings(currentVersion: String) = {
    Seq(
      organization := "io.paradox",

      version := s"${currentVersion}-${BuildConfig.Revision.revision}",

      credentials += Credentials(Path.userHome / ".sbt" / "credentials"),

      scalaVersion := "2.12.4",

      crossScalaVersions := Seq("2.11.8", scalaVersion.value),

      scalacOptions ++= Seq(
        "-deprecation",
        "-encoding", "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-language:postfixOps",
        "-language:experimental.macros",
        "-unchecked",
        "-Ywarn-nullary-unit",
        "-Xfatal-warnings",
        "-Ywarn-dead-code",
        "-Xfuture"
      ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12)) => Seq("-Xlint:-unused")
        case _ => Seq("-Xlint")
      }),

      scalacOptions in doc := scalacOptions.value.filterNot(_ == "-Xfatal-warnings")
    )
  }
}
