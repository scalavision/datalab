addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.7.0")
//libraryDependencies += "ch.epfl.scala" % "sbt-bloop" % "1.4.0-RC1+9-e94b10c3"
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.1.0")
// One of:
//   ~/.sbt/0.13/plugins/plugins.sbt
//   ~/.sbt/1.0/plugins/plugins.sbt
resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.4.10")
