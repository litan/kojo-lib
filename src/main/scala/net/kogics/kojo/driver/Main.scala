package net.kogics.kojo.driver

import net.kogics.kojo.lite.KojoFrame

object Main {
  def main(args: Array[String]): Unit = {

    val kojo = new KojoFrame
    val builtins = kojo.builtins

    import builtins._
    import CanvasAPI._
    import TurtleAPI._

    def spiral(size: Int, angle: Int) {
      if (size <= 300) {
        forward(size)
        right(angle)
        spiral(size + 2, angle)
      }
    }

    size(20, 900)
    setDrawingCanvasToA4()
    clear()
    setPenColor(darkGray)
    setFillColor(green)
    setBackgroundH(red, yellow)
    setPenThickness(1)
    setSpeed(fast)
    spiral(0, 91)
  }
}
