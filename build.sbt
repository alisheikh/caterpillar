name := "caterpillar"

organization := "com.chimpler"

version := "0.0.1"

scalaVersion := "2.11.4"

val akkaVersion = "2.3.7"

libraryDependencies ++= Seq(
  // Akka
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  // HTTP dispatch
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  // HTML parser
  "org.jodd" % "jodd-lagarto" % "3.6.2",
  // Kafka
  "com.sclasen" %% "akka-kafka" % "0.0.9-0.8.2-beta-SNAPSHOT" % "compile",
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.slf4j" % "log4j-over-slf4j" % "1.7.7" % "compile",
  // Joda
  "com.github.nscala-time" %% "nscala-time" % "1.0.0",
  // for serialization of case classes
  "com.novus" %% "salat" % "1.9.9",
  // MongoDB
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
  // Logger
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  // For Bloom filters
  "com.google.guava" % "guava" % "18.0",
  // Off Heap / disk
  "org.mapdb" % "mapdb" % "1.0.6",
  // Dependency injection
  "org.scaldi" %% "scaldi-akka" % "0.3.2",
  // URL normalizer
  "io.mola.galimatias" % "galimatias" % "0.2.0"
)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

// Local Maven Repository
resolvers += Resolver.mavenLocal
