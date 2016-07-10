
name := "MultiPoints"
version := "1.4.2"
organization := "net.pointsgame"

scalaVersion := "2.11.7"
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
assembly <<= assembly dependsOn (test in Test)
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false) // pure java project

val createSignedJar = TaskKey[Unit]("createSignedJar")
createSignedJar := {
	"jarsigner -keystore project/vasya.ks -storepass:env multipoints_pass target/PointsOnPaper.jar mp".run()
}
createSignedJar <<= createSignedJar dependsOn assembly

assemblyExcludedJars in assembly += unmanagedBase.value / "javaws.jar"
//assemblyExcludedJars in assembly ++= unmanagedBase.value.listFiles().toSeq

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.3" % Test

lazy val root = project.in(file(".")).dependsOn(uri("git://github.com/vn971/pircbot-sbt#java-1.5-generics"))
