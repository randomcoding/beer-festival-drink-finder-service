import sbt._

/**
 * Declaration of dependencies, library versions etc.
 */
object Dependencies {
  // Common Versions for libraries
  val liftVersion = "2.4"

  // Functions to create dependencies
  val liftDep = (componentId: String, scope: String ) => "net.liftweb" %% componentId % liftVersion % scope
	
  // Actual dependencies
  // liftweb
  val liftUtil = liftDep("lift-util", "compile")
  val liftCommon = liftDep("lift-common", "compile")
  val liftWebkit = liftDep("lift-webkit", "compile")
  val liftMongoRecord = liftDep("lift-mongodb-record", "compile")

  // Rogue - used for Mongo DB Queries
  val rogue = "com.foursquare" %% "rogue" % "1.1.1" intransitive()

  // Joda time
  val jodaTime = "joda-time" % "joda-time" % "2.0"
  val jodaConvert = "org.joda" % "joda-convert" % "1.2"

  // jetty
  val jettyVersion = "8.0.3.v20111011"
  val jetty = "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "container"
  val logback = "ch.qos.logback" % "logback-classic" % "1.0.0"

  // Apache poi
  val poi = "org.apache.poi" % "poi-ooxml" % "3.7"

  // Casbah - only before migrate to MongoRecord
  val casbahCore = "com.mongodb.casbah" %% "casbah-core" % "2.1.5-1"
  val casbahQuery = "com.mongodb.casbah" %% "casbah-query" % "2.1.5-1"
  val casbahCommons = "com.mongodb.casbah" %% "casbah-commons" % "2.1.5-1"

  val scalatest = "org.scalatest" %% "scalatest" % "1.8" % "test"

  val groovy = "org.codehaus.groovy" % "groovy" % "2.0.0"

  // Dependency groups
  val testDeps = Seq(scalatest)
  val liftDeps = Seq(liftUtil, liftCommon, liftWebkit, liftMongoRecord, rogue)
  val loggingDeps = Seq(logback, groovy, liftCommon)
  val jettyDeps = Seq(jetty)
  val jodaDeps = Seq(jodaTime, jodaConvert)

  val oldCasbahDeps = Seq(casbahCore, casbahQuery, casbahCommons)
}

