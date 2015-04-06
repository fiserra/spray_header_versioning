name := "spray_header_versioning"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq("io.spray" %% "spray-can" % "1.3.3",
 "io.spray" %% "spray-routing" % "1.3.3",
"org.json4s" %% "json4s-native" % "3.2.11",
"com.typesafe.akka" %% "akka-actor" % "2.3.9")