import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys
import sbtassembly.Plugin._
import sbtassembly.Plugin.AssemblyKeys._
import spray.revolver.RevolverPlugin.Revolver

name := "PointsOnPaper"

version := "1.0"

scalaVersion := "2.11.1"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

EclipseKeys.withSource := true


Revolver.settings.settings

// Revolver.enableDebugging(port = 5005, suspend = false)

fork in Test := true


crossPaths := false // pure java project (except testing)

autoScalaLibrary := false // pure java project (except testing)

packageOptions in(Compile, packageBin) +=
		Package.ManifestAttributes("Permissions" -> "all-permissions", "Codebase" -> "*")

assemblySettings

jarName in assembly := "PointsOnPaper.jar"

val createSignedJar = TaskKey[Unit]("createSignedJar")

createSignedJar := {
	"jarsigner -keystore project/vasya.ks -storepass:env pass target/PointsOnPaper.jar mp".run()
}

createSignedJar <<= createSignedJar dependsOn assembly

assembly <<= assembly dependsOn (test in Test)


mergeStrategy in assembly := {
	case PathList("javax", "jnlp", _*) => MergeStrategy.discard
	case PathList("META-INF", _*) => MergeStrategy.discard
	case x => MergeStrategy.deduplicate
}


resolvers ++= Seq(
	"Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0" % "test"
