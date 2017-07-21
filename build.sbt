lazy val commonSettings = Seq(
  organization := "org.mskim",
  scalaVersion := "2.11.8",
  version := "0.0.3-SNAPSHOT",
  licenses += "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")
)

lazy val core = project
  .settings(commonSettings)
  .settings(Seq(
    libraryDependencies ++= Seq(
      "org.tensorflow" % "tensorflow" % "1.2.1",
      "com.github.fommil.netlib" % "all" % "1.1.2" pomOnly()
    )
  ))

lazy val rest = project
  .dependsOn(core)
  .settings(commonSettings)
  .settings(Seq(
    resolvers += "Twitter Maven" at "http://maven.twttr.com",
    libraryDependencies ++= Seq(
      "com.twitter" %% "finatra-http" % "2.11.0",
      "ch.qos.logback" % "logback-classic" % "1.1.7"
    ),
    excludeDependencies += "org.slf4j" % "slf4j-log4j12"
  ))

lazy val root = (project in file("."))
  .aggregate(core, rest)
  .dependsOn(core, rest)
  .settings(commonSettings)

