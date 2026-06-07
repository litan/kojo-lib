package driver

import net.kogics.kojo.lite.KojoHeadless

object MainHeadless {
  // An example of how to use Kojo in a headless fashion
  def main(args: Array[String]): Unit = {
    val kojo = KojoHeadless.create(600, 600)
    val builtins = kojo.builtins

    import builtins._
    import DCanvas._
    import TurtleWorld._

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
    println("Exporting Kojo Output...")
    exportImageToFile("canvas.png")
    println("Done")
  }
}
