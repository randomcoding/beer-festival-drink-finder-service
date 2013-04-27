import sbt._
import sbt.Keys._

import com.typesafe.sbteclipse.core.EclipsePlugin._

object ProjectBuild extends Build {
  import BuildSettings._
  import Dependencies._
  import com.github.siasia.WebPlugin.webSettings

  lazy val root = Project("root",
    file("."),
    settings = buildSettings ++ Unidoc.settings ++ Seq (
      scalacOptions in doc := Seq(),
      // Disable publish and publish-local for empty root project
      publish := {},
      publishLocal := {}
    )
  ) aggregate(coreProject, liftProject, loggingProject)

  lazy val coreProject: Project = Project("core",
    file("core"),
      settings = buildSettings ++ Seq(libraryDependencies ++= coreProjectDeps,
      name := "DrinkServiceCore"
    )
  ) dependsOn (loggingProject)

  lazy val liftProject: Project = Project("lift",
    file("lift"),
    settings = buildSettings ++ Seq(libraryDependencies ++= liftProjectDeps,
      name := "DrinkServiceLift"
    ) ++ webSettings
  ) dependsOn(coreProject)

  lazy val loggingProject: Project = Project("logging",
    file("logging"),
    settings = buildSettings ++ Seq(libraryDependencies ++= loggingDeps,
      name := "DrinkServiceLogging"
    )
  )

  val commonDeps = testDeps ++ jodaDeps ++ utilsDeps
  val coreProjectDeps = commonDeps ++ Seq(liftMongoRecord, poi)
  val liftProjectDeps = commonDeps ++ liftDeps ++ jettyDeps
}

