import sbt._
import de.element34.sbteclipsify._

class LiftDrinkServiceProject(info: ProjectInfo) extends DefaultWebProject(info) with Eclipsify {
  val liftVersion = "2.3"

  // uncomment the following if you want to use the snapshot repo
  //  val scalatoolsSnapshot = ScalaToolsSnapshots

  // If you're using JRebel for Lift development, uncomment
  // this line
  // override def scanDirectories = Nil
	  
  //val scalaTest = "org.scalatest" % "scalatest" % "1.3"

  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test",
    "junit" % "junit" % "4.5" % "test",
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    "org.scala-tools.testing" %% "specs" % "1.6.6" % "test",
    "org.scalatest" % "scalatest" % "1.3" % "test"
  ) ++ super.libraryDependencies
}
