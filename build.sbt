name := "caterpillar"

version := "0.0.1"

scalaVersion := "2.10.4"

val akkaVersion = "2.3.6"

libraryDependencies ++= Seq(
  // Akka
  "com.sclasen" %% "akka-zk-cluster-seed" % "0.0.6",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  // HTTP dispatch
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  // HTML parser
  "org.jodd" % "jodd-lagarto" % "3.6.2",
  // Kafka
  "com.sclasen" %% "akka-kafka" % "0.0.9-SNAPSHOT" % "compile",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.2" % "compile",
  "org.slf4j" % "log4j-over-slf4j" % "1.6.6" % "compile",
  // Joda
  "com.github.nscala-time" %% "nscala-time" % "1.0.0",
  // for serialization of case class
  "com.novus" %% "salat" % "1.9.8",
  // SLF4J
  "org.clapper" %% "grizzled-slf4j" % "1.0.2"
)

// Local Maven Repository
resolvers += Resolver.mavenLocal
