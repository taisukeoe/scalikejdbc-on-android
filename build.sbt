import android.Keys._

lazy val root = (project in file(".")).settings(
name := "MyAndroidProject",
scalaVersion := "2.11.6",
libraryDependencies ++= Seq(
  "org.sqldroid" % "sqldroid" % "1.0.3",
  "org.scalikejdbc" %% "scalikejdbc" % "2.2.6"),
android.Plugin.androidBuild,
versionCode := Some(0),
versionName := Some("0.1"),
apkbuildExcludes in Android ++= Seq("META-INF/LICENSE.txt", "META-INF/NOTICE.txt"),
proguardCache in Android += ProguardCache("parser-combinators") % "org.scala-lang.modules" %% "scala-parser-combinators"
)