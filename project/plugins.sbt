logLevel := Level.Warn

// Intellij plugin
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

// Eclipse plugin
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")

// Check dependencies updates
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.6")

// Scalastyle
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.6.0")
