lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.13.3"
    )),
    name := "scalatest-example"
  )

// lazy val util = (project in file("util"))
//   .enablePlugins(FooPlugin, BarPlugin)
//   .settings(
//     name := "util"
//   )

// resolvers += Resolver.url("bintray-sbt-plugins", url("https://dl.bintray.com/sbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns) 
 
resolvers ++= Seq(
  "bintray-sbt-plugin-releases" at "https://dl.bintray.com/content/sbt/sbt-plugin-releases",
  "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
)

// enablePlugins(JavaAppPackaging)

// enablePlugins(JavaServerAppPackaging)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % Test
// https://mvnrepository.com/artifact/org.mongodb.scala/mongo-scala-driver
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.1.1"

