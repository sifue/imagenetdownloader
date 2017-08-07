name := "imagenetdownloader"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.3" % Test,
  "com.squareup.okhttp3" % "okhttp" % "3.8.1",
  "com.typesafe" % "config" % "1.3.1"
)

mainClass in assembly := Some("org.soichiro.imagenetdownloader.Main")