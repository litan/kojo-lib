name := "kojo-lib"

organization := "net.kogics"

version := "0.1.0"

scalaVersion := "2.13.6"

scalacOptions := Seq("-feature", "-deprecation")
run / javaOptions ++= Seq("-Xmx1024m", "-Xss1m")

fork := true

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-s")

// parallelExecution in Test := false

autoScalaLibrary := false

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-library" % scalaVersion.value,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value % "test",
    "org.scala-lang.modules" % "scala-xml_2.13" % "1.3.0" % "test",
    "com.typesafe.akka" % "akka-actor_2.13" % "2.5.29",
    "org.piccolo2d" % "piccolo2d-core" % "1.3.1",
    "org.piccolo2d" % "piccolo2d-extras" % "1.3.1",
    "com.vividsolutions" % "jts" % "1.13" intransitive(),
    "org.apache.commons" % "commons-math3" % "3.6.1",
    "javax.xml.bind" % "jaxb-api" % "2.2",
    "com.sun.xml.bind" % "jaxb-impl" % "2.2",
    "org.scalatest" % "scalatest_2.13" % "3.0.8" % "test" intransitive(),
    "org.scalactic" % "scalactic_2.13" % "3.0.8" % "test" intransitive(),
    "junit" % "junit" % "4.10" % "test",
    "org.jmock" % "jmock" % "2.5.1" % "test",
    "org.jmock" % "jmock-legacy" % "2.5.1" % "test",
    ("org.jmock" % "jmock-junit4" % "2.5.1" intransitive()) % "test",
    "cglib" % "cglib-nodep" % "2.1_3" % "test",
    "org.objenesis" % "objenesis" % "1.0" % "test",
    "org.hamcrest" % "hamcrest-core" % "1.1" % "test",
    "org.hamcrest" % "hamcrest-library" % "1.1" % "test",
    ("org.scalacheck"  % "scalacheck_2.13" % "1.14.3" intransitive()) % "test"
)

//Build distribution
val distOutPath             = settingKey[File]("Where to copy all dependencies (except scala ones) and kojo")
val distOutScalaPath             = settingKey[File]("Where to copy scala dependencies")
val buildDist  = taskKey[Unit]("Copy runtime dependencies and built kojo to 'distOutpath'")

lazy val dist = project
  .in(file("."))
  .settings(
    distOutPath              := baseDirectory.value / "dist",
    distOutScalaPath         := baseDirectory.value / "dist-scala",
    buildDist   := {
      val allLibs:                List[File]          = (Runtime / dependencyClasspath).value.map(_.data).filter(f => f.isFile && !f.getName.startsWith("scala")).toList
      val scalaLibs:              List[File]          = (Runtime / dependencyClasspath).value.map(_.data).filter(f => f.isFile && f.getName.startsWith("scala")).toList
      val buildArtifact:          File                = Runtime / packageBin value
      val jars:                   List[File]          = buildArtifact :: allLibs
      val scalaJars:              List[File]          = scalaLibs
      val `mappings src->dest`:   List[(File, File)]  = jars.map(f => (f, distOutPath.value / f.getName))
      val `mappings-scala src->dest`: List[(File, File)]  = scalaJars.map(f => (f, distOutScalaPath.value / f.getName))
      val log                                         = streams.value.log
      log.info(s"Copying jars to ${distOutPath.value}:")
      log.info(s"${`mappings src->dest`.map(f => s" * ${f._1}").mkString("\n")}")
      IO.copy(`mappings src->dest`)
      log.info(s"Copying Scala jars to ${distOutScalaPath.value}:")
      log.info(s"${`mappings-scala src->dest`.map(f => s" * ${f._1}").mkString("\n")}")
      IO.copy(`mappings-scala src->dest`)
    }
  )

//libraryDependencies += "com.novocode" % "junit-interface" % "0.10-M2" % "test"

Compile / packageOptions +=
    Package.ManifestAttributes("Permissions" -> "all-permissions", "Application-Name" -> "KojoLib")

// packageBin / packageOptions +=
//     Package.ManifestAttributes("Permissions" -> "all-permissions", "Application-Name" -> "KojoLib")
    
assembly / logLevel := Level.Info
assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false)

ThisBuild / publishMavenStyle := false
