
name := "MultiPoints"
version := "1.4.2"
organization := "net.pointsgame"

scalaVersion := "2.11.7"
javacOptions in (Compile, compile) ++= Seq("-source", "1.7", "-target", "1.7", "-g:none", "-Xlint:-serial")
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
