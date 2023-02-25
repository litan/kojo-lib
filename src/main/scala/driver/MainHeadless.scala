package driver

import net.kogics.kojo.lite.{KojoFrame, KojoHeadless}

object MainHeadless {
  def main(args: Array[String]): Unit = {
    val kojo = KojoHeadless.create()
    val builtins = kojo.builtins

    import builtins._
    import TSCanvas._
    import Tw._

    def spiral(size: Int, angle: Int): Unit = {
      if (size <= 300) {
        forward(size)
        right(angle)
        spiral(size + 2, angle)
      }
    }

    clear()
    disablePanAndZoom()
    setPenColor(darkGray)
    setFillColor(green)
    setBackgroundH(red, yellow)
    setPenThickness(1)
    setSpeed(fast)
    spiral(0, 91)
    println("Exporting screenshot...")
    exportImage("kojo-screenshot")
    println("Done")
  }
}
