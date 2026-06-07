package net.kogics.kojo.lite

object Versions {
  val KojoLibMajorVersion = "Pre 1.0"
  val KojoLibVersion = "0.3.3"
  val KojoLibRevision = "r1"
  val KojoLibBuildDate = "07 June 2026"
  val JavaVersion = {
    val jrv = System.getProperty("java.runtime.version")
    val arch = System.getProperty("os.arch")
    if (jrv == null) {
      val jv = System.getProperty("java.version")
      s"$jv; $arch"
    }
    else {
      s"$jrv; $arch"
    }
  }
  val ScalaVersion = util.Properties.versionNumberString
}
