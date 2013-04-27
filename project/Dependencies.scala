import sbt._

/**
 * Declaration of dependencies, library versions etc.
 */
object Dependencies {
  // Common Versions for libraries
  val liftVersion = "2.5-RC5"

  // Functions to create dependencies
  val liftDep = (componentId: String, scope: String ) => "net.liftweb" %% componentId % liftVersion % scope

  // Actual dependencies
  // liftweb
  val liftUtil = liftDep("lift-util", "compile")
  val liftCommon = liftDep("lift-common", "compile")
  val liftWebkit = liftDep("lift-webkit", "compile")
  val liftMongoRecord = liftDep("lift-mongodb-record", "compile")

  // Joda time
  val jodaTime = "joda-time" % "joda-time" % "2.0"
  val jodaConvert = "org.joda" % "joda-convert" % "1.2"

  // jetty
  val jettyVersion = "8.0.3.v20111011"
  val jetty = "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "container"
  val logback = "ch.qos.logback" % "logback-classic" % "1.0.0"

  // Apache poi
  val poi = "org.apache.poi" % "poi-ooxml" % "3.7"

  val scalatest = "org.scalatest" %% "scalatest" % "2.0.M5b" % "test"

  val groovy = "org.codehaus.groovy" % "groovy" % "2.0.0"

  // Common Utilities
  val utilsVersion = "0.3.0-SNAPSHOT"
  val coreUtils = "uk.co.randomcoding" %% "scala-utilities-core" % utilsVersion
  val liftUtils = "uk.co.randomcoding" %% "scala-utilities-lift" % utilsVersion % "compile->compile;test->test"

  // Dependency groups
  val testDeps = Seq(scalatest)
  val liftDeps = Seq(liftUtil, liftCommon, liftWebkit, liftMongoRecord)
  val loggingDeps = Seq(logback, groovy, liftCommon)
  val jettyDeps = Seq(jetty)
  val jodaDeps = Seq(jodaTime, jodaConvert)
  val utilsDeps = Seq(coreUtils, liftUtils)
}

