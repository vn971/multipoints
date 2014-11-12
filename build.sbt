import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._
import spray.revolver.RevolverPlugin.Revolver

name := "MultiPoints"

version := "1.2"

organization := "net.pointsgame"

scalaVersion := "2.11.4"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

EclipseKeys.withSource := true


Revolver.settings.settings

// Revolver.enableDebugging(port = 5005, suspend = false)

fork in Test := true


crossPaths := false // pure java project (except testing)

autoScalaLibrary := false // pure java project (except testing)

packageOptions in(Compile, packageBin) += Package.ManifestAttributes(
	"Permissions" -> "all-permissions",
	"Codebase" -> "*",
	"Application-Name" -> name.value
)

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
	case PathList("com", "sun", _*) => MergeStrategy.discard
	case PathList("build.id", _*) => MergeStrategy.discard
	case PathList("META-INF", _*) => MergeStrategy.discard
	case x => MergeStrategy.deduplicate
}


resolvers ++= Seq(
	"Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % Test
