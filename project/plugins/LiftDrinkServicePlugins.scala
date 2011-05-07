import sbt._

class LiftDrinkServicePlugins(info : ProjectInfo) extends PluginDefinition(info) {
  lazy val sbteclipsify = "de.element34" % "sbt-eclipsify" % "0.7.0"

}
