import LibraryDeps._

import sbt.Keys

addCommandAlias(
  "c",
  "~compile"
)

addCommandAlias(
  "r",
  "~run"
)

addCommandAlias(
  "t",
  "~test"
)

lazy val commonSettings = Seq(
  version := "0.1.0",
  scalaVersion := "3.1.0"
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "analytics",
    description := "DataScience and analytics suite"
  )
  .settings(
    commonSettings: _*
  )
  .enablePlugins(ScalaJSPlugin)
  .aggregate(bioJVM, bioJS, api, workflow)

lazy val bio = crossProject(JVMPlatform, JSPlatform)
  .in(file("./bioanalytics/bio"))
  .settings(
    commonSettings: _*
  )
  .settings(
    libraryDependencies ++= zioStack,
    testFrameworks ++= testFrameworkStack
  )

lazy val bioJVM = bio.jvm
lazy val bioJS = bio.js

lazy val api = project
  .in(file("./bioanalytics/api"))
  .settings(commonSettings: _*)
  .settings(
    name := "api",
    description := "Api of Bioinf tooling for JVM",
    version := "0.1.0"
  )
  .settings(
    commonSettings: _*
  )
  .settings(
    libraryDependencies ++= zioStack ++ lihaoyiStack,
    testFrameworks ++= testFrameworkStack
  )
  .dependsOn(bioJVM)

lazy val workflow = project
  .in(file("./workflow"))
  .settings(commonSettings: _*)
  .settings(
    name := "workflow",
    description := "Dsl for generating and running simple workflows in Scala",
    version := "0.1.0",
    libraryDependencies ++= zioStack ++ lihaoyiStack,
    testFrameworks ++= testFrameworkStack
  )
