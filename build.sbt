
name := "MultiPoints"
version := "1.4.2"
organization := "net.pointsgame"

scalaVersion := "2.11.4"
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

mainClass in Compile := Some("ru.narod.vn91.pointsop.gui.SelfishGuiStarter")
// Revolver.enableDebugging(port = 5005, suspend = false)
Revolver.settings.settings
fork in Test := true

crossPaths := false // pure java project (except testing)
autoScalaLibrary := false // pure java project (except testing)
compileOrder in Compile := CompileOrder.JavaThenScala

packageOptions in(Compile, packageBin) += Package.ManifestAttributes(
	"Permissions" -> "all-permissions",
	"Codebase" -> "*",
	"Application-Name" -> name.value
)

assemblyJarName in assembly := "PointsOnPaper.jar"
assembly <<= assembly dependsOn (test in Test)

val createSignedJar = TaskKey[Unit]("createSignedJar")
createSignedJar := {
	"jarsigner -keystore project/vasya.ks -storepass:env pass target/PointsOnPaper.jar mp".run()
}
createSignedJar <<= createSignedJar dependsOn assembly

assemblyMergeStrategy in assembly := {
	case PathList("javax", "jnlp", _*) => MergeStrategy.discard
	case PathList("com", "sun", _*) => MergeStrategy.discard
	case PathList("build.id", _*) => MergeStrategy.discard
	case PathList("META-INF", _*) => MergeStrategy.discard
	case x => MergeStrategy.deduplicate
}

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.3" % Test
