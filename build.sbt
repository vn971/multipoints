name := "MultiPoints"
version := "1.4.2"
organization := "net.pointsgame"

scalaVersion := "2.12.8"
javacOptions in (Compile, compile) ++= Seq("-source", "1.7", "-target", "1.7", "-Xlint", "-Xlint:-serial")
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

mainClass in Compile := Some("ru.narod.vn91.pointsop.gui.SelfishGuiStarter")
// Revolver.enableDebugging(port = 5005, suspend = false)
Revolver.settings.settings
fork in Test := true

packageOptions in(Compile, packageBin) += Package.ManifestAttributes(
	"Permissions" -> "all-permissions",
	"Codebase" -> "*",
	"Application-Name" -> name.value
)

crossPaths := false // no Scala suffix for published jar-s
assemblyJarName in assembly := "PointsOnPaper.jar"
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false) // pure java project
assemblyExcludedJars in assembly += unmanagedBase.value / "javaws.jar"
/* javaOptions in reStart ++= List(
	"-Dcom.sun.management.jmxremote.port=3333",
	"-Dcom.sun.management.jmxremote.ssl=false",
	"-Dcom.sun.management.jmxremote.authenticate=false",
	"-Djava.rmi.server.hostname=127.0.0.1"
) */

val createSignedJar = TaskKey[Unit]("createSignedJar")
createSignedJar := {
	import scala.sys.process._
	"jarsigner -keystore project/vasya.ks -storepass:env multipoints_pass target/PointsOnPaper.jar mp".!
}
createSignedJar := (createSignedJar dependsOn assembly).value

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test

lazy val root = project.in(file(".")).dependsOn(RootProject(uri("git://github.com/vn971/pircbot-sbt#java-1.5-generics")))
