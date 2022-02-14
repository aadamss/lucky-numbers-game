import Dependencies._

name := "onair-game"
organization := "com.onairentertainment"
version := "0.1"

scalaVersion := "2.13.6"

scalacOptions += "-Ymacro-annotations"

libraryDependencies ++= Circe.deps ++ Cats.deps ++ Seq(
  Specs.scalaTest % Test,
  Specs.scalamock,
  Common.config,
  Http4s.server,
  Http4s.dsl,
  Common.logback,
  Common.log4catsSl4j
)
