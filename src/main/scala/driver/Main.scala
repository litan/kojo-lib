package driver

import net.kogics.kojo.lite.KojoFrame
import net.kogics.kojo.util.Utils

object Main {
  def main(args: Array[String]): Unit = {
    val kojo = KojoFrame.create()
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
  }
}
