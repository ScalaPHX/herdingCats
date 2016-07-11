name := "herdingCats"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.typelevel" %% "cats" % "0.6.0"

// plugin for kind-projector
resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.8.0")